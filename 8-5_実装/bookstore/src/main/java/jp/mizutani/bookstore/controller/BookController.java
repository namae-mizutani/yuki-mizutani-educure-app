package jp.mizutani.bookstore.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jp.mizutani.bookstore.entity.Book;
import jp.mizutani.bookstore.entity.Sales;
import jp.mizutani.bookstore.entity.Stock;
import jp.mizutani.bookstore.entity.User;
import jp.mizutani.bookstore.form.BookForm;
import jp.mizutani.bookstore.form.SalesForm;
import jp.mizutani.bookstore.repository.BookMapper;
import jp.mizutani.bookstore.repository.StockMapper;
import jp.mizutani.bookstore.service.BookService;
import jp.mizutani.bookstore.service.SalesService;
import jp.mizutani.bookstore.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final StockService stockService;
    private final BookMapper bookMapper;
    private final SalesService salesService;
    private final StockMapper stockMapper;

    @GetMapping("/top")
    public String top(@RequestParam(defaultValue = "1") int page, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");

        if (user != null) {

            model.addAttribute("role", user.getRole());
        }

        int pageSize = 5;
        List<Book> booklist = bookService.selectAll(page, pageSize);
        int totalBooks = bookService.getBookCount();
        int totalPages = (int) Math.ceil((double) totalBooks / pageSize);
        model.addAttribute("books", booklist);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("bookForm", new BookForm());
        return "top";
    }

    @GetMapping("/new")
    public String newBookRegistration(Model model) {
        model.addAttribute("bookForm", new BookForm());
        return "newbook_registration";
    }

    @PostMapping("/registration")
    public String registration(@Validated @ModelAttribute BookForm form,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "newbook_registration";
        }
        Book book = new Book();
        book.setTitle(form.getTitle());
        book.setCategory(form.getCategory());
        book.setPrice(form.getPrice());
        bookService.insert(book);

        Stock stock = new Stock();
        stock.setBookId(book.getId());
        stock.setStock(form.getStock());
        stockService.insert(stock);
        model.addAttribute("message", " 登録しました");
        return "newbook_register_completed";
    }

    @GetMapping("/purchase/{id}")
    public String purchase(@PathVariable("id") int id, Model model) {
        Book book = bookService.selectById(id);
        model.addAttribute("book", book);
        return "purchase";
    }

    @PostMapping("/purchaseconfirmation")
    public String executePurchase(@RequestParam("id") int id, @RequestParam("quantity") int quantity,
            Model model) {
        Stock stock = stockService.selectById(id);
        if (stock.getStock() < quantity) {
            model.addAttribute("message", "在庫数が不足してます。購入数を減らしてください");
            Book book = bookService.selectById(id);
            model.addAttribute("book", book);
            return "purchase";
        }
        Book book = bookService.selectById(id);
        model.addAttribute("book", book);
        model.addAttribute("quantity", quantity);
        return "purchase_confirmation";
    }

    @PostMapping("/purchasecompleted")
    public String purchaseCompleted(@RequestParam("id") int id, @RequestParam("quantity") int quantity,
            Model model, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");

        Stock stock = stockService.selectById(id);
        int currentStock = stock.getStock();
        stock.setStock(currentStock - quantity);
        stockService.update(stock);

        Sales sales = new Sales();
        sales.setUserId(user.getId());
        sales.setBookId(id);
        sales.setQuantity(quantity);
        salesService.insert(sales);

        return "purchase_completed";
    }

    @GetMapping("")
    public String bookserch(@RequestParam String title, Model model) {
        List<Book> bookList = bookService.searchBooks(title);
        model.addAttribute("books", bookList);
        return "top";
    }

    @GetMapping("/edit/{id}")
    public String productEdit(@PathVariable("id") int id, Model model) {
        Book book = bookService.selectById(id);
        Stock stock = stockService.selectById(id);
        model.addAttribute("book", book);
        model.addAttribute("stock", stock);
        return "product_edit";
    }

    @PostMapping("/update")
    public String update(@RequestParam("id") int id,
            @RequestParam("price") int price, @RequestParam("stock") int stockCount,
            Model model, String title) {

        Book book = bookService.selectById(id);
        book.setPrice(price);
        bookService.update(book);

        Stock stock = stockService.selectById(id);
        stock.setStock(stockCount);
        stockService.update(stock);
        model.addAttribute("message", "商品の更新が完了しました。");

        return "product_edit_completed";
    }

    @PostMapping("/delete/{id}")
    public String userDelete(@PathVariable("id") int id, Model model) {
        stockMapper.delete(id);
        bookService.delete(id);
        model.addAttribute("message", "商品の削除が完了しました。");
        return "product_edit_completed";
    }

    @PostMapping("/update-confirm")
    public String updateProduct(@RequestParam("id") int id,
            @RequestParam("price") int price,
            @RequestParam("stock") int stockCount,
            Model model) {
        Book book = bookService.selectById(id);
        book.setPrice(price);
        bookService.update(book);

        Stock stock = stockService.selectById(id);
        stock.setStock(stockCount);
        stockService.update(stock);

        model.addAttribute("message", "商品の更新が完了しました。");

        return "product_edit_completed";
    }
   
    @PostMapping("csvdownload")
    public String downloadCsv(@ModelAttribute SalesForm salesForm, Model model,
            HttpServletResponse response)
            throws IOException {
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
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=books.csv");

        response.setCharacterEncoding("UTF-8"); // 明示的にエンコーディングを指定
        PrintWriter writer = response.getWriter();
        writer.write('\ufeff');
        String startDate = salesForm.getStartDate();
        String endDate = salesForm.getEndDate();
        List<Sales> salesList = salesService.findAllGroupedByTitle(startDate, endDate);
        writer.println("期間：" + startDate + "〜" + endDate);
        writer.println("書籍名,売れた数,売上金額");
        for (Sales sales : salesList) {
            writer.println(sales.getTitle() + "," + sales.getSalesAmount() + "," + sales.getTotalSales());
        }
        writer.flush();
        return null;
    }

     /**
     *CSVインポート仕様:
     *必須項目:書籍名、販売数、販売合計金額、販売日 
     */
    @PostMapping("/total")
    public String uploadCsv(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
            throws Exception {
        try {
            bookService.uploadCsv(file);
            redirectAttributes.addFlashAttribute("msg", "CSVのアップロードが成功しました");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msg", "エラーが発生しました。" + e.getMessage());
        }
        return "redirect:/total";
    }

    @GetMapping("/api")
    public String getBook(@RequestParam("isbn") String isbn, Model model) {
        Book googleBook = bookService.getBookInfoFromGoogle(isbn);
        if (googleBook == null) {
            model.addAttribute("errorMessage", "書籍情報が取得できませんでした。ISBNを確認するか、手動で入力してください。");
            model.addAttribute("bookForm", new BookForm());
            return "newbook_registration";
        }

        BookForm form = new BookForm();
        form.setTitle(googleBook.getTitle());
        form.setPrice(googleBook.getPrice());
        form.setCategory(googleBook.getCategory());
        form.setStock(googleBook.getStock());
        model.addAttribute("bookForm", form);
        return "newbook_registration";
    }

    @PostMapping("/apibooks")
    public String createBook(Book book) {
        bookMapper.insert(book);
        return "newbook_register_completed";
    }
}
