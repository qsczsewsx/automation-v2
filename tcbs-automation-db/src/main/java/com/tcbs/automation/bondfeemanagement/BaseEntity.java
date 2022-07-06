package com.tcbs.automation.bondfeemanagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Setter
@Getter
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

}
