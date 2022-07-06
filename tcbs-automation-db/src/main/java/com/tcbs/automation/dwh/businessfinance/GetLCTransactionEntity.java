package com.tcbs.automation.dwh.businessfinance;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetLCTransactionEntity {

  @Step("Get Data")
  public static List<HashMap<String, Object>> getLCTransactionInfo(String from, String to){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select doccode as docType, d.bizDocId, DocNo as loanCode, d.description, customername as counterPartyName, customercode as counterPartyCode, cast(DocDate as date) as loanDate  ");
    queryBuilder.append(" , cast(Datedue as date) as maturityDate, cast(finishedDate as date) as finishedDate, null as tenorMonths, DueDate as tenorDays, i.interestRate, i.EffectiveDate as intEffDate ");
    queryBuilder.append(" , i.IsActive as intStatus, cast(LoanOriginalAmount as bigint) as loanOriginal, cast(LoanAmount as bigint) as loan, cast(exchangeRate as int) as exchangeRate, null as withdrawal ");
    queryBuilder.append(" , null as outstandingBalance, null as pnL, d.isactive as statusLoan, d.iscancelled as statusLoanCancel, CAST(d.CurrencyCode as varchar) currencyCode, case when CAST(d.CurrencyCode as varchar) like '%VND%' then 'Local' else 'Offshore' end as loanType ");
    queryBuilder.append(" from Stg_brv_vB30BizDoc d ");
    queryBuilder.append(" left join [dbo].[Stg_brv_B30BizDocInterestRate] i on i.BizDocId = d.BizDocId ");
    queryBuilder.append(" where doccode like '%LC%' and DocDate >= :from and DocDate <= :to ");
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
