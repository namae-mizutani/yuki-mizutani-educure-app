package jp.mizutani.bookstore.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesForm {
    private int orderId;      // 注文を特定するID（発送済にするために必要）
    private int bookId;       // 本を特定するID（在庫を減らすために必要）
    private String title;     // 書籍名
    private int orderCount;   // 購入数（名前を合わせると分かりやすいです）
    private String status;    // 配送ステータス（未発送・発送済）
    private String createdAt; // 注文日
}
