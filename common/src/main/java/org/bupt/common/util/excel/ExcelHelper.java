package org.bupt.common.util.excel;


import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

/**
 * Created by ken on 2017/6/6.
 * 对excel读取、写入的工具类
 */
public class ExcelHelper {


    //写入
    public synchronized static boolean write(String path, List<ExcelCell> excelCells) {

        try {

            XSSFWorkbook workbook = read(path);
            XSSFSheet sheet = workbook.getSheet("生化");
            if (sheet == null)
                sheet = workbook.createSheet("生化");
            Header header = sheet.getHeader();//得到页眉
            header.setLeft("页眉左边");
            header.setRight("页眉右边");
            header.setCenter("页眉中间");


            PrintSetup print = sheet.getPrintSetup();//得到打印对象
            print.setUsePage(false);
            print.setPageStart((short) 1);//设置打印起始页码


            //写入到内存
            for (ExcelCell ce : excelCells) {

                XSSFRow row = sheet.getRow(ce.getRow());
                if (row == null) row = sheet.createRow(ce.getRow());
                XSSFCell cell = row.getCell(ce.getCol());
                if (cell == null) cell = row.createCell(ce.getCol());
                cell.setCellValue(ce.getContent());
            }

            //写入到文件
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(new File(path));
                workbook.write(out);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        System.out.println("successfully wrote into an existed excel at " + path);
        return true;
    }


    //读取workbook
    public synchronized static XSSFWorkbook read(String path) {

        //若在path下找到对应的excel
        XSSFWorkbook workbook = null;
        File file = new File(path);
        if (file != null && file.isFile() && file.exists()) { //已存在

            try {
                FileInputStream in = new FileInputStream(file);
                workbook = new XSSFWorkbook(in);
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("successfully opened an existed excel at" + path);
        } else { //若不存在创建新的

            //Create Blank workbook
            workbook = new XSSFWorkbook();
            //Create file system using specific name
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(new File(path));
                workbook.write(out);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("successfully created a new excel at" + path);
        }

        return workbook;
    }
}
