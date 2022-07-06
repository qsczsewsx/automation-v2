package com.tcbs.automation.dwh.businessfinance;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetTDTransEntity {

  @Step("Get Data")
  public static List<HashMap<String, Object>> getTDTransactionInfo(String from, String to){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select doccode as docType, d.bizDocId, DocNo as tdCode, d.description, customername as counterPartyName, customercode as counterPartyCode ");
    queryBuilder.append(" , cast(DocDate as date) as depositDate , cast(DateDue as date) as maturityDate, cast(finishedDate as date) as finishedDate, null as tenorMonths ");
    queryBuilder.append(" , DueDate as tenorDays, i.interestRate, i.effectiveDate as intEffDate, i.IsActive as intStatus ");
    queryBuilder.append(" , cast(LoanOriginalAmount as bigint) as depositOriginal, cast(LoanAmount as bigint) as deposit, cast(exchangeRate as int) as exchangeRate, null as withdrawal ");
    queryBuilder.append(" , null as outstandingBalance, null as pnL, d.isactive as statusTD, iscancelled as statusTDCancel ");
    queryBuilder.append(" from Stg_brv_vB30BizDoc d ");
    queryBuilder.append(" left join [dbo].[Stg_brv_B30BizDocInterestRate] i on i.BizDocId = d.BizDocId ");
    queryBuilder.append(" where doccode like '%C4%' and DocDate >= :from and DocDate <= :to ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
