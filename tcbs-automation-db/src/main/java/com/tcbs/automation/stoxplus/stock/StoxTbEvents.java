package com.tcbs.automation.stoxplus.stock;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "stox_tb_Events", schema = "dbo")
public class StoxTbEvents {
  private int id;
  private String eventName;
  private String eventNameE;
  private String eventCode;
  private String ticker;
  private Date anDate;
  private Date exDate;
  private Date regFinalDate;
  private Date exRigthDate;
  private String eventDesc;
  private String eventDescE;
  private String note;
  private String noteE;
  private String fileUpdate;
  private String eventNameJp;
  private String eventDescJp;
  private String noteJp;
  private Double volumn;
  private String sourceLink;
  private Long idv2;

  @Id
  @Column(name = "ID")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "EventName")
  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  @Basic
  @Column(name = "EventName_E")
  public String getEventNameE() {
    return eventNameE;
  }

  public void setEventNameE(String eventNameE) {
    this.eventNameE = eventNameE;
  }

  @Basic
  @Column(name = "EventCode")
  public String getEventCode() {
    return eventCode;
  }

  public void setEventCode(String eventCode) {
    this.eventCode = eventCode;
  }

  @Basic
  @Column(name = "Ticker")
  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  @Basic
  @Column(name = "AnDate")
  public Date getAnDate() {
    return anDate;
  }

  public void setAnDate(Date anDate) {
    this.anDate = anDate;
  }

  @Basic
  @Column(name = "ExDate")
  public Date getExDate() {
    return exDate;
  }

  public void setExDate(Date exDate) {
    this.exDate = exDate;
  }

  @Basic
  @Column(name = "RegFinalDate")
  public Date getRegFinalDate() {
    return regFinalDate;
  }

  public void setRegFinalDate(Date regFinalDate) {
    this.regFinalDate = regFinalDate;
  }

  @Basic
  @Column(name = "ExRigthDate")
  public Date getExRigthDate() {
    return exRigthDate;
  }

  public void setExRigthDate(Date exRigthDate) {
    this.exRigthDate = exRigthDate;
  }

  @Basic
  @Column(name = "EventDesc")
  public String getEventDesc() {
    return eventDesc;
  }

  public void setEventDesc(String eventDesc) {
    this.eventDesc = eventDesc;
  }

  @Basic
  @Column(name = "EventDesc_E")
  public String getEventDescE() {
    return eventDescE;
  }

  public void setEventDescE(String eventDescE) {
    this.eventDescE = eventDescE;
  }

  @Basic
  @Column(name = "Note")
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  @Basic
  @Column(name = "Note_E")
  public String getNoteE() {
    return noteE;
  }

  public void setNoteE(String noteE) {
    this.noteE = noteE;
  }

  @Basic
  @Column(name = "FileUpdate")
  public String getFileUpdate() {
    return fileUpdate;
  }

  public void setFileUpdate(String fileUpdate) {
    this.fileUpdate = fileUpdate;
  }

  @Basic
  @Column(name = "EventName_JP")
  public String getEventNameJp() {
    return eventNameJp;
  }

  public void setEventNameJp(String eventNameJp) {
    this.eventNameJp = eventNameJp;
  }

  @Basic
  @Column(name = "EventDesc_JP")
  public String getEventDescJp() {
    return eventDescJp;
  }

  public void setEventDescJp(String eventDescJp) {
    this.eventDescJp = eventDescJp;
  }

  @Basic
  @Column(name = "Note_JP")
  public String getNoteJp() {
    return noteJp;
  }

  public void setNoteJp(String noteJp) {
    this.noteJp = noteJp;
  }

  @Basic
  @Column(name = "Volumn")
  public Double getVolumn() {
    return volumn;
  }

  public void setVolumn(Double volumn) {
    this.volumn = volumn;
  }

  @Basic
  @Column(name = "SourceLink")
  public String getSourceLink() {
    return sourceLink;
  }

  public void setSourceLink(String sourceLink) {
    this.sourceLink = sourceLink;
  }

  @Basic
  @Column(name = "Idv2")
  public Long getIdv2() {
    return idv2;
  }

  public void setIdv2(Long idv2) {
    this.idv2 = idv2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StoxTbEvents that = (StoxTbEvents) o;
    return id == that.id &&
      Objects.equals(eventName, that.eventName) &&
      Objects.equals(eventNameE, that.eventNameE) &&
      Objects.equals(eventCode, that.eventCode) &&
      Objects.equals(ticker, that.ticker) &&
      Objects.equals(anDate, that.anDate) &&
      Objects.equals(exDate, that.exDate) &&
      Objects.equals(regFinalDate, that.regFinalDate) &&
      Objects.equals(exRigthDate, that.exRigthDate) &&
      Objects.equals(eventDesc, that.eventDesc) &&
      Objects.equals(eventDescE, that.eventDescE) &&
      Objects.equals(note, that.note) &&
      Objects.equals(noteE, that.noteE) &&
      Objects.equals(fileUpdate, that.fileUpdate) &&
      Objects.equals(eventNameJp, that.eventNameJp) &&
      Objects.equals(eventDescJp, that.eventDescJp) &&
      Objects.equals(noteJp, that.noteJp) &&
      Objects.equals(volumn, that.volumn) &&
      Objects.equals(sourceLink, that.sourceLink) &&
      Objects.equals(idv2, that.idv2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, eventName, eventNameE, eventCode, ticker, anDate, exDate, regFinalDate, exRigthDate, eventDesc, eventDescE, note, noteE, fileUpdate, eventNameJp, eventDescJp, noteJp,
      volumn,
      sourceLink, idv2);
  }
}
