package com.tcbs.automation.ipartner.iwp;

import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.functions.PublicConstant;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.ipartner.IWpartner.iwpDBConnection;

@Entity
@Table(name = "tcbs_employee")
public class TcbsEmployee {
  private static Logger logger = LoggerFactory.getLogger(TcbsEmployee.class);
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "id")
  private Long id;
  @Column(name = "tcbsid")
  private String tcbsid;
  @Column(name = "status")
  private String status;
  @Column(name = "created_date")
  private Timestamp createdDate;
  @Column(name = "modified_date")
  private Timestamp modifiedDate;
  @Column(name = "modified_by")
  private String modifiedBy;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getTcbsid() {
    return tcbsid;
  }

  public void setTcbsid(String tcbsid) {
    this.tcbsid = tcbsid;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public String getCreatedDate() {
    return createdDate == null ? null : PublicConstant.dateTimeFormat.format(createdDate);
  }

  public void setCreatedDate(String createdDate) throws ParseException {
    this.createdDate = new Timestamp(PublicConstant.dateTimeFormat.parse(createdDate).getTime());
  }


  public String getModifiedDate() {
    return modifiedDate == null ? null : PublicConstant.dateTimeFormat.format(modifiedDate);
  }

  public void setModifiedDate(String modifiedDate) throws ParseException {
    this.modifiedDate = new Timestamp(PublicConstant.dateTimeFormat.parse(modifiedDate).getTime());
  }


  public String getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  @Step
  public List<TcbsEmployee> getTcbsEmployee(String filters) {
    try {
      Query<TcbsEmployee> query;
      if (filters.contains("105C")) {
        int lastIndex = 0;
        lastIndex = filters.indexOf("105C");
        String custodyCode = filters.substring(lastIndex, lastIndex + 10);
        //System.out.printf("get 105C: %s",custodyCode);
        TcbsUser tcbsUser = new TcbsUser();
        String tcbsId = tcbsUser.getByUserName(custodyCode).getTcbsid();
        System.out.printf("get 105C: %s id =%s \n", custodyCode, tcbsId);
        query = iwpDBConnection.getSession().createQuery("from TcbsEmployee where tcbsid=:tcbsId", TcbsEmployee.class);
        query.setParameter("tcbsId", tcbsId);

      } else {
        query = iwpDBConnection.getSession().createQuery("from TcbsEmployee ", TcbsEmployee.class);
      }

      List<TcbsEmployee> result = query.getResultList();
      if (result.size() > 0) {
        System.out.printf("result.size()=%d \n", result.size());
        return result;
      } else {
        return null;
      }
    } catch (Exception ex) {
      logger.info(ex.getMessage());
    }
    return null;

  }

  public List<TcbsEmployee> getTcbsEmployeeByListUsername(ArrayList<String> filters) {
    Query<TcbsEmployee> query;
    query = iwpDBConnection.getSession().createQuery("from TcbsEmployee where  tcbsid in :filters", TcbsEmployee.class);
    query.setParameterList("filters", filters);

    List<TcbsEmployee> items = query.getResultList();
    HashMap<String, TcbsEmployee> map = new HashMap<String, TcbsEmployee>();
    for (TcbsEmployee i : items) {
      map.put(i.getTcbsid(), i);
    }
    List list = new ArrayList();
    list.add(map);
    return items;
  }

  public void updateTcbsEmployeeStatus(String username) {
    try {
      TcbsUser tcbsUser = new TcbsUser();
      String tcbsId = tcbsUser.getByUserName(username).getTcbsid();

      Session session = iwpDBConnection.getSession();
      session.clear();
      Transaction trans = session.beginTransaction();
      Query query = session.createSQLQuery(String.format("UPDATE tcbs_employee SET STATUS='ACTIVE'  WHERE tcbsid = '%s'", tcbsId));
      query.executeUpdate();
      trans.commit();
    } catch (Exception ex) {
      logger.info(ex.getMessage());
    }
  }

  public List<TcbsEmployee> getTcbsEmployeeByTcbsid(String tcbsId) {
    Query<TcbsEmployee> query;
    query = iwpDBConnection.getSession().createQuery("from TcbsEmployee where tcbsid=:tcbsId", TcbsEmployee.class);
    query.setParameter("tcbsId", tcbsId);
    List<TcbsEmployee> items = query.getResultList();
    return items;
  }

  public void deleteEmployeeByUsername(ArrayList<String> filters) {
    try {
      List<String> listTcbsId = new ArrayList<String>();
      TcbsUser tcbsUser = new TcbsUser();
      for (String username : filters) {
        listTcbsId.add(tcbsUser.getByUserName(username).getTcbsid());
      }
      Session session = iwpDBConnection.getSession();
      session.clear();
      Transaction trans = session.beginTransaction();
      Query query = session.createSQLQuery("delete from tcbs_employee where tcbsid in (?1)");
      query.setParameterList(1, listTcbsId);
      query.executeUpdate();
      trans.commit();
    } catch (Exception ex) {
      logger.info(ex.getMessage());
    }
  }

  public void insertTcbsEmployee(String username) {
    try {
      TcbsUser tcbsUser = new TcbsUser();
      String tcbsId = tcbsUser.getByUserName(username).getTcbsid();

      Session session = iwpDBConnection.getSession();
      session.clear();
      Transaction trans = session.beginTransaction();
      Query query = session.createSQLQuery(String.format("INSERT INTO tcbs_employee (tcbsid, status) VALUES ('%s','ACTIVE')", tcbsId));
      query.executeUpdate();
      trans.commit();
    } catch (Exception ex) {
      logger.info(ex.getMessage());
    }
  }
}
