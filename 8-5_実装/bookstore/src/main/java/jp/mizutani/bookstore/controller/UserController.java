package jp.mizutani.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jp.mizutani.bookstore.entity.User;
import jp.mizutani.bookstore.form.UserForm;
import jp.mizutani.bookstore.repository.UserMapper;
import jp.mizutani.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/registration")
    public String displayRegistrationForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "newuser_registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute UserForm form, Model model) {
        User user = new User();
        user.setName(form.getName());
        user.setPassword(form.getPassword());
        user.setRole(form.getRole());
        userService.insert(user);
        model.addAttribute("message", " 登録しました");
        return "redirect:/";
    }

    @GetMapping("/mypage/{id}")
    public String getUserInform(@PathVariable int id, Model model) {
        User user = userService.selectById(id);
        UserForm form = new UserForm();
        form.setId(user.getId());
        form.setName(user.getName());
        form.setPassword(user.getPassword());
        return "mypage";
    }

    @PostMapping("/update")
    public String userInformUpdate(@ModelAttribute UserForm form, Model model) {
        User user = new User();
        user.setId(form.getId());
        user.setName(form.getName());
        user.setPassword(form.getPassword());

        userService.update(user);

        model.addAttribute("message", "変更しました。");
        return "mypage";
    }

    @PostMapping("/delete/{id}")
    public String userDelete(@ModelAttribute UserForm form) {
        userService.delete(form.getId());
        return "withdrawal_completed";
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

    @GetMapping("/userlist")
    public String getUserList() {
        userMapper.selectAll();
        return "userlist";
    }
}
