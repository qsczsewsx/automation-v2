package com.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "OAUTH2_REFRESH_TOKEN")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Oauth2RefreshToken {
  @javax.persistence.Id
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;

  @Column(name = "SESSION_ID")
  private String sessionId;

  @Column(name = "REFRESH_TOKEN_STATUS")
  private BigDecimal status;

  public static Oauth2RefreshToken getBySessionId(String sessionId) {
    CAS.casConnection.getSession().clear();
    Query<Oauth2RefreshToken> query = CAS.casConnection.getSession().createNativeQuery(
      "SELECT ID, SESSION_ID, REFRESH_TOKEN_STATUS FROM OAUTH2_REFRESH_TOKEN WHERE SESSION_ID =:sessionId", Oauth2RefreshToken.class);
    query.setParameter("sessionId", sessionId);
    return query.getSingleResult();
  }
}
