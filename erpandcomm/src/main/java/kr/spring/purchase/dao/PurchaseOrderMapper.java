package kr.spring.purchase.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Delete;

import kr.spring.purchase.vo.PurchaseOrderVO;
import kr.spring.purchase.vo.PurchaseOrderDetailVO;

@Mapper
public interface PurchaseOrderMapper {
    // purchase_order CRUD
    void insertPurchaseOrder(PurchaseOrderVO purchaseOrder);
    PurchaseOrderVO selectPurchaseOrder(long purchase_order_num);
    List<PurchaseOrderVO> selectPurchaseOrderList();
    List<PurchaseOrderVO> selectPurchaseOrderListByMap(Map<String, Object> map);
    void updatePurchaseOrder(PurchaseOrderVO purchaseOrder);
    void deletePurchaseOrder(long purchase_order_num);

    // purchase_order_detail CRUD
    void insertPurchaseOrderDetail(PurchaseOrderDetailVO detail);
    List<PurchaseOrderDetailVO> selectPurchaseOrderDetailList(long purchase_order_num);
    void updatePurchaseOrderDetail(PurchaseOrderDetailVO detail);
    void deletePurchaseOrderDetail(long purchase_order_num);

    List<Map<String, Object>> getMonthlyPurchaseStats();
} 