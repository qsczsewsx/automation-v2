package com.automation.tools;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Calendar;
import java.util.Date;

public class Excel {
  public static void setValueForCell(XSSFSheet sheet, CellReference cellReference, Date value) {
    sheet.getRow(cellReference.getRow()).getCell(cellReference.getCol()).setCellValue(value);
  }

  public static void setValueForCell(XSSFSheet sheet, CellReference cellReference, Calendar value) {
    sheet.getRow(cellReference.getRow()).getCell(cellReference.getCol()).setCellValue(value.getTime());
  }

  public static void setValueForCell(XSSFSheet sheet, CellReference cellReference, String value) {
    sheet.getRow(cellReference.getRow()).getCell(cellReference.getCol()).setCellValue(value);
  }

  public static void setValueForCell(XSSFSheet sheet, CellReference cellReference, Double value) {
    sheet.getRow(cellReference.getRow()).getCell(cellReference.getCol()).setCellValue(value);
  }

  public static void setValueForCell(XSSFSheet sheet, CellReference cellReference, Boolean value) {
    sheet.getRow(cellReference.getRow()).getCell(cellReference.getCol()).setCellValue(value);
  }

  public static Object getValue(XSSFSheet sheet, CellReference cellReference) {
    return ExcelUtils.getValueFromObject(sheet.getRow(cellReference.getRow()).getCell(cellReference.getCol()));
  }

  public static Cell getCellFromReference(XSSFSheet sheet, String reference) {
    CellReference ref = new CellReference(reference);
    Row r = sheet.getRow(ref.getRow());
    Cell c = null;
    if (r != null) {
      c = r.getCell(ref.getCol());
    }
    return c;
  }
}
