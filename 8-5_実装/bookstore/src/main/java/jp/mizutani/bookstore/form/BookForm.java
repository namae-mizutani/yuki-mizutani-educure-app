package jp.mizutani.bookstore.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookForm {
    private int id;
    @NotBlank(message= "タイトルを入力してください")
    private String title;
    @NotBlank(message= "価格を入力してください")
    private int price;
    @NotBlank(message= "カテゴリーを入力してください")
    private String category;
    @NotBlank(message= "在庫数を入力してください")
    private int stock;
}
