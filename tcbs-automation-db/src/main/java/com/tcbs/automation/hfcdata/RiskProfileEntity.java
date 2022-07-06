package com.tcbs.automation.hfcdata;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RiskProfileEntity {
  @Step("Get random data")
  public static List<HashMap<String, Object>> getRandomData() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select * from ( ");
    queryStringBuilder.append(" select ");
    queryStringBuilder.append(" 	field_name, ");
    queryStringBuilder.append(" 	answer_id, ");
    queryStringBuilder.append(" 	row_number() over(partition by field_name order by random()) as random_sort ");
    queryStringBuilder.append(" from ");
    queryStringBuilder.append("     tf_riskprofile_score ");
    queryStringBuilder.append(" ) a where random_sort = 1 ");
    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Update data test")
  public static void updateDataTest(String tcbsId, List<HashMap<String, Object>> list) {
    try {
      Session session = Tcbsdwh.tcbsDwhDbConnection.getSession();
      session.clear();
      Transaction trans = session.beginTransaction();

      StringBuilder queryStringBuilder = new StringBuilder();
      queryStringBuilder.append(" update dwh.smy_dwh_tf_riskprofile ");
      queryStringBuilder.append(" set ");
      HashMap<String, Object> item;
      for (int i = 0; i < list.size() - 2; i++) {
        item = list.get(i);
        queryStringBuilder.append(item.get("field_name")).append(" = '").append(item.get("answer_id")).append("' , ");
      }
      item = list.get(list.size() - 1);
      queryStringBuilder.append(item.get("field_name")).append(" = '").append(item.get("answer_id")).append("' ");
      queryStringBuilder.append(" where tcbsid =:tcbsid ");

      Query<?> query = session.createNativeQuery(queryStringBuilder.toString());
      query.setParameter("tcbsid", tcbsId);
      query.executeUpdate();
      trans.commit();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
  }

  @Step("Get data")
  public static List<HashMap<String, Object>> getRiskScore(String tcbsId) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT a.val ");
    queryStringBuilder.append(" 	, a.risk_score ");
    queryStringBuilder.append(" 	, b.*  ");
    queryStringBuilder.append(" 	, max_score_smy.max_score_group ");
    queryStringBuilder.append(" from ( ");
    queryStringBuilder.append(" 	select *  ");
    queryStringBuilder.append(" 	FROM (SELECT top 1 cast(risk_score AS varchar) AS risk_score ");
    queryStringBuilder.append(" 			, risk_marriage_status ");
    queryStringBuilder.append(" 			, risk_education_status ");
    queryStringBuilder.append(" 			, risk_annual_income ");
    queryStringBuilder.append(" 			, risk_current_asset ");
    queryStringBuilder.append(" 			, risk_financial_safety ");
    queryStringBuilder.append(" 			, risk_financial_knowledge ");
    queryStringBuilder.append(" 			, risk_information_update ");
    queryStringBuilder.append(" 			, risk_experience_investment ");
    queryStringBuilder.append(" 			, risk_holding_period ");
    queryStringBuilder.append(" 			, risk_profit_expectation ");
    queryStringBuilder.append(" 			, risk_achieve_target ");
    queryStringBuilder.append(" 			, risk_loss_acceptable ");
    queryStringBuilder.append(" 			, risk_loan ");
    queryStringBuilder.append(" 			, risk_description ");
    queryStringBuilder.append(" 			, risk_investment_chance ");
    queryStringBuilder.append(" 		FROM dwh.smy_dwh_tf_riskprofile WHERE tcbsid  = :tcbsId ");
    queryStringBuilder.append(" 		order by risk_submitted_date desc ");
    queryStringBuilder.append(" 		)  UNPIVOT ( val FOR field_name IN (risk_marriage_status ");
    queryStringBuilder.append(" 	    	, risk_education_status ");
    queryStringBuilder.append(" 	    	, risk_annual_income ");
    queryStringBuilder.append(" 	    	, risk_current_asset ");
    queryStringBuilder.append(" 	    	, risk_financial_safety ");
    queryStringBuilder.append(" 	    	, risk_financial_knowledge ");
    queryStringBuilder.append(" 	    	, risk_information_update ");
    queryStringBuilder.append(" 	    	, risk_experience_investment ");
    queryStringBuilder.append(" 	    	, risk_holding_period ");
    queryStringBuilder.append(" 	    	, risk_profit_expectation ");
    queryStringBuilder.append(" 	    	, risk_achieve_target ");
    queryStringBuilder.append(" 	    	, risk_loss_acceptable ");
    queryStringBuilder.append(" 	    	, risk_loan ");
    queryStringBuilder.append(" 	    	, risk_description ");
    queryStringBuilder.append(" 	    	, risk_investment_chance) ");
    queryStringBuilder.append(" 	) ");
    queryStringBuilder.append(" ) a left join tf_riskprofile_score b ON a.val = b.answer_id ");
    queryStringBuilder.append(" left join ( ");
    queryStringBuilder.append(" 	select sum(max_score) max_score_group , group_id  ");
    queryStringBuilder.append(" 	from ( ");
    queryStringBuilder.append(" 		select max(score_all) max_score, group_id  ");
    queryStringBuilder.append(" 		from tf_riskprofile_score  ");
    queryStringBuilder.append(" 		group by field_name,group_id ");
    queryStringBuilder.append(" 	) group by group_id ");
    queryStringBuilder.append(" ) max_score_smy on b.group_id = max_score_smy.group_id ");

    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter("tcbsId", tcbsId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data")
  public static List<HashMap<String, Object>> getAllRiskScore() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select avg(risk_score) avg_risk_score, avg(risk_score / max_score_all) avg_percent, avg(max_score_all) max_score_all ");
    queryStringBuilder.append(" from dwh.smy_dwh_tf_riskprofile ");
    queryStringBuilder.append(" left join ( ");
    queryStringBuilder.append(" 	select sum(max_score) max_score_all  ");
    queryStringBuilder.append(" 	from ( ");
    queryStringBuilder.append(" 		select max(score_all) max_score, group_id  ");
    queryStringBuilder.append(" 		from tf_riskprofile_score  ");
    queryStringBuilder.append(" 		group by field_name,group_id ");
    queryStringBuilder.append(" 	)	 ");
    queryStringBuilder.append(" ) max_score_all on 1 = 1 ");

    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data")
  public static List<HashMap<String, Object>> getAvgPercentGroupId() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select group_id as grpid, round(avg(score_e), 2 ) as avg_e from ( ");
    queryStringBuilder.append("	select b.tcbsid, b.group_id , cast(sum_score as float)/ max_score_group as score_e from ( ");
    queryStringBuilder.append("		SELECT a.tcbsid, b.group_id , sum(score_all) as sum_score ");
    queryStringBuilder.append("		from ( ");
    queryStringBuilder.append("			select *  ");
    queryStringBuilder.append("			FROM (select tcbsid, cast(risk_score AS varchar) AS risk_score ");
    queryStringBuilder.append("					, risk_marriage_status ");
    queryStringBuilder.append("					, risk_education_status ");
    queryStringBuilder.append("					, risk_annual_income ");
    queryStringBuilder.append("					, risk_current_asset ");
    queryStringBuilder.append("					, risk_financial_safety ");
    queryStringBuilder.append("					, risk_financial_knowledge ");
    queryStringBuilder.append("					, risk_information_update ");
    queryStringBuilder.append("					, risk_experience_investment ");
    queryStringBuilder.append("					, risk_holding_period ");
    queryStringBuilder.append("					, risk_profit_expectation ");
    queryStringBuilder.append("					, risk_achieve_target ");
    queryStringBuilder.append("					, risk_loss_acceptable ");
    queryStringBuilder.append("					, risk_loan ");
    queryStringBuilder.append("					, risk_description ");
    queryStringBuilder.append("					, risk_investment_chance ");
    queryStringBuilder.append("				FROM dwh.smy_dwh_tf_riskprofile  ");
    queryStringBuilder.append("				order by risk_submitted_date desc ");
    queryStringBuilder.append("				)  UNPIVOT ( val FOR field_name IN (risk_marriage_status ");
    queryStringBuilder.append("			    	, risk_education_status ");
    queryStringBuilder.append("			    	, risk_annual_income ");
    queryStringBuilder.append("			    	, risk_current_asset ");
    queryStringBuilder.append("			    	, risk_financial_safety ");
    queryStringBuilder.append("			    	, risk_financial_knowledge ");
    queryStringBuilder.append("			    	, risk_information_update ");
    queryStringBuilder.append("			    	, risk_experience_investment ");
    queryStringBuilder.append("			    	, risk_holding_period ");
    queryStringBuilder.append("			    	, risk_profit_expectation ");
    queryStringBuilder.append("			    	, risk_achieve_target ");
    queryStringBuilder.append("			    	, risk_loss_acceptable ");
    queryStringBuilder.append("			    	, risk_loan ");
    queryStringBuilder.append("			    	, risk_description ");
    queryStringBuilder.append("			    	, risk_investment_chance) ");
    queryStringBuilder.append("			) ");
    queryStringBuilder.append("		) a left join tf_riskprofile_score b ON a.val = b.answer_id ");
    queryStringBuilder.append("		group by a.tcbsid, b.group_id  ");
    queryStringBuilder.append("	) b left join ( ");
    queryStringBuilder.append("		select sum(max_score) max_score_group , group_id  ");
    queryStringBuilder.append("		from ( ");
    queryStringBuilder.append("			select max(score_all) max_score, group_id  ");
    queryStringBuilder.append("			from tf_riskprofile_score  ");
    queryStringBuilder.append("			group by field_name,group_id ");
    queryStringBuilder.append("		) group by group_id ");
    queryStringBuilder.append("	) max_score_smy on b.group_id = max_score_smy.group_id ");
    queryStringBuilder.append(") t where group_id is not null group by group_id ");

    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
