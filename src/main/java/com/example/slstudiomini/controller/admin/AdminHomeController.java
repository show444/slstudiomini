package com.example.slstudiomini.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.slstudiomini.model.User;
import com.example.slstudiomini.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String index() {
        return "admin/index";
    }

    @GetMapping("/users")
    public String showUser(Model model){
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/user-list";
    }

    @GetMapping("users/add")
    public String addUserForm(Model model){
        model.addAttribute("user", new User());
        return "admin/user-add";
    }

    @PostMapping("users/add")
    public String addUser(@Valid User user, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("erros", result.getAllErrors());
            return "admin/user-add";
        }
        userService.addEnableStudentAndHashPassword(user);
        return "redirect:/admin/users";
    }
}
