package com.tcbs.automation.tcbsdwh.ani.isail;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "prc_account_matomo", schema = "dwh")
public class PrcAccountMatomoEntity {
  public static final HibernateEdition stagingDb = Database.TCBS_DWH_STAGING.getConnection();
  private String customername;
  private String tcbsid;
  private String lastCountryVisit;
  private String lastBrowser;
  private String lastDevice;
  private String lastDeviceBranch;
  private String lastScreenResolution;
  private String lastTcinvestVersion;
  private String lastOsVersion;
  private String lastChannel;
  private String lastMobileAppVersion;
  private String lastPlatform;
  private String lastRefererType;
  private Date lastVisitDate;
  private Long daysSinceLastVisit;
  private Date firstVisitDate;
  private Long daysSinceFirstVisit;
  private Long totalVisit;
  private Long totalPageview;
  private Long totalTimeSpent;
  private Long avgActionPerVisit;
  private Long avgVisitDuration;
  private Long noVisitsFromDesktop;
  private Long noVisitsFromMobile;
  private String avgLoadingTime;
  private Long avgActionTimeSpend;
  private String mostActionPage;
  private String noMostActionPage;
  private String secondActionPage;
  private String mostExitPage;
  private String noMostExitPage;
  private String secondExitPage;
  private String usingTcinvestApp;
  private String usingTcbIb;
  private String timezone;
  private String etlcurdate;
  private Timestamp etlrundatetime;
  private String matFmbLastlogindate;

  @Basic
  @Column(name = "customername")
  public String getCustomername() {
    return customername;
  }

  public void setCustomername(String customername) {
    this.customername = customername;
  }

  @Basic
  @Id
  @Column(name = "tcbsid")
  public String getTcbsid() {
    return tcbsid;
  }

  public void setTcbsid(String tcbsid) {
    this.tcbsid = tcbsid;
  }

  @Basic
  @Column(name = "last_country_visit")
  public String getLastCountryVisit() {
    return lastCountryVisit;
  }

  public void setLastCountryVisit(String lastCountryVisit) {
    this.lastCountryVisit = lastCountryVisit;
  }

  @Basic
  @Column(name = "last_browser")
  public String getLastBrowser() {
    return lastBrowser;
  }

  public void setLastBrowser(String lastBrowser) {
    this.lastBrowser = lastBrowser;
  }

  @Basic
  @Column(name = "last_device")
  public String getLastDevice() {
    return lastDevice;
  }

  public void setLastDevice(String lastDevice) {
    this.lastDevice = lastDevice;
  }

  @Basic
  @Column(name = "last_device_branch")
  public String getLastDeviceBranch() {
    return lastDeviceBranch;
  }

  public void setLastDeviceBranch(String lastDeviceBranch) {
    this.lastDeviceBranch = lastDeviceBranch;
  }

  @Basic
  @Column(name = "last_screen_resolution")
  public String getLastScreenResolution() {
    return lastScreenResolution;
  }

  public void setLastScreenResolution(String lastScreenResolution) {
    this.lastScreenResolution = lastScreenResolution;
  }

  @Basic
  @Column(name = "last_tcinvest_version")
  public String getLastTcinvestVersion() {
    return lastTcinvestVersion;
  }

  public void setLastTcinvestVersion(String lastTcinvestVersion) {
    this.lastTcinvestVersion = lastTcinvestVersion;
  }

  @Basic
  @Column(name = "last_os_version")
  public String getLastOsVersion() {
    return lastOsVersion;
  }

  public void setLastOsVersion(String lastOsVersion) {
    this.lastOsVersion = lastOsVersion;
  }

  @Basic
  @Column(name = "last_channel")
  public String getLastChannel() {
    return lastChannel;
  }

  public void setLastChannel(String lastChannel) {
    this.lastChannel = lastChannel;
  }

  @Basic
  @Column(name = "last_mobile_app_version")
  public String getLastMobileAppVersion() {
    return lastMobileAppVersion;
  }

  public void setLastMobileAppVersion(String lastMobileAppVersion) {
    this.lastMobileAppVersion = lastMobileAppVersion;
  }

  @Basic
  @Column(name = "last_platform")
  public String getLastPlatform() {
    return lastPlatform;
  }

  public void setLastPlatform(String lastPlatform) {
    this.lastPlatform = lastPlatform;
  }

  @Basic
  @Column(name = "last_referer_type")
  public String getLastRefererType() {
    return lastRefererType;
  }

  public void setLastRefererType(String lastRefererType) {
    this.lastRefererType = lastRefererType;
  }

  @Basic
  @Column(name = "last_visit_date")
  public Date getLastVisitDate() {
    return lastVisitDate;
  }

  public void setLastVisitDate(Date lastVisitDate) {
    this.lastVisitDate = lastVisitDate;
  }

  @Basic
  @Column(name = "days_since_last_visit")
  public Long getDaysSinceLastVisit() {
    return daysSinceLastVisit;
  }

  public void setDaysSinceLastVisit(Long daysSinceLastVisit) {
    this.daysSinceLastVisit = daysSinceLastVisit;
  }

  @Basic
  @Column(name = "first_visit_date")
  public Date getFirstVisitDate() {
    return firstVisitDate;
  }

  public void setFirstVisitDate(Date firstVisitDate) {
    this.firstVisitDate = firstVisitDate;
  }

  @Basic
  @Column(name = "days_since_first_visit")
  public Long getDaysSinceFirstVisit() {
    return daysSinceFirstVisit;
  }

  public void setDaysSinceFirstVisit(Long daysSinceFirstVisit) {
    this.daysSinceFirstVisit = daysSinceFirstVisit;
  }

  @Basic
  @Column(name = "total_visit")
  public Long getTotalVisit() {
    return totalVisit;
  }

  public void setTotalVisit(Long totalVisit) {
    this.totalVisit = totalVisit;
  }

  @Basic
  @Column(name = "total_pageview")
  public Long getTotalPageview() {
    return totalPageview;
  }

  public void setTotalPageview(Long totalPageview) {
    this.totalPageview = totalPageview;
  }

  @Basic
  @Column(name = "total_time_spent")
  public Long getTotalTimeSpent() {
    return totalTimeSpent;
  }

  public void setTotalTimeSpent(Long totalTimeSpent) {
    this.totalTimeSpent = totalTimeSpent;
  }

  @Basic
  @Column(name = "avg_action_per_visit")
  public Long getAvgActionPerVisit() {
    return avgActionPerVisit;
  }

  public void setAvgActionPerVisit(Long avgActionPerVisit) {
    this.avgActionPerVisit = avgActionPerVisit;
  }

  @Basic
  @Column(name = "avg_visit_duration")
  public Long getAvgVisitDuration() {
    return avgVisitDuration;
  }

  public void setAvgVisitDuration(Long avgVisitDuration) {
    this.avgVisitDuration = avgVisitDuration;
  }

  @Basic
  @Column(name = "no_visits_from_desktop")
  public Long getNoVisitsFromDesktop() {
    return noVisitsFromDesktop;
  }

  public void setNoVisitsFromDesktop(Long noVisitsFromDesktop) {
    this.noVisitsFromDesktop = noVisitsFromDesktop;
  }

  @Basic
  @Column(name = "no_visits_from_mobile")
  public Long getNoVisitsFromMobile() {
    return noVisitsFromMobile;
  }

  public void setNoVisitsFromMobile(Long noVisitsFromMobile) {
    this.noVisitsFromMobile = noVisitsFromMobile;
  }

  @Basic
  @Column(name = "avg_loading_time")
  public String getAvgLoadingTime() {
    return avgLoadingTime;
  }

  public void setAvgLoadingTime(String avgLoadingTime) {
    this.avgLoadingTime = avgLoadingTime;
  }

  @Basic
  @Column(name = "avg_action_time_spend")
  public Long getAvgActionTimeSpend() {
    return avgActionTimeSpend;
  }

  public void setAvgActionTimeSpend(Long avgActionTimeSpend) {
    this.avgActionTimeSpend = avgActionTimeSpend;
  }

  @Basic
  @Column(name = "most_action_page")
  public String getMostActionPage() {
    return mostActionPage;
  }

  public void setMostActionPage(String mostActionPage) {
    this.mostActionPage = mostActionPage;
  }

  @Basic
  @Column(name = "no_most_action_page")
  public String getNoMostActionPage() {
    return noMostActionPage;
  }

  public void setNoMostActionPage(String noMostActionPage) {
    this.noMostActionPage = noMostActionPage;
  }

  @Basic
  @Column(name = "second_action_page")
  public String getSecondActionPage() {
    return secondActionPage;
  }

  public void setSecondActionPage(String secondActionPage) {
    this.secondActionPage = secondActionPage;
  }

  @Basic
  @Column(name = "most_exit_page")
  public String getMostExitPage() {
    return mostExitPage;
  }

  public void setMostExitPage(String mostExitPage) {
    this.mostExitPage = mostExitPage;
  }

  @Basic
  @Column(name = "no_most_exit_page")
  public String getNoMostExitPage() {
    return noMostExitPage;
  }

  public void setNoMostExitPage(String noMostExitPage) {
    this.noMostExitPage = noMostExitPage;
  }

  @Basic
  @Column(name = "second_exit_page")
  public String getSecondExitPage() {
    return secondExitPage;
  }

  public void setSecondExitPage(String secondExitPage) {
    this.secondExitPage = secondExitPage;
  }

  @Basic
  @Column(name = "using_tcinvest_app")
  public String getUsingTcinvestApp() {
    return usingTcinvestApp;
  }

  public void setUsingTcinvestApp(String usingTcinvestApp) {
    this.usingTcinvestApp = usingTcinvestApp;
  }

  @Basic
  @Column(name = "using_tcb_ib")
  public String getUsingTcbIb() {
    return usingTcbIb;
  }

  public void setUsingTcbIb(String usingTcbIb) {
    this.usingTcbIb = usingTcbIb;
  }

  @Basic
  @Column(name = "timezone")
  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  @Basic
  @Column(name = "etlcurdate")
  public String getEtlcurdate() {
    return etlcurdate;
  }

  public void setEtlcurdate(String etlcurdate) {
    this.etlcurdate = etlcurdate;
  }

  @Basic
  @Column(name = "etlrundatetime")
  public Timestamp getEtlrundatetime() {
    return etlrundatetime;
  }

  public void setEtlrundatetime(Timestamp etlrundatetime) {
    this.etlrundatetime = etlrundatetime;
  }

  @Basic
  @Column(name = "mat_fmb_lastlogindate")
  public String getMatFmbLastlogindate() {
    return matFmbLastlogindate;
  }

  public void setMatFmbLastlogindate(String matFmbLastlogindate) {
    this.matFmbLastlogindate = matFmbLastlogindate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PrcAccountMatomoEntity that = (PrcAccountMatomoEntity) o;
    return Objects.equals(customername, that.customername) && Objects.equals(tcbsid, that.tcbsid) && Objects.equals(lastCountryVisit,
      that.lastCountryVisit) && Objects.equals(lastBrowser, that.lastBrowser) && Objects.equals(lastDevice, that.lastDevice) && Objects.equals(lastDeviceBranch,
      that.lastDeviceBranch) && Objects.equals(lastScreenResolution, that.lastScreenResolution) && Objects.equals(lastTcinvestVersion,
      that.lastTcinvestVersion) && Objects.equals(lastOsVersion, that.lastOsVersion) && Objects.equals(lastChannel, that.lastChannel) && Objects.equals(
      lastMobileAppVersion, that.lastMobileAppVersion) && Objects.equals(lastPlatform, that.lastPlatform) && Objects.equals(lastRefererType,
      that.lastRefererType) && Objects.equals(lastVisitDate, that.lastVisitDate) && Objects.equals(daysSinceLastVisit, that.daysSinceLastVisit) && Objects.equals(
      firstVisitDate, that.firstVisitDate) && Objects.equals(daysSinceFirstVisit, that.daysSinceFirstVisit) && Objects.equals(totalVisit,
      that.totalVisit) && Objects.equals(totalPageview, that.totalPageview) && Objects.equals(totalTimeSpent, that.totalTimeSpent) && Objects.equals(avgActionPerVisit,
      that.avgActionPerVisit) && Objects.equals(avgVisitDuration, that.avgVisitDuration) && Objects.equals(noVisitsFromDesktop, that.noVisitsFromDesktop) && Objects.equals(
      noVisitsFromMobile, that.noVisitsFromMobile) && Objects.equals(avgLoadingTime, that.avgLoadingTime) && Objects.equals(avgActionTimeSpend,
      that.avgActionTimeSpend) && Objects.equals(mostActionPage, that.mostActionPage) && Objects.equals(noMostActionPage, that.noMostActionPage) && Objects.equals(
      secondActionPage, that.secondActionPage) && Objects.equals(mostExitPage, that.mostExitPage) && Objects.equals(noMostExitPage, that.noMostExitPage) && Objects.equals(
      secondExitPage, that.secondExitPage) && Objects.equals(usingTcinvestApp, that.usingTcinvestApp) && Objects.equals(usingTcbIb, that.usingTcbIb) && Objects.equals(
      timezone, that.timezone) && Objects.equals(etlcurdate, that.etlcurdate) && Objects.equals(etlrundatetime, that.etlrundatetime) && Objects.equals(matFmbLastlogindate,
      that.matFmbLastlogindate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customername, tcbsid, lastCountryVisit, lastBrowser, lastDevice, lastDeviceBranch, lastScreenResolution, lastTcinvestVersion, lastOsVersion, lastChannel, lastMobileAppVersion,
      lastPlatform, lastRefererType, lastVisitDate, daysSinceLastVisit, firstVisitDate, daysSinceFirstVisit, totalVisit, totalPageview, totalTimeSpent, avgActionPerVisit, avgVisitDuration,
      noVisitsFromDesktop, noVisitsFromMobile, avgLoadingTime, avgActionTimeSpend, mostActionPage, noMostActionPage, secondActionPage, mostExitPage, noMostExitPage, secondExitPage, usingTcinvestApp,
      usingTcbIb, timezone, etlcurdate, etlrundatetime, matFmbLastlogindate);
  }

  @Step("insert data")
  public void saveEntity(PrcAccountMatomoEntity entity) {
    Session session = stagingDb.getSession();
    session.beginTransaction();
    session.save(entity);
    session.getTransaction().commit();
    stagingDb.closeSession();

  }

  @Step("delete data by key")
  public void deleteByTcbsId(PrcAccountMatomoEntity entity) {
    Session session = stagingDb.getSession();
    session.clear();
    beginTransaction(session);
    Query<?> query = session.createNativeQuery("DELETE dwh.prc_account_matomo WHERE tcbsid = :tcbsid ");
    query.setParameter("tcbsid", entity.getTcbsid());
    query.executeUpdate();
    session.getTransaction().commit();
    stagingDb.closeSession();
  }
}