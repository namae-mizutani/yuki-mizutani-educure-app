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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import jakarta.servlet.http.HttpSession;
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
    public String displayOrderCheck(@RequestParam(defaultValue = "1") int page, HttpSession session, Model model) {
        int pageSize = 10;
        List <SalesForm> salesList = salesService.findAllOrders(page, pageSize);
        int totalOrders = salesService.getOrdercount();
        int totalPages = (int) Math.ceil((double)totalOrders / pageSize);
        model.addAttribute("books",salesList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "ordercheck";
    }

    @PostMapping("/return-product")
    public String returnProduct(@RequestParam int id, RedirectAttributes redirectAttributes) {
        Sales sales = salesMapper.selectById(id);
        if (sales == null) {
            redirectAttributes.addFlashAttribute("message", "対象の注文が見つかりませんでした");
            return "redirect:/users/purchasehistory";
        }

        if ("返却".equals(sales.getStatus())) {
            redirectAttributes.addFlashAttribute("message", "すでに返却済みです");
            return "redirect:/users/purchasehistory";
        }

        if ("発送済".equals(sales.getStatus())) {
            redirectAttributes.addFlashAttribute("message", "発送済みのため返却できません");
            return "redirect:/users/purchasehistory";
        }

        salesMapper.updateStatus("返却", id);
        redirectAttributes.addFlashAttribute("message", "返却しました");
        return "redirect:/users/purchasehistory";
    }

    @PostMapping("/shipping")
    public String shipping(@RequestParam int id, Model model) {
        return shipping(id, model, new RedirectAttributesModelMap());
    }

    public String shipping(@RequestParam int id, Model model, RedirectAttributes redirectAttributes) {
        Sales sales = salesMapper.selectById(id);
        if (sales == null) {
            redirectAttributes.addFlashAttribute("message", "対象の注文が見つかりませんでした");
            return "redirect:/ordercheck";
        }

        if ("返却".equals(sales.getStatus())) {
            redirectAttributes.addFlashAttribute("message", "返却済みのため発送できません");
            return "redirect:/ordercheck";
        }

        if ("発送済".equals(sales.getStatus())) {
            redirectAttributes.addFlashAttribute("message", "すでに発送済みです");
            return "redirect:/ordercheck";
        }

        salesMapper.updateStatus("発送済", id);
        redirectAttributes.addFlashAttribute("message", "発送しました");
        return "redirect:/ordercheck";
    }

}
