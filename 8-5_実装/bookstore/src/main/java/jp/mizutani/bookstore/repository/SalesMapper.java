package jp.mizutani.bookstore.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import jp.mizutani.bookstore.entity.Sales;

@Mapper
public interface SalesMapper {

    List<Sales> selectAll();

    Sales selectById(@Param("id") int id);

    void insert(Sales sales);
}
