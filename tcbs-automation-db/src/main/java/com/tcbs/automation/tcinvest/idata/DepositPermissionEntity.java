package com.tcbs.automation.tcinvest.idata;

import com.tcbs.automation.tcinvest.TcInvest;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "DEPOSIT_PERMISSION", schema = "IDATASIT")
public class DepositPermissionEntity {
  @Id
  private String tcbsid;
  private String custodyid;
  private String email;
  private Long tdPermission;
  private Timestamp updateTime;

  @Basic
  @Column(name = "TCBSID")
  public String getTcbsid() {
    return tcbsid;
  }

  public void setTcbsid(String tcbsid) {
    this.tcbsid = tcbsid;
  }

  @Basic
  @Column(name = "CUSTODYID")
  public String getCustodyid() {
    return custodyid;
  }

  public void setCustodyid(String custodyid) {
    this.custodyid = custodyid;
  }

  @Basic
  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Basic
  @Column(name = "TD_PERMISSION")
  public Long getTdPermission() {
    return tdPermission;
  }

  public void setTdPermission(Long tdPermission) {
    this.tdPermission = tdPermission;
  }

  @Basic
  @Column(name = "UPDATE_TIME")
  public Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Timestamp updateTime) {
    this.updateTime = updateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DepositPermissionEntity that = (DepositPermissionEntity) o;
    return Objects.equals(tcbsid, that.tcbsid) &&
      Objects.equals(custodyid, that.custodyid) &&
      Objects.equals(email, that.email) &&
      Objects.equals(tdPermission, that.tdPermission) &&
      Objects.equals(updateTime, that.updateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tcbsid, custodyid, email, tdPermission, updateTime);
  }

  @Step("delete data by key")
  public void deleteByTcbsId(String tcbsId) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<DepositPermissionEntity> query = session.createQuery(
      "DELETE FROM DepositPermissionEntity i WHERE i.tcbsid=:tcbsId"
    );
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
