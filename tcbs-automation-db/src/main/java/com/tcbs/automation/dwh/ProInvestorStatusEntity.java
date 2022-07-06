package com.tcbs.automation.dwh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProInvestorStatusEntity {
  @Id
  private String histDate;
  private String custodyCd;
  private Float totalValue;
  private String assetType;

  @Step("Get total listed assets of investor")
  public static List<Map<String, Object>> getTotalAssetByCondition(String custody, String fromDate, String toDate, String adjRate) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("    select *, 'Stock' as AssetType  ");
    queryStringBuilder.append("    from ( 	select HistDate, CustodyCd, Numerator* :adjRate as TotalValue  ");
    queryStringBuilder.append("      from Prc_Stock_Indices  ");
    queryStringBuilder.append("      where CustodyCd = :custodyCd  ");
    queryStringBuilder.append("  and HistDate >= :fromDate  ");
    queryStringBuilder.append("  and HistDate < :toDate) sp  ");
    queryStringBuilder.append("  union all  ");
    queryStringBuilder.append("  select *, 'Bond' as AssetType  ");
    queryStringBuilder.append("  from ( 	select HistDate, CustodyCd, sum(ObalPar) as ObalPar  ");
    queryStringBuilder.append("    from ( 	select HistDate, CustodyCd, CloseQuantity*Price as OBalPar  ");
    queryStringBuilder.append("      from Prc_Stock_PortfolioByDate_B lbp  ");
    queryStringBuilder.append("      left join Stg_tcb_Bond b on b.ListedCode = lbp.SymbolAdj  ");
    queryStringBuilder.append("      where CustodyCd = :custodyCd  ");
    queryStringBuilder.append("  and HistDate >= :fromDate  ");
    queryStringBuilder.append("  and HistDate < :toDate) lbp  ");
    queryStringBuilder.append("  group by HistDate, CustodyCd) lbp1 order by HistDate");

    try {
      List<Map<String, Object>> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("custodyCd", custody)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("adjRate", adjRate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
