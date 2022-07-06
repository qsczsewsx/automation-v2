package com.tcbs.automation.paymentprocess;

import com.tcbs.automation.ops.OPS;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "H2H_BATCH")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Batch extends CRUEntity {

  @Id
  @Column(name = "ID", updatable = false)
  private String id;

  @Column(name = "CLIENT_ID")
  private String clientId;

  @Column(name = "BATCH_TYPE")
  private String batchType;

  @Column(name = "DEBIT_ACCOUNT_NO")
  private String debitAccountNo;

  @Column(name = "DESCRIPTION")
  private String desc;

  @Column(name = "BPM_INSTANCE_ID")
  private String bpmInstanceId;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "batch")
  private List<Transaction> transactions;

  public static Batch getBatchById(String id) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    Query<Batch> query = session.createQuery("select b from Batch b where b.id =:id",
      Batch.class);
    query.setParameter("id", id);
    List<Batch> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }

  public static Batch getBatchAndAllTransactionsById(String id) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    Query<Batch> query = session.createQuery("select b from Batch b join fetch b.transactions where b.id =:id",
      Batch.class);
    query.setParameter("id", id);
    List<Batch> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }

  public static List<Batch> getBatchsByIdList(List<String> ids) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    Query<Batch> query = session.createQuery("select d from Batch b where b.id in :ids",
      Batch.class);
    query.setParameter("ids", ids);
    List<Batch> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results;
  }

  public static void insert(Batch batch) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    org.hibernate.Transaction trans = session.beginTransaction();
    session.save(batch);
    trans.commit();
  }

  public static void deleteById(String id) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM Batch b WHERE b.id = :id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }

}
