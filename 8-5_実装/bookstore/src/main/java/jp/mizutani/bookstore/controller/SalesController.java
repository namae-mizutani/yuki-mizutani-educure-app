package jp.mizutani.bookstore.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jp.mizutani.bookstore.entity.Sales;
import jp.mizutani.bookstore.form.SalesForm;
import jp.mizutani.bookstore.repository.SalesMapper;
import jp.mizutani.bookstore.repository.StockMapper;
import jp.mizutani.bookstore.service.SalesService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesController {
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private SalesMapper salesMapper;
    private final SalesService salesService;

    @GetMapping("/total")
    public String displaySalesTally(Model model) {

        List<Sales> salesList = salesService.findAllGroupedByTitle();
        model.addAttribute("salesList", salesList);
        return "sales_tally";
    }

    @GetMapping("/ordercheck")
    public String displayOrderCheck(Model model) {
        List<SalesForm> orderList = salesMapper.findAllOrders();

        model.addAttribute("books", orderList);
        return "ordercheck";
    }

    @PostMapping("/shipping")
    public String shipping(@RequestParam("bookId") int bookId,
            @RequestParam("orderId") int orderId, Model model) {

        stockMapper.updateStock(bookId, 1);
        salesMapper.updateStatus(orderId);
        model.addAttribute("message", "発送しました");
        return "redirect:ordercheck";
    }

    @PostMapping("/return-product")
    public String returnProduct(@RequestParam int orderId, @RequestParam int bookId) {
        Sales order = salesMapper.selectById(orderId);

        int returnCount = order.getQuantity();

        stockMapper.updateStock(bookId, returnCount);

        salesMapper.updateStatus(orderId, "返却");
        return "purchase_history_change_complete";
    }

    @PostMapping("/cancel-order")
    public String cancelOrder(@RequestParam int orderId, @RequestParam String status) {

        Sales sales = salesMapper.selectById(orderId);
        int count = sales.getQuantity();
        int bookId = sales.getBookId();
        if ("発送済み".equals(status)) {
            stockMapper.updateStock(bookId, count);
        }
        salesMapper.updateStatus(orderId, "キャンセル");
        return "purchase_history_change_complete";
    }
}
