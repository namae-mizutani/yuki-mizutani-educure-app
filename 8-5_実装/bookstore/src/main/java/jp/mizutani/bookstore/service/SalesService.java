package jp.mizutani.bookstore.service;

import java.util.List;
import jp.mizutani.bookstore.entity.Sales;

public interface SalesService {
    List<Sales> selectAll();

    Sales selectById(int id);

    void insert(Sales sales);

    List<Sales> findAllGroupedByTitle();

}
