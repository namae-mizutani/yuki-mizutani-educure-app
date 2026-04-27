package jp.mizutani.bookstore.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import jp.mizutani.bookstore.entity.Stock;

@Mapper
public interface StockMapper {

    List<Stock> selectAll();

    Stock selectById(@Param("id") int id);

    void insert(Stock stock);

    void update(Stock stock);
}
