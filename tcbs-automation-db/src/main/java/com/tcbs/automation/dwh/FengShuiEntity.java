package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FengShuiEntity {

  @Step("Get data")
  public static List<HashMap<String, Object>> getCommentaryByCondition(String inputDate) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("  SELECT sps.ngay_duong_lich as date, ntbt_sao, ntbt_dong_vat, ntbt_ngu_hanh, ntbt_sao_day_du, ntbt_sao_day_du_2,  ");
    queryStringBuilder.append("  ntbt_sao_tot_xau , ntbt_sao_mo_ta, ntbt_nen_lam , ntbt_kieng_cu , ntbt_ngoai_le , ntbt_dien_giai,   ");
    queryStringBuilder.append("  ntbt_tho, span.ngay_xuat_hanh_khong_minh, span.ngay_theo_truc , sphxh.hxh_hythan, sphxh.hxh_taithan, sphxh.hxh_hacthan  ");
    queryStringBuilder.append("  FROM Stg_pt_28sao sps  ");
    queryStringBuilder.append("  LEFT JOIN Stg_pt_amlich_nguyhiem span ON span.ngay_duong_lich = sps.ngay_duong_lich  ");
    queryStringBuilder.append("  LEFT JOIN Stg_pt_huong_xuat_hanh sphxh ON sphxh.ngay_duong_lich = sps.ngay_duong_lich  ");
    queryStringBuilder.append("  where sps.ngay_duong_lich = :inputDate  ;  ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("inputDate", inputDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data")
  public static List<HashMap<String, Object>> getDouShu(String currentDate, String type) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("  SELECT ten_sao as name, * FROM Stg_pt_sao_tot_xau  ");
    queryStringBuilder.append("  where ngay_duong_lich = :currentDate and loai_sao = :type ;  ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("currentDate", currentDate)
        .setParameter("type", type)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data")
  public static List<HashMap<String, Object>> getZodiacHour(String currentDate, Boolean isWhiteZodiacHour) {
    StringBuilder queryStringBuilder = new StringBuilder();
    if (isWhiteZodiacHour.equals(true)) {
      queryStringBuilder.append("  SELECT gio_hoang_dao as zodiacName, gio_hoang_dao_dong_ho as zodiacRange, gio_hoang_dao_sao as zodiacStar,   ");
      queryStringBuilder.append("  'true' as isWhiteZodiac  ");
    } else {
      queryStringBuilder.append("  SELECT gio_hac_dao as zodiacName, gio_hac_dao_dong_ho as zodiacRange, gio_hac_dao_sao as zodiacStar,   ");
      queryStringBuilder.append("  'false' as isWhiteZodiac  ");
    }

    queryStringBuilder.append(" FROM Stg_pt_gio_dep  where ngay_duong_lich = :currentDate  ;  ");
    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("currentDate", currentDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data")
  public static List<HashMap<String, Object>> getLunarCalendar() {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("  SELECT ngay_duong_lich, ngay_trong_tuan , ngay_am_lich , thang_am_number, loai_ngay   ");
    queryStringBuilder.append("  FROM vw_pt_amlich ngay   ");
    queryStringBuilder.append("  left join stg_pt_menh_hop menh on ngay.ngu_hanh_ngay = menh.Menh    ");
    queryStringBuilder.append("  left join stg_pt_tuoi_hop tuoi on ngay.ngay_am_12giap_id = tuoi.Tuoi_Id   ");
    queryStringBuilder.append("  where ngay_duong_lich >= cast(format(GETDATE(), 'yyyy-MM-01') as Date)  ");
    queryStringBuilder.append("  and ngay_duong_lich <= eomonth(getdate())  order by ngay_duong_lich ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data")
  public static List<HashMap<String, Object>> getTickerDestiny(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("  SELECT *, Ticker as ticker FROM vw_pt_company  ");
    queryStringBuilder.append("  where Ticker = :ticker   ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get static data")
  public static List<HashMap<String, Object>> getUserDestinyData(String custodyCode) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("  SELECT c.CUSTODYCD as id, c.birthday, dds.yearName , dds.yearMing   ");
    queryStringBuilder.append("  FROM Smy_dwh_cas_AllUserView c  ");
    queryStringBuilder.append("  LEFT JOIN smy_dwh_pt_date_destiny dds ON CAST(c.birthday AS DATE) = CAST(dds.[date]  AS DATE)  ");
    queryStringBuilder.append("  WHERE c.CustodyCD = :custodyCode AND c.EtlCurDate = (SELECT MAX(EtlCurDate) FROM Smy_dwh_cas_AllUserView)");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("custodyCode", custodyCode)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get date zodiac")
  public static List<HashMap<String, Object>> getZodiacDateData(String date) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" SELECT ngay_duong_lich as date, ngay_am_lich, thang_am, ngay_am_12giap , thang_am_12giap , nam_am_12giap, loai_ngay  ");
    queryStringBuilder.append(" FROM Stg_pt_amlich WHERE ngay_duong_lich = :date");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("date", date)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
