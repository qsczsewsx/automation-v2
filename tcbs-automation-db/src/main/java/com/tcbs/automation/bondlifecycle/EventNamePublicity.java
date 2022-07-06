package com.tcbs.automation.bondlifecycle;

import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "EVENT_NAME_PUBLICITY")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventNamePublicity {

  @EmbeddedId
  private EventNamePublicityId id;

  @Step
  public static List<EventNamePublicity> getEventNamePublicityByEventNameId(Integer eventNameId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<EventNamePublicity> query = session.createNativeQuery("select * from EVENT_NAME_PUBLICITY where  EVENT_NAME_ID =:eventNameId", EventNamePublicity.class);
    query.setParameter("eventNameId", eventNameId);
    List<EventNamePublicity> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results;
  }

  @Embeddable
  @Setter
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class EventNamePublicityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "EVENT_NAME_ID")
    private Integer eventNameId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PUBLICITY_ID")
    private Publicity publicity;
  }
}
