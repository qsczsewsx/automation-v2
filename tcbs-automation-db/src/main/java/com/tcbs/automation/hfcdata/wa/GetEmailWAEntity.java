package com.tcbs.automation.hfcdata.wa;

import com.tcbs.automation.hfcdata.HfcData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetEmailWAEntity {

  @Step("call proc")
  public static void callProc(String procName, String date) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("call ").append(procName).append("('").append(date).append("')");
    executeQuery(queryBuilder);
  }

  @Step(" get data from table")
  public static List<HashMap<String, Object>> getDataFromTable(String tableName) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select * from ").append(tableName);
    return getData(queryBuilder);
  }

  @Step(" get data from proc")
  public static List<HashMap<String, Object>> getDataFromProc() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" WITH tmp_IB_Email AS (");
    queryBuilder.append("SELECT \"zone\" AS user_zone, LISTAGG(Email, '; ') AS cc_IB_Email ");
    queryBuilder.append(" FROM dwh.Off_WA_ZoneUser ");
    queryBuilder.append(" WHERE createddate = (SELECT max(createddate) FROM dwh.Off_WA_ZoneUser) ");
    queryBuilder.append(" AND \"permission\" IN ('IBSales', 'IBSupport') ");
    queryBuilder.append(" AND email <> 'habtt3@techcombank.com.vn' ");
    queryBuilder.append(" AND email LIKE '%@techcombank.com.vn' GROUP BY \"zone\") ");

    queryBuilder.append(", tmp_BM_Email AS (");
    queryBuilder.append("SELECT AgencyId, LISTAGG(Email, '; ') AS cc_BM_Email ");
    queryBuilder.append(" FROM staging.Stg_Tcb_rm ");
    queryBuilder.append(" WHERE \"permission\" = 'BM' ");
    queryBuilder.append(" AND Active = 1  ");
    queryBuilder.append(" AND email like '%@techcombank.com.vn' ");
    queryBuilder.append(" GROUP BY AgencyId) ");

    queryBuilder.append(", tmp_ccemail AS (");
    queryBuilder.append("select a.Id, a.AgencyCode, ib.user_zone ");
    queryBuilder.append(" , CASE WHEN cc_BM_Email is null THEN cc_IB_Email ELSE concat(cc_BM_Email, concat(';' ,cc_IB_Email)) END AS CC_Email ");
    queryBuilder.append(" FROM staging.Stg_Tcb_Agency a ");
    queryBuilder.append(" LEFT JOIN tmp_BM_Email bm  ");
    queryBuilder.append(" ON a.id = bm.AgencyId  ");
    queryBuilder.append(" LEFT JOIN tmp_IB_Email ib ");
    queryBuilder.append(" ON a.\"zone\" = ib.user_zone ");
    queryBuilder.append(" WHERE a.Active = 1 AND a.\"zone\" like 'Vùng%') ");

    queryBuilder.append(", holiday as (");
    queryBuilder.append("select HolidayDate  ");
    queryBuilder.append(" from staging.stg_tcb_holiday ho ");
    queryBuilder.append(" where ho.etlcurdate = (select max(etlcurdate) from staging.stg_tcb_holiday) ");
    queryBuilder.append(" and HolidayDate >= cast(getdate()-5 as date) and HolidayDate <= cast(getdate() + 5 as date) ");
    queryBuilder.append(" union ALL ");
    queryBuilder.append(" select \"date\" ");
    queryBuilder.append(" from dwh.smy_dwh_date_dim ");
    queryBuilder.append(" where \"day\" in ('Saturday', 'Sunday') ");
    queryBuilder.append(" and etlcurdate = (select max(etlcurdate) from dwh.smy_dwh_date_dim) ");
    queryBuilder.append(" and \"date\" >= cast(getdate()-5 as date) and \"date\" <= cast(getdate() + 5 as date)) ");

    queryBuilder.append(", cf as (");
    queryBuilder.append("select cf.CustomerID, cf.OBal_Quantity, cf.CFI, cf.\"tag\", cf.bondcode, cf.cfidate  ");
    queryBuilder.append(
      " , cas.rm_name, cas.rm_email, cas.agencycode, cas.\"zone\", cas.rm_code, cas.customercode_tcb, case when cas.\"permission\" = 'RBO' then 'RBO' else 'RM' end as \"permission\" ");

    queryBuilder.append(" from dwh.smy_dwh_tcb_bondcontractcfi cf ");
    queryBuilder.append(" inner join( ");
    queryBuilder.append(" select * from dwh.Smy_dwh_cas_ParentUser  ");
    queryBuilder.append(" where etlcurdate = (select max(etlcurdate) from dwh.Smy_dwh_cas_ParentUser) and RMType in ('Identified','RBO', 'BB') ");
    queryBuilder.append(" ) cas on cf.CustomerID = cas.CustomerId ");
    queryBuilder.append(" inner join staging.stg_tcb_rm rm  ");
    queryBuilder.append(" on cast(rm.usercode as varchar) = cast(cas.rm_code as varchar)  ");
    queryBuilder.append(" and rm.active = 1 and rm.email like '%@techcombank.com.vn' and rm.\"permission\" in ('RM', 'RBO', 'BM') ");

    queryBuilder.append(" inner join dwh.smy_dwh_tcb_bondfeature bf  ");
    queryBuilder.append(" on bf.bondcode = cf.bondcode and cast(bf.expireddate as date) = cast(cf.CFIDate as date) ");
    queryBuilder.append(" where cf.Tradingcode not in ('IB.%', 'IWPC.%')  ");
    queryBuilder.append(" and ((cf.Tradingcode like 'IWDC%' and cas.cus_sharing_option = 0) is false ) ");
    queryBuilder.append(" and cf.cfidate = cast((select staging.f_dateadd_working_day(getdate() ,5)) as date) ");
    queryBuilder.append(" and not exists (select * from holiday where holidaydate = cast (getdate() as date)))  ");

    queryBuilder.append(" , cfbycus as( ");
    queryBuilder.append(" select CustomerID, rm_name, rm_email, agencycode, \"zone\", rm_code, customercode_tcb, \"permission\" ");
    queryBuilder.append(" , cf.BondCode, cast (CFIDate as date) as CFIDate  ");
    queryBuilder.append(" , sum(OBal_Quantity) /2 as Quantity ");
    queryBuilder.append(" , sum(CFI) as Total ");
    queryBuilder.append(" , sum (case when cf.\"tag\" = 'Coupon Payment' then CFI end) as Coupon  ");
    queryBuilder.append(" , sum (case when cf.\"tag\" = 'Bond Matured' then CFI end) as FaceValue ");
    queryBuilder.append(" from cf group by CustomerID, rm_name, rm_email, agencycode, \"zone\", rm_code, customercode_tcb, \"permission\", cf.BondCode, CFIDate) ");

    queryBuilder.append(
      " select cf1.Total, cf1.Quantity, cf1.Coupon, cf1.FaceValue, cf1.CFIDate, cf1.BondCode, c.Tcbsid, cf1.customercode_tcb as CustomerCode, c.CustomerName, cf1.RM_Code, cf1.RM_Name\n" +
        "\t\t, cf1.AgencyCode, cf1.RM_Email\n" +
        "\t\t, em.CC_Email\n" +
        "\t\t, cf1.\"zone\", b.listedcode\n" +
        "\t\t, case when b.listedstatus = 0 then 'Không' \n" +
        "\t\t\telse 'Có'\n" +
        "\t\tend as listedstatus\n" +
        "\t\t, case when b.listedstatus = 0 then 'TK thụ hưởng' \n" +
        "\t\t\telse c.custodycode \n" +
        "\t\tend as TKTT\n" +
        "\t\t, cf1.\"permission\" ");

    queryBuilder.append(" from cfbycus cf1 left join staging.stg_Tcb_Customer c on cf1.CustomerId = c.CustomerId ");
    queryBuilder.append(" left join(select * from dwh.smy_dwh_tcb_bondfeature where etlcurdate = (select max(etlcurdate) from dwh.smy_dwh_tcb_bondfeature)) b on b.bondCode = cf1.bondcode\n" +
      "\tLEFT JOIN tmp_ccemail em \n" +
      "\t\tON cf1.agencycode = em.AgencyCode  ");

    return getData(queryBuilder);
  }

  public static List<HashMap<String, Object>> getData(StringBuilder queryBuilder) {
    Session session = HfcData.tcbsDwhDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    try {
      return session.createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    HfcData.tcbsDwhDbConnection.closeSession();
    return new ArrayList<>();
  }

  @Step("execute query")
  public static void executeQuery(StringBuilder sql) {
    Session session = HfcData.tcbsDwhWriteDbConnection.getSession();
    try {
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query query = session.createNativeQuery(sql.toString());
      query.executeUpdate();
//      session.getTransaction().commit();
      HfcData.tcbsDwhWriteDbConnection.closeSession();
    } catch (Exception e) {
      HfcData.tcbsDwhWriteDbConnection.closeSession();
      throw e;
    }

  }

}
