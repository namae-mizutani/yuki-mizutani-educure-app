package jp.mizutani.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.mizutani.bookstore.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer>{

} 
