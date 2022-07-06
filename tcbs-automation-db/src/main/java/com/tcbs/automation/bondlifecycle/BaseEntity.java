package com.tcbs.automation.bondlifecycle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.beanutils.ConvertUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity {
  @Column(name = "CREATED_AT", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  @JsonIgnore
  private Date createdAtDate = null;

  @Transient
  private String createdAt = null;

  @Column(name = "CREATED_BY", updatable = false)
  private String createdBy = null;

  @Column(name = "UPDATED_AT")
  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @JsonIgnore
  private Date updatedAtDate = null;

  @Transient
  private String updatedAt = null;

  @Column(name = "UPDATED_BY")
  private String updatedBy = null;

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

  public Date getUpdatedAtDate() {
    return updatedAtDate;
  }

  public void setUpdatedAtDate(Date updatedAtDate) {
    this.updatedAtDate = updatedAtDate;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Object convertToDto(Class<?> dtoClass) {
    return ConvertUtils.convert(this, dtoClass);
  }
}
