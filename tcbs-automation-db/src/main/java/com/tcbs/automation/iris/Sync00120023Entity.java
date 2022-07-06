package com.tcbs.automation.iris;

import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sync00120023Entity {
  @Id
  private String ticker;
  private Integer loanType;
  private String loanPrice;
  private String loanRatio;
  private String roomFinal;
  private String roomUsed;
  private String afmaxamt;
  private String afmaxamtt3;
  private String reviewedDate;
  private String updatedDate;
  private String updatedBy;


  @Step("Get backlist")
  public static List<HashMap<String, Object>> getBlacklist() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select *  ");
    queryBuilder.append("  from Stg_flx_020012  ");
    queryBuilder.append("  where  (MRMAXQTTY = 0 or ROOMLIMIT74 = 0 or MRPRICERATE = 0 or ISMARGINALLOW = 'N' or ISMARGINALLOW = 'Không')  ");
    queryBuilder.append("  and len(SYMBOL) = 3 and (ISMARGINALLOW = 'Y' or ISMARGINALLOW = 'Có') ");
    try {

      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get blacklist")
  public static List<HashMap<String, Object>> getLoanlist(List<String> ticker) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select * ");
    queryBuilder.append("  from Stg_flx_020012  ");
    queryBuilder.append("  where symbol not in :ticker  ");
    queryBuilder.append("  and len(SYMBOL) = 3 and (ISMARGINALLOW = 'Y' or ISMARGINALLOW = 'Có') ");
    try {

      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }


  @Step("Get data from Fulllist After")
  public static List<HashMap<String, Object>> getFulllistAfter() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT  TICKER, LOAN_TYPE, LOAN_PRICE, LOAN_RATIO, ROOM_FINAL, ROOM_USED, AFMAXAMT, AFMAXAMTT3, UPDATED_DATE, UPDATED_BY, REVIEWED_DATE  ");
    queryBuilder.append(" FROM RISK_ANALYST_MARGIN_REVIEWED_FULL  ");
    queryBuilder.append(" where UPDATED_BY = 'SYNC_020012_020023' and UPDATED_DATE = (select max(UPDATED_DATE) from RISK_ANALYST_MARGIN_REVIEWED_FULL)  ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get data from 0023")
  public static List<HashMap<String, Object>> get0023(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append("  select max(MRPRICERATE) as Gia_chan,SYMBOL, max(MRRATIOLOAN) as Ty_le   ");
    queryBuilder.append("  from Stg_flx_020023   ");
    queryBuilder.append("  where SYMBOL = :ticker   ");
    queryBuilder.append("  and UpdatedDate = (select max(UpdatedDate) from Stg_flx_020023 where symbol = :ticker)   ");
    queryBuilder.append("  group by SYMBOL   ");
    try {

      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get data from Fulllist Before")
  public static List<HashMap<String, Object>> getFulllistBefore(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT *  ");
    queryBuilder.append(" FROM RISK_ANALYST_MARGIN_REVIEWED_FULL where TICKER = :symbol");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("symbol", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}