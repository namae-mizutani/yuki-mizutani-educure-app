package jp.mizutani.bookstore.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sales {
    private int id;
    private int userId;
    private int bookId;
    private int quantity;
    private int stock;
    private LocalDateTime createdAt;
}
