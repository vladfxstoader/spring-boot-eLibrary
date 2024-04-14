package com.example.eLibrary.controller;

import com.example.eLibrary.dto.BookDto;
import com.example.eLibrary.dto.CategoryDto;
import com.example.eLibrary.model.Category;
import com.example.eLibrary.model.User;
import com.example.eLibrary.service.BookService;
import com.example.eLibrary.service.CategoryService;
import com.example.eLibrary.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CategoryController {
    private CategoryService categoryService;
    private BookService bookService;
    private UserService userService;

    public CategoryController(CategoryService categoryService, BookService bookService, UserService userService) {
        this.categoryService = categoryService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/add-category")
    public String showRegistrationForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }
        CategoryDto category = new CategoryDto();
        model.addAttribute("category", category);
        return "add-category";
    }

    @PostMapping("/add-category/save")
    public String registration(@Valid @ModelAttribute("category") CategoryDto categoryDto,
                               BindingResult result,
                               Model model) {
        String name = categoryDto.getName();
        if (name == null) {
            result.rejectValue("name", null, "Category name cannot be empty");
        }
        Category existingCategory = categoryService.findByName(categoryDto.getName());

        if(existingCategory!=null) {
            result.rejectValue("name", null, "There is already a category registered with the same name");
        }

        if(result.hasErrors()) {
            model.addAttribute("category", categoryDto);
            return "/add-category";
        }

        categoryService.save(categoryDto);
        return "redirect:/add-category?success";

    }

    @GetMapping("/categories")
    public String categories(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }
        List<CategoryDto> categories = categoryService.findAllCategories();
        if(categories.size() == 0) {
            return "categories?noData";
        }
        model.addAttribute("categories", categories);
        return "categories";
    }

    @PostMapping("/delete-category/{id}")
    public String deleteCategory(@PathVariable("id") Integer id) {
        CategoryDto category = categoryService.findById(id);
        List<BookDto> books = bookService.findAllByCategoryName(category.getName());
        if(books != null && books.size() > 0) {
            return "redirect:/categories?error";
        }
        categoryService.deleteCategoryById(id);
        return "redirect:/categories";
    }

    @GetMapping("/edit-category/{id}")
    public String showEditCategoryForm(@PathVariable("id") Integer id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }
        CategoryDto category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "edit-category";
    }

    @PostMapping("/edit-category/{id}/save")
    public String saveEditedCategory(@PathVariable("id") Integer id,
                                     @ModelAttribute("category") CategoryDto categoryDto,
                                     BindingResult result,
                                     Model model) {
        Category existingCategory = categoryService.findByName(categoryDto.getName());
        if (existingCategory != null && !existingCategory.getId().equals(id)) {
            return "redirect:/edit-category/" + id + "?error";
        }

        categoryDto.setId(id);
        categoryService.save(categoryDto);
        return "redirect:/categories";
    }
}
