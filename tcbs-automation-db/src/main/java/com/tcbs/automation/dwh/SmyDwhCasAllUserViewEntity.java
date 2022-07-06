package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "Smy_dwh_cas_AllUserView")
public class SmyDwhCasAllUserViewEntity {
  private Integer userId;
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
  private Timestamp birthday;
  private Integer birthdate;
  private Integer birthmonth;
  private Integer birthyear;
  private Integer relationship;
  private String address;
  private String permanentAddress;
  private String wardAddress;
  private String districtAddress;
  private String city;
  private String province;
  private Integer custype;
  private Integer sysUserType;
  private String bankname;
  private String bankaccountnumber;
  private String bankcode;
  private String bankaccountname;
  private String bankbranch;
  private String bankprovince;
  private Timestamp createdDate;
  private String username;
  private Integer flexVsd;
  private Integer fundVsd;
  private Timestamp stockactivatedDate;
  private Timestamp fundactivatedDate;
  private Integer approvedFile;
  private String custodycd;
  private String custodycdFund;
  private String status;
  private String referenceid;
  private Integer openingaccountId;
  private Integer refereeId;
  private String openingaccountTcbsid;
  private String openingaccountEmail;
  private String openingaccountUsername;
  private String openingaccountName;
  private String refereeName;
  private String refereeCustodycd;
  private String refereeEmail;
  private String uploadedstatus;
  private Integer uploaderId;
  private String uploaderUsername;
  private String uploaderName;
  private String uploaderTcbsid;
  private String uploaderEmail;
  private String campaignCode;
  private String bookType;
  private Integer bookStatus;
  private Date bookReceivedDate;
  private String bookNote;
  private Integer processStatus;
  private String mst;
  private Date iaOpenedDate;
  private Integer etlCurDate;
  private Timestamp etlRunDatetime;
  private String nationality;
  private String openAccountChannel;

  @Id
  @Column(name = "UserId")
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
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
  @Column(name = "idnumber")
  public String getIdnumber() {
    return idnumber;
  }

  public void setIdnumber(String idnumber) {
    this.idnumber = idnumber;
  }

  @Basic
  @Column(name = "idplace")
  public String getIdplace() {
    return idplace;
  }

  public void setIdplace(String idplace) {
    this.idplace = idplace;
  }

  @Basic
  @Column(name = "iddate")
  public Timestamp getIddate() {
    return iddate;
  }

  public void setIddate(Timestamp iddate) {
    this.iddate = iddate;
  }

  @Basic
  @Column(name = "fullname")
  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  @Basic
  @Column(name = "communication_name")
  public String getCommunicationName() {
    return communicationName;
  }

  public void setCommunicationName(String communicationName) {
    this.communicationName = communicationName;
  }

  @Basic
  @Column(name = "globalname")
  public String getGlobalname() {
    return globalname;
  }

  public void setGlobalname(String globalname) {
    this.globalname = globalname;
  }

  @Basic
  @Column(name = "firstname")
  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  @Basic
  @Column(name = "lastname")
  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  @Basic
  @Column(name = "email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Basic
  @Column(name = "communication_email")
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
  @Column(name = "communication_phone")
  public String getCommunicationPhone() {
    return communicationPhone;
  }

  public void setCommunicationPhone(String communicationPhone) {
    this.communicationPhone = communicationPhone;
  }

  @Basic
  @Column(name = "gender")
  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  @Basic
  @Column(name = "birthday")
  public Timestamp getBirthday() {
    return birthday;
  }

  public void setBirthday(Timestamp birthday) {
    this.birthday = birthday;
  }

  @Basic
  @Column(name = "birthdate")
  public Integer getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(Integer birthdate) {
    this.birthdate = birthdate;
  }

  @Basic
  @Column(name = "birthmonth")
  public Integer getBirthmonth() {
    return birthmonth;
  }

  public void setBirthmonth(Integer birthmonth) {
    this.birthmonth = birthmonth;
  }

  @Basic
  @Column(name = "birthyear")
  public Integer getBirthyear() {
    return birthyear;
  }

  public void setBirthyear(Integer birthyear) {
    this.birthyear = birthyear;
  }

  @Basic
  @Column(name = "RELATIONSHIP")
  public Integer getRelationship() {
    return relationship;
  }

  public void setRelationship(Integer relationship) {
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
  @Column(name = "wardAddress")
  public String getWardAddress() {
    return wardAddress;
  }

  public void setWardAddress(String wardAddress) {
    this.wardAddress = wardAddress;
  }

  @Basic
  @Column(name = "districtAddress")
  public String getDistrictAddress() {
    return districtAddress;
  }

  public void setDistrictAddress(String districtAddress) {
    this.districtAddress = districtAddress;
  }

  @Basic
  @Column(name = "city")
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
  public Integer getCustype() {
    return custype;
  }

  public void setCustype(Integer custype) {
    this.custype = custype;
  }

  @Basic
  @Column(name = "SYS_USER_TYPE")
  public Integer getSysUserType() {
    return sysUserType;
  }

  public void setSysUserType(Integer sysUserType) {
    this.sysUserType = sysUserType;
  }

  @Basic
  @Column(name = "bankname")
  public String getBankname() {
    return bankname;
  }

  public void setBankname(String bankname) {
    this.bankname = bankname;
  }

  @Basic
  @Column(name = "bankaccountnumber")
  public String getBankaccountnumber() {
    return bankaccountnumber;
  }

  public void setBankaccountnumber(String bankaccountnumber) {
    this.bankaccountnumber = bankaccountnumber;
  }

  @Basic
  @Column(name = "bankcode")
  public String getBankcode() {
    return bankcode;
  }

  public void setBankcode(String bankcode) {
    this.bankcode = bankcode;
  }

  @Basic
  @Column(name = "bankaccountname")
  public String getBankaccountname() {
    return bankaccountname;
  }

  public void setBankaccountname(String bankaccountname) {
    this.bankaccountname = bankaccountname;
  }

  @Basic
  @Column(name = "bankbranch")
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
  @Column(name = "created_date")
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
  @Column(name = "Flex_VSD")
  public Integer getFlexVsd() {
    return flexVsd;
  }

  public void setFlexVsd(Integer flexVsd) {
    this.flexVsd = flexVsd;
  }

  @Basic
  @Column(name = "Fund_VSD")
  public Integer getFundVsd() {
    return fundVsd;
  }

  public void setFundVsd(Integer fundVsd) {
    this.fundVsd = fundVsd;
  }

  @Basic
  @Column(name = "stockactivated_date")
  public Timestamp getStockactivatedDate() {
    return stockactivatedDate;
  }

  public void setStockactivatedDate(Timestamp stockactivatedDate) {
    this.stockactivatedDate = stockactivatedDate;
  }

  @Basic
  @Column(name = "fundactivated_date")
  public Timestamp getFundactivatedDate() {
    return fundactivatedDate;
  }

  public void setFundactivatedDate(Timestamp fundactivatedDate) {
    this.fundactivatedDate = fundactivatedDate;
  }

  @Basic
  @Column(name = "Approved_File")
  public Integer getApprovedFile() {
    return approvedFile;
  }

  public void setApprovedFile(Integer approvedFile) {
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
  @Column(name = "status")
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
  @Column(name = "openingaccount_Id")
  public Integer getOpeningaccountId() {
    return openingaccountId;
  }

  public void setOpeningaccountId(Integer openingaccountId) {
    this.openingaccountId = openingaccountId;
  }

  @Basic
  @Column(name = "referee_Id")
  public Integer getRefereeId() {
    return refereeId;
  }

  public void setRefereeId(Integer refereeId) {
    this.refereeId = refereeId;
  }

  @Basic
  @Column(name = "openingaccount_Tcbsid")
  public String getOpeningaccountTcbsid() {
    return openingaccountTcbsid;
  }

  public void setOpeningaccountTcbsid(String openingaccountTcbsid) {
    this.openingaccountTcbsid = openingaccountTcbsid;
  }

  @Basic
  @Column(name = "openingaccount_Email")
  public String getOpeningaccountEmail() {
    return openingaccountEmail;
  }

  public void setOpeningaccountEmail(String openingaccountEmail) {
    this.openingaccountEmail = openingaccountEmail;
  }

  @Basic
  @Column(name = "openingaccount_Username")
  public String getOpeningaccountUsername() {
    return openingaccountUsername;
  }

  public void setOpeningaccountUsername(String openingaccountUsername) {
    this.openingaccountUsername = openingaccountUsername;
  }

  @Basic
  @Column(name = "openingaccount_Name")
  public String getOpeningaccountName() {
    return openingaccountName;
  }

  public void setOpeningaccountName(String openingaccountName) {
    this.openingaccountName = openingaccountName;
  }

  @Basic
  @Column(name = "referee_Name")
  public String getRefereeName() {
    return refereeName;
  }

  public void setRefereeName(String refereeName) {
    this.refereeName = refereeName;
  }

  @Basic
  @Column(name = "referee_Custodycd")
  public String getRefereeCustodycd() {
    return refereeCustodycd;
  }

  public void setRefereeCustodycd(String refereeCustodycd) {
    this.refereeCustodycd = refereeCustodycd;
  }

  @Basic
  @Column(name = "referee_Email")
  public String getRefereeEmail() {
    return refereeEmail;
  }

  public void setRefereeEmail(String refereeEmail) {
    this.refereeEmail = refereeEmail;
  }

  @Basic
  @Column(name = "uploadedstatus")
  public String getUploadedstatus() {
    return uploadedstatus;
  }

  public void setUploadedstatus(String uploadedstatus) {
    this.uploadedstatus = uploadedstatus;
  }

  @Basic
  @Column(name = "uploader_Id")
  public Integer getUploaderId() {
    return uploaderId;
  }

  public void setUploaderId(Integer uploaderId) {
    this.uploaderId = uploaderId;
  }

  @Basic
  @Column(name = "uploader_Username")
  public String getUploaderUsername() {
    return uploaderUsername;
  }

  public void setUploaderUsername(String uploaderUsername) {
    this.uploaderUsername = uploaderUsername;
  }

  @Basic
  @Column(name = "uploader_Name")
  public String getUploaderName() {
    return uploaderName;
  }

  public void setUploaderName(String uploaderName) {
    this.uploaderName = uploaderName;
  }

  @Basic
  @Column(name = "uploader_Tcbsid")
  public String getUploaderTcbsid() {
    return uploaderTcbsid;
  }

  public void setUploaderTcbsid(String uploaderTcbsid) {
    this.uploaderTcbsid = uploaderTcbsid;
  }

  @Basic
  @Column(name = "uploader_Email")
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
  @Column(name = "bookType")
  public String getBookType() {
    return bookType;
  }

  public void setBookType(String bookType) {
    this.bookType = bookType;
  }

  @Basic
  @Column(name = "bookStatus")
  public Integer getBookStatus() {
    return bookStatus;
  }

  public void setBookStatus(Integer bookStatus) {
    this.bookStatus = bookStatus;
  }

  @Basic
  @Column(name = "book_ReceivedDate")
  public Date getBookReceivedDate() {
    return bookReceivedDate;
  }

  public void setBookReceivedDate(Date bookReceivedDate) {
    this.bookReceivedDate = bookReceivedDate;
  }

  @Basic
  @Column(name = "book_Note")
  public String getBookNote() {
    return bookNote;
  }

  public void setBookNote(String bookNote) {
    this.bookNote = bookNote;
  }

  @Basic
  @Column(name = "processStatus")
  public Integer getProcessStatus() {
    return processStatus;
  }

  public void setProcessStatus(Integer processStatus) {
    this.processStatus = processStatus;
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
  @Column(name = "IA_Opened_Date")
  public Date getIaOpenedDate() {
    return iaOpenedDate;
  }

  public void setIaOpenedDate(Date iaOpenedDate) {
    this.iaOpenedDate = iaOpenedDate;
  }

  @Basic
  @Column(name = "EtlCurDate")
  public Integer getEtlCurDate() {
    return etlCurDate;
  }

  public void setEtlCurDate(Integer etlCurDate) {
    this.etlCurDate = etlCurDate;
  }

  @Basic
  @Column(name = "EtlRunDatetime")
  public Timestamp getEtlRunDatetime() {
    return etlRunDatetime;
  }

  public void setEtlRunDatetime(Timestamp etlRunDatetime) {
    this.etlRunDatetime = etlRunDatetime;
  }

  @Basic
  @Column(name = "nationality")
  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  @Basic
  @Column(name = "open_account_channel")
  public String getOpenAccountChannel() {
    return openAccountChannel;
  }

  public void setOpenAccountChannel(String openAccountChannel) {
    this.openAccountChannel = openAccountChannel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmyDwhCasAllUserViewEntity that = (SmyDwhCasAllUserViewEntity) o;
    return userId == that.userId &&
      approvedFile == that.approvedFile &&
      etlCurDate == that.etlCurDate &&
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
      Objects.equals(wardAddress, that.wardAddress) &&
      Objects.equals(districtAddress, that.districtAddress) &&
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
      Objects.equals(bookType, that.bookType) &&
      Objects.equals(bookStatus, that.bookStatus) &&
      Objects.equals(bookReceivedDate, that.bookReceivedDate) &&
      Objects.equals(bookNote, that.bookNote) &&
      Objects.equals(processStatus, that.processStatus) &&
      Objects.equals(mst, that.mst) &&
      Objects.equals(iaOpenedDate, that.iaOpenedDate) &&
      Objects.equals(etlRunDatetime, that.etlRunDatetime) &&
      Objects.equals(nationality, that.nationality) &&
      Objects.equals(openAccountChannel, that.openAccountChannel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, tcbsid, idnumber, idplace, iddate, fullname, communicationName, globalname, firstname, lastname, email, communicationEmail, phone, communicationPhone, gender, birthday,
      birthdate, birthmonth, birthyear, relationship, address, permanentAddress, wardAddress, districtAddress, city, province, custype, sysUserType, bankname, bankaccountnumber, bankcode,
      bankaccountname, bankbranch, bankprovince, createdDate, username, flexVsd, fundVsd, stockactivatedDate, fundactivatedDate, approvedFile, custodycd, custodycdFund, status, referenceid,
      openingaccountId, refereeId, openingaccountTcbsid, openingaccountEmail, openingaccountUsername, openingaccountName, refereeName, refereeCustodycd, refereeEmail, uploadedstatus, uploaderId,
      uploaderUsername, uploaderName, uploaderTcbsid, uploaderEmail, campaignCode, bookType, bookStatus, bookReceivedDate, bookNote, processStatus, mst, iaOpenedDate, etlCurDate, etlRunDatetime,
      nationality, openAccountChannel);
  }

  @Step("insert data")
  public boolean saveUserInfo(SmyDwhCasAllUserViewEntity userInfo) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.beginTransaction();
    Integer id = (Integer) session.save(userInfo);
    session.getTransaction().commit();
    return true;
  }

  @Step("delete data by key")
  public void deleteByUserId(Integer userId) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<SmyDwhCasAllUserViewEntity> query = session.createQuery(
      "DELETE FROM SmyDwhCasAllUserViewEntity i WHERE i.userId=:userId"
    );
    query.setParameter("userId", userId);
    query.executeUpdate();
    session.getTransaction().commit();
  }


  @Step("Get data by key")
  public String getByTcbsId(String tcbsid) {
    Query<SmyDwhCasAllUserViewEntity> query = Dwh.dwhDbConnection.getSession().createQuery(
      "from SmyDwhCasAllUserViewEntity a where a.tcbsid=:tcbsId and a.status = 'active' and a.etlCurDate = (select max (etlCurDate) from SmyDwhCasAllUserViewEntity )",
      SmyDwhCasAllUserViewEntity.class);
    query.setParameter("tcbsId", tcbsid);
    List<SmyDwhCasAllUserViewEntity> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0).getCustodycd();
    } else {
      return null;
    }
  }

}
