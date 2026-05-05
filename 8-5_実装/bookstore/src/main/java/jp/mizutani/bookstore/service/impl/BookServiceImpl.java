package jp.mizutani.bookstore.service.impl;

import java.io.PrintWriter;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                
                Book book = new Book();
                book.setTitle(data[1]);
                book.setPrice(Integer.parseInt(data[2])); 
                book.setCategory(data[3]);

                bookMapper.insert(book);
            }
        }
    }
}
