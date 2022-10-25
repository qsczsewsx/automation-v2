package com.automation.cas;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "xxxx_IAGENT_VIDEO_CALL")
public class xxxxIagentVideoCall {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  private Long id;
  @NotNull
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "ZOOM_USER_ID")
  private String zoomUserId;
  @Column(name = "VIDEO_CALL_TYPE")
  private String videoCallType;
  @Column(name = "MEETING_ID")
  private String meetingId;
  @Column(name = "MEETING_STATUS")
  private String meetingStatus;
  @Column(name = "MEETING_REQUEST_BODY")
  private String meetingRequestBody;
  @Column(name = "MEETING_RESPONSE_BODY")
  private String meetingResponseBody;
  @Column(name = "MEETING_REQUEST_DATE")
  private Date meetingRequestDate;
  @Column(name = "MEETING_STARTED_PAYLOAD")
  private String meetingStartedPayload;
  @Column(name = "MEETING_ENDED_PAYLOAD")
  private String meetingEndedPayload;
  @Column(name = "START_MEETING_TIME")
  private Date startMeetingTime;
  @Column(name = "END_MEETING_TIME")
  private Date endMeetingTime;
  @Column(name = "RECORDING_STARTED_PAYLOAD")
  private String recordingStartedPayload;
  @Column(name = "RECORDING_COMPLETED_PAYLOAD")
  private String recordingCompletedPayload;
  @Column(name = "CUSTOMER_INFO")
  private String customerInfo;
  @Column(name = "DOWNLOAD_STATUS")
  private String downloadStatus;
  @Column(name = "DOWNLOAD_FILE_LOCATION")
  private String downloadFileLocation;
  @Column(name = "DELETE_RECORDING_FILE_STATUS")
  private String deleteRecordingFileStatus;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  public xxxxIagentVideoCall(String userId, String zoomUserId, String videoCallType, String meetingId, String meetingStatus, String meetingRequestBody, String meetingResponseBody, Date meetingRequestDate, String meetingStartedPayload, String meetingEndedPayload, Date startMeetingTime, Date endMeetingTime, String recordingStartedPayload, String recordingCompletedPayload, String customerInfo, String downloadStatus, String downloadFileLocation, String deleteRecordingFileStatus, Date createdDate, Date updatedDate) {
    this.userId = userId;
    this.zoomUserId = zoomUserId;
    this.videoCallType = videoCallType;
    this.meetingId = meetingId;
    this.meetingStatus = meetingStatus;
    this.meetingRequestBody = meetingRequestBody;
    this.meetingResponseBody = meetingResponseBody;
    this.meetingRequestDate = meetingRequestDate;
    this.meetingStartedPayload = meetingStartedPayload;
    this.meetingEndedPayload = meetingEndedPayload;
    this.startMeetingTime = startMeetingTime;
    this.endMeetingTime = endMeetingTime;
    this.recordingStartedPayload = recordingStartedPayload;
    this.recordingCompletedPayload = recordingCompletedPayload;
    this.customerInfo = customerInfo;
    this.downloadStatus = downloadStatus;
    this.downloadFileLocation = downloadFileLocation;
    this.deleteRecordingFileStatus = deleteRecordingFileStatus;
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
  }

  public xxxxIagentVideoCall() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getZoomUserId() {
    return zoomUserId;
  }

  public void setZoomUserId(String zoomUserId) {
    this.zoomUserId = zoomUserId;
  }

  public String getVideoCallType() {
    return videoCallType;
  }

  public void setVideoCallType(String videoCallType) {
    this.videoCallType = videoCallType;
  }

  public String getMeetingId() {
    return meetingId;
  }

  public void setMeetingId(String meetingId) {
    this.meetingId = meetingId;
  }

  public String getMeetingStatus() {
    return meetingStatus;
  }

  public void setMeetingStatus(String meetingStatus) {
    this.meetingStatus = meetingStatus;
  }

  public String getMeetingRequestBody() {
    return meetingRequestBody;
  }

  public void setMeetingRequestBody(String meetingRequestBody) {
    this.meetingRequestBody = meetingRequestBody;
  }

  public String getMeetingResponseBody() {
    return meetingResponseBody;
  }

  public void setMeetingResponseBody(String meetingResponseBody) {
    this.meetingResponseBody = meetingResponseBody;
  }

  public Date getMeetingRequestDate() {
    return meetingRequestDate;
  }

  public void setMeetingRequestDate(Date meetingRequestDate) {
    this.meetingRequestDate = meetingRequestDate;
  }

  public String getMeetingStartedPayload() {
    return meetingStartedPayload;
  }

  public void setMeetingStartedPayload(String meetingStartedPayload) {
    this.meetingStartedPayload = meetingStartedPayload;
  }

  public String getMeetingEndedPayload() {
    return meetingEndedPayload;
  }

  public void setMeetingEndedPayload(String meetingEndedPayload) {
    this.meetingEndedPayload = meetingEndedPayload;
  }

  public Date getStartMeetingTime() {
    return startMeetingTime;
  }

  public void setStartMeetingTime(Date startMeetingTime) {
    this.startMeetingTime = startMeetingTime;
  }

  public Date getEndMeetingTime() {
    return endMeetingTime;
  }

  public void setEndMeetingTime(Date endMeetingTime) {
    this.endMeetingTime = endMeetingTime;
  }

  public String getRecordingStartedPayload() {
    return recordingStartedPayload;
  }

  public void setRecordingStartedPayload(String recordingStartedPayload) {
    this.recordingStartedPayload = recordingStartedPayload;
  }

  public String getRecordingCompletedPayload() {
    return recordingCompletedPayload;
  }

  public void setRecordingCompletedPayload(String recordingCompletedPayload) {
    this.recordingCompletedPayload = recordingCompletedPayload;
  }

  public String getCustomerInfo() {
    return customerInfo;
  }

  public void setCustomerInfo(String customerInfo) {
    this.customerInfo = customerInfo;
  }

  public String getDownloadStatus() {
    return downloadStatus;
  }

  public void setDownloadStatus(String downloadStatus) {
    this.downloadStatus = downloadStatus;
  }

  public String getDownloadFileLocation() {
    return downloadFileLocation;
  }

  public void setDownloadFileLocation(String downloadFileLocation) {
    this.downloadFileLocation = downloadFileLocation;
  }

  public String getDeleteRecordingFileStatus() {
    return deleteRecordingFileStatus;
  }

  public void setDeleteRecordingFileStatus(String deleteRecordingFileStatus) {
    this.deleteRecordingFileStatus = deleteRecordingFileStatus;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }

  @Step
  public xxxxIagentVideoCall getVideoCallByUserId(String userId) {
    Session session = CAS.casConnection.getSession();
    session.clear();
    Query<xxxxIagentVideoCall> query = session.createQuery("from xxxxIagentVideoCall where userId=:userId order by id desc", xxxxIagentVideoCall.class);
    xxxxIagentVideoCall result = new xxxxIagentVideoCall();
    query.setParameter("userId", userId);
    query.setMaxResults(1);
    try {
      result = query.getSingleResult();
    } catch (Exception ex) {
      System.out.println(ex);
    }
    return result;
  }

  @Step
  public xxxxIagentVideoCall getVideoCallByMeetingId(String meetingId) {
    Session session = CAS.casConnection.getSession();
    session.clear();
    Query<xxxxIagentVideoCall> query = session.createQuery("from xxxxIagentVideoCall where meetingId=:meetingId order by id desc", xxxxIagentVideoCall.class);
    xxxxIagentVideoCall result = new xxxxIagentVideoCall();
    query.setParameter("meetingId", meetingId);
    query.setMaxResults(1);
    try {
      result = query.getSingleResult();
    } catch (Exception ex) {
      System.out.println(ex);
    }
    return result;
  }

  public void insert() {
    Session session = CAS.casConnection.getSession();
    session.clear();
    session.beginTransaction();
    session.save(this);
    session.getTransaction().commit();
  }

  public void delete() {
    Session session = CAS.casConnection.getSession();
    session.clear();
    session.beginTransaction();
    session.delete(this);
    session.getTransaction().commit();
  }

  @Step
  public xxxxIagentVideoCall getStatusMeeting(String meetingStatus) {
    Session session = CAS.casConnection.getSession();
    session.clear();
    Query<xxxxIagentVideoCall> query = session.createQuery("from xxxxIagentVideoCall where meetingStatus=: meetingStatus", xxxxIagentVideoCall.class);
    return query.getSingleResult();
  }

}
