package com.automation.cas.negativequery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;

import javax.persistence.Entity;
import java.io.IOException;

import static com.automation.cas.CAS.createNativeQuery;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UsernameByCondition {
  private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
  private static ObjectMapper mapper = new ObjectMapper();

  @JsonProperty("USERNAME")
  public String username;

  @JsonProperty("xxxxID")
  public String xxxxId;

  public static UsernameByCondition getUsernameForOrdersAPI(String purAccStatus, String purVsdStatus, String purFundStatus) throws IOException {
    int vsdStatus = Integer.parseInt(purVsdStatus);
    int fundStatus = Integer.parseInt(purFundStatus);
    int accountStatus = Integer.parseInt(purAccStatus);
    Query query = createNativeQuery(buildQueryGetUsernameByCondition());
    query.setParameter("vsdStatus", vsdStatus);
    query.setParameter("fundStatus", fundStatus);
    query.setParameter("accountSTATUS", accountStatus);
    String json = (gson.toJson(query.getResultList()).replace("[", "")).replace("]", "");
    UsernameByCondition username = StringUtils.isBlank(json) ? new UsernameByCondition() : mapper.readValue(json, UsernameByCondition.class);
    return username;
  }

  static String buildQueryGetUsernameByCondition() {
    StringBuilder sql = new StringBuilder("SELECT USERNAME, xxxxID from xxxx_USER")
      .append(" where id in (")
      .append(" select USER_ID from xxxx_application_user where APP_ID =2 and STATUS =:vsdStatus and USER_ID  in( ")
      .append(" select USER_ID from xxxx_application_user where APP_ID =4 and STATUS =:fundStatus )) AND ACCOUNT_STATUS =:accountSTATUS AND ROWNUM=1");
    return sql.toString();
  }
}
