package jp.mizutani.bookstore.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import jp.mizutani.bookstore.entity.Sales;
import jp.mizutani.bookstore.form.SalesForm;

@Mapper
public interface SalesMapper {

    List<Sales> selectAll();

    Sales selectById(@Param("id") int id);

    void insert(Sales sales);

    void updateStatus(int orderId);

    List<SalesForm> findAllOrders();

    List<Sales> findAllGroupedByTitle();

    void updateStatus(@Param("orderId") int orderId, @Param("status") String status);
}
