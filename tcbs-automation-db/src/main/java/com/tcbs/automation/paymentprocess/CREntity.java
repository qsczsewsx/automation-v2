package com.tcbs.automation.paymentprocess;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@MappedSuperclass
public abstract class CREntity {
  @Column(name = "CREATED_AT", updatable = false, nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  @JsonIgnore
  private Date createdAtDate = null;

  @Transient
  private String createdAt = null;

  @Column(name = "CREATED_BY", updatable = false, nullable = false)
  private String createdBy = null;

  public Date getCreatedAtDate() {
    return createdAtDate;
  }

  public void setCreatedAtDate(Date createdAtDate) {
    this.createdAtDate = createdAtDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public void convertDateToString() {
    if (this.createdAtDate != null) {
      this.createdAt = dateToString(this.createdAtDate, "yyyy-MM-dd HH:mm:ss");
    }

  }

  public String dateToString(Date date, String format) {
    SimpleDateFormat formatter = new SimpleDateFormat();
    formatter.applyPattern(format);
    return formatter.format(date);
  }
}
