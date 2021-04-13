package com.netcracker.mycosts.controllers;

import com.netcracker.mycosts.entities.User;
import com.netcracker.mycosts.services.UserService;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import com.netcracker.mycosts.services.CategoryService;
import com.netcracker.mycosts.entities.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {

    private CategoryService categoryService;
    private UserService userService;

    @PostMapping("/category")
    public ResponseEntity<Category> create(@RequestBody Category category, @AuthenticationPrincipal User user) {
        category = categoryService.save(category);
        boolean alreadyExist = user.getCategories().stream()
                .map(cat -> cat.getName())
                .collect(Collectors.toSet())
                .contains(category.getName());
        if (!alreadyExist) {
            user.addCategory(category);
            userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/category/{id}")
    public void delete(@PathVariable int id, @AuthenticationPrincipal User user) {
        Category category = categoryService.findCategoryById(id);
        userService.removeCategoryFromUser(user, category);
        //try to remove user from category or redownload category from db
        //category.removeUser(user);
        if (shouldDeleteCategory(category)) {
            categoryService.deleteCategoryById(category.getId());
        }
    }


    private boolean shouldDeleteCategory(Category category) {
        return category.getUsers().size() == 0 && !category.isDefault();
    }


    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}

