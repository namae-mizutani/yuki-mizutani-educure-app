package jp.mizutani.bookstore.service;

import java.util.List;
import jp.mizutani.bookstore.entity.Sales;
import jp.mizutani.bookstore.form.SalesForm;

public interface SalesService {
    List<Sales> selectAll();

    Sales selectById(int id);

    void insert(Sales sales);

    List<Sales> findAllGroupedByTitle(String starttime, String endTime);

    List<Sales> selectByUserId(int userId);

    void updateStatus(int id, String status);

    void csvInsert(Sales sales);

   List<SalesForm> findAllOrders(int page, int pageSize);

    int getOrdercount();
}
