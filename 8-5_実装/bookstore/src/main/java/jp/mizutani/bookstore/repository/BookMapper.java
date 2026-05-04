package jp.mizutani.bookstore.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import jp.mizutani.bookstore.entity.Book;

@Mapper
public interface BookMapper {

    List<Book> selectAll();

    Book selectById(@Param("id") int id);

    void insert(Book book);

    void update(Book book);

    void delete(int id);

}
