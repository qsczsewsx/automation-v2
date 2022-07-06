package com.tcbs.automation.dwh.iaconnection;

import com.tcbs.automation.dwh.Dwh;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class CustomerIaConnectEntity {
  private String refId;
  @Id
  private String custodyCd;
  private String fullName;
  private Timestamp txDate;
  private Timestamp createDt;
  private String bankAcctNo;
  private String bankName;
  private String autoTrf;
  private String isIaPaid;
  private String via;
  private String flagType;
  private String status;


  public static List<HashMap<String, Object>> getByCondition(String custodyCd) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("  Select Cf.custodyCd , af.bankAcctNo  ");
    queryStringBuilder.append("  From RT_flx_cfmast cf, RT_flx_afmast af ");
    queryStringBuilder.append("  where cf.custid = af.custid ");
    queryStringBuilder.append("  and cf.status <>'C' and af.alternateacct = 'Y' and Cf.custodycd = :custody ");
    queryStringBuilder.append("  group by Cf.custodycd , af.bankAcctNo ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("custody", custodyCd)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


}
