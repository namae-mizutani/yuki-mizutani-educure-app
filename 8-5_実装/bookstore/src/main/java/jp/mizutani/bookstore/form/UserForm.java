package jp.mizutani.bookstore.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {
    private int id;
    @NotBlank(message = "名前を入力してください")
    private String name;
    @NotBlank(message = "パスワードが違います。正しいパスワードを入力してください。")
    private String password;
    private String role;
}
