package pl.pownug.marek.financeAnalyzer.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.pownug.marek.financeAnalyzer.charts.DataSet;
import pl.pownug.marek.financeAnalyzer.charts.LineChart;
import pl.pownug.marek.financeAnalyzer.charts.PieChart;
import pl.pownug.marek.financeAnalyzer.domain.Category;
import pl.pownug.marek.financeAnalyzer.domain.Transaction;
import pl.pownug.marek.financeAnalyzer.domain.User;
import pl.pownug.marek.financeAnalyzer.service.AccountService;
import pl.pownug.marek.financeAnalyzer.service.CategoryService;
import pl.pownug.marek.financeAnalyzer.service.TransactionService;

@Controller
public class ChartsController {
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	CategoryService categoryService;
    
    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping("/incomePieChart")
    public @ResponseBody List<PieChart> incomePieChart(@RequestParam(required=false) String accountId) {
    	
    	List<PieChart> pieCharts = new ArrayList<PieChart>();
        
		List<Category> categories = categoryService.findUserCategories(User.getAuthenticatedUser());
		for (Category category : categories) {
			List <Transaction> categotyTransations = transactionService.findByCategory(category);
			PieChart piechart = new PieChart();
			piechart.setColor(generateColor());
			piechart.setHighlight(piechart.getColor());
			piechart.setLabel(category.getName());
			piechart.setValue(sumIncome(categotyTransations));
			pieCharts.add(piechart);
		}
        return pieCharts;
    }
    
    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping("/expenditurePieChart")
    public @ResponseBody List<PieChart> expenditurePieChart(@RequestParam(required=false) String accountId) {
    	
    	List<PieChart> pieCharts = new ArrayList<PieChart>();
        
		List<Category> categories = categoryService.findUserCategories(User.getAuthenticatedUser());
		for (Category category : categories) {
			List <Transaction> categotyTransations = transactionService.findByCategory(category);
			PieChart piechart = new PieChart();
			piechart.setColor(generateColor());
			piechart.setHighlight(piechart.getColor());
			piechart.setLabel(category.getName());
			piechart.setValue(sumExpenditure(categotyTransations));
			pieCharts.add(piechart);
		}
        return pieCharts;
    }
    
    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping("/lineChart")
    public @ResponseBody LineChart lineChart(@RequestParam(required=false) String accountId) {
    	
    	LineChart lineChart = new LineChart();
    	List<DataSet> dataSets = new ArrayList<DataSet>();
    	List<Transaction> transations = transactionService.findUserTransactions(User.getAuthenticatedUser());
    	String[] labels = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    	List<float[]> values = getData(transations);
    	DataSet incomeData = new DataSet();
    	DataSet expeditureData = new DataSet();
    	
    	expeditureData.setData(values.get(1));
    	expeditureData.setLabel("Expediture");
    	expeditureData.setFillColor("rgba(255,106,106,0.2)");
    	expeditureData.setStrokeColor("rgba(255,106,106,1)");
    	expeditureData.setPointColor("rgba(255,106,106,1)");
    	expeditureData.setPointStrokeColor("#fff");
    	expeditureData.setPointHighlightFill("#fff");
    	expeditureData.setPointHighlightStroke("rgba(255,106,106,1)");
    	dataSets.add(expeditureData);
    	
    	incomeData.setData(values.get(0));
    	incomeData.setLabel("Income");
    	incomeData.setFillColor("rgba(193,255,193,0.4)");
    	incomeData.setStrokeColor("rgba(193,255,193,1)");
    	incomeData.setPointColor("rgba(193,255,193,1)");
    	incomeData.setPointStrokeColor("#fff");
    	incomeData.setPointHighlightFill("#fff");
    	incomeData.setPointHighlightStroke("rgba(193,255,193,1)");
    	dataSets.add(incomeData);
    	
    	lineChart.setLabels(labels);
    	lineChart.setDatasets(dataSets);
        return lineChart;
    }
    
    private float sumIncome(List <Transaction> transactions) {
    	float sum = 0;
		for (Transaction transaction : transactions) {
			if(transaction.getType().equals(1)) {
				sum += transaction.getAmount();
			}
		}
		return sum;
    }
    
    private float sumExpenditure(List <Transaction> transactions) {
    	float sum = 0;
		for (Transaction transaction : transactions) {
			if(transaction.getType().equals(0)) {
				sum += transaction.getAmount();
			}
		}
		return sum;
    }
    
    private String generateColor()
    {
        String[] letters = new String[15];
        letters = "0123456789ABCDEF".split("");
        String code ="#";
        for(int i=0;i<6;i++)
        {
            double ind = Math.random() * 15;
            int index = (int)Math.round(ind);
            code += letters[index]; 
        }
        return code;
    }
    
    private List<float[]> getData(List<Transaction> transations) {
    	float[] incomes = new float[7];
    	float[] expenditures = new float[7];
    	Date currentdate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentdate);
        int toFirst = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -toFirst - 7);
        Date date = c.getTime();
        for (int i=0;i<6;i++) {
        	for (Transaction transation : transations) {
        		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
    			if (transation.getFormattedDate().equals(df.format(date))) {	
    				if(transation.getType() == 1)
    					incomes[i] += transation.getAmount();
    				if(transation.getType() == 0)
    					expenditures[i] += transation.getAmount();
    			}
    		}
    		c.add(Calendar.DATE, 1);
    		date = c.getTime();
		}
        List<float[]> list = new ArrayList<float[]>();
        list.add(incomes);
        list.add(expenditures);
    	return list;
    }
}