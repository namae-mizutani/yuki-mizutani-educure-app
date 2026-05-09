package jp.mizutani.bookstore.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import jp.mizutani.bookstore.entity.Sales;
import jp.mizutani.bookstore.entity.User;
import jp.mizutani.bookstore.form.UserForm;
import jp.mizutani.bookstore.repository.UserMapper;
import jp.mizutani.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/newuser_registration")
    public String displayRegistrationForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "newuser_registration";
    }

    @PostMapping("/registration")
    public String registration(@Validated @ModelAttribute UserForm form, BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "newuser_registration";
        } else if (userMapper.findByName(form.getName())) {
            model.addAttribute("message", "そのユーザーは既に登録されています。他の名前で入力してください。");
            return "newuser_registration";
        }
        User user = new User();
        user.setName(form.getName());
        user.setPassword(form.getPassword());
        user.setRole(form.getRole());
        userService.insert(user);
        model.addAttribute("message", " 登録しました");
        return "newuser_registration";
    }

    @GetMapping("/userlist")
    public String getUserList(Model model) {
        model.addAttribute("users", userMapper.selectAll());
        return "userlist";
    }

    @GetMapping("/mypage")
    public String getUserInform(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("user", user);
        return "mypage";
    }

    @PostMapping("/withdraw")
    public String withdraw(HttpSession session) {
        User user = (User) session.getAttribute("loginUser");

        if (user != null) {
            userMapper.delete(user.getId());
            session.invalidate();
            return "withdrawalcompleted";
        }
        return "redirect:/login";
    }

    @PostMapping("/update")
    public String userInformUpdate(@ModelAttribute UserForm form, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user != null) {
            if (form.getName() == null || form.getName().isEmpty() ||
                    form.getPassword() == null || form.getPassword().isEmpty()) {

                model.addAttribute("user", user);
                model.addAttribute("message", "名前とパスワードを入力してください。");
                return "mypage";
            }
            user.setName(form.getName());
            user.setPassword(form.getPassword());
            userMapper.update(user);
            model.addAttribute("user", user);
            model.addAttribute("message", "変更しました。");
            return "mypage";
        }
        return "redirect:/login";
    }

    @GetMapping("/purchasehistory")
    public String purchaseHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        List<Sales> books = userService.getPurchaseHistory(user.getId());
        model.addAttribute("books", userService.getPurchaseHistory(user.getId()));

        return "purchasehistory";
    }

}
