package com.tcbs.automation.tcbsdwh.ani.riskprofile;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RiskprofileEntity {
  public static final HibernateEdition redShiftDb = Database.TCBS_DWH_STAGING.getConnection();

  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getRiskprofileSql(String etldate) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" with risk as( ");
    queryBuilder.append(" select tr.index, tr.TCBSID, tr.submitted_at ");
    queryBuilder.append(" ,json_extract_path_text(sr.field,'id') as question_id ");
    queryBuilder.append(" , case when choice <> '' then json_extract_path_text(choice,'label') ");
    queryBuilder.append(" else json_extract_path_text(choices,'labels') end as label ");
    queryBuilder.append(" , case  when choice <> '' then json_extract_path_text(choice,'id') ");
    queryBuilder.append(" else json_extract_path_text(choices,'ids') end as answer_id ");
    queryBuilder.append(" , tr.landed_at as CreatedDate ");
    queryBuilder.append(" , case when json_extract_path_text(field,'id') = 'f4FvA79MTj7n' then 'Risk_Marriage_status'  ");
    queryBuilder.append(" when json_extract_path_text(field,'id') = 'YhtSWgSgeXFf' then 'Risk_Education_status'  ");
    queryBuilder.append(" when json_extract_path_text(field,'id') = 'DVxSqSTHT1Gd' then 'Risk_Annual_income'  ");
    queryBuilder.append(" when json_extract_path_text(field,'id') = 'HwGAuAVmDC4S' then 'Risk_Current_asset'  ");
    queryBuilder.append(" when json_extract_path_text(field,'id') = '26AEqJ3RU612' then 'Risk_Financial_safety'   ");
    queryBuilder.append(" when json_extract_path_text(field,'id') = '5imCDLuPKZzv' then 'Risk_Financial_knowledge' ");
    queryBuilder.append(" when json_extract_path_text(field,'id') = 'DTxIL0bf19Bw' then 'Risk_Information_update'  ");
    queryBuilder.append(" when json_extract_path_text(field,'id') = 'MqBZc7oOq0s5' then 'Risk_Experience_investment'  ");
    queryBuilder.append(" when json_extract_path_text(field,'id') = 'mJaNBS0ddGzr' then 'Risk_Holding_period'  ");
    queryBuilder.append(" when json_extract_path_text(field,'id') = 'LLsI14R75AZt' then 'Risk_Profit_expectation'  ");
    queryBuilder.append(" when json_extract_path_text(field,'id') = 'vjf0U7lFYsY0' then 'Risk_Achieve_target'  ");
    queryBuilder.append(" when json_extract_path_text(field,'id') = 'ccmQKrIZ8WeZ' then 'Risk_Loss_acceptable'  ");
    queryBuilder.append(" when json_extract_path_text(field,'id') = 'XWSuGVM8n64o' then 'Risk_Loan'  ");
    queryBuilder.append(" when json_extract_path_text(field,'id') = 'p8LkzkhP4sCR' then 'Risk_Description'  ");
    queryBuilder.append(" when json_extract_path_text(field,'id') = 'gvIPp7qE0mta' then 'Risk_Investment_chance'  ");
    queryBuilder.append(" end as question_value ");
    queryBuilder.append(" , tr.form_id ");
    queryBuilder.append(" , tr.response_id, tr.calculated_score ");
    queryBuilder.append(" from dwh.smy_dwh_tf_split_response sr ");
    queryBuilder.append(" left join staging.stg_social_stg_typeform_responses tr  ");
    queryBuilder.append(" on sr.response_id = tr.response_id ");
    queryBuilder.append(" where tr.form_id = 'pa0h30xD' ");
    queryBuilder.append(" and tcbsid != '' and tcbsid is not null ");
    queryBuilder.append(" ),rs1 as ( ");
    queryBuilder.append(" select tcbsid,question_id, ");
    queryBuilder.append(" answer_id, ");
    queryBuilder.append(" submitted_at     Risk_Submitted_Date, ");
    queryBuilder.append(" index, ");
    queryBuilder.append(" cast(question_value as varchar), ");
    queryBuilder.append(" calculated_score Risk_Score ");
    queryBuilder.append(" from risk ");
    queryBuilder.append(" ),rs2 as ( ");
    queryBuilder.append(" select tcbsid, ");
    queryBuilder.append(" question_value, ");
    queryBuilder.append(" Risk_Submitted_Date, ");
    queryBuilder.append(" Risk_Score, ");
    queryBuilder.append(" answer_id, ");
    queryBuilder.append(" rank() over (partition by TCBSID,index  order by index desc) as rn ");
    queryBuilder.append(" from rs1  ");
    queryBuilder.append(" )select tcbsid,risk_submitted_date,risk_score,risk_marriage_status,risk_education_status ");
    queryBuilder.append(" ,risk_annual_income,risk_current_asset,risk_financial_safety,risk_financial_knowledge,risk_information_update ");
    queryBuilder.append(" ,risk_experience_investment,risk_holding_period,risk_profit_expectation,risk_achieve_target,risk_loss_acceptable ");
    queryBuilder.append(" ,risk_loan,risk_description,risk_investment_chance,etlcurdate,etlrundatetime ");
    queryBuilder.append(" from( ");
    queryBuilder.append(" select *,:p_etldate as EtlCurDate, dateadd(hour,7,getdate()) as EtlRunDateTime ");
    queryBuilder.append(" from( ");
    queryBuilder.append(" select *, ROW_NUMBER() over (partition by TCBSID order by Risk_Submitted_Date desc) as rn_2  ");
    queryBuilder.append(" from rs2   ");
    queryBuilder.append(" pivot ");
    queryBuilder.append(" (min (answer_id) for question_value in ");
    queryBuilder.append(" ('Risk_Marriage_status', 'Risk_Education_status', 'Risk_Annual_income', 'Risk_Current_asset', 'Risk_Financial_safety' ");
    queryBuilder.append(" , 'Risk_Financial_knowledge', 'Risk_Information_update', 'Risk_Experience_investment', 'Risk_Holding_period' ");
    queryBuilder.append(" , 'Risk_Profit_expectation' , 'Risk_Achieve_target', 'Risk_Loss_acceptable', 'Risk_Loan', 'Risk_Description' ");
    queryBuilder.append(" , 'Risk_Investment_chance') ");
    queryBuilder.append(" )where rn = '1'   ");
    queryBuilder.append(" )where rn_2 = '1' ");
    queryBuilder.append(" )order by tcbsid  ");



    try {
      redShiftDb.closeSession();
      redShiftDb.openSession();
      List<HashMap<String, Object>> data = redShiftDb.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("p_etldate", etldate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return data;


    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    redShiftDb.closeSession();
    return new ArrayList<>();
  }

  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getRiskprofileProcSql() {
    StringBuilder queryBuilderProc = new StringBuilder();

    queryBuilderProc.append(" select * from dwh.smy_dwh_tf_riskprofile order by tcbsid ");

    try {
      redShiftDb.closeSession();
      redShiftDb.openSession();
      return redShiftDb.getSession().createNativeQuery(queryBuilderProc.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    redShiftDb.closeSession();
    return new ArrayList<>();
  }

}
