package com.tcbs.automation.dwh.ibond;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetBondCOGSAccEntity {

  @Step("Get bond COGS acc from db")
  public static List<HashMap<String, Object>> getBondCOGSAcc(String bondCode, String tradingDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("Select BondCode, AccountantDate, COGS from ");
    queryBuilder.append("	(select *, row_number() over (partition by BondCode order by AccountantDate desc) as rn ");
    queryBuilder.append("	from Smy_dwh_tca_BondCOGS ");
    queryBuilder.append("	where BondCode = :bondCode	and AccountantDate <= :tradingDate and ");
    queryBuilder.append("	ETLCurDate = (select max(ETLCurDate) from Smy_dwh_tca_BondCOGS) ");
    queryBuilder.append("	) t ");
    queryBuilder.append("where rn = 1 ; ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("bondCode", bondCode)
        .setParameter("tradingDate", tradingDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
