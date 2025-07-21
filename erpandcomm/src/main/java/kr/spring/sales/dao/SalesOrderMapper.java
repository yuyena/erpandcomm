package kr.spring.sales.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.spring.sales.vo.SalesOrderVO;
import kr.spring.sales.vo.SalesOrderDetailVO;

@Mapper
public interface SalesOrderMapper {
    // 판매주문 등록
    void insertSalesOrder(SalesOrderVO salesOrder);
    // 판매주문 개별 조회
    SalesOrderVO selectSalesOrder(long sales_order_num);
    // 판매주문 전체 목록 조회
    List<SalesOrderVO> selectSalesOrderList();
    // 판매주문 수정
    void updateSalesOrder(SalesOrderVO salesOrder);
    // 판매주문 삭제
    void deleteSalesOrder(long sales_order_num);
    // 판매주문 상세 등록
    void insertSalesOrderDetail(SalesOrderDetailVO detail);
    // 특정 판매주문에 대한 상세 목록 조회
    List<SalesOrderDetailVO> selectSalesOrderDetailList(long sales_order_num);
    // 판매주문 상세 수정
    void updateSalesOrderDetail(SalesOrderDetailVO detail);
    // 판매주문 상세 삭제 (주문번호 기준 전체 삭제)
    void deleteSalesOrderDetail(long sales_order_num);

    List<Map<String, Object>> getMonthlySalesStats();
} 