package kr.spring.purchase.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.purchase.dao.PurchaseOrderMapper;
import kr.spring.purchase.vo.PurchaseOrderVO;
import kr.spring.purchase.vo.PurchaseOrderDetailVO;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Override
    @Transactional
    public void insertPurchaseOrder(PurchaseOrderVO purchaseOrder) {
        purchaseOrderMapper.insertPurchaseOrder(purchaseOrder);
    }

    @Override
    public PurchaseOrderVO selectPurchaseOrder(long purchase_order_num) {
        return purchaseOrderMapper.selectPurchaseOrder(purchase_order_num);
    }

    @Override
    public List<PurchaseOrderVO> selectPurchaseOrderList() {
        return purchaseOrderMapper.selectPurchaseOrderList();
    }

    @Override
    @Transactional
    public void updatePurchaseOrder(PurchaseOrderVO purchaseOrder) {
        purchaseOrderMapper.updatePurchaseOrder(purchaseOrder);
    }

    @Override
    @Transactional
    public void deletePurchaseOrder(long purchase_order_num) {
        purchaseOrderMapper.deletePurchaseOrder(purchase_order_num);
    }

    @Override
    @Transactional
    public void insertPurchaseOrderDetail(PurchaseOrderDetailVO detail) {
        purchaseOrderMapper.insertPurchaseOrderDetail(detail);
    }

    @Override
    public List<PurchaseOrderDetailVO> selectPurchaseOrderDetailList(long purchase_order_num) {
        return purchaseOrderMapper.selectPurchaseOrderDetailList(purchase_order_num);
    }

    @Override
    @Transactional
    public void updatePurchaseOrderDetail(PurchaseOrderDetailVO detail) {
        purchaseOrderMapper.updatePurchaseOrderDetail(detail);
    }

    @Override
    @Transactional
    public void deletePurchaseOrderDetail(long purchase_order_num) {
        purchaseOrderMapper.deletePurchaseOrderDetail(purchase_order_num);
    }
} 