package jp.mizutani.bookstore.service;

import java.io.PrintWriter;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jp.mizutani.bookstore.entity.Book;

public interface BookService {
    List<Book> selectAll();

    Book selectById(int id);

    void insert(Book book);

    void update(Book book);

    void delete(int id);

    void downloadCsv(PrintWriter writer);

    void uploadCsv(MultipartFile file) throws Exception;
}
