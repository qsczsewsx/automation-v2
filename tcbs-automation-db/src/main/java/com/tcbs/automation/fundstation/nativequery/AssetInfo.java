package com.tcbs.automation.fundstation.nativequery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.query.Query;

import javax.persistence.Entity;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.tcbs.automation.config.fundstation.FundStationConstants.*;
import static com.tcbs.automation.fundstation.entity.TcAssets.createNativeQuery;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AssetInfo {
  private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
  private static ObjectMapper obm = new ObjectMapper();

  @JsonProperty("TICKER")
  public String ticker;

  @JsonProperty("LISTED")
  public String listed;

  @JsonProperty("DIRTY_VALUE")
  public Double dirtyValue;

  @JsonProperty("TOTAL_SUM")
  public Double totalSum;

  @JsonProperty("FUND_HLDG")
  public Double fundHolding;

  @JsonProperty("COMPANY")
  public String company;

  @JsonProperty("TOTAL_ISSUER")
  public String totalIssuer;

  @JsonProperty("RATE_ISSUER")
  public String rateIssuer;

  @JsonProperty("COMPANY_GROUP")
  public String companyGroup;

  public static List<AssetInfo> getListPortfolioBy(String portfolioCode, Date reportDate, String objectCode, Integer policyType, String status) {
    return executeSqlPolicy(buildQueryGetListPortfolio(objectCode, policyType, status), portfolioCode, reportDate, objectCode, status);
  }

  public static List<AssetInfo> getListHoldingRateOfCompany(String portfolioCode, Date reportDate, String objectCode, Integer policyType, String status) {
    StringBuilder sql = new StringBuilder("SELECT a.COMPANY , SUM(a.FUND_HLDG) TOTAL_SUM FROM (\n")
      .append(buildQueryGetListPortfolio(objectCode, policyType, status))
      .append(")a GROUP BY COMPANY\n")
      .append("ORDER BY TOTAL_SUM DESC");

    return executeSqlPolicy(sql.toString(), portfolioCode, reportDate, objectCode, status);
  }

  public static List<AssetInfo> getListIssuerRateOfCompany(String portfolioCode, Date reportDate, String objectCode, Integer policyType, String status) {
    StringBuilder sql = new StringBuilder("SELECT a.COMPANY , SUM(a.RATE_ISSUER) TOTAL_SUM FROM (\n")
      .append(buildQueryGetListPortfolio(objectCode, policyType, status))
      .append(")a GROUP BY COMPANY\n")
      .append("ORDER BY TOTAL_SUM DESC NULLS LAST");

    return executeSqlPolicy(sql.toString(), portfolioCode, reportDate, objectCode, status);
  }

  public static List<AssetInfo> getListHoldingRateOfGroupCompany(String portfolioCode, Date reportDate, String objectCode, Integer policyType, String status) {
    StringBuilder sql = new StringBuilder("SELECT a.COMPANY_GROUP , SUM(a.FUND_HLDG) TOTAL_SUM FROM (\n")
      .append(buildQueryGetListPortfolio(objectCode, policyType, status))
      .append(")a GROUP BY COMPANY_GROUP\n")
      .append("ORDER BY TOTAL_SUM DESC");

    return executeSqlPolicy(sql.toString(), portfolioCode, reportDate, objectCode, status);
  }

  static List<AssetInfo> executeSqlPolicy(String sql, String portfolioCode, Date reportDate, String objectCode, String status) {
    Query<AssetInfo> query = createNativeQuery(sql);
    query.setParameter("portfolioCode", portfolioCode);
    query.setParameter("reportDate", reportDate);
    if (!"All".equalsIgnoreCase(objectCode)) {
      query.setParameter("listCondition", Arrays.asList(objectCode.split(",")));
    }

    if (status != null) {
      query.setParameter("status", status);
    }
    try {
      return obm.readValue(gson.toJson(query.getResultList()), new TypeReference<List<AssetInfo>>() {
      });
    } catch (IOException e) {
      return null;
    }
  }


  static String buildQueryGetListPortfolio(String objectCode, Integer policyType, String status) {
    StringBuilder sql = new StringBuilder(
      "SELECT pf.TICKER, pf.LISTED, pf.DIRTY_VALUE, pf.FUND_HLDG, c.CODE COMPANY, COALESCE(cg.CODE, c.CODE) COMPANY_GROUP, c.TOTAL_VALUE TOTAL_ISSUER, (pf.DIRTY_VALUE / c.TOTAL_VALUE) RATE_ISSUER\n")
      .append("FROM REPORT_PORTFOLIO_FUND pf LEFT JOIN PRODUCT_GLOBAL pg ON pf.TICKER = pg.TICKER\n")
      .append("LEFT JOIN COMPANY c ON pg.COMPANY = c.ID\n")
      .append("LEFT JOIN COMPANY_GROUP cg ON c.COMPANY_GROUP_ID = cg.ID\n")
      .append("WHERE pf.PORTFOLIO_CODE =:portfolioCode\n")
      .append("AND pf.REPORT_DATE =:reportDate\n");

    if (!"ALL".equalsIgnoreCase(objectCode)) {
      if (Objects.equals(POLICY_TYPE_COMPANY, policyType)) {
        sql.append("AND c.CODE IN :listCondition\n");
      } else if (Objects.equals(POLICY_TYPE_GROUP, policyType)) {
        sql.append("AND cg.CODE IN :listCondition\n");
      } else if (Objects.equals(POLICY_TYPE_PRODUCT, policyType)) {
        sql.append("AND pf.TYPE IN :listCondition\n");
      }
    }
    if (status != null) {
      sql.append("AND pf.LISTED =:status\n");
    }
    sql.append("ORDER BY RATE_ISSUER DESC NULLS LAST\n");

    return sql.toString();
  }

}
