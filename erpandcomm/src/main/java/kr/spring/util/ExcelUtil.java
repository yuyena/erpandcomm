package kr.spring.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.http.HttpServletResponse;
import kr.spring.product.vo.ProductVO;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.io.IOException;
import java.util.List;

public class ExcelUtil {
    
    public static void createProductExcel(HttpServletResponse response, List<ProductVO> productList) throws IOException {
        // 워크북 생성
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("상품목록");
        
        // 헤더 스타일 설정
        CellStyle headerStyle = createHeaderStyle(workbook);
        
        // 헤더 행 생성
        createHeaderRow(sheet, headerStyle);
        
        // 데이터 행 생성
        createDataRows(sheet, workbook, productList);
        
        // 컬럼 너비 자동 조정
        autoSizeColumns(sheet);
        
        // HTTP 응답 설정 및 파일 출력
        writeExcelToResponse(response, workbook);
    }
    
    private static CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
        return headerStyle;
    }
    
    private static void createHeaderRow(XSSFSheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {"상품번호", "상품명", "카테고리", "단가", "단위", "상태"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }
    
    private static void createDataRows(XSSFSheet sheet, XSSFWorkbook workbook, List<ProductVO> productList) {
        // 데이터 셀 스타일
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        
        // 가격 셀 스타일 (우측 정렬)
        CellStyle priceStyle = workbook.createCellStyle();
        priceStyle.cloneStyleFrom(dataStyle);
        priceStyle.setAlignment(HorizontalAlignment.RIGHT);
        
        for (int i = 0; i < productList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            ProductVO product = productList.get(i);
            
            // 데이터 입력
            createCell(row, 0, product.getProduct_num(), dataStyle);
            createCell(row, 1, product.getProduct_name(), dataStyle);
            createCell(row, 2, product.getCategory_name(), dataStyle);
            createCell(row, 3, product.getUnit_price(), priceStyle);
            createCell(row, 4, product.getUnit(), dataStyle);
            createCell(row, 5, product.getStatus() == 1 ? "판매중" : "판매중지", dataStyle);
        }
    }
    
    private static void createCell(Row row, int columnIndex, Object value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        
        cell.setCellStyle(style);
    }
    
    private static void autoSizeColumns(XSSFSheet sheet) {
        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private static void writeExcelToResponse(HttpServletResponse response, XSSFWorkbook workbook) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"product_list.xlsx\"");
        
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // 판매주문 목록 엑셀 생성
    public static void createSalesOrderExcel(HttpServletResponse response, List<kr.spring.sales.vo.SalesOrderVO> orderList) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("판매주문목록");
        CellStyle headerStyle = createHeaderStyle(workbook);
        Row headerRow = sheet.createRow(0);
        String[] headers = {"주문번호", "고객명", "주문일", "총금액", "담당자"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        CellStyle priceStyle = workbook.createCellStyle();
        priceStyle.cloneStyleFrom(dataStyle);
        priceStyle.setAlignment(HorizontalAlignment.RIGHT);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < orderList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            kr.spring.sales.vo.SalesOrderVO order = orderList.get(i);
            createCell(row, 0, order.getSales_order_num(), dataStyle);
            createCell(row, 1, order.getClient_name(), dataStyle);
            createCell(row, 2, order.getOrder_date() != null ? sdf.format(order.getOrder_date()) : "", dataStyle);
            createCell(row, 3, order.getTotal_price(), priceStyle);
            createCell(row, 4, order.getEmp_name(), dataStyle);
        }
        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"sales_order_list.xlsx\"");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // 구매주문 목록 엑셀 생성
    public static void createPurchaseOrderExcel(HttpServletResponse response, List<kr.spring.purchase.vo.PurchaseOrderVO> orderList) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("구매주문목록");
        CellStyle headerStyle = createHeaderStyle(workbook);
        Row headerRow = sheet.createRow(0);
        String[] headers = {"주문번호", "공급처", "주문일", "총금액", "담당자"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        CellStyle priceStyle = workbook.createCellStyle();
        priceStyle.cloneStyleFrom(dataStyle);
        priceStyle.setAlignment(HorizontalAlignment.RIGHT);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < orderList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            kr.spring.purchase.vo.PurchaseOrderVO order = orderList.get(i);
            createCell(row, 0, order.getPurchase_order_num(), dataStyle);
            createCell(row, 1, order.getSupplier_name() != null ? order.getSupplier_name() : order.getClient_name(), dataStyle);
            createCell(row, 2, order.getOrder_date() != null ? sdf.format(order.getOrder_date()) : "", dataStyle);
            createCell(row, 3, order.getTotal_price(), priceStyle);
            createCell(row, 4, order.getEmp_name(), dataStyle);
        }
        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"purchase_order_list.xlsx\"");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}