package kr.spring.sales.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.YearMonth;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.sales.dao.SalesOrderMapper;
import kr.spring.sales.vo.SalesOrderVO;
import kr.spring.sales.vo.SalesOrderDetailVO;

@Service
@Transactional
public class SalesOrderServiceImpl implements SalesOrderService {
    @Autowired
    private SalesOrderMapper salesOrderMapper;

    @Override
    @Transactional
    public void insertSalesOrder(SalesOrderVO salesOrder) {
        salesOrderMapper.insertSalesOrder(salesOrder);
    }

    @Override
    public SalesOrderVO selectSalesOrder(long sales_order_num) {
        return salesOrderMapper.selectSalesOrder(sales_order_num);
    }

    @Override
    public List<SalesOrderVO> selectSalesOrderList() {
        return salesOrderMapper.selectSalesOrderList();
    }

    @Override
    public List<SalesOrderVO> selectSalesOrderListByMap(Map<String, Object> map) {
        return salesOrderMapper.selectSalesOrderListByMap(map);
    }

    @Override
    @Transactional
    public void updateSalesOrder(SalesOrderVO salesOrder) {
        salesOrderMapper.updateSalesOrder(salesOrder);
    }

    @Override
    @Transactional
    public void deleteSalesOrder(long sales_order_num) {
        salesOrderMapper.deleteSalesOrder(sales_order_num);
    }

    @Override
    @Transactional
    public void insertSalesOrderDetail(SalesOrderDetailVO detail) {
        salesOrderMapper.insertSalesOrderDetail(detail);
    }

    @Override
    public List<SalesOrderDetailVO> selectSalesOrderDetailList(long sales_order_num) {
        return salesOrderMapper.selectSalesOrderDetailList(sales_order_num);
    }

    @Override
    @Transactional
    public void updateSalesOrderDetail(SalesOrderDetailVO detail) {
        salesOrderMapper.updateSalesOrderDetail(detail);
    }

    @Override
    @Transactional
    public void deleteSalesOrderDetail(long sales_order_num) {
        salesOrderMapper.deleteSalesOrderDetail(sales_order_num);
    }

    @Override
    public List<Map<String, Object>> getMonthlySalesStats() {
        List<Map<String, Object>> dbData = salesOrderMapper.getMonthlySalesStats();
        Map<String, Object> dataMap = dbData.stream()
                .collect(Collectors.toMap(
                        m -> (String) m.get("month"),
                        m -> m.get("total")
                ));

        int currentYear = YearMonth.now().getYear();
        List<Map<String, Object>> fullData = IntStream.rangeClosed(1, 12)
                .mapToObj(month -> {
                    String monthStr = String.format("%d-%02d", currentYear, month);
                    Map<String, Object> map = new HashMap<>();
                    map.put("month", monthStr);
                    map.put("total", dataMap.getOrDefault(monthStr, 0));
                    return map;
                })
                .collect(Collectors.toList());

        return fullData;
    }
} 