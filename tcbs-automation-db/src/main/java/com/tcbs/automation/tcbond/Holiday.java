package com.tcbs.automation.tcbond;

import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "HOLIDAY")
public class Holiday {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "HOLIDAYNAME")
  private String holidayname;
  @NotNull
  @Column(name = "HOLIDAYDATE")
  private Date holidaydate;
  @Column(name = "ACTIVE")
  private BigDecimal active;
  @Column(name = "UPDATEVALUE")
  private BigDecimal updatevalue;


  public Holiday() {
  }

  public Holiday(Integer id, String holidayname, Date holidaydate, BigDecimal active, BigDecimal updatevalue) {
    this.id = id;
    this.holidayname = holidayname;
    this.holidaydate = holidaydate;
    this.active = active;
    this.updatevalue = updatevalue;
  }


  public Holiday(String holidayname, Date holidaydate, BigDecimal active, BigDecimal updatevalue) {
    this.holidayname = holidayname;
    this.holidaydate = holidaydate;
    this.active = active;
    this.updatevalue = updatevalue;
  }

  public Holiday(String holidayname, BigDecimal active, BigDecimal updatevalue) {
    this.holidayname = holidayname;
    this.active = active;
    this.updatevalue = updatevalue;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getHolidayname() {
    return holidayname;
  }

  public void setHolidayname(String holidayname) {
    this.holidayname = holidayname;
  }

  public Date getHolidaydate() {
    return holidaydate;
  }

  public void setHolidaydate(Date holidaydate) {
    this.holidaydate = holidaydate;
  }

  public BigDecimal getActive() {
    return active;
  }

  public void setActive(BigDecimal active) {
    this.active = active;
  }

  public BigDecimal getUpdatevalue() {
    return updatevalue;
  }

  public void setUpdatevalue(BigDecimal updatevalue) {
    this.updatevalue = updatevalue;
  }

  public void insert() {
    Session session = TcBond.tcBondDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    session.save(this);
    session.getTransaction().commit();
  }

  public void delete() {
    Session session = TcBond.tcBondDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<Holiday> query = session.createQuery(
      "DELETE FROM Holiday WHERE id=:id "
    );
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}