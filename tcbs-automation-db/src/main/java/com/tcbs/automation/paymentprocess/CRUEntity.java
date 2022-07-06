package com.tcbs.automation.paymentprocess;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@MappedSuperclass
public abstract class CRUEntity {
  @Column(name = "CREATED_AT", updatable = false, nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  @JsonIgnore
  private Date createdAtDate = null;

  @Transient
  private String createdAt = null;


  @Column(name = "UPDATED_AT")
  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @JsonIgnore
  private Date updatedAtDate = null;

  @Transient
  private String updatedAt = null;

  public Date getCreatedAtDate() {
    return createdAtDate;
  }

  public void setCreatedAtDate(Date createdAtDate) {
    this.createdAtDate = createdAtDate;
  }

  public Date getUpdatedAtDate() {
    return updatedAtDate;
  }

  public void setUpdatedAtDate(Date updatedAtDate) {
    this.updatedAtDate = updatedAtDate;
  }

  public void convertDateToString() {
    if (this.createdAtDate != null) {
      this.createdAt = dateToString(this.createdAtDate, "yyyy-MM-dd HH:mm:ss");
    }

    if (this.updatedAtDate != null) {
      this.updatedAt = dateToString(this.updatedAtDate, "yyyy-MM-dd HH:mm:ss");
    }
  }

  public String dateToString(Date date, String format) {
    SimpleDateFormat formatter = new SimpleDateFormat();
    formatter.applyPattern(format);
    return formatter.format(date);
  }
}
