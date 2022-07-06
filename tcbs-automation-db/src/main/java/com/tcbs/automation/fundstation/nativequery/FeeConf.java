package com.tcbs.automation.fundstation.nativequery;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.fundstation.entity.TcAssets.createNativeQuery;

@Data
@NoArgsConstructor
@Entity
public class FeeConf {
  private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "PORTFOLIO")
  private String portfolio;

  @Column(name = "FEE_ID")
  private Integer feeId;

  @Column(name = "VALUE")
  private Double value;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  @Column(name = "NAME")
  private String feeName;

  @Column(name = "TYPE")
  private Integer type;

  @Column(name = "TYPE_NAME")
  private String typeName;

  @Column(name = "CALCULATE_BY")
  private Integer calculateBy;

  @Column(name = "CALCULATE_BY_NAME")
  private String calculateByName;

  @Column(name = "PERIOD")
  private String period;

  public static List<FeeConf> getListFeeOfPortfolio(String portfolio) {
    Query query = createNativeQuery(buildQueryGetFeeConf(portfolio));

    return gson.fromJson(gson.toJson(query.getResultList()), new TypeReference<List<FeeConf>>() {
    }.getType());
  }

  static String buildQueryGetFeeConf(String portfolioCode) {
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT cf.*, f.NAME, f.TYPE, ")
      .append("(SELECT KEY FROm CONF WHERE LABEL = 'FEE_TYPE' AND VALUE = f.TYPE) TYPE_NAME, ")
      .append("f.CALCULATE_BY, ")
      .append("(SELECT KEY FROm CONF WHERE LABEL = 'FEE_CALCULATE_BY' AND VALUE = f.CALCULATE_BY) CALCULATE_BY_NAME, ")
      .append("f.PERIOD FROM  CONF_FEE cf LEFT JOIN FEE f ON cf.FEE_ID = f.ID ")
      .append("WHERE cf.STATUS = 'ACTIVE' ")
      .append("AND cf.PORTFOLIO = '").append(portfolioCode).append("'");
    return sql.toString();
  }
}
