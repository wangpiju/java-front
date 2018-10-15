package com.hs3.home.utils;

import jxl.Cell;
import jxl.Workbook;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jason.wang
 */
public class ExcelUtils {

    private static final Logger logger = Logger.getLogger(ExcelUtils.class);
    /**
     * 測試
     */
    public static void main(String[] args) {
        List<List<String>> content = new ArrayList<>();
        String title[][] = {{"方案号", "彩种id", "玩法id", "下注内容", "注单金额", "派彩", "开奖号码", "开奖结果"}};
        try {
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet();
            XSSFCellStyle style = ExcelUtils.getTitleStyleX(wb);
            XSSFRow row = null;
            content.add(Arrays.asList(title[0]));

            for (int i = 0; i < content.size(); i++) {
                row = sheet.createRow(i);
                for (int j = 0; j < content.get(i).size(); j++) {
                    ExcelUtils.createCell(row, j, i, content.get(i).get(j), style);
                }
            }

            ExcelUtils.createXFile(wb, "D://", "test.xlsx");
        } catch (IOException e) {
            logger.error("--> error", e);
        }
    }

    /**
     * 设置表头样式
     *
     * @param workbook
     * @return
     */
    public static HSSFCellStyle getTitleStyle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.WHITE.index);
        font.setFontHeight((short) 200);
        font.setFontName("楷体_GB2312");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        return style;
    }

    /**
     * 设置xlsx表头样式
     *
     * @param workbook
     * @return
     */
    public static XSSFCellStyle getTitleStyleX(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.WHITE.index);
        font.setFontHeight((short) 200);
        font.setFontName("楷体_GB2312");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        return style;
    }

    /**
     * 设置表格特别数据样式
     *
     * @param workbook
     * @return
     */
    public static HSSFCellStyle getDataStyle2(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeight((short) 200);
        font.setFontName("楷体_GB2312");

        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        return style;
    }

    /**
     * 设置xlsx表格特别数据样式
     *
     * @param workbook
     * @return
     */
    public static XSSFCellStyle getDataStyleX(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeight((short) 200);
        font.setFontName("楷体_GB2312");

        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        return style;
    }

    /**
     * 创建单元格内容
     *
     * @param row
     * @param id
     * @param value
     * @param style
     */
    public static void createCell(HSSFRow row, int id, int rowid, String value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(id);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(value);
        if (style != null && rowid == 0) {
            cell.setCellStyle(style);
        }
    }

    /**
     * 创建xlsx单元格内容
     *
     * @param row
     * @param id
     * @param value
     * @param style
     */
    public static void createCell(XSSFRow row, int id, int rowid, String value, XSSFCellStyle style) {
        XSSFCell cell = row.createCell(id);
        cell.setCellType(XSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(value);
        if (style != null && rowid == 0) {
            cell.setCellStyle(style);
        }
    }
    /**
     * 创建单元格内容
     *
     * @param row
     * @param id
     * @param value
     * @param style
     */
    public static void createCell(HSSFRow row, int id, String value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(id);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(value);
        if (style != null && id == 0) {
            cell.setCellStyle(style);
        }
    }

    /**
//     * 创建单元格内容
//     *
//     * @param row
//     * @param id
//     * @param value
//     * @param style
//     */
//    public static void createCellX(XSSFRow row, int id, String value, XSSFCellStyle style) {
//        XSSFCell cell = row.createCell(id);
//        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//        cell.setCellValue(value);
//        if (style != null && id == 0) {
//            cell.setCellStyle(style);
//        }
//    }

    /**
     * 创建单元格内容
     *
     * @param row
     * @param id
     * @param value
     * @param style
     */
    public static void createCellX(XSSFRow row, int id, Object value, String style) {
        XSSFCell cell = row.createCell(id);
        if(null == style){
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(value.toString());
        }

        else if(style.equals("number")){
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            Double result = (Double) value;
            cell.setCellValue(result);
        }
        XSSFCellStyle cellStyle=cell.getCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        cell.setCellStyle(cellStyle);
    }

    /**
     * 创建xlsx报表文件
     *
     * @param workbook
     * @param dir
     * @param filename
     * @throws IOException
     */
    public static void createXFile(XSSFWorkbook workbook, String dir, String filename) throws IOException {
        dir = dir == null ? "" : dir.trim();
        if (!"".equals(dir)) {
            if (!dir.endsWith(File.separator)) {
                dir += File.separator;
            }
        }
        File outdir = new File(dir);
        if (!outdir.exists()) {
            outdir.mkdirs();
        }
        FileOutputStream fOut = new FileOutputStream(dir + filename);
        workbook.write(fOut);
        fOut.flush();
        fOut.close();
    }

    /**
     * 创建报表文件
     *
     * @param workbook
     * @param dir
     * @param filename
     * @throws IOException
     */
    public static void createFile(HSSFWorkbook workbook, String dir, String filename) throws IOException {
        dir = dir == null ? "" : dir.trim();
        if (!"".equals(dir)) {
            if (!dir.endsWith(File.separator)) {
                dir += File.separator;
            }
        }
        File outdir = new File(dir);
        if (!outdir.exists()) {
            outdir.mkdirs();
        }
        FileOutputStream fOut = new FileOutputStream(dir + filename);
        workbook.write(fOut);
        fOut.flush();
        fOut.close();
    }

    /**
     * 读取Excel中所有的列
     *
     * @param filename
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private static List<Cell[]> jxlGetExcelColumns(String filename) throws IOException {
        InputStream is = null;
        jxl.Workbook rwb = null;
        List<Cell[]> list = new ArrayList<Cell[]>();
        try {
            is = new FileInputStream(filename);
            rwb = Workbook.getWorkbook(is);
            // Sheet[] sheets = rwb.getSheets();
            // int sheetLen = sheets.length;
            jxl.Sheet rs = rwb.getSheet(0); // 读取第一个工作表的数据

            // getRows() 获取总共多少列...getColumn(n)获取第n列...
            for (int i = 0; i < rs.getColumns(); i++) {
                list.add(rs.getColumn(i));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            rwb.close();
            is.close();
        }
        return list;
    }

    /**
     * 读取Excel中所有的行
     *
     * @param filename
     * @return
     */
    @SuppressWarnings("unused")
    private static List<Cell[]> jxlGetExcelRows(String filename) {
        InputStream is = null;
        jxl.Workbook rwb = null;
        List<Cell[]> list = new ArrayList<Cell[]>();
        try {
            is = new FileInputStream(filename);
            rwb = Workbook.getWorkbook(is);
            // Sheet[] sheets = rwb.getSheets();
            // int sheetLen = sheets.length;
            jxl.Sheet rs = rwb.getSheet(0); // 默认先读取第一个工作表的数据

            // getRows() 获取总共多少行...getRow(n)获取第n行...
            for (int i = 0; i < rs.getRows(); i++) {
                list.add(rs.getRow(i));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            rwb.close();
            try {
                is.close();
            } catch (IOException e) {
                logger.error("--> error", e);
            }
        }
        return list;
    }
}
