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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.fundstation.entity.TcAssets.createNativeQuery;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CompanyDetailInfo {
  private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
  private static ObjectMapper obm = new ObjectMapper();
  private static Map<Object, CompanyDetailInfo> mapCompanyCache = new HashMap<>();
  @JsonProperty("COMPANY_ID")
  public Integer companyId;
  @JsonProperty("COMPANY_CODE")
  public String companyCode;
  @JsonProperty("COMPANY_NAME")
  public String companyName;
  @JsonProperty("COMPANY_NAME_EN")
  public String companyNameEn;
  @JsonProperty("GROUP_ID")
  public Integer groupId;
  @JsonProperty("GROUP_CODE")
  public String groupCode;
  @JsonProperty("GROUP_NAME")
  public String groupName;
  @JsonProperty("INDUSTRY_ID")
  public Integer industryId;
  @JsonProperty("INDUSTRY_CODE")
  public String industryCode;
  @JsonProperty("INDUSTRY_NAME")
  public String industryName;
  @JsonProperty("INDUSTRY_NAME_EN")
  public String industryNameEn;

  public static CompanyDetailInfo getCompanyInfo(Object key) {
    Map<Object, CompanyDetailInfo> mapAll = mapAllCompanyInfo(false);
    if (mapAll.containsKey(key)) {
      return mapAll.get(key);
    } else {
      return new CompanyDetailInfo();
    }
  }

  public static Map<Object, CompanyDetailInfo> mapAllCompanyInfo(Boolean isReload) {
    if (mapCompanyCache.isEmpty() || isReload) {
      mapCompanyCache.clear();
      StringBuilder sql = new StringBuilder("SELECT ")
        .append("\tc.ID COMPANY_ID,")
        .append("\tc.CODE COMPANY_CODE,")
        .append("\tc.NAME COMPANY_NAME,")
        .append("\tc.NAME_EN COMPANY_NAME_EN,")
        .append("\tgr.ID GROUP_ID,")
        .append("\tgr.CODE GROUP_CODE,")
        .append("\tgr.NAME GROUP_NAME,")
        .append("\ti.ID INDUSTRY_ID,")
        .append("\ti.CODE INDUSTRY_CODE,")
        .append("\ti.NAME INDUSTRY_NAME,")
        .append("\ti.NAME_EN INDUSTRY_NAME_EN")
        .append("\tFROM COMPANY c ")
        .append("\tLEFT JOIN COMPANY_GROUP gr ON c.COMPANY_GROUP_ID = gr.ID")
        .append("\tLEFT JOIN INDUSTRY i ON c.INDUSTRY_ID = i.ID");

      Query<CompanyDetailInfo> query = createNativeQuery(sql.toString());
      try {
        List<CompanyDetailInfo> listResult = obm.readValue(gson.toJson(query.getResultList()), new TypeReference<List<CompanyDetailInfo>>() {
        });
        for (CompanyDetailInfo company : listResult) {
          mapCompanyCache.put(company.getCompanyId(), company);
          mapCompanyCache.put(company.getCompanyCode(), company);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return mapCompanyCache;
  }
}
