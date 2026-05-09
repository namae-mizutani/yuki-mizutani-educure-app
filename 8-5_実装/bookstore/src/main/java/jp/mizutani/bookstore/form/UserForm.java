package jp.mizutani.bookstore.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {
    private int id;
    @Size(min = 1, max = 10, message = "10文字以内で入力してください")
    private String name;
    @Size(min = 1, max = 10, message = "10文字以内で入力してください")
    private String password;
    private String role;
}
