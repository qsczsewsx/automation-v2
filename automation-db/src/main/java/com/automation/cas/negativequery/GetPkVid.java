package com.automation.cas.negativequery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.automation.cas.CAS;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.automation.cas.CAS.createNativeQuery;

@Getter
@Setter
@NoArgsConstructor
@Component
public class GetPkVid {
  private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
  private static ObjectMapper mapper = new ObjectMapper();

  @JsonProperty("PK_VID")
  public String pkVid;

  @JsonProperty("EMAIL")
  public String email;

  @JsonProperty("PHONE")
  public String phone;

  @JsonProperty("FIRSTNAME")
  public String firstName;

  @JsonProperty("LASTNAME")
  public String lastName;

  @JsonProperty("PROVINCE")
  public String province;

  @JsonProperty("IDNUMBER")
  public String idNumber;

  @JsonProperty("IDPLACE")
  public String idPlace;

  @JsonProperty("BANKACCOUNTNUMBER")
  public String bankAccountNumber;

  @JsonProperty("BANKACCOUNTNAME")
  public String bankAccountName;

  @JsonProperty("BANKNAME")
  public String bankName;

  @JsonProperty("BANKPROVINCE")
  public String bankProvince;

  @JsonProperty("BANKCODE")
  public String bankCode;

  @JsonProperty("BANKBRANCH")
  public String bankBranch;

  @JsonProperty("GENDER")
  public int gender;

  @JsonProperty("ADDRESS")
  public String address;

  @JsonProperty("CITIZENSHIP")
  public String citizenship;

  @JsonProperty("BIRTHDAY")
  public Date birthday;

  @JsonProperty("IDDATE")
  public Date idDate;

  @JsonProperty("WEALTH_PARTNER_CODE")
  public String wealthPartnerCode;

  @JsonProperty("WEALTH_PARTNER_CHANNEL")
  public String wealthPartnerChannel;

  @JsonProperty("RECEIVE_ADVERTISE")
  public Integer receiveAdvertise;

  public static GetPkVid query(String refId) {
    GetPkVid res = null;
    Session session = CAS.casConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    try {
      Query query = createNativeQuery(
        "SELECT WEALTH_PARTNER_CODE, RECEIVE_ADVERTISE, WEALTH_PARTNER_CHANNEL, GENDER, PK_VID, EMAIL, PHONE, FIRSTNAME, LASTNAME, PROVINCE, BANKPROVINCE, BANKCODE, BANKBRANCH, IDNUMBER, IDPLACE, CITIZENSHIP, BIRTHDAY, IDDATE, BANKACCOUNTNUMBER, BANKACCOUNTNAME, BANKNAME, ADDRESS from xxxx_USER_OPENACCOUNT_QUEUE WHERE REFERENCEID = :refId");
      query.setParameter("refId", refId);
      String json = (gson.toJson(query.getResultList()).replace("[", "")).replace("]", "");
      res = StringUtils.isBlank(json) ? new GetPkVid() : mapper.readValue(json, GetPkVid.class);
    } catch (Exception e) {
      session.getTransaction().rollback();
    }
    session.close();
    return res;
  }
}
