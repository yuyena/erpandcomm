package kr.spring.purchase.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.YearMonth;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.purchase.dao.PurchaseOrderMapper;
import kr.spring.purchase.vo.PurchaseOrderVO;
import kr.spring.purchase.vo.PurchaseOrderDetailVO;

@Service
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Override
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

    @Override
    public List<Map<String, Object>> getMonthlyPurchaseStats() {
        List<Map<String, Object>> dbData = purchaseOrderMapper.getMonthlyPurchaseStats();
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