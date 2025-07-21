package kr.spring.purchase.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Delete;

import kr.spring.purchase.vo.PurchaseOrderVO;
import kr.spring.purchase.vo.PurchaseOrderDetailVO;

@Mapper
public interface PurchaseOrderMapper {
    // 구매주문 등록
    void insertPurchaseOrder(PurchaseOrderVO purchaseOrder);
    // 구매주문 개별 조회
    PurchaseOrderVO selectPurchaseOrder(long purchase_order_num);
    // 구매주문 전체 목록 조회
    List<PurchaseOrderVO> selectPurchaseOrderList();
    // 구매주문 수정
    void updatePurchaseOrder(PurchaseOrderVO purchaseOrder);
    // 구매주문 삭제
    void deletePurchaseOrder(long purchase_order_num);

    // 구매주문 상세 등록
    void insertPurchaseOrderDetail(PurchaseOrderDetailVO detail);
    // 특정 구매주문에 대한 상세 목록 조회
    List<PurchaseOrderDetailVO> selectPurchaseOrderDetailList(long purchase_order_num);
    // 구매주문 상세 수정
    void updatePurchaseOrderDetail(PurchaseOrderDetailVO detail);
    // 구매주문 상세 삭제 (주문번호 기준 전체 삭제)
    @Delete("DELETE FROM purchase_order_detail WHERE purchase_order_num = #{purchase_order_num}")
    void deletePurchaseOrderDetail(long purchase_order_num);

    List<Map<String, Object>> getMonthlyPurchaseStats();
} 