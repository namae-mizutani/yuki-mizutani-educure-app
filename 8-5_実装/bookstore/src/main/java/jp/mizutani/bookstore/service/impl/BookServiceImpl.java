package jp.mizutani.bookstore.service.impl;

import java.io.PrintWriter;
import java.util.List;

import org.springframework.http.ResponseEntity;
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

    public Book getBookInfoFromGoogle(String isbn) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(url, String.class);
        } catch (Exception e) {
            System.out.println("API通信エラー: " + e.getMessage());
            return null;
        }

        if (response.getStatusCode().value() != 200) {
            System.out.println("APIエラー: HTTPステータス " + response.getStatusCode().value());
            return null;
        }
        Book book = new Book();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            JsonNode items = root.path("items");
            if (items.isMissingNode() || items.isEmpty()) {
                System.out.println("書籍情報が見つかりませんでした: isbn=" + isbn);
                return null;
            }

            JsonNode volumeInfo = items.get(0).path("volumeInfo");
            JsonNode saleInfo = items.get(0).path("saleInfo");

            book.setTitle(volumeInfo.path("title").asText("タイトル不明"));

            JsonNode categories = volumeInfo.path("categories");
            book.setCategory(categories.isEmpty() ? "未分類" : categories.get(0).asText());

            JsonNode listPrice = saleInfo.path("listPrice").path("amount");
            book.setPrice(listPrice.isMissingNode() ? 0 : listPrice.asInt());

        } catch (Exception e) {
            System.out.println("データ解析エラー: " + e.getMessage());
            return null;
        }
        return book;
    }
}
