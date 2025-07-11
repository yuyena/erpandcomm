package kr.spring.sales.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.sales.dao.SalesOrderMapper;
import kr.spring.sales.vo.SalesOrderVO;
import kr.spring.sales.vo.SalesOrderDetailVO;

@Service
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
} 