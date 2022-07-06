package com.tcbs.automation.tcwealth;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "WEALTH_PERSON_INFOR")
public class WealthPersonInfor {

  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Long id;
  @Column(name = "AGE")
  private Long age;
  @Column(name = "MARITAL_STATUS")
  private String maritalStatus;
  @Column(name = "OCCUPATION")
  private String occupation;
  @Column(name = "INVESTMENT_EXP")
  private String investmentExp;
  @Column(name = "FREQ_INCOME")
  private String freqIncome;
  @Column(name = "INCOME")
  private Long income;
  @Column(name = "SAVING_PERCENT")
  private Long savingPercent;
  @Column(name = "TOTAL_ASSETS")
  private Long totalAssets;
  @Column(name = "INVEST_ASSETS")
  private Long investAssets;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public Long getAge() {
    return age;
  }

  public void setAge(Long age) {
    this.age = age;
  }


  public String getMaritalStatus() {
    return maritalStatus;
  }

  public void setMaritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
  }


  public String getOccupation() {
    return occupation;
  }

  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }


  public String getInvestmentExp() {
    return investmentExp;
  }

  public void setInvestmentExp(String investmentExp) {
    this.investmentExp = investmentExp;
  }


  public String getFreqIncome() {
    return freqIncome;
  }

  public void setFreqIncome(String freqIncome) {
    this.freqIncome = freqIncome;
  }


  public Long getIncome() {
    return income;
  }

  public void setIncome(Long income) {
    this.income = income;
  }


  public Long getSavingPercent() {
    return savingPercent;
  }

  public void setSavingPercent(Long savingPercent) {
    this.savingPercent = savingPercent;
  }


  public Long getTotalAssets() {
    return totalAssets;
  }

  public void setTotalAssets(Long totalAssets) {
    this.totalAssets = totalAssets;
  }


  public Long getInvestAssets() {
    return investAssets;
  }

  public void setInvestAssets(Long investAssets) {
    this.investAssets = investAssets;
  }


  @Step
  public void removeAllPersonInfor() {
    Tcwealth.tcWealthDbConnection.getSession().clear();
    Tcwealth.tcWealthDbConnection.getSession().beginTransaction();
    Query query = Tcwealth.tcWealthDbConnection.getSession().createQuery(
      "delete from WealthPersonInfor ");
    int numberRowDeleted = query.executeUpdate();
    Tcwealth.tcWealthDbConnection.getSession().getTransaction().commit();
    System.out.println("so data bi xoa: " + numberRowDeleted);
  }

  @Step
  public WealthPersonInfor getData() {
    Tcwealth.tcWealthDbConnection.getSession().clear();
    Query<WealthPersonInfor> query = Tcwealth.tcWealthDbConnection.getSession().createQuery(
      "from WealthPersonInfor Where 1=1", WealthPersonInfor.class);
    return query.getSingleResult();
  }


}
