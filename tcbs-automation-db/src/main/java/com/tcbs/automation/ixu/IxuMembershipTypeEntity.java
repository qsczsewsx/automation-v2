package com.tcbs.automation.ixu;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "IXU_MEMBERSHIP_TYPE")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IxuMembershipTypeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Long id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "POINT")
  private Long point;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;


  @Step
  public static List<IxuMembershipTypeEntity> findMembershipTypeById(Long id) {
    Query<IxuMembershipTypeEntity> query = ixuDbConnection.getSession().createQuery(
      "from IxuMembershipTypeEntity a where a.id=:id ",
      IxuMembershipTypeEntity.class);
    query.setParameter("id", id);
    List<IxuMembershipTypeEntity> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }
}