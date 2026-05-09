package jp.mizutani.bookstore.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sales {
    private int id;
    private int userId;
    private int bookId;
    private int quantity;
    private String title;
    private String status;
    private LocalDate createdAt;
    private int orderId;
    private int salesAmount;
    private int totalSales;
}
