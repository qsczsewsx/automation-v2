package com.tcbs.automation.hfcdata;

import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "HSX_CW_ARTICLE")
public class HsxCwArticleEntity {
  private String symbol;
  @Id
  private String id;
  private String title;
  private Timestamp publishTime;
  private String attachmentLink;
  private Timestamp createDate;
  private Timestamp ngayDkChaoBan;
  private Timestamp ngayDkChaoBanBs;
  private Timestamp ngayPhatHanhLanDau;
  private Timestamp ngayPhatHanhBoSung;
  private String infoBox;

  @Step("insert data")
  public static void insertData(HsxCwArticleEntity entity) {
    Session session = HfcData.hfcDataDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" INSERT INTO HSX_CW_ARTICLE ");
    queryStringBuilder.append(" (SYMBOL, ID, TITLE, PUBLISH_TIME, ATTACHMENT_LINK, CREATE_DATE, ");
    if (entity.getNgayDkChaoBan() != null) {
      queryStringBuilder.append("  NGAY_DK_CHAO_BAN, ");
    }
    if (entity.getNgayDkChaoBanBs() != null) {
      queryStringBuilder.append("  NGAY_DK_CHAO_BAN_BS, ");
    }
    if (entity.getNgayPhatHanhLanDau() != null) {
      queryStringBuilder.append("  NGAY_PHAT_HANH_LAN_DAU, ");
    }
    if (entity.getNgayPhatHanhBoSung() != null) {
      queryStringBuilder.append("  NGAY_PHAT_HANH_BO_SUNG, ");
    }
    queryStringBuilder.append(" INFO_BOX )");
    queryStringBuilder.append(" values (?, ?, ?, ?, ?, ?, ?, ? )");

    org.hibernate.query.Query<?> query = session.createNativeQuery(queryStringBuilder.toString());
    query.setParameter(1, entity.getSymbol());
    query.setParameter(2, entity.getId());
    query.setParameter(3, entity.getTitle());
    query.setParameter(4, entity.getPublishTime());
    query.setParameter(5, entity.getAttachmentLink());
    query.setParameter(6, entity.getCreateDate());
    if (entity.getNgayDkChaoBan() != null) {
      query.setParameter(7, entity.getNgayDkChaoBan());
    }
    if (entity.getNgayDkChaoBanBs() != null) {
      query.setParameter(7, entity.getNgayDkChaoBanBs());
    }
    if (entity.getNgayPhatHanhLanDau() != null) {
      query.setParameter(7, entity.getNgayPhatHanhLanDau());
    }
    if (entity.getNgayPhatHanhBoSung() != null) {
      query.setParameter(7, entity.getNgayPhatHanhBoSung());
    }
    query.setParameter(8, entity.getInfoBox());
    query.executeUpdate();
    trans.commit();

  }

  @Step("delete data by object")
  public static void deleteData(HsxCwArticleEntity entity) {
    Session session = HfcData.hfcDataDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query;

    query = session.createNativeQuery(" DELETE from HSX_CW_ARTICLE where SYMBOL = :cw");

    query.setParameter("cw", entity.getSymbol());
    query.executeUpdate();
    trans.commit();
  }

  @Basic
  @Column(name = "SYMBOL")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  @Basic
  @Column(name = "ID")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Basic
  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Basic
  @Column(name = "PUBLISH_TIME")
  public Timestamp getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(Timestamp publishTime) {
    this.publishTime = publishTime;
  }

  @Basic
  @Column(name = "ATTACHMENT_LINK")
  public String getAttachmentLink() {
    return attachmentLink;
  }

  public void setAttachmentLink(String attachmentLink) {
    this.attachmentLink = attachmentLink;
  }

  @Basic
  @Column(name = "CREATE_DATE")
  public Timestamp getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Timestamp createDate) {
    this.createDate = createDate;
  }

  @Basic
  @Column(name = "NGAY_DK_CHAO_BAN")
  public Timestamp getNgayDkChaoBan() {
    return ngayDkChaoBan;
  }

  public void setNgayDkChaoBan(Timestamp ngayDkChaoBan) {
    this.ngayDkChaoBan = ngayDkChaoBan;
  }

  @Basic
  @Column(name = "NGAY_DK_CHAO_BAN_BS")
  public Timestamp getNgayDkChaoBanBs() {
    return ngayDkChaoBanBs;
  }

  public void setNgayDkChaoBanBs(Timestamp ngayDkChaoBanBs) {
    this.ngayDkChaoBanBs = ngayDkChaoBanBs;
  }

  @Basic
  @Column(name = "NGAY_PHAT_HANH_LAN_DAU")
  public Timestamp getNgayPhatHanhLanDau() {
    return ngayPhatHanhLanDau;
  }

  public void setNgayPhatHanhLanDau(Timestamp ngayPhatHanhLanDau) {
    this.ngayPhatHanhLanDau = ngayPhatHanhLanDau;
  }

  @Basic
  @Column(name = "NGAY_PHAT_HANH_BO_SUNG")
  public Timestamp getNgayPhatHanhBoSung() {
    return ngayPhatHanhBoSung;
  }

  public void setNgayPhatHanhBoSung(Timestamp ngayPhatHanhBoSung) {
    this.ngayPhatHanhBoSung = ngayPhatHanhBoSung;
  }

  @Basic
  @Column(name = "INFO_BOX")
  public String getInfoBox() {
    return infoBox;
  }

  public void setInfoBox(String infoBox) {
    this.infoBox = infoBox;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HsxCwArticleEntity that = (HsxCwArticleEntity) o;
    return Objects.equals(symbol, that.symbol) && Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(publishTime,
      that.publishTime) && Objects.equals(attachmentLink, that.attachmentLink) && Objects.equals(createDate, that.createDate) && Objects.equals(ngayDkChaoBan,
      that.ngayDkChaoBan) && Objects.equals(ngayDkChaoBanBs, that.ngayDkChaoBanBs) && Objects.equals(ngayPhatHanhLanDau, that.ngayPhatHanhLanDau) && Objects.equals(
      ngayPhatHanhBoSung, that.ngayPhatHanhBoSung) && Objects.equals(infoBox, that.infoBox);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, id, title, publishTime, attachmentLink, createDate, ngayDkChaoBan, ngayDkChaoBanBs, ngayPhatHanhLanDau, ngayPhatHanhBoSung, infoBox);
  }
}
