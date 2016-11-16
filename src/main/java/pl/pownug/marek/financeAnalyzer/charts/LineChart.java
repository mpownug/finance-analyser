package pl.pownug.marek.financeAnalyzer.charts;

import java.util.ArrayList;
import java.util.List;

public class LineChart {

	private String[] labels;
	private List<DataSet> datasets;

	public LineChart() {
		datasets = new ArrayList<DataSet>();
	}
	
	public String[] getLabels() {
		return labels;
	}
	
	public void setLabels(String[] labels) {
		this.labels = labels;
	}
	
	public List<DataSet> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<DataSet> datasets) {
		this.datasets = datasets;
	}
}


