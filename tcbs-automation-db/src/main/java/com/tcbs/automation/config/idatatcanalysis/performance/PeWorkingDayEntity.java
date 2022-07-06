package com.tcbs.automation.config.idatatcanalysis.performance;

import com.tcbs.automation.config.idatatcanalysis.IData;
import lombok.Data;
import net.thucydides.core.annotations.Step;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "PE_WORKING_DAY")
public class PeWorkingDayEntity {
  @Id
  @Column(name = "W_DATE")
  private Date wDate;

  @Step
  public static PeWorkingDayEntity getPreWorkingDay(String reportDate) {
    Query<PeWorkingDayEntity> query = IData.idataDbConnection.getSession()
      .createQuery("from PeWorkingDayEntity WHERE wDate < to_date(:reportDate, 'yyyy-MM-dd') ORDER BY wDate DESC ",
        PeWorkingDayEntity.class);
    query.setParameter("reportDate", reportDate);
    query.setFirstResult(0);
    query.setMaxResults(1);
    List<PeWorkingDayEntity> list = query.getResultList();
    IData.idataDbConnection.closeSession();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }
}
