package jp.mizutani.bookstore.form;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
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
    @Size(min = 1, max = 10, message = "10文字以内で入力してください")
    private String name;
    @Size(min = 8, max = 15, message = "8文字以上15文字以内で入力してください")
    @Pattern (regexp="^[a-zA-Z0-9]+$", message = "半角英数字で入力してください.スペースは使用できません")
    private String password;
    private String role;
}
