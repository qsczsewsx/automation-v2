package com.tcbs.automation.projection.tcanalysis;


import com.tcbs.automation.projection.Projection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_idata_RatingKey", schema = "db_owner")
public class RatingKey {
  private static final Logger log = LoggerFactory.getLogger(RatingKey.class);

  @Id
  private Integer id;

  @Column(name = "ParentID")
  private Integer parentID;

  @Column(name = "RatingKey")
  private String ratingKey;

  @Column(name = "Note")
  private String note;


  /**
   * get lastest rating
   *
   * @param ratingKey
   * @return
   */

  @Step
  public static Integer getRatingKeyIdByName(String ratingKey) {
    Query<RatingKey> query = Projection.projectionDbConnection.getSession().createQuery(
      "FROM RatingKey rat WHERE rat.ratingKey=:ratingKey ", RatingKey.class);
    query.setParameter("ratingKey", ratingKey);
    query.setMaxResults(1);
    List<RatingKey> results = query.getResultList();
    if (CollectionUtils.isNotEmpty(results)) {
      return results.get(0).getId();
    }
    Projection.projectionDbConnection.closeSession();
    return null;
  }
}
