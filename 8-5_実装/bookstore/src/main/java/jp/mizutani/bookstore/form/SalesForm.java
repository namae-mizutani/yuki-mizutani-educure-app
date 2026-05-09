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
    private int quantity;  
    private String status;    
    private String createdAt; 
    private String startDate;
    private String endDate;
}
