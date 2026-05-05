package jp.mizutani.bookstore.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {
    @NotBlank(message = "名前を入力してください")
    private String name;
    @NotBlank(message = "パスワードを入力してください")
    private String password;
}
