package jp.mizutani.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import jp.mizutani.bookstore.form.LoginForm;

@Controller
public class LoginController {
    @GetMapping("/")
    public String login(@ModelAttribute LoginForm form) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm form,
            BindingResult result) {
        if (result.hasErrors()) {
            return "login";
        }
        return "redirect:/menu";
    }

    @GetMapping("/menu")
    public String menu(@RequestParam String role, Model  model) {
       model.addAttribute("role", role);
       return "menu";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

}