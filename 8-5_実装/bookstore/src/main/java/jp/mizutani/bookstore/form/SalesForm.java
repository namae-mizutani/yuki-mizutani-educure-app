package jp.mizutani.bookstore.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesForm {
    private int orderId;      
    private int bookId;       
    private String title;    
    private int orderCount;  
    private String status;    
    private String createdAt; 
}
