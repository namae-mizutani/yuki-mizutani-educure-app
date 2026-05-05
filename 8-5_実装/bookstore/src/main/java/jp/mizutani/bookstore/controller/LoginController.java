package jp.mizutani.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import jp.mizutani.bookstore.form.LoginForm;
import jp.mizutani.bookstore.form.UserForm;
import jp.mizutani.bookstore.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import jp.mizutani.bookstore.entity.User;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserMapper userMapper;

    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm form,
            BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "login";
        }
        User user = userMapper.selectByName(form.getName());

        if (user == null || !user.getPassword().equals(form.getPassword())) {
            model.addAttribute("message", "名前またはパスワードが違います");
            return "login";
        }
        session.setAttribute("role", user.getRole());
        return "redirect:/menu";
    }

    @GetMapping("/menu")
    public String menu(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");
        model.addAttribute("role", role);
        return "menu";
    }

    @GetMapping("password_reset")
    public String getPasswordReset() {
        return "password_reset";
    }

    @PostMapping("/password_reset_execute")
    public String userDeleteLogin(@ModelAttribute UserForm form) {
        User user = userMapper.selectByName(form.getName());

        if (user != null) {
            userMapper.delete(user);
            return "password_reset_completed";
        }

        return "password_reset";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}