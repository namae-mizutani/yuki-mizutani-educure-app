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
    // トップページとログインページは同じ画面を表示するようにしています！どちらからアクセスしてもログイン画面が出るイメージです。
    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }
    // ログイン処理 (トップページとログインページの両方からアクセスできるようにするため、URLは/loginにしています！)
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }
    // ログイン処理 
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
    public String passwordResetExecute(@RequestParam String name,
            HttpSession session, Model model) {
        User user = userMapper.selectByName(name);
        if (user != null) {
            session.setAttribute("resetUserName", name); // セッションに保存
            return "password_reset_confirm"; // 本人確認画面へ
        }
        model.addAttribute("message", "登録されていない名前です");
        return "password_reset";
    }

    @PostMapping("/password_reset_confirm")
    public String passwordResetConfirm(@RequestParam String role,
            HttpSession session, Model model) {
        String name = (String) session.getAttribute("resetUserName");
        if (name == null) {
            return "redirect:/password_reset";
        }
        User user = userMapper.selectByName(name);
        if (user != null && user.getRole().equals(role)) {
            return "password_reset_completed"; // 新パスワード入力画面へ
        }
        model.addAttribute("message", "役職が一致しません");
        return "password_reset_confirm";
    }
    // パスワードリセット後の新しいパスワードを保存するためのメソッド
    @PostMapping("/password_reset_update")
    public String passwordResetUpdate(@RequestParam String password,
            HttpSession session, Model model) {

        String name = (String) session.getAttribute("resetUserName");
        if (name == null) {
            return "redirect:/password_reset";
        }
        if (password == null || password.isEmpty()) {
            model.addAttribute("message", "パスワードを入力してください");
            return "password_reset_completed";
        }
        User user = userMapper.selectByName(name);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(password));
            userMapper.update(user);
            session.removeAttribute("resetUserName");
            return "redirect:/";
        }
        return "password_reset";
    }
}
