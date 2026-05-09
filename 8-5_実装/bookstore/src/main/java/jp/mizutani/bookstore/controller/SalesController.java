package jp.mizutani.bookstore.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jp.mizutani.bookstore.entity.Sales;
import jp.mizutani.bookstore.form.SalesForm;
import jp.mizutani.bookstore.repository.SalesMapper;
import jp.mizutani.bookstore.service.SalesService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SalesController {
    @Autowired
    private SalesMapper salesMapper;
    private final SalesService salesService;

    @GetMapping("/total")
    public String total(Model model) {
        model.addAttribute("salesForm", new SalesForm());
        model.addAttribute("salesList", new ArrayList<Sales>());
        return "sales_tally";
    }

    @PostMapping("/tally")
    public String displaySalesTally(@ModelAttribute SalesForm salesForm, Model model) {

        if (salesForm.getStartDate() == null || salesForm.getStartDate().isEmpty() ||
                salesForm.getEndDate() == null || salesForm.getEndDate().isEmpty()) {

            model.addAttribute("message", "日付を入力してください");
            model.addAttribute("salesList", new ArrayList<Sales>());
            return "sales_tally";
        }

        java.time.LocalDate start = java.time.LocalDate.parse(salesForm.getStartDate());
        java.time.LocalDate end = java.time.LocalDate.parse(salesForm.getEndDate());

        if (start.isAfter(end)) {
            model.addAttribute("message", "終了日は開始日より後にしてください");
            model.addAttribute("salesList", new ArrayList<Sales>());
            return "sales_tally";
        }

        List<Sales> salesList = salesService.findAllGroupedByTitle(
                salesForm.getStartDate(),
                salesForm.getEndDate());

        model.addAttribute("salesList", salesList);
        return "sales_tally";
    }

    @GetMapping("/ordercheck")
    public String displayOrderCheck(Model model) {
        model.addAttribute("books", salesMapper.findAllOrders());
        return "ordercheck";
    }

    @PostMapping("/return-product")
    public String returnProduct(@RequestParam int id, Model model) {
        salesMapper.updateStatus("返却", id);
        model.addAttribute("message", "返却しました");
        return "purchase_done";
    }

    @PostMapping("/shipping")
    public String shipping(@RequestParam int id, Model model) {

        salesMapper.updateStatus("発送済", id);
        model.addAttribute("message", "発送しました");
        return "redirect:ordercheck";
    }

}
