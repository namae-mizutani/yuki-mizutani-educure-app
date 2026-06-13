package jp.mizutani.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jp.mizutani.bookstore.form.LoginForm;
import jp.mizutani.bookstore.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import jp.mizutani.bookstore.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
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
        if (user == null || !passwordEncoder.matches(form.getPassword(), user.getPassword())) {

            model.addAttribute("message", "名前またはパスワードが違います");
            return "login";
        }
        session.setAttribute("role", user.getRole());
        session.setAttribute("loginUser", user);
        var auth = new UsernamePasswordAuthenticationToken(
                user.getName(),
                null,
                AuthorityUtils.createAuthorityList("ROLE_" + user.getRole()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
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
    public String userDeleteLogin(@RequestParam String name) {
        User user = userMapper.selectByName(name);

        if (user != null) {
            user.setPassword(passwordEncoder.encode("Pass1234"));
            userMapper.update(user);
            return "password_reset_completed";
        }

        return "password_reset";
    }
}
