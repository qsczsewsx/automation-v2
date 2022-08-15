package com.tcbs.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "OAUTH2_REGISTERED_CLIENT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Oauth2RegisteredClient {
  @Id
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;

  @Column(name = "CLIENT_ID")
  private String clientId;

  @Column(name = "SCOPES")
  private String scopes;

  public static Oauth2RegisteredClient getByClientId(String clientId) {
    CAS.casConnection.getSession().clear();
    Query<Oauth2RegisteredClient> query = CAS.casConnection.getSession().createQuery(
      "from Oauth2RegisteredClient a where a.clientId=:clientId", Oauth2RegisteredClient.class);
    query.setParameter("clientId", clientId);
    return query.getSingleResult();
  }
}
