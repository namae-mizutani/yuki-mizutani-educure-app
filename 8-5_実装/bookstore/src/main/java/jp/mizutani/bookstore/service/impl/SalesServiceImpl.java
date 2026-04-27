package jp.mizutani.bookstore.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import jp.mizutani.bookstore.entity.Sales;
import jp.mizutani.bookstore.repository.SalesMapper;
import jp.mizutani.bookstore.service.SalesService;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesServiceImpl implements SalesService {
    private final SalesMapper salesMapper;

    @Override
    public List<Sales> selectAll() {
        return salesMapper.selectAll();
    }

    @Override
    public Sales selectById(int id) {
        return salesMapper.selectById(id);
    }

    @Override
    public void insert(Sales sales) {
        salesMapper.insert(sales);
    }
}
