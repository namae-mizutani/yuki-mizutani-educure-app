package jp.mizutani.bookstore.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.mizutani.bookstore.entity.Book;
import jp.mizutani.bookstore.entity.Stock;
import jp.mizutani.bookstore.form.BookForm;
import jp.mizutani.bookstore.repository.BookMapper;
import jp.mizutani.bookstore.service.BookService;
import jp.mizutani.bookstore.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final StockService stockService;
    private final BookMapper bookMapper;

    @GetMapping("/top")
    public String top(Model model) {
        List<Book> bookList = bookService.selectAll();
        model.addAttribute("books", bookList);
        model.addAttribute("bookForm", new BookForm());
        return "top";
    }

    @GetMapping("/new")
    public String newBookRegistration(Model model) {
        model.addAttribute("bookForm", new BookForm());
        return "newbook_registration";
    }
    
    @PostMapping("/registration")
    public String registration(@Validated @ModelAttribute BookForm form, Model model) {
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
    // @GetMapping("/book list")
    // public String bookList() {
    //     return "top";
    // }



    // @PostMapping("/delete/{id}")
    // public String userDelete(@ModelAttribute BookForm form) {
    //     bookService.delete(form.getId());
    //     return "product_edit_completed";
    // }

    @GetMapping("/edit")
    public String productEdit(@RequestParam("id") int id, Model model) {
        Book book = bookService.selectById(id);
        Stock stock = stockService.selectById(id);
        model.addAttribute("book", book);
        model.addAttribute("stock", stock);
        return "product_edit";
    }

    @PostMapping("/update")
    public String update(@RequestParam("id") int id,
            @RequestParam("price") int price, @RequestParam("stock") int stockCount) {
        Book book = bookService.selectById(id);
        book.setPrice(price);
        bookService.update(book);

        Stock stock = stockService.selectById(id);

        stock.setStock(stockCount);

        stockService.update(stock);
        return "redirect:/top";
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

    @PostMapping("/delete/{id}")
    public String userDelete(@PathVariable("id") int id, Model model) {
        bookService.delete(id);
        model.addAttribute("message", "商品の削除が完了しました。");
        return "product_edit_completed";
    }

    @GetMapping("purchase")
    public String purchase(@RequestParam("id") int id, Model model) {
        Book book = bookService.selectById(id);
        model.addAttribute("book", book);

        return "purchase";
    }

    @PostMapping("/purchase")
    public String executePurchase(@RequestParam("id") int id, @RequestParam("quantity") int quantity,
            Model model) {
        Book book = bookService.selectById(id);
        model.addAttribute("book", book);
        model.addAttribute("quantity", quantity);
        return "purchase_confirmation";
    }

    @PostMapping("/purchase_completed")
    public String purchaseCompleted(@RequestParam("id") int id, @RequestParam("quantity") int quantity,
            Model model) {
        Stock stock = stockService.selectById(id);

        stock.setStock(stock.getStock() - quantity);

        stockService.update(stock);

        return "purchase_completed";
    }

    @GetMapping("/api/books")
    public String getBook() {
        return "newbook_registration";
    }

    @PostMapping("/api/books")
    public String createBook(Book book) {
        bookMapper.insert(book);
        return "newbook_register_completed";
    }

    @GetMapping("/api/books/download")
    public void downloadCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=books.csv");

        response.getWriter().write('\ufeff');

        bookService.downloadCsv(response.getWriter());
    }

    @PostMapping("/api/books/upload")
    public String uploadCsv(@RequestParam("file") MultipartFile file) throws Exception {
        bookService.uploadCsv(file);

        return "redirect:/total";
    }
}
