package com.tcbs.automation.tcbond;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class EventId implements Serializable {
  @Column(name = "BOND_ORDER_ID")
  private Integer bondOrderId;
  @Column(name = "COUPON_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSZ")
  private String couponDate;

  public EventId() {

  }

  public EventId(Integer bondOrderId, String couponDate) {
    this.bondOrderId = bondOrderId;
    this.couponDate = couponDate;
  }

  @Override
  public String toString() {
    return this.bondOrderId + " + " + this.couponDate;
  }

  public Integer getBondOrderId() {
    return bondOrderId;
  }

  public String getCouponDate() {
    return couponDate;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
      + ((bondOrderId == null) ? 0 : bondOrderId.hashCode());
    result = prime * result + couponDate == null ? 0 : couponDate.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    EventId other = (EventId) obj;
    if (bondOrderId == null) {
      if (other.bondOrderId != null) {
        return false;
      }
    } else if (!bondOrderId.equals(other.bondOrderId)) {
      return false;
    }
    if (couponDate == null) {
      if (other.couponDate != null) {
        return false;
      }
    } else if (!couponDate.equals(other.couponDate)) {
      return false;
    }
    return true;
  }
}
