package jp.mizutani.bookstore.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {
    @NotBlank(message = "名前を入力してください")
    @Size(min = 1, max = 20, message = "20文字以内で入力してください")
    private String name;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 1, max = 20, message = "20文字以内で入力してください")
    private String password;
}
