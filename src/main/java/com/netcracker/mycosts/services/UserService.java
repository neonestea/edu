package com.netcracker.mycosts.services;

import com.netcracker.mycosts.entities.Category;
import com.netcracker.mycosts.entities.User;
import com.netcracker.mycosts.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private CategoryService categoryService;

    public User getUserById(int id) {
        return userRepository.findById(id).get();
    }

    public User getUserByName(String name) {
        return userRepository.findUserByName(name);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    //TODO чекнуть
    @Transactional
    public void create(User user) {
        //List<Category> defaultCategories = categoryService.findDefaultCategories();
        //user.addCategories(defaultCategories);
        save(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}
