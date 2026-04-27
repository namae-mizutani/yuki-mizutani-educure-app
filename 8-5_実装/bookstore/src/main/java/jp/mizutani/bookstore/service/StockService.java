package jp.mizutani.bookstore.service;

import java.util.List;
import jp.mizutani.bookstore.entity.Stock;

public interface StockService {

    List<Stock> selectAll();

    Stock selectById(int id);

    void insert(Stock stock);

    void update(Stock stock);
}
