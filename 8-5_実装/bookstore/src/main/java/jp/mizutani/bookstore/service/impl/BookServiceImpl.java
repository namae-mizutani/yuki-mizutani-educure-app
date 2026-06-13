package jp.mizutani.bookstore.service.impl;

import java.io.PrintWriter;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import jp.mizutani.bookstore.entity.Book;
import jp.mizutani.bookstore.repository.BookMapper;
import jp.mizutani.bookstore.service.BookService;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;

    @Override
    public List<Book> selectAll() {
        return bookMapper.selectAll();
    }

    @Override
    public Book selectById(int id) {
        return bookMapper.selectById(id);
    }

    @Override
    public void insert(Book book) {
        bookMapper.insert(book);
    }

    @Override
    public void update(Book book) {
        bookMapper.update(book);
    }

    @Override
    public void delete(int id) {
        bookMapper.delete(id);
    }

    @Override
    public void downloadCsv(PrintWriter writer) {
        List<Book> books = bookMapper.selectAll();

        writer.println("ID,タイトル,価格,カテゴリー");

        for (Book book : books) {
            writer.println(book.getId() + "," +
                    book.getTitle() + "," +
                    book.getPrice() + "," +
                    book.getCategory());
        }
    }


    @Override
public void uploadCsv(MultipartFile file) throws Exception {

if (file.isEmpty()) {
    throw new Exception("ファイルが空です");
}

String charset = "UTF-8";
byte[] bytes = file.getBytes();
try {
    new String(bytes, "UTF-8").getBytes("UTF-8");
} catch (Exception e) {
    charset = "Shift-JIS";
}

try (BufferedReader br = new BufferedReader(
        new InputStreamReader(new java.io.ByteArrayInputStream(bytes), charset))) {

    String line;
    int lineNumber = 0;
    br.readLine();
    lineNumber++;

    while ((line = br.readLine()) != null) {
        lineNumber++;

        if (line.trim().isEmpty()) {
            continue;
        }

        String[] data = line.split(",");

        if (data.length < 4) {
            throw new Exception(lineNumber + "行目: 列数が不足しています（" + data.length + "列）");
        }

        int price;
        try {
            price = Integer.parseInt(data[2].trim());
        } catch (NumberFormatException e) {
            throw new Exception(lineNumber + "行目: 価格に数字以外の値が入っています→「" + data[2] + "」");
        }

        if (price < 0) {
            throw new Exception(lineNumber + "行目: 価格に負の値は設定できません→「" + price + "」");
        }

        Book book = new Book();
        book.setTitle(data[1].trim());
        book.setPrice(price);
        book.setCategory(data[3].trim());
        bookMapper.insert(book);
    }
 }
}

    @Override
    public List<Book> searchBooks(String title) {
        return bookMapper.searchBooks(title);
    }

    @Override
    public Book getBookInfoFromGoogle(String isbn) {
        String apiKey = "AlzaSyD6EVARo5T0Fxxvatve-DKYCq-85uQuU0M";
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(url, String.class);
        Book book = new Book();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            String title = root.path("items").get(0).path("volumeInfo").path("title").asText();
            book.setTitle(title);
            book.setCategory(root.path("items").get(0).path("volumeInfo").path("categories").get(0).asText());
            book.setPrice(root.path("items").get(0).path("saleInfo").path("listPrice").path("amount").asInt());
        } catch (Exception e) {
            System.out.println("データ解析エラー: " + e.getMessage());
        }
        return book;
    }
}
