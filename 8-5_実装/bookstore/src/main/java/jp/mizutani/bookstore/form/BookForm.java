package jp.mizutani.bookstore.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookForm {
    private int id;
    @NotBlank(message= "タイトルを入力してください")
    @Size(max = 20, message = "タイトルは20文字以内で入力してください")
    private String title;
    @NotNull(message= "価格を入力してください")
    private int price;
    @NotBlank(message= "カテゴリーを入力してください")
    @Size(max = 10, message = "カテゴリーは10文字以内で入力してください")
    private String category;
    @NotNull(message= "在庫数を入力してください")
    private int stock;
}
