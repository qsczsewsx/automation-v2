package com.tcbs.automation.tcbsdbapi1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import wiremock.org.apache.commons.collections4.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "idata_deposit_productDictionary")
public class TermDepositProductDictionaryEntity {
  @Id
  private String productCode;
  private String productName;
  private String productNameEn;
  private String note;

  @Basic
  @Column(name = "ProductCode")
  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  @Basic
  @Column(name = "ProductName")
  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  @Basic
  @Column(name = "ProductName_En")
  public String getProductNameEn() {
    return productNameEn;
  }

  public void setProductNameEn(String productNameEn) {
    this.productNameEn = productNameEn;
  }

  @Basic
  @Column(name = "Note")
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TermDepositProductDictionaryEntity that = (TermDepositProductDictionaryEntity) o;
    return Objects.equals(productCode, that.productCode) &&
      Objects.equals(productName, that.productName) &&
      Objects.equals(productNameEn, that.productNameEn) &&
      Objects.equals(note, that.note);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productCode, productName, productNameEn, note);
  }

  @Step("Get data by key")
  public String getNameByCodeAndLang(String productCode, String lang) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM idata_deposit_productDictionary a  ");
    queryStringBuilder.append("WHERE productCode = :productCode  ");

    List<TermDepositProductDictionaryEntity> listResult = new ArrayList<>();
    try {
      List<Object[]> result = DbApi1.dbApiDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("productCode", productCode)
        .getResultList();

      if (CollectionUtils.isNotEmpty(result)) {
        result.forEach(object -> {
            TermDepositProductDictionaryEntity entity = TermDepositProductDictionaryEntity.builder()
              .productCode((String) object[0])
              .productName((String) object[1])
              .productNameEn((String) object[2])
              .note((String) object[3])
              .build();
            listResult.add(entity);
          }
        );
        if (lang.equals("vi")) {
          return listResult.get(0).getProductName();
        } else {
          return listResult.get(0).getProductNameEn();
        }
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }
}

