package jp.mizutani.bookstore.service;

import java.util.List;
import jp.mizutani.bookstore.entity.Book;

public interface BookService {
    List<Book> selectAll();
    
    Book selectById(int id);

    void insert(Book book);

    void update(Book book);

}
