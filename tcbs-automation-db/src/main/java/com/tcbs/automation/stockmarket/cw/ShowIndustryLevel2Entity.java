package com.tcbs.automation.stockmarket.cw;

import com.tcbs.automation.stockmarket.Stockmarket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ShowIndustryLevel2Entity {
  String industryName;
  String industryEnName;
  @Id
  Integer industryId;

  @Step("Get Industry")

  public static java.util.List<HashMap<String, Object>> getIndustry() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select distinct Namel2,NameEnl2,IdLevel2 from tca_vw_idata_index_industry_exchange_v2 ");
    queryStringBuilder.append(" where IdLevel2 is not null ");
    queryStringBuilder.append(" and CAST(createdDate as date) = (SELECT MAX(CAST(createdDate as date))) ;");


    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  public String getIndustryName() {
    return industryName;
  }

  public void setIndustryName(String industryName) {
    this.industryName = industryName;
  }

  public String getIndustryEnName() {
    return industryEnName;
  }

  public void setIndustryEnName(String industryEnName) {
    this.industryEnName = industryEnName;
  }

  public Integer getIndustryId() {
    return industryId;
  }

  public void setIndustryId(Integer industryId) {
    this.industryId = industryId;
  }
}
