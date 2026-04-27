package jp.mizutani.bookstore.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import jp.mizutani.bookstore.entity.Stock;
import jp.mizutani.bookstore.repository.StockMapper;
import jp.mizutani.bookstore.service.StockService;

@Service
@RequiredArgsConstructor
@Transactional
public class StockServiceImpl implements StockService {
    private final StockMapper stockMapper;

     @Override
    public List<Stock> selectAll() {
        return stockMapper.selectAll();
    }

    @Override
    public Stock selectById(int id) {
        return stockMapper.selectById(id);
    }

    @Override
    public void insert(Stock stock) {
        stockMapper.insert(stock);
    }

    @Override
    public void update(Stock stock) {
        stockMapper.update(stock);
    }
}
