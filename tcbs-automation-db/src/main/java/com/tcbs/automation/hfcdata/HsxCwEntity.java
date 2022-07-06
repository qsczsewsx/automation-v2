package com.tcbs.automation.hfcdata;

import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "HSX_CW")
public class HsxCwEntity {
  @Id
  private String cwSymbol;
  private String cwType;
  private String cwName;
  private Long registrationVolume;
  private String underlyingSymbol;
  private String issuer;
  private String refLink;
  private Boolean isVsdUpdated;


  @Step("insert data")
  public static void insertData(HsxCwEntity entity) {
    Session session = HfcData.hfcDataDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" INSERT INTO HSX_CW ");
    queryStringBuilder.append("(CW_SYMBOL, CW_TYPE, CW_NAME, REGISTRATION_VOLUME, UNDERLYING_SYMBOL, ISSUER, REF_LINK,IS_VSD_UPDATED  ) ");
    queryStringBuilder.append("values (?, ?, ?, ?, ?, ?, ?, ? )");

    org.hibernate.query.Query<?> query = session.createNativeQuery(queryStringBuilder.toString());
    query.setParameter(1, entity.getCwSymbol());
    query.setParameter(2, entity.getCwType());
    query.setParameter(3, entity.getCwName());
    query.setParameter(4, entity.getRegistrationVolume());
    query.setParameter(5, entity.getUnderlyingSymbol());
    query.setParameter(6, entity.getIssuer());
    query.setParameter(7, entity.getRefLink());
    query.setParameter(8, entity.getIsVsdUpdated());
    query.executeUpdate();
    trans.commit();
  }

  @Step("delete data by object")
  public static void deleteData(HsxCwEntity entity) {
    Session session = HfcData.hfcDataDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query;

    query = session.createNativeQuery(" DELETE from HSX_CW where CW_SYMBOL = :cw");

    query.setParameter("cw", entity.getCwSymbol());
    query.executeUpdate();
    trans.commit();
  }

  @Basic
  @Column(name = "CW_SYMBOL")
  public String getCwSymbol() {
    return cwSymbol;
  }

  public void setCwSymbol(String cwSymbol) {
    this.cwSymbol = cwSymbol;
  }

  @Basic
  @Column(name = "CW_TYPE")
  public String getCwType() {
    return cwType;
  }

  public void setCwType(String cwType) {
    this.cwType = cwType;
  }

  @Basic
  @Column(name = "CW_NAME")
  public String getCwName() {
    return cwName;
  }

  public void setCwName(String cwName) {
    this.cwName = cwName;
  }

  @Basic
  @Column(name = "REGISTRATION_VOLUME")
  public Long getRegistrationVolume() {
    return registrationVolume;
  }

  public void setRegistrationVolume(Long registrationVolume) {
    this.registrationVolume = registrationVolume;
  }

  @Basic
  @Column(name = "UNDERLYING_SYMBOL")
  public String getUnderlyingSymbol() {
    return underlyingSymbol;
  }

  public void setUnderlyingSymbol(String underlyingSymbol) {
    this.underlyingSymbol = underlyingSymbol;
  }

  @Basic
  @Column(name = "ISSUER")
  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }


  @Basic
  @Column(name = "REF_LINK")
  public String getRefLink() {
    return refLink;
  }

  public void setRefLink(String refLink) {
    this.refLink = refLink;
  }

  @Basic
  @Column(name = "IS_VSD_UPDATED")
  public Boolean getVsdUpdated() {
    return isVsdUpdated;
  }

  public void setVsdUpdated(Boolean vsdUpdated) {
    isVsdUpdated = vsdUpdated;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HsxCwEntity that = (HsxCwEntity) o;
    return Objects.equals(cwSymbol, that.cwSymbol) && Objects.equals(cwType, that.cwType) && Objects.equals(cwName, that.cwName) && Objects.equals(
      registrationVolume, that.registrationVolume) && Objects.equals(underlyingSymbol, that.underlyingSymbol) && Objects.equals(issuer, that.issuer)
      && Objects.equals(refLink, that.refLink) && Objects.equals(isVsdUpdated, that.isVsdUpdated);
  }


  @Override
  public int hashCode() {
    return Objects.hash(cwSymbol, cwType, cwName, registrationVolume, underlyingSymbol, issuer, refLink, isVsdUpdated);
  }
}