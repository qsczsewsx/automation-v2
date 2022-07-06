package com.tcbs.automation.ops;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum DocType {
  HSPL("HSPL", "HS pháp lý", "docx"),
  HSPL1("HSPL1", "Quyết định bổ nhiệm", "docx"),
  HSPL2("HSPL2", "Chứng minh thư hoặc căn cước công dân", "docx"),
  HSPL3("HSPL3", "Thông báo chữ ký để giao dịch", "docx"),
  MB01("MB01", "MB01", "docx"),
  MB03("MB03", "MB03", "docx"),
  MB04("MB04", "MB04", "docx"),
  MB05("MB05", "MB05", "docx"),
  MB06("MB06", "MB06", "docx"),
  MB07("MB07", "MB07", "docx"),
  MB09("MB09", "MB09", "docx"),
  MS52("MS52", "Phiếu yêu cầu đăng ký biện pháp bảo đảm", "docx"),
  MS53("MS53", "Bản kê chứng khoán đề nghị đăng ký biện pháp bảo đảm", "docx"),
  MS56("MS56", "Phiếu yêu cầu xóa đăng ký biện pháp bảo đảm", "docx"),
  MS57("MS57", "Bảng kê chứng khoán đề nghị xóa đăng ký biện pháo bảo đảm", "docx"),
  LK31("31LK", "31LK", "docx"),
  LK32("32LK", "32LK", "docx"),
  LK33("33LK", "33LK", "xlsx"),
  LK34("34LK", "34LK", "docx"),
  LK35("35LK", "35LK", "xlsx"),
  OTHER("OTHER", "OTHER", "*"),
  BGTP("BGTP", "Báo giá trái phiếu", "xlsx");

  @JsonValue
  private String value;
  private String name;
  private String fileExtension;

  @JsonCreator
  public static DocType fromValue(String value) throws Exception {
    Optional<DocType> comp = Stream.of(values()).filter(e -> e.getValue().equals(value)).findFirst();
    if (comp.isPresent()) {
      return comp.get();
    }
    throw new Exception("Unknown DocType: " + value);
  }

  @Override
  public String toString() {
    return value;
  }

}
