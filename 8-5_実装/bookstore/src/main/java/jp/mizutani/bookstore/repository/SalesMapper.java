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


    List<SalesForm> findAllOrders();

    List<Sales> findAllGroupedByTitle( @Param("startDate") String startDate, @Param("endDate") String endDate);

    void updateStatus(@Param("status") String status,@Param("id") int id);

 
    List<Sales> selectByUserId(int id); 

    void updateStatus(int id, String status);

}
