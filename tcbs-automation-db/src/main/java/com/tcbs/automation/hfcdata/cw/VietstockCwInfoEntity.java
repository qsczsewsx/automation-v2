package com.tcbs.automation.hfcdata.cw;

import com.tcbs.automation.hfcdata.HfcData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
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
@Table(name = "VIETSTOCK_CW_INFO")
public class VietstockCwInfoEntity {
  @Id
  private String cwSymbol;
  private String ckCoso;
  private String tochucphathanhCkCoso;
  private String tochucphathanhCw;
  private String loaichungquyen;
  private String kieuthuchien;
  private String phuongthucthuchienquyen;
  private String thoihan;
  private Timestamp ngayphathanh;
  private Timestamp ngayniemyet;
  private Timestamp ngaygdDautien;
  private Timestamp ngaygdCuoicung;
  private Timestamp ngaydaohan;
  private String tilechuyendoi;
  private String tlcdDieuchinh;
  private Long giaphathanh;
  private Long giathuchien;
  private Long giathDieuchinh;
  private Integer klNiemyet;
  private Integer klLuuhanh;
  private String urlCaobachphathanh;
  private Timestamp createdate;
  private Timestamp updatedDate;

  private String lang;

  @Step("Insert data")
  public static void insertData(VietstockCwInfoEntity entity) {
    Session session = HfcData.hfcDataDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();
    if (entity.getLang().equalsIgnoreCase("vi")) {
      queryStringBuilder.append("INSERT INTO VIETSTOCK_CW_INFO");
    } else {
      queryStringBuilder.append("INSERT INTO VIETSTOCK_CW_INFO_ENG");
    }
    queryStringBuilder.append("(CW_SYMBOL,CK_COSO,TOCHUCPHATHANH_CK_COSO,TOCHUCPHATHANH_CW,LOAICHUNGQUYEN, KIEUTHUCHIEN," +
      "PHUONGTHUCTHUCHIENQUYEN,THOIHAN,NGAYPHATHANH,NGAYNIEMYET,NGAYGD_DAUTIEN,NGAYGD_CUOICUNG,NGAYDAOHAN,TILECHUYENDOI,TLCD_DIEUCHINH," +
      "GIAPHATHANH,GIATHUCHIEN,GIATH_DIEUCHINH,KL_NIEMYET,KL_LUUHANH,URL_CAOBACHPHATHANH,CREATEDATE,UPDATED_DATE)");
    queryStringBuilder.append("value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
    org.hibernate.query.Query<?> query = session.createNativeQuery(queryStringBuilder.toString());
    query.setParameter(1, entity.getCwSymbol());
    query.setParameter(2, entity.getCkCoso());
    query.setParameter(3, entity.getTochucphathanhCkCoso());
    query.setParameter(4, entity.getTochucphathanhCw());
    query.setParameter(5, entity.getLoaichungquyen());
    query.setParameter(6, entity.getKieuthuchien());
    query.setParameter(7, entity.getPhuongthucthuchienquyen());
    query.setParameter(8, entity.getThoihan());
    query.setParameter(9, entity.getNgayphathanh());
    query.setParameter(10, entity.getNgayniemyet());
    query.setParameter(11, entity.getNgaygdDautien());
    query.setParameter(12, entity.getNgaygdCuoicung());
    query.setParameter(13, entity.getNgaydaohan());
    query.setParameter(14, entity.getTilechuyendoi());
    query.setParameter(15, entity.getTlcdDieuchinh());
    query.setParameter(16, entity.getGiaphathanh());
    query.setParameter(17, entity.getGiathuchien());
    query.setParameter(18, entity.getGiathDieuchinh());
    query.setParameter(19, entity.getKlNiemyet());
    query.setParameter(20, entity.getKlLuuhanh());
    query.setParameter(21, entity.getUrlCaobachphathanh());
    query.setParameter(22, entity.getCreatedate());
    query.setParameter(23, entity.getUpdatedDate());
    query.executeUpdate();
    trans.commit();
  }

  @Step("Delete data by key")
  public static void deleteData(VietstockCwInfoEntity entity) {
    Session session = HfcData.hfcDataDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query;
    if (entity.getLang().equalsIgnoreCase("vi")) {
      query = session.createNativeQuery("DELETE FROM VIETSTOCK_CW_INFO WHERE CW_SYMBOL = :cw");
    } else {
      query = session.createNativeQuery("DELETE FROM VIETSTOCK_CW_INFO_ENG WHERE CW_SYMBOL = :cw");
    }
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
  @Column(name = "CK_COSO")
  public String getCkCoso() {
    return ckCoso;
  }

  public void setCkCoso(String ckCoso) {
    this.ckCoso = ckCoso;
  }

  @Basic
  @Column(name = "TOCHUCPHATHANH_CK_COSO")
  public String getTochucphathanhCkCoso() {
    return tochucphathanhCkCoso;
  }

  public void setTochucphathanhCkCoso(String tochucphathanhCkCoso) {
    this.tochucphathanhCkCoso = tochucphathanhCkCoso;
  }

  @Basic
  @Column(name = "TOCHUCPHATHANH_CW")
  public String getTochucphathanhCw() {
    return tochucphathanhCw;
  }

  public void setTochucphathanhCw(String tochucphathanhCw) {
    this.tochucphathanhCw = tochucphathanhCw;
  }

  @Basic
  @Column(name = "LOAICHUNGQUYEN")
  public String getLoaichungquyen() {
    return loaichungquyen;
  }

  public void setLoaichungquyen(String loaichungquyen) {
    this.loaichungquyen = loaichungquyen;
  }

  @Basic
  @Column(name = "KIEUTHUCHIEN")
  public String getKieuthuchien() {
    return kieuthuchien;
  }

  public void setKieuthuchien(String kieuthuchien) {
    this.kieuthuchien = kieuthuchien;
  }

  @Basic
  @Column(name = "PHUONGTHUCTHUCHIENQUYEN")
  public String getPhuongthucthuchienquyen() {
    return phuongthucthuchienquyen;
  }

  public void setPhuongthucthuchienquyen(String phuongthucthuchienquyen) {
    this.phuongthucthuchienquyen = phuongthucthuchienquyen;
  }

  @Basic
  @Column(name = "THOIHAN")
  public String getThoihan() {
    return thoihan;
  }

  public void setThoihan(String thoihan) {
    this.thoihan = thoihan;
  }

  @Basic
  @Column(name = "NGAYPHATHANH")
  public Timestamp getNgayphathanh() {
    return ngayphathanh;
  }

  public void setNgayphathanh(Timestamp ngayphathanh) {
    this.ngayphathanh = ngayphathanh;
  }

  @Basic
  @Column(name = "NGAYNIEMYET")
  public Timestamp getNgayniemyet() {
    return ngayniemyet;
  }

  public void setNgayniemyet(Timestamp ngayniemyet) {
    this.ngayniemyet = ngayniemyet;
  }

  @Basic
  @Column(name = "NGAYGD_DAUTIEN")
  public Timestamp getNgaygdDautien() {
    return ngaygdDautien;
  }

  public void setNgaygdDautien(Timestamp ngaygdDautien) {
    this.ngaygdDautien = ngaygdDautien;
  }

  @Basic
  @Column(name = "NGAYGD_CUOICUNG")
  public Timestamp getNgaygdCuoicung() {
    return ngaygdCuoicung;
  }

  public void setNgaygdCuoicung(Timestamp ngaygdCuoicung) {
    this.ngaygdCuoicung = ngaygdCuoicung;
  }

  @Basic
  @Column(name = "NGAYDAOHAN")
  public Timestamp getNgaydaohan() {
    return ngaydaohan;
  }

  public void setNgaydaohan(Timestamp ngaydaohan) {
    this.ngaydaohan = ngaydaohan;
  }

  @Basic
  @Column(name = "TILECHUYENDOI")
  public String getTilechuyendoi() {
    return tilechuyendoi;
  }

  public void setTilechuyendoi(String tilechuyendoi) {
    this.tilechuyendoi = tilechuyendoi;
  }

  @Basic
  @Column(name = "TLCD_DIEUCHINH")
  public String getTlcdDieuchinh() {
    return tlcdDieuchinh;
  }

  public void setTlcdDieuchinh(String tlcdDieuchinh) {
    this.tlcdDieuchinh = tlcdDieuchinh;
  }

  @Basic
  @Column(name = "GIAPHATHANH")
  public Long getGiaphathanh() {
    return giaphathanh;
  }

  public void setGiaphathanh(Long giaphathanh) {
    this.giaphathanh = giaphathanh;
  }

  @Basic
  @Column(name = "GIATHUCHIEN")
  public Long getGiathuchien() {
    return giathuchien;
  }

  public void setGiathuchien(Long giathuchien) {
    this.giathuchien = giathuchien;
  }

  @Basic
  @Column(name = "GIATH_DIEUCHINH")
  public Long getGiathDieuchinh() {
    return giathDieuchinh;
  }

  public void setGiathDieuchinh(Long giathDieuchinh) {
    this.giathDieuchinh = giathDieuchinh;
  }

  @Basic
  @Column(name = "KL_NIEMYET")
  public Integer getKlNiemyet() {
    return klNiemyet;
  }

  public void setKlNiemyet(Integer klNiemyet) {
    this.klNiemyet = klNiemyet;
  }

  @Basic
  @Column(name = "KL_LUUHANH")
  public Integer getKlLuuhanh() {
    return klLuuhanh;
  }

  public void setKlLuuhanh(Integer klLuuhanh) {
    this.klLuuhanh = klLuuhanh;
  }

  @Basic
  @Column(name = "URL_CAOBACHPHATHANH")
  public String getUrlCaobachphathanh() {
    return urlCaobachphathanh;
  }

  public void setUrlCaobachphathanh(String urlCaobachphathanh) {
    this.urlCaobachphathanh = urlCaobachphathanh;
  }

  @Basic
  @Column(name = "CREATEDATE")
  public Timestamp getCreatedate() {
    return createdate;
  }

  public void setCreatedate(Timestamp createdate) {
    this.createdate = createdate;
  }

  @Basic
  @Column(name = "UPDATED_DATE")
  public Timestamp getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Timestamp updatedDate) {
    this.updatedDate = updatedDate;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VietstockCwInfoEntity that = (VietstockCwInfoEntity) o;
    return Objects.equals(cwSymbol, that.cwSymbol) &&
      Objects.equals(ckCoso, that.ckCoso) &&
      Objects.equals(tochucphathanhCkCoso, that.tochucphathanhCkCoso) &&
      Objects.equals(tochucphathanhCw, that.tochucphathanhCw) &&
      Objects.equals(loaichungquyen, that.loaichungquyen) &&
      Objects.equals(kieuthuchien, that.kieuthuchien) &&
      Objects.equals(phuongthucthuchienquyen, that.phuongthucthuchienquyen) &&
      Objects.equals(thoihan, that.thoihan) &&
      Objects.equals(ngayphathanh, that.ngayphathanh) &&
      Objects.equals(ngayniemyet, that.ngayniemyet) &&
      Objects.equals(ngaygdDautien, that.ngaygdDautien) &&
      Objects.equals(ngaygdCuoicung, that.ngaygdCuoicung) &&
      Objects.equals(ngaydaohan, that.ngaydaohan) &&
      Objects.equals(tilechuyendoi, that.tilechuyendoi) &&
      Objects.equals(tlcdDieuchinh, that.tlcdDieuchinh) &&
      Objects.equals(giaphathanh, that.giaphathanh) &&
      Objects.equals(giathuchien, that.giathuchien) &&
      Objects.equals(giathDieuchinh, that.giathDieuchinh) &&
      Objects.equals(klNiemyet, that.klNiemyet) &&
      Objects.equals(klLuuhanh, that.klLuuhanh) &&
      Objects.equals(urlCaobachphathanh, that.urlCaobachphathanh) &&
      Objects.equals(createdate, that.createdate) &&
      Objects.equals(updatedDate, that.updatedDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cwSymbol, ckCoso, tochucphathanhCkCoso, tochucphathanhCw, loaichungquyen, kieuthuchien, phuongthucthuchienquyen, thoihan, ngayphathanh, ngayniemyet, ngaygdDautien,
      ngaygdCuoicung, ngaydaohan, tilechuyendoi, tlcdDieuchinh, giaphathanh, giathuchien, giathDieuchinh, klNiemyet, klLuuhanh, urlCaobachphathanh, createdate, updatedDate);
  }

}
