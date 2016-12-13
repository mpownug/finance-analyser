package pl.pownug.marek.financeAnalyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pownug.marek.financeAnalyzer.domain.Category;
import pl.pownug.marek.financeAnalyzer.domain.User;
import pl.pownug.marek.financeAnalyzer.helpers.Message;
import pl.pownug.marek.financeAnalyzer.service.CategoryService;
import pl.pownug.marek.financeAnalyzer.service.TransactionService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    TransactionService transactionService;

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public @ResponseBody List<Category> categories(@RequestParam(required=false) LinkedHashMap model) {
        List<Category> categories = categoryService.findUserCategories(User.getAuthenticatedUser());
        return categories;
    }

    @RequestMapping(value = "/categories/add", method = RequestMethod.GET)
    public String newCategory(ModelMap model) {
        Category category = new Category();
        category.setDeleted(false);
        model.addAttribute("category", category);
        return "categories/editor";
    }

    @RequestMapping(value = "/categories/edit/{id}", method = RequestMethod.GET)
    public String editCategory(@PathVariable int id, ModelMap model) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "categories/editor";
    }

    @RequestMapping(value = "/categories/save", method = RequestMethod.POST)
    public String saveCategory(@Valid @ModelAttribute Category category,
                               BindingResult result,
                               ModelMap model,
                               final RedirectAttributes redirectAttributes) {

        List<Message> messages = new ArrayList<Message>();

        if(result.hasErrors()) {
            model.addAttribute("category", category);
            return "categories/editor";
        }

        if(category.isNew()) {
            category.setUser(User.getAuthenticatedUser());
            categoryService.persistCategory(category);
            messages.add(new Message("success", "Category named <strong>" + category.getName() + "</strong> has been successfully created.", "thumb-up"));

        } else {
            categoryService.updateCategory(category);
            messages.add(new Message("success", "Category named <strong>" + category.getName() + "</strong> has been successfully updated.", "thumb-up"));
        }

        redirectAttributes.addFlashAttribute("flashMessages", messages);
        return "redirect:/categories";
    }

    @RequestMapping(value = "/categories/delete/{id}", method = RequestMethod.GET)
    public String deleteCategory(@PathVariable int id, final RedirectAttributes redirectAttributes) {
        Category category = categoryService.findById(id);
        categoryService.deleteCategoryById(category.getId());

        List<Message> messages = new ArrayList<Message>();
        messages.add(new Message("success", "Category named <strong>" + category.getName() + "</strong> has been successfully removed.", "trash"));

        redirectAttributes.addFlashAttribute("flashMessages", messages);
        return "redirect:/categories";
    }
}
