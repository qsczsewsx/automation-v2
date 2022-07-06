package com.tcbs.automation.hfcdata;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "SMY_DWH_CAS_ALLUSERVIEW")
public class SmyDwhCasAlluserviewEntity {
  @Id
  private Long userid;
  private String tcbsid;
  private String idnumber;
  private String idplace;
  private Timestamp iddate;
  private String fullname;
  private String communicationName;
  private String globalname;
  private String firstname;
  private String lastname;
  private String email;
  private String communicationEmail;
  private String phone;
  private String communicationPhone;
  private String gender;
  private String birthday;
  private Long birthdate;
  private Long birthmonth;
  private Long birthyear;
  private Long relationship;
  private String address;
  private String permanentAddress;
  private String wardaddress;
  private String districtaddress;
  private String city;
  private String province;
  private Long custype;
  private Long sysUserType;
  private String bankname;
  private String bankaccountnumber;
  private String bankcode;
  private String bankaccountname;
  private String bankbranch;
  private String bankprovince;
  private Timestamp createdDate;
  private String username;
  private Long flexVsd;
  private Long fundVsd;
  private Timestamp stockactivatedDate;
  private Timestamp fundactivatedDate;
  private Long approvedFile;
  private String custodycd;
  private String custodycdFund;
  private String status;
  private String referenceid;
  private Long openingaccountId;
  private Long refereeId;
  private String openingaccountTcbsid;
  private String openingaccountEmail;
  private String openingaccountUsername;
  private String openingaccountName;
  private String refereeName;
  private String refereeCustodycd;
  private String refereeEmail;
  private String uploadedstatus;
  private Long uploaderId;
  private String uploaderUsername;
  private String uploaderName;
  private String uploaderTcbsid;
  private String uploaderEmail;
  private String campaignCode;
  private String booktype;
  private Long bookstatus;
  private Time bookReceiveddate;
  private String bookNote;
  private Long processstatus;
  private String mst;
  private Time iaOpenedDate;
  private Long etlcurdate;
  private Timestamp etlrundatetime;
  private String nationality;
  private String openAccountChannel;
  private String businessType;


  @Step("Insert data")
  public static void insertData(SmyDwhCasAlluserviewEntity entity) {
    Session session = HfcData.hfcDataDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("INSERT INTO SMY_DWH_CAS_ALLUSERVIEW");
    queryStringBuilder.append("(USERID,TCBSID,CUSTODYCD,IDNUMBER, FULLNAME,BIRTHDAY,CUSTYPE,ETLCURDATE) ");
    queryStringBuilder.append((" VALUES( "));
    queryStringBuilder.append("'" + entity.getUserid() + "'" + ", ");
    queryStringBuilder.append("'" + entity.getTcbsid() + "'" + ", ");
    queryStringBuilder.append("'" + entity.getCustodycd() + "'" + ", ");
    queryStringBuilder.append("'" + entity.getIdnumber() + "'" + ", ");
    queryStringBuilder.append("'" + entity.getFullname() + "'" + ", ");
    queryStringBuilder.append("TO_DATE('" + entity.getBirthday() + "', " + "'YY-MM-DD')" + ", ");
    queryStringBuilder.append("'" + entity.getCustype() + "'" + ", ");
    queryStringBuilder.append("(select MAX(ETLCURDATE) FROM SMY_DWH_CAS_ALLUSERVIEW)) ");

    org.hibernate.query.Query<?> query = session.createNativeQuery(queryStringBuilder.toString());

    query.executeUpdate();
    trans.commit();
  }

  @Step("Delete data by key")
  public static void deleteData(com.tcbs.automation.hfcdata.SmyDwhCasAlluserviewEntity entity) {
    Session session = HfcData.hfcDataDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery("DELETE FROM SMY_DWH_CAS_ALLUSERVIEW WHERE TCBSID = :tcbsid");
    query.setParameter("tcbsid", entity.getTcbsid());
    query.executeUpdate();
    trans.commit();

  }

  @Basic
  @Column(name = "USERID")
  public Long getUserid() {
    return userid;
  }

  public void setUserid(Long userid) {
    this.userid = userid;
  }

  @Basic
  @Column(name = "TCBSID")
  public String getTcbsid() {
    return tcbsid;
  }

  public void setTcbsid(String tcbsid) {
    this.tcbsid = tcbsid;
  }

  @Basic
  @Column(name = "IDNUMBER")
  public String getIdnumber() {
    return idnumber;
  }

  public void setIdnumber(String idnumber) {
    this.idnumber = idnumber;
  }

  @Basic
  @Column(name = "IDPLACE")
  public String getIdplace() {
    return idplace;
  }

  public void setIdplace(String idplace) {
    this.idplace = idplace;
  }

  @Basic
  @Column(name = "IDDATE")
  public Timestamp getIddate() {
    return iddate;
  }

  public void setIddate(Timestamp iddate) {
    this.iddate = iddate;
  }

  @Basic
  @Column(name = "FULLNAME")
  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  @Basic
  @Column(name = "COMMUNICATION_NAME")
  public String getCommunicationName() {
    return communicationName;
  }

  public void setCommunicationName(String communicationName) {
    this.communicationName = communicationName;
  }

  @Basic
  @Column(name = "GLOBALNAME")
  public String getGlobalname() {
    return globalname;
  }

  public void setGlobalname(String globalname) {
    this.globalname = globalname;
  }

  @Basic
  @Column(name = "FIRSTNAME")
  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  @Basic
  @Column(name = "LASTNAME")
  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
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
  @Column(name = "COMMUNICATION_EMAIL")
  public String getCommunicationEmail() {
    return communicationEmail;
  }

  public void setCommunicationEmail(String communicationEmail) {
    this.communicationEmail = communicationEmail;
  }

  @Basic
  @Column(name = "PHONE")
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Basic
  @Column(name = "COMMUNICATION_PHONE")
  public String getCommunicationPhone() {
    return communicationPhone;
  }

  public void setCommunicationPhone(String communicationPhone) {
    this.communicationPhone = communicationPhone;
  }

  @Basic
  @Column(name = "GENDER")
  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  @Basic
  @Column(name = "BIRTHDAY")
  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  @Basic
  @Column(name = "BIRTHDATE")
  public Long getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(Long birthdate) {
    this.birthdate = birthdate;
  }

  @Basic
  @Column(name = "BIRTHMONTH")
  public Long getBirthmonth() {
    return birthmonth;
  }

  public void setBirthmonth(Long birthmonth) {
    this.birthmonth = birthmonth;
  }

  @Basic
  @Column(name = "BIRTHYEAR")
  public Long getBirthyear() {
    return birthyear;
  }

  public void setBirthyear(Long birthyear) {
    this.birthyear = birthyear;
  }

  @Basic
  @Column(name = "RELATIONSHIP")
  public Long getRelationship() {
    return relationship;
  }

  public void setRelationship(Long relationship) {
    this.relationship = relationship;
  }

  @Basic
  @Column(name = "ADDRESS")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Basic
  @Column(name = "PERMANENT_ADDRESS")
  public String getPermanentAddress() {
    return permanentAddress;
  }

  public void setPermanentAddress(String permanentAddress) {
    this.permanentAddress = permanentAddress;
  }

  @Basic
  @Column(name = "WARDADDRESS")
  public String getWardaddress() {
    return wardaddress;
  }

  public void setWardaddress(String wardaddress) {
    this.wardaddress = wardaddress;
  }

  @Basic
  @Column(name = "DISTRICTADDRESS")
  public String getDistrictaddress() {
    return districtaddress;
  }

  public void setDistrictaddress(String districtaddress) {
    this.districtaddress = districtaddress;
  }

  @Basic
  @Column(name = "CITY")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Basic
  @Column(name = "PROVINCE")
  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  @Basic
  @Column(name = "CUSTYPE")
  public Long getCustype() {
    return custype;
  }

  public void setCustype(Long custype) {
    this.custype = custype;
  }

  @Basic
  @Column(name = "SYS_USER_TYPE")
  public Long getSysUserType() {
    return sysUserType;
  }

  public void setSysUserType(Long sysUserType) {
    this.sysUserType = sysUserType;
  }

  @Basic
  @Column(name = "BANKNAME")
  public String getBankname() {
    return bankname;
  }

  public void setBankname(String bankname) {
    this.bankname = bankname;
  }

  @Basic
  @Column(name = "BANKACCOUNTNUMBER")
  public String getBankaccountnumber() {
    return bankaccountnumber;
  }

  public void setBankaccountnumber(String bankaccountnumber) {
    this.bankaccountnumber = bankaccountnumber;
  }

  @Basic
  @Column(name = "BANKCODE")
  public String getBankcode() {
    return bankcode;
  }

  public void setBankcode(String bankcode) {
    this.bankcode = bankcode;
  }

  @Basic
  @Column(name = "BANKACCOUNTNAME")
  public String getBankaccountname() {
    return bankaccountname;
  }

  public void setBankaccountname(String bankaccountname) {
    this.bankaccountname = bankaccountname;
  }

  @Basic
  @Column(name = "BANKBRANCH")
  public String getBankbranch() {
    return bankbranch;
  }

  public void setBankbranch(String bankbranch) {
    this.bankbranch = bankbranch;
  }

  @Basic
  @Column(name = "BANKPROVINCE")
  public String getBankprovince() {
    return bankprovince;
  }

  public void setBankprovince(String bankprovince) {
    this.bankprovince = bankprovince;
  }

  @Basic
  @Column(name = "CREATED_DATE")
  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Timestamp createdDate) {
    this.createdDate = createdDate;
  }

  @Basic
  @Column(name = "USERNAME")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Basic
  @Column(name = "FLEX_VSD")
  public Long getFlexVsd() {
    return flexVsd;
  }

  public void setFlexVsd(Long flexVsd) {
    this.flexVsd = flexVsd;
  }

  @Basic
  @Column(name = "FUND_VSD")
  public Long getFundVsd() {
    return fundVsd;
  }

  public void setFundVsd(Long fundVsd) {
    this.fundVsd = fundVsd;
  }

  @Basic
  @Column(name = "STOCKACTIVATED_DATE")
  public Timestamp getStockactivatedDate() {
    return stockactivatedDate;
  }

  public void setStockactivatedDate(Timestamp stockactivatedDate) {
    this.stockactivatedDate = stockactivatedDate;
  }

  @Basic
  @Column(name = "FUNDACTIVATED_DATE")
  public Timestamp getFundactivatedDate() {
    return fundactivatedDate;
  }

  public void setFundactivatedDate(Timestamp fundactivatedDate) {
    this.fundactivatedDate = fundactivatedDate;
  }

  @Basic
  @Column(name = "APPROVED_FILE")
  public Long getApprovedFile() {
    return approvedFile;
  }

  public void setApprovedFile(Long approvedFile) {
    this.approvedFile = approvedFile;
  }

  @Basic
  @Column(name = "CUSTODYCD")
  public String getCustodycd() {
    return custodycd;
  }

  public void setCustodycd(String custodycd) {
    this.custodycd = custodycd;
  }

  @Basic
  @Column(name = "CUSTODYCD_FUND")
  public String getCustodycdFund() {
    return custodycdFund;
  }

  public void setCustodycdFund(String custodycdFund) {
    this.custodycdFund = custodycdFund;
  }

  @Basic
  @Column(name = "STATUS")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Basic
  @Column(name = "REFERENCEID")
  public String getReferenceid() {
    return referenceid;
  }

  public void setReferenceid(String referenceid) {
    this.referenceid = referenceid;
  }

  @Basic
  @Column(name = "OPENINGACCOUNT_ID")
  public Long getOpeningaccountId() {
    return openingaccountId;
  }

  public void setOpeningaccountId(Long openingaccountId) {
    this.openingaccountId = openingaccountId;
  }

  @Basic
  @Column(name = "REFEREE_ID")
  public Long getRefereeId() {
    return refereeId;
  }

  public void setRefereeId(Long refereeId) {
    this.refereeId = refereeId;
  }

  @Basic
  @Column(name = "OPENINGACCOUNT_TCBSID")
  public String getOpeningaccountTcbsid() {
    return openingaccountTcbsid;
  }

  public void setOpeningaccountTcbsid(String openingaccountTcbsid) {
    this.openingaccountTcbsid = openingaccountTcbsid;
  }

  @Basic
  @Column(name = "OPENINGACCOUNT_EMAIL")
  public String getOpeningaccountEmail() {
    return openingaccountEmail;
  }

  public void setOpeningaccountEmail(String openingaccountEmail) {
    this.openingaccountEmail = openingaccountEmail;
  }

  @Basic
  @Column(name = "OPENINGACCOUNT_USERNAME")
  public String getOpeningaccountUsername() {
    return openingaccountUsername;
  }

  public void setOpeningaccountUsername(String openingaccountUsername) {
    this.openingaccountUsername = openingaccountUsername;
  }

  @Basic
  @Column(name = "OPENINGACCOUNT_NAME")
  public String getOpeningaccountName() {
    return openingaccountName;
  }

  public void setOpeningaccountName(String openingaccountName) {
    this.openingaccountName = openingaccountName;
  }

  @Basic
  @Column(name = "REFEREE_NAME")
  public String getRefereeName() {
    return refereeName;
  }

  public void setRefereeName(String refereeName) {
    this.refereeName = refereeName;
  }

  @Basic
  @Column(name = "REFEREE_CUSTODYCD")
  public String getRefereeCustodycd() {
    return refereeCustodycd;
  }

  public void setRefereeCustodycd(String refereeCustodycd) {
    this.refereeCustodycd = refereeCustodycd;
  }

  @Basic
  @Column(name = "REFEREE_EMAIL")
  public String getRefereeEmail() {
    return refereeEmail;
  }

  public void setRefereeEmail(String refereeEmail) {
    this.refereeEmail = refereeEmail;
  }

  @Basic
  @Column(name = "UPLOADEDSTATUS")
  public String getUploadedstatus() {
    return uploadedstatus;
  }

  public void setUploadedstatus(String uploadedstatus) {
    this.uploadedstatus = uploadedstatus;
  }

  @Basic
  @Column(name = "UPLOADER_ID")
  public Long getUploaderId() {
    return uploaderId;
  }

  public void setUploaderId(Long uploaderId) {
    this.uploaderId = uploaderId;
  }

  @Basic
  @Column(name = "UPLOADER_USERNAME")
  public String getUploaderUsername() {
    return uploaderUsername;
  }

  public void setUploaderUsername(String uploaderUsername) {
    this.uploaderUsername = uploaderUsername;
  }

  @Basic
  @Column(name = "UPLOADER_NAME")
  public String getUploaderName() {
    return uploaderName;
  }

  public void setUploaderName(String uploaderName) {
    this.uploaderName = uploaderName;
  }

  @Basic
  @Column(name = "UPLOADER_TCBSID")
  public String getUploaderTcbsid() {
    return uploaderTcbsid;
  }

  public void setUploaderTcbsid(String uploaderTcbsid) {
    this.uploaderTcbsid = uploaderTcbsid;
  }

  @Basic
  @Column(name = "UPLOADER_EMAIL")
  public String getUploaderEmail() {
    return uploaderEmail;
  }

  public void setUploaderEmail(String uploaderEmail) {
    this.uploaderEmail = uploaderEmail;
  }

  @Basic
  @Column(name = "CAMPAIGN_CODE")
  public String getCampaignCode() {
    return campaignCode;
  }

  public void setCampaignCode(String campaignCode) {
    this.campaignCode = campaignCode;
  }

  @Basic
  @Column(name = "BOOKTYPE")
  public String getBooktype() {
    return booktype;
  }

  public void setBooktype(String booktype) {
    this.booktype = booktype;
  }

  @Basic
  @Column(name = "BOOKSTATUS")
  public Long getBookstatus() {
    return bookstatus;
  }

  public void setBookstatus(Long bookstatus) {
    this.bookstatus = bookstatus;
  }

  @Basic
  @Column(name = "BOOK_RECEIVEDDATE")
  public Time getBookReceiveddate() {
    return bookReceiveddate;
  }

  public void setBookReceiveddate(Time bookReceiveddate) {
    this.bookReceiveddate = bookReceiveddate;
  }

  @Basic
  @Column(name = "BOOK_NOTE")
  public String getBookNote() {
    return bookNote;
  }

  public void setBookNote(String bookNote) {
    this.bookNote = bookNote;
  }

  @Basic
  @Column(name = "PROCESSSTATUS")
  public Long getProcessstatus() {
    return processstatus;
  }

  public void setProcessstatus(Long processstatus) {
    this.processstatus = processstatus;
  }

  @Basic
  @Column(name = "MST")
  public String getMst() {
    return mst;
  }

  public void setMst(String mst) {
    this.mst = mst;
  }

  @Basic
  @Column(name = "IA_OPENED_DATE")
  public Time getIaOpenedDate() {
    return iaOpenedDate;
  }

  public void setIaOpenedDate(Time iaOpenedDate) {
    this.iaOpenedDate = iaOpenedDate;
  }

  @Basic
  @Column(name = "ETLCURDATE")
  public Long getEtlcurdate() {
    return etlcurdate;
  }

  public void setEtlcurdate(Long etlcurdate) {
    this.etlcurdate = etlcurdate;
  }

  @Basic
  @Column(name = "ETLRUNDATETIME")
  public Timestamp getEtlrundatetime() {
    return etlrundatetime;
  }

  public void setEtlrundatetime(Timestamp etlrundatetime) {
    this.etlrundatetime = etlrundatetime;
  }

  @Basic
  @Column(name = "NATIONALITY")
  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  @Basic
  @Column(name = "OPEN_ACCOUNT_CHANNEL")
  public String getOpenAccountChannel() {
    return openAccountChannel;
  }

  public void setOpenAccountChannel(String openAccountChannel) {
    this.openAccountChannel = openAccountChannel;
  }

  @Basic
  @Column(name = "BUSINESS_TYPE")
  public String getBusinessType() {
    return businessType;
  }

  public void setBusinessType(String businessType) {
    this.businessType = businessType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmyDwhCasAlluserviewEntity that = (SmyDwhCasAlluserviewEntity) o;
    return Objects.equals(userid, that.userid) &&
      Objects.equals(tcbsid, that.tcbsid) &&
      Objects.equals(idnumber, that.idnumber) &&
      Objects.equals(idplace, that.idplace) &&
      Objects.equals(iddate, that.iddate) &&
      Objects.equals(fullname, that.fullname) &&
      Objects.equals(communicationName, that.communicationName) &&
      Objects.equals(globalname, that.globalname) &&
      Objects.equals(firstname, that.firstname) &&
      Objects.equals(lastname, that.lastname) &&
      Objects.equals(email, that.email) &&
      Objects.equals(communicationEmail, that.communicationEmail) &&
      Objects.equals(phone, that.phone) &&
      Objects.equals(communicationPhone, that.communicationPhone) &&
      Objects.equals(gender, that.gender) &&
      Objects.equals(birthday, that.birthday) &&
      Objects.equals(birthdate, that.birthdate) &&
      Objects.equals(birthmonth, that.birthmonth) &&
      Objects.equals(birthyear, that.birthyear) &&
      Objects.equals(relationship, that.relationship) &&
      Objects.equals(address, that.address) &&
      Objects.equals(permanentAddress, that.permanentAddress) &&
      Objects.equals(wardaddress, that.wardaddress) &&
      Objects.equals(districtaddress, that.districtaddress) &&
      Objects.equals(city, that.city) &&
      Objects.equals(province, that.province) &&
      Objects.equals(custype, that.custype) &&
      Objects.equals(sysUserType, that.sysUserType) &&
      Objects.equals(bankname, that.bankname) &&
      Objects.equals(bankaccountnumber, that.bankaccountnumber) &&
      Objects.equals(bankcode, that.bankcode) &&
      Objects.equals(bankaccountname, that.bankaccountname) &&
      Objects.equals(bankbranch, that.bankbranch) &&
      Objects.equals(bankprovince, that.bankprovince) &&
      Objects.equals(createdDate, that.createdDate) &&
      Objects.equals(username, that.username) &&
      Objects.equals(flexVsd, that.flexVsd) &&
      Objects.equals(fundVsd, that.fundVsd) &&
      Objects.equals(stockactivatedDate, that.stockactivatedDate) &&
      Objects.equals(fundactivatedDate, that.fundactivatedDate) &&
      Objects.equals(approvedFile, that.approvedFile) &&
      Objects.equals(custodycd, that.custodycd) &&
      Objects.equals(custodycdFund, that.custodycdFund) &&
      Objects.equals(status, that.status) &&
      Objects.equals(referenceid, that.referenceid) &&
      Objects.equals(openingaccountId, that.openingaccountId) &&
      Objects.equals(refereeId, that.refereeId) &&
      Objects.equals(openingaccountTcbsid, that.openingaccountTcbsid) &&
      Objects.equals(openingaccountEmail, that.openingaccountEmail) &&
      Objects.equals(openingaccountUsername, that.openingaccountUsername) &&
      Objects.equals(openingaccountName, that.openingaccountName) &&
      Objects.equals(refereeName, that.refereeName) &&
      Objects.equals(refereeCustodycd, that.refereeCustodycd) &&
      Objects.equals(refereeEmail, that.refereeEmail) &&
      Objects.equals(uploadedstatus, that.uploadedstatus) &&
      Objects.equals(uploaderId, that.uploaderId) &&
      Objects.equals(uploaderUsername, that.uploaderUsername) &&
      Objects.equals(uploaderName, that.uploaderName) &&
      Objects.equals(uploaderTcbsid, that.uploaderTcbsid) &&
      Objects.equals(uploaderEmail, that.uploaderEmail) &&
      Objects.equals(campaignCode, that.campaignCode) &&
      Objects.equals(booktype, that.booktype) &&
      Objects.equals(bookstatus, that.bookstatus) &&
      Objects.equals(bookReceiveddate, that.bookReceiveddate) &&
      Objects.equals(bookNote, that.bookNote) &&
      Objects.equals(processstatus, that.processstatus) &&
      Objects.equals(mst, that.mst) &&
      Objects.equals(iaOpenedDate, that.iaOpenedDate) &&
      Objects.equals(etlcurdate, that.etlcurdate) &&
      Objects.equals(etlrundatetime, that.etlrundatetime) &&
      Objects.equals(nationality, that.nationality) &&
      Objects.equals(openAccountChannel, that.openAccountChannel) &&
      Objects.equals(businessType, that.businessType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userid, tcbsid, idnumber, idplace, iddate, fullname, communicationName, globalname, firstname, lastname, email, communicationEmail, phone, communicationPhone, gender, birthday,
      birthdate, birthmonth, birthyear, relationship, address, permanentAddress, wardaddress, districtaddress, city, province, custype, sysUserType, bankname, bankaccountnumber, bankcode,
      bankaccountname, bankbranch, bankprovince, createdDate, username, flexVsd, fundVsd, stockactivatedDate, fundactivatedDate, approvedFile, custodycd, custodycdFund, status, referenceid,
      openingaccountId, refereeId, openingaccountTcbsid, openingaccountEmail, openingaccountUsername, openingaccountName, refereeName, refereeCustodycd, refereeEmail, uploadedstatus, uploaderId,
      uploaderUsername, uploaderName, uploaderTcbsid, uploaderEmail, campaignCode, booktype, bookstatus, bookReceiveddate, bookNote, processstatus, mst, iaOpenedDate, etlcurdate, etlrundatetime,
      nationality, openAccountChannel, businessType);
  }
}
