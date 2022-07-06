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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.fundstation.entity.TcAssets.createNativeQuery;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BalanceRemain {
  private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
  private static ObjectMapper obm = new ObjectMapper();

  @JsonProperty("TICKER")
  public String ticker;

  @JsonProperty("VOLUME_REMAIN")
  public Double volumeRemain;

  @JsonProperty("LATEST_ID")
  public Integer latestId;

  public static List<BalanceRemain> getListBalanceRemain(Integer accountId, Date reportDate) {

    StringBuilder sql = new StringBuilder("\nSELECT TICKER, (BUY - SELL) VOLUME_REMAIN, ")
      .append("\n(SELECT * FROM ( ")
      //.append("\n(SELECT ID FROM TRANSACTION WHERE TRANSACTION_DATE <= TO_DATE(:reportDate, 'yyyy-MM-dd')  ")
      .append(" (SELECT ID FROM TRANSACTION WHERE TRANSACTION_DATE <=:reportDate  ")
      .append("\nAND ACCOUNT_ID =:accountId AND TICKER = A.TICKER ORDER BY ID DESC) ")
      .append("\n) WHERE ROWNUM = 1) LATEST_ID ")
      .append("\nFROM (SELECT TICKER, SUM(CASE WHEN ACTION = 1 OR ACTION = 6 THEN UNIT ELSE 0 END) BUY,")
      .append("\nSUM(CASE WHEN ACTION = 2 OR ACTION = 5 THEN UNIT ELSE 0 END) SELL ")
      .append("\nFROM TRANSACTION ")
      //.append("\nWHERE TRANSACTION_DATE <=TO_DATE(:reportDate, 'yyyy-MM-dd') ")
      .append(" WHERE TRANSACTION_DATE <=:reportDate ")
      .append("\nAND ACCOUNT_ID =:accountId  GROUP BY TICKER) A ")
      .append("\nWHERE (BUY - SELL) > 0");

    Query<BalanceRemain> query = createNativeQuery(sql.toString());
    query.setParameter("accountId", accountId);
    query.setParameter("reportDate", reportDate);
    try {
      return obm.readValue(gson.toJson(query.getResultList()), new TypeReference<List<BalanceRemain>>() {
      });
    } catch (IOException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }
}
