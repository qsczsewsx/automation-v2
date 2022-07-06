package com.tcbs.automation.bondlifecycle.dto;

import java.util.List;

public class CategoryDetail {
  private String categoryType;
  private List<ReferenceDataDto> refs;

  public CategoryDetail() {
  }

  public CategoryDetail(String categoryType, List<ReferenceDataDto> refs) {
    this.categoryType = categoryType;
    this.refs = refs;
  }

  public String getCategoryType() {
    return categoryType;
  }

  public void setCategoryType(String categoryType) {
    this.categoryType = categoryType;
  }

  public List<ReferenceDataDto> getRefs() {
    return refs;
  }

  public void setRefs(List<ReferenceDataDto> refs) {
    this.refs = refs;
  }
}
