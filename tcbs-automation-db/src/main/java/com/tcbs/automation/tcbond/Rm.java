package com.tcbs.automation.tcbond;

import com.tcbs.automation.cas.CAS;
import com.tcbs.automation.functions.PublicConstant;
import lombok.Getter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.tcbs.automation.config.ipartner.IpartnerConstants.TCBSID_TEXT;
import static com.tcbs.automation.tcbond.TcBond.tcBondDbConnection;

@Entity
@Getter
@Table(name = "RM")
public class Rm {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Integer id;
  @Column(name = "USERCODE")
  private String usercode;
  @NotNull
  @Column(name = "USERNAME")
  private String username;
  @NotNull
  @Column(name = "PASSWORD")
  private String password;
  @Column(name = "FULLNAME")
  private String fullname;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "PHONE")
  private String phone;
  @Column(name = "FAX")
  private String fax;
  @NotNull
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "AGENCYID")
  private Integer agencyid;
  @Column(name = "PERMISSION")
  private String permission;
  @Column(name = "RMTYPECODE")
  private String rmtypecode;
  @Column(name = "HEADPRIORITY")
  private String headpriority;
  @Column(name = "PMEMAIL")
  private String pmemail;
  @Column(name = "NOTEZONE")
  private String notezone;
  @Column(name = "BDM")
  private String bdm;
  @Column(name = "STAFTID")
  private String staftid;
  @Column(name = "PARENTID")
  private Integer parentid;
  @Column(name = "ACTIVE")
  private Integer active;
  @Column(name = "CREATEDDATE")
  private Timestamp createddate;
  @Column(name = "AGENCY")
  private Long agency;
  @Column(name = "REFERENCEUSER")
  private Integer referenceuser;
  @Column(name = "STARTWORKDATE")
  private Date startworkdate;
  @Column(name = "ENDWORKDATE")
  private Date endworkdate;
  @Column(name = "TCBSID")
  private String tcbsid;
  @Column(name = "ACCESSRIGHTS")
  private Integer accessrights;


  public Rm setId(Integer id) {
    this.id = id;
    return this;
  }


  public Rm setUsercode(String usercode) {
    this.usercode = usercode;
    return this;
  }


  public Rm setUsername(String username) {
    this.username = username;
    return this;
  }


  public Rm setPassword(String password) {
    this.password = password;
    return this;
  }


  public Rm setFullname(String fullname) {
    this.fullname = fullname;
    return this;
  }


  public Rm setAddress(String address) {
    this.address = address;
    return this;
  }


  public Rm setPhone(String phone) {
    this.phone = phone;
    return this;
  }


  public Rm setFax(String fax) {
    this.fax = fax;
    return this;
  }


  public Rm setEmail(String email) {
    this.email = email;
    return this;
  }


  public Rm setAgencyid(Integer agencyid) {
    this.agencyid = agencyid;
    return this;
  }


  public Rm setPermission(String permission) {
    this.permission = permission;
    return this;
  }


  public Rm setRmtypecode(String rmtypecode) {
    this.rmtypecode = rmtypecode;
    return this;
  }


  public Rm setHeadpriority(String headpriority) {
    this.headpriority = headpriority;
    return this;
  }


  public Rm setPmemail(String pmemail) {
    this.pmemail = pmemail;
    return this;
  }


  public Rm setNotezone(String notezone) {
    this.notezone = notezone;
    return this;
  }


  public Rm setBdm(String bdm) {
    this.bdm = bdm;
    return this;
  }


  public Rm setStaftid(String staftid) {
    this.staftid = staftid;
    return this;
  }


  public Rm setParentid(Integer parentid) {
    this.parentid = parentid;
    return this;
  }


  public Rm setActive(Integer active) {
    this.active = active;
    return this;
  }


  public Rm setCreateddate(String createddate) throws ParseException {
    this.createddate = new Timestamp(PublicConstant.dateTimeFormat.parse(createddate).getTime());
    return this;
  }


  public Rm setAgency(Long agency) {
    this.agency = agency;
    return this;
  }


  public Rm setReferenceuser(Integer referenceuser) {
    this.referenceuser = referenceuser;
    return this;
  }


  public Rm setStartworkdate(Date startworkdate) {
    this.startworkdate = startworkdate;
    return this;
  }


  public Rm setEndworkdate(Date endworkdate) {
    this.endworkdate = endworkdate;
    return this;
  }


  public Rm setTcbsid(String tcbsid) {
    this.tcbsid = tcbsid;
    return this;
  }


  public Rm setAccessrights(Integer accessrights) {
    this.accessrights = accessrights;
    return this;
  }

  @Step("Update RM {0} data")
  public Rm updateRmData() {
    Session session = tcBondDbConnection.getSession();
    Transaction trans = session.beginTransaction();
    Query<Rm> query = session.createQuery(
      "update Rm SET active =:active where tcbsid=:tcbsid");
    query.setParameter("tcbsid", this.tcbsid);
    query.setParameter("active", this.active);
    query.executeUpdate();
    trans.commit();
    return this;
  }


  public List<Rm> getRm(String tcbsid) {
    Session session = CAS.casConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<Rm> query = session.createQuery("from TcbsReferral WHERE tcbsid=:tcbsid", Rm.class);
    List<Rm> result = new ArrayList<>();
    query.setParameter(TCBSID_TEXT, tcbsid);
    query.setMaxResults(1);
    try {
      result = query.getResultList();
    } catch (Exception e) {
      return result;
    }

    return result;
  }

}