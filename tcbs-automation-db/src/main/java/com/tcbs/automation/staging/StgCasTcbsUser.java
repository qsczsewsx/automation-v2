package com.tcbs.automation.staging;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import wiremock.org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Stg_cas_TCBS_USER")
public class StgCasTcbsUser {
  private String custodyCd;
  @Id
  private String tcbsId;
  private Integer userId;
  private Date birthDay;
  private String customerName;

  @Step
  public static List<StgCasTcbsUser> getCusBd(String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select au.user_app_id as custodyCd, u.*  \r\n");
    queryStringBuilder.append("from ( \r\n");
    queryStringBuilder.append("  select tcbsId, userId, birthDay,customerName \r\n");
    queryStringBuilder.append("  from ( \r\n");
    queryStringBuilder.append("  select tcbsId, userId, birthDay,customerName, DATEFROMPARTS(year(:fromDate), month(birthDateStart), day(birthDateStart)) bt,  \r\n");
    queryStringBuilder.append("  case when DATEFROMPARTS(year(:fromDate), month(birthDateStart), day(birthDateStart)) between :fromDate and :toDate then 1 else 0 end chk,  \r\n");
    queryStringBuilder.append("  case when DATEFROMPARTS(year(:toDate), month(birthDateEnd), day(birthDateEnd)) between :fromDate and :toDate then 1 else 0 end chk2 \r\n");
    queryStringBuilder.append("  from (   \r\n");
    queryStringBuilder.append("   select tcbsId, u.ID as userId, cast(u.birthDay as date) as birthDay,  \r\n");
    queryStringBuilder.append("   case when DAY(EOMONTH(DATEFROMPARTS(year(:fromDate),2,1))) = 28 and day(birthDay) = 29 and month(birthDay) = 2 then dateadd(dd, -1, birthDay) else birthDay  \r\n");
    queryStringBuilder.append("   end as birthDateStart,  \r\n");
    queryStringBuilder.append("   case when DAY(EOMONTH(DATEFROMPARTS(year(:toDate),2,1))) = 28 and day(birthDay) = 29 and month(birthDay) = 2 then dateadd(dd, -1, birthDay) else birthDay \r\n");
    queryStringBuilder.append("   end as birthDateEnd,  \r\n");
    queryStringBuilder.append("   dbo.InitialCap(ISNULL(CONVERT(nvarchar(600),u.lastname),'') + ' ' + ISNULL(CONVERT(nvarchar(600),u.firstname),'')) as customerName \r\n");
    queryStringBuilder.append("   from  Stg_cas_tcbs_user u    \r\n");
    queryStringBuilder.append(" ) birthday   \r\n");
    queryStringBuilder.append(") birthChk   \r\n");
    queryStringBuilder.append("  where (chk = 1 or chk2 = 1)    \r\n");
    queryStringBuilder.append(") u  \r\n");
    queryStringBuilder.append("left join Stg_cas_TCBS_APPLICATION_USER au on au.user_id = u.userId and au.app_id = 2 and au.user_app_id is not null \r\n");

    List<StgCasTcbsUser> listResult = new ArrayList<>();
    try {
      List<Object[]> result = Staging.stagingDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .getResultList();

      if (CollectionUtils.isNotEmpty(result)) {
        result.forEach(object -> {
            StgCasTcbsUser userInfo = StgCasTcbsUser.builder()
              .custodyCd((String) object[0])
              .tcbsId((String) object[1])
              .userId((Integer) object[2])
              .birthDay((Date) object[3])
              .customerName((String) object[4])
              .build();
            listResult.add(userInfo);
          }
        );

        return listResult;
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
