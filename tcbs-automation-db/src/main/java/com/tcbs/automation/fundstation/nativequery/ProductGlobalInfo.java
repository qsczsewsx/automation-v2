package com.tcbs.automation.fundstation.nativequery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tcbs.automation.fundstation.entity.Coupon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.query.Query;

import javax.persistence.Entity;
import java.io.IOException;
import java.util.*;

import static com.tcbs.automation.fundstation.entity.Coupon.getCouponOfTicker;
import static com.tcbs.automation.fundstation.entity.TcAssets.createNativeQuery;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ProductGlobalInfo {
  private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
  private static ObjectMapper obm = new ObjectMapper();
  private static Map<Object, Map<Object, ProductGlobalInfo>> mapMaturityByDate = new HashMap<>();
  @JsonProperty("ID")
  public Integer id;
  @JsonProperty("CODE")
  public String code;
  @JsonProperty("DESCRIPTION")
  public String description;
  @JsonProperty("TICKER")
  public String ticker;
  @JsonProperty("LISTED_NAME")
  public String listedName;
  @JsonProperty("TYPE")
  public String type;
  @JsonProperty("TYPE_CODE")
  public String typeCode;
  @JsonProperty("STATUS")
  public Integer status;
  @JsonProperty("EXCHANGE")
  public Integer exchange;
  @JsonProperty("PAR")
  public Double par;
  @JsonProperty("ISSUE_TIMESTAMP")
  public Date issueDate;
  @JsonProperty("MATURITY_TIMESTAMP")
  public Date maturityDate;
  @JsonProperty("LISTED_TIMESTAMP")
  public Date listedDate;
  @JsonProperty("DELISTED_TIMESTAMP")
  public Date deListedDate;
  @JsonProperty("COUPON_PAYMENT_TYPE")
  public Integer couponPaymentType;
  @JsonProperty("CONVENTION")
  public Integer convention;
  @JsonProperty("COMPANY")
  public Integer company;
  @JsonProperty("COMPANY_GROUP")
  public Integer companyGroup;
  @JsonProperty("INPUT_SOURCE")
  public String inputSource;
  @JsonProperty("CREATED_TIMESTAMP")
  public Date createdDate;
  @JsonProperty("UPDATED_TIMESTAMP")
  public Date updatedDate;
  @JsonProperty("INDUSTRY")
  public String industry;
  @JsonProperty("EARLY_WITHDRAWN_INTEREST")
  public String earlyWithdrawInterest;
  @JsonProperty("AUTO_ROLLOVER")
  public String autoRollover;
  @JsonProperty("AUTO_ROLLOVER_INTEREST")
  public String autoRolloverInterest;
  @JsonProperty("INTEREST_PERIOD_EXCLUDING")
  public String interestPeriodExcluding;
  @JsonProperty("HOLIDAY_INTEREST")
  public String holidayInterest;
  @JsonProperty("COUPON_PAYMENT_METHOD")
  public String couponPaymentMethod;
  @JsonProperty("NAME")
  public String name;
  @JsonProperty("NAME_EN")
  public String nameEn;
  @JsonProperty("META_DATA")
  public Object metaData;
  @JsonProperty("INSTRUMENT_ID")
  public Integer instrumentId;
  @JsonProperty("INS_CODE")
  public String instrumentCode;
  @JsonProperty("INS_NAME")
  public String instrumentName;
  @JsonProperty("UNDERLYING_ID")
  public Integer underlyingId;
  @JsonProperty("UNDER_CODE")
  public String underlyingCode;
  @JsonProperty("UNDER_NAME")
  public String underlyingName;
  @JsonProperty("UNDERLYING_TYPE_CODE")
  public String underlyingTypeCode;

  public static Map<Object, ProductGlobalInfo> getAllProductByMaturityDate(Date maturityDate) {
    if (!mapMaturityByDate.containsKey(maturityDate)) {
      Query query = createNativeQuery(buildQueryGetProductGlobal() + " WHERE MATURITY_TIMESTAMP =:maturityDate");
      query.setParameter("maturityDate", maturityDate);
      List<ProductGlobalInfo> listResult = convertQueryToList(query.getResultList());
      mapMaturityByDate.put(maturityDate, convertListToMap(listResult));
    }

    return mapMaturityByDate.get(maturityDate);
  }

  public static Map<Object, ProductGlobalInfo> getAllProductByMaturityDateRage(Date fromDate, Date toDate) {
    Query query = createNativeQuery(buildQueryGetProductGlobal() + " WHERE MATURITY_TIMESTAMP >=:fromDate AND MATURITY_TIMESTAMP <=:toDate");
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    List<ProductGlobalInfo> listResult = convertQueryToList(query.getResultList());
    return convertListToMap(listResult);
  }

  public static Map<Object, ProductGlobalInfo> getProductGlobalAllInfo() {
    Query query = createNativeQuery(buildQueryGetProductGlobal());
    List<ProductGlobalInfo> listResult = convertQueryToList(query.getResultList());
    return convertListToMap(listResult);
  }

  static Map<Object, ProductGlobalInfo> convertListToMap(List<ProductGlobalInfo> listProduct) {
    Map<Object, ProductGlobalInfo> mapProduct = new HashMap<>();
    for (ProductGlobalInfo product : listProduct) {
      product.setCreatedDate(null);
      product.setUpdatedDate(null);
      mapProduct.put(product.getId(), product);
      mapProduct.put(product.getTicker(), product);
    }
    return mapProduct;
  }

  static List<ProductGlobalInfo> convertQueryToList(List<?> list) {
    try {
      return obm.readValue(gson.toJson(list), new TypeReference<List<ProductGlobalInfo>>() {
      });
    } catch (IOException e) {
      return new ArrayList<>();
    }
  }

  static String buildQueryGetProductGlobal() {
    StringBuilder sql = new StringBuilder("SELECT ab.*, u.CODE UNDER_CODE, u.NAME UNDER_NAME, u.UNDERLYING_TYPE_CODE ")
      .append(" FROM (")
      .append(" SELECT a.*, i.CODE INS_CODE, i.NAME INS_NAME, i.UNDERLYING_ID ")
      .append(" FROM ( ")
      .append(" SELECT pg.*, pi.INSTRUMENT_ID ")
      .append(" FROM PRODUCT_GLOBAL pg ")
      .append(" LEFT JOIN PRODUCT_INSTRUMENT pi ")
      .append(" ON pg.ID = pi.PRODUCT_ID ")
      .append(") a")
      .append(" LEFT JOIN INSTRUMENT i ")
      .append(" ON a.INSTRUMENT_ID = i.ID ")
      .append(") ab")
      .append(" LEFT JOIN UNDERLYING u ")
      .append(" ON ab.UNDERLYING_ID = u.ID");
    return sql.toString();
  }

  public List<Coupon> getCoupons() {
    return getCouponOfTicker(ticker);
  }
}
