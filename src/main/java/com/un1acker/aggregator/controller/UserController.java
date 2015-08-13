package com.un1acker.aggregator.controller;

import com.un1acker.aggregator.entity.Blog;
import com.un1acker.aggregator.entity.User;
import com.un1acker.aggregator.service.BlogService;
import com.un1acker.aggregator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @ModelAttribute("user")
    public User constructUser() {
        return new User();
    }

    @ModelAttribute("blog")
    public Blog constructBlog() {
        return new Blog();
    }

    @RequestMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @RequestMapping("/users/{id}")
    public String details(Model model, @PathVariable Integer id) {
        model.addAttribute("user", userService.findOneWithBlogs(id));
        return "user-detail";
    }

    @RequestMapping("/register")
    public String showRegister() {
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String doRegister(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/register.html?success=true";
    }

    @RequestMapping("/account")
    public String account(Model model, Principal principal) {
        String name = principal.getName();
        model.addAttribute("user", userService.findOneWithBlogs(name));
        return "user-detail";
    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public String addBlog(@ModelAttribute("blog") Blog blog, Principal principal) {
        String name = principal.getName();
        blogService.save(blog, name);
        return "redirect:/account.html";
    }

    @RequestMapping("/blog/delete/{id}")
    public String deleteBlog(@PathVariable Integer id) {
        Blog blog = blogService.findOne(id);
        blogService.delete(blog);
        return "redirect:/account.html";
    }

    @RequestMapping("users/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.delete(id);
        return "redirect:/users.html";
    }
}
