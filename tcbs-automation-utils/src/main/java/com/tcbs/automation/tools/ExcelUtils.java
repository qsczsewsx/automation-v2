package com.tcbs.automation.tools;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@NoArgsConstructor
public class ExcelUtils {
  private static XSSFWorkbook wb;
  private static XSSFSheet sh;
  private static XSSFRow row;
  private static XSSFCell cell;
  private static Logging log = new Logging(ExcelUtils.class);

  public static XSSFSheet getSh() {
    return sh;
  }

  /**
   * get cell value
   *
   * @param cell
   * @returngetTableArray
   */
  public static Object getValueFromObject(Cell cell) {
    if (cell != null) {
      return getValueFromObject(cell, cell.getCellType());
    }
    return null;
  }

  /**
   * get cell value by type
   *
   * @param cell
   * @param type
   * @return
   */
  public static Object getValueFromObject(Cell cell, CellType type) {
    if (type == CellType.NUMERIC) {
      if (DateUtil.isCellDateFormatted(cell)) {
        return cell.getDateCellValue();
      } else {
        return cell.getNumericCellValue();
      }
    } else if (type == CellType.STRING) {
      return cell.getStringCellValue();
    } else if (type == CellType.BOOLEAN) {
      return cell.getBooleanCellValue();
    } else if (type == CellType.FORMULA) {
      return getValueFromObject(cell, cell.getCachedFormulaResultType());
    } else {
      return null;
    }
  }

  public static List<HashMap<String, String>> readFileExcel(String path, String sheetName) {
    try {
      return ExcelUtils.getDataInExcel(System.getProperty("user.dir") + path, sheetName == null ? "Sheet1" : sheetName);
    } catch (Exception ex) {
      return null;
    }
  }

  /* Read data in Excel file */
  public static List<HashMap<String, String>> getDataInExcel(String path, String sheetName) throws IOException {
    List<HashMap<String, String>> data = new ArrayList<>();
    setFile(path, sheetName);
    int i = 0;
    while (getCellValue(i + 1, 0) != null) {
      HashMap<String, String> row = new HashMap<String, String>();
      int j = 0;
      while (getCellValue(0, j) != null) {
        row.put(getCellValue(0, j), StringUtils.isBlank(getCellValue(i + 1, j)) ? null
          : getCellValue(i + 1, j));
        j++;
      }
      data.add(row);
      i++;
    }
    return data;
  }

  public static void setFile(String url, String sheet) throws IOException {
    wb = new XSSFWorkbook(new FileInputStream(url));
    sh = wb.getSheet(sheet);
  }

  public static String getCellValue(int h, int c) {
    String cellData;
    try {
      cell = sh.getRow(h).getCell(c);
      cellData = cell.getStringCellValue();
    } catch (Exception e) {
      try {
        cellData = String.valueOf(cell.getRawValue());
      } catch (Exception ex) {
        cellData = null;
      }
    }
    return cellData;
  }

  public static void setExcelFile(String path, String sheetName) throws Exception {
    FileInputStream excelFile = new FileInputStream(path);
    wb = new XSSFWorkbook(excelFile);
    wb.setForceFormulaRecalculation(true);
    sh = wb.getSheet(sheetName);
    sh.setForceFormulaRecalculation(true);
  }

  public static void recalculatingAllFormulasInSheet() {
    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
    Iterator var2 = wb.iterator();

    while (var2.hasNext()) {
      Sheet s = (Sheet) var2.next();
      Iterator var4 = s.iterator();

      while (var4.hasNext()) {
        Row r = (Row) var4.next();
        Iterator var6 = r.iterator();

        while (var6.hasNext()) {
          Cell c = (Cell) var6.next();
          if (c.getCellType() == CellType.FORMULA) {
            try {
              evaluator.evaluateFormulaCell(c);
            } catch (Exception var9) {
              log.error("sheet " + s.getSheetName() + " cell " + c.getAddress() + " column index " + c.getColumnIndex() + " row index " + c.getRowIndex() + " error message:" + var9.getMessage());
            }
          }
        }
      }
    }
  }

  public static int getNumrow() throws Exception {
    try {
      int numrow = sh.getLastRowNum() - sh.getFirstRowNum();
      return numrow;
    } catch (Exception var2) {
      return 0;
    }
  }

  public static int getLastColumnIndex() throws Exception {
    try {
      boolean cont = true;
      int columnIndex = 0;

      while (cont) {
        if (getCellData(sh.getFirstRowNum(), columnIndex) != "") {
          ++columnIndex;
        } else {
          cont = false;
        }
      }

      return columnIndex;
    } catch (Exception var3) {
      return 0;
    }
  }

  public static String getCellData(int rowNum, int colNum) {
    try {
      String cellData = "";
      Cell cell = sh.getRow(rowNum).getCell(colNum);
      if (cell.getCellType() == CellType.STRING) {
        cellData = cell.getStringCellValue();
      } else if (cell.getCellType() == CellType.NUMERIC) {
        cellData = String.valueOf(cell.getNumericCellValue());
      }

      return cellData;
    } catch (Exception var5) {
      return "";
    }
  }

  public static void setCellData(String filepath, String result, int rowNum, int colNum) throws Exception {
    try {
      row = sh.getRow(rowNum);
      if (row == null) {
        row = sh.createRow(rowNum);
      }

      cell = row.getCell(colNum, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
      if (cell == null) {
        cell = row.createCell(colNum);
        cell.setCellValue(result);
      } else {
        cell.setCellValue(result);
      }

      FileOutputStream fileOut = new FileOutputStream(filepath);
      wb.write(fileOut);
      fileOut.flush();
      fileOut.close();
    } catch (Exception var6) {
      log.error(var6.getMessage());
    }
  }

  public static void setRowData(String filepath, String[] arrResult, int rowNum, int colNum) throws Exception {
    try {
      row = sh.getRow(rowNum);
      if (row == null) {
        row = sh.createRow(rowNum);
      }

      for (int i = 0; i < arrResult.length; ++i) {
        cell = row.getCell(colNum + i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) {
          cell = row.createCell(colNum + i);
          cell.setCellValue(arrResult[i]);
        } else {
          cell.setCellValue(arrResult[i]);
        }
      }

      FileOutputStream fileOut = new FileOutputStream(filepath);
      wb.write(fileOut);
      fileOut.flush();
      fileOut.close();
    } catch (Exception var6) {
      log.error(var6.getMessage());
    }
  }

  public static int getColumnIndex(Integer rowNum, String value) {
    int columnIndex = -1;

    try {
      int totalColumn = getLastColumnIndex();

      for (int i = 0; i < totalColumn; ++i) {
        cell = sh.getRow(rowNum).getCell(i);
        String cellData = cell.getStringCellValue().trim();
        if (cellData.equals(value)) {
          columnIndex = i;
        }
      }
    } catch (Exception var7) {
      log.error(var7.getMessage());
    }

    return columnIndex;
  }

  public static String[][] getTableArrayData(int totalRows, int filterColumnIndex, int totalCols, int filterRows, int filterColumns, String filter) {
    String[][] tabArray = new String[filterRows][filterColumns];
    int ci = 0;
    int startRow = 1;
    int startCol = 0;

    for (int i = startRow; i <= totalRows; ++i) {
      int cj = 0;
      if (filterColumnIndex == -1 | getCellData(i, filterColumnIndex).equals(filter) | filter == "") {
        for (int j = startCol; j < totalCols; ++j) {
          try {
            if (j != filterColumnIndex) {
              tabArray[ci][cj] = getCellData(i, j).toString();
              ++cj;
            }
          } catch (Exception var18) {
            log.error(var18.getMessage());
          }
        }

        ++ci;
      }
    }

    return tabArray;
  }

  public static Object[][] getTableArray(String filePath, String sheetName, String filter) throws IOException {
    FileInputStream excelFile = new FileInputStream(filePath);
    wb = new XSSFWorkbook(excelFile);
    sh = wb.getSheet(sheetName);
    int filterColumnIndex = getColumnIndex(0, "filter");
    int startRow = 1;
    int totalRows = sh.getLastRowNum() - sh.getFirstRowNum();
    int totalCols = 0;

    try {
      totalCols = getLastColumnIndex();
    } catch (Exception var19) {
      log.error(var19.getMessage());
    }

    int filterRows = 0;

    int filterColumns;
    for (filterColumns = startRow; filterColumns <= totalRows; ++filterColumns) {
      if (filterColumnIndex == -1 | getCellData(filterColumns, filterColumnIndex).equals(filter) | filter == "") {
        ++filterRows;
      }
    }

    filterColumns = 0;
    if (filterColumnIndex > -1) {
      filterColumns = totalCols - 1;
    } else {
      filterColumns = totalCols;
    }

    return getTableArrayData(totalRows, filterColumnIndex, totalCols, filterRows, filterColumns, filter);
  }

  public static Object[][] getExcelSheet(String filePath, String sheetName, int startRow, int startCol) {
    String[][] tabArray = (String[][]) null;

    try {
      FileInputStream excelFile = new FileInputStream(filePath);
      wb = new XSSFWorkbook(excelFile);
      sh = wb.getSheet(sheetName);
      int totalRows = sh.getLastRowNum() + 1 - startRow;
      int totalCols = 0;

      try {
        totalCols = getLastColumnIndex();
      } catch (Exception var15) {
        log.error(var15.getMessage());
      }

      tabArray = new String[totalRows][totalCols];
      int ci = 0;
      int cj = 0;

      for (int i = startRow; i <= totalRows; ++i) {
        for (int j = startCol; j < totalCols; ++j) {
          try {
            tabArray[ci][cj++] = getCellData(i, j).toString();
          } catch (Exception var14) {
            log.error(var14.getMessage());
          }
        }

        ++ci;
        cj = 0;
      }

      excelFile.close();
    } catch (FileNotFoundException var16) {
      log.error(var16.getMessage());
    } catch (IOException var17) {
      log.error(var17.getMessage());
    }

    return tabArray;
  }
}