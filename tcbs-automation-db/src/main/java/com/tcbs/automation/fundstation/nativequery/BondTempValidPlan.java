package com.tcbs.automation.fundstation.nativequery;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.fundstation.entity.TcAssets.createNativeQuery;

@Data
@Entity
public class BondTempValidPlan {
  private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
  private static ObjectMapper obm = new ObjectMapper();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @SerializedName("ID")
  @Column(name = "ID")
  private int id;

  @SerializedName("CODE")
  @Column(name = "CODE")
  private String code;

  @SerializedName("NAME")
  @Column(name = "NAME")
  private String name;

  @SerializedName("UNDERLYING_TYPE_CODE")
  @Column(name = "UNDERLYING_TYPE_CODE")
  private String underlyingTypeCode;

  @SerializedName("CREATED_TIMESTAMP")
  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @SerializedName("UPDATED_TIMESTAMP")
  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  @SerializedName("SUFFIX")
  @Column(name = "SUFFIX")
  private String suffix;

  @SerializedName("COMPANY_ID")
  @Column(name = "COMPANY_ID")
  private Integer companyId;

  @SerializedName("FIRST_ISSUE_DATE")
  @Column(name = "FIRST_ISSUE_DATE")
  private Date issueDate;

  @SerializedName("VALUE")
  @Column(name = "VALUE")
  private Double value;

  @SerializedName("DESCRIPTION")
  @Column(name = "DESCRIPTION")
  private String description;

  @SerializedName("STATUS")
  @Column(name = "STATUS")
  private String status;

  static String buildQueryGetListBondTempValidPlan() {
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT * FROm UNDERLYING u WHERE u.STATUS = 'ACTIVE' ")
      .append("AND (SELECT COUNT(1) FROM UNDERLYING_DETAIL WHERE UNDERLYING_ID = u.ID AND STATUS = 'ACTIVE') > 0 ")
      .append("AND (SELECT count(1) from COMPANY WHERE id = u.COMPANY_ID AND STATUS = 'ACTIVE') > 0 ")
      .append("AND (SELECT count(1) from TS_PLAN_DETAIL WHERE PLAN_ID =:planId AND PRODUCT_CODE = u.CODE AND STATUS = 'ACTIVE') = 0");
    return sql.toString();
  }

  public static List<BondTempValidPlan> getListBondTempValidPlan(Integer planId) {
    Query query = createNativeQuery(buildQueryGetListBondTempValidPlan());
    query.setParameter("planId", planId);

    return gson.fromJson(gson.toJson(query.getResultList()), new TypeReference<List<BondTempValidPlan>>() {
    }.getType());
  }
}
