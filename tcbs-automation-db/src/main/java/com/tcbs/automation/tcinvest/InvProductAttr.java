package com.tcbs.automation.tcinvest;

import com.tcbs.automation.enumerable.bond.ApplyType;
import com.tcbs.automation.enumerable.bond.AttributeName;
import lombok.Getter;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;

import static com.tcbs.automation.functions.PublicConstant.dateTimeFormat;


@Entity
@Getter
@Table(name = "INV_PRODUCT_ATTR")
public class InvProductAttr {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "VALUE")
  private String value;
  @Column(name = "CAPTION")
  private String caption;
  @Column(name = "PRODUCT_ID")
  private Integer productId;
  @Column(name = "TIMESTAMP")
  private Date timestamp;
  @Column(name = "APPLYTYPE")
  private String applytype;

  public InvProductAttr() {
  }

  @SneakyThrows
  public InvProductAttr(String name, String value, String caption, Integer productId, Date timestamp, String applytype) {
    this.name = name;
    this.value = value;
    this.caption = caption;
    this.productId = productId;
    this.timestamp = timestamp;
    this.applytype = applytype;
  }

  public static void deleteByProductId(String productId) {
    Integer pId = Integer.parseInt(productId);
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvProductAttr> query = session.createQuery(
      "DELETE FROM InvProductAttr ip WHERE ip.productId =: pId"
    );
    query.setParameter("pId", pId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public InvProductAttr setId(Long id) {
    this.id = id;
    return this;
  }

  public InvProductAttr setName(String name) {
    this.name = name;
    return this;
  }

  public InvProductAttr setName(AttributeName name) {
    this.name = name.getValue();
    return this;
  }

  public InvProductAttr setValue(String value) {
    this.value = value;
    return this;
  }

  public InvProductAttr setCaption(String caption) {
    this.caption = caption;
    return this;
  }

  public InvProductAttr setProductId(Integer productId) {
    this.productId = productId;
    return this;
  }

  @SneakyThrows
  public InvProductAttr setTimestamp(String timestamp) {
    this.timestamp = dateTimeFormat.parse(timestamp);
    return this;
  }

  public InvProductAttr setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public InvProductAttr setApplytype(String applytype) {
    this.applytype = applytype;
    return this;
  }

  public InvProductAttr setApplytype(ApplyType applytype) {
    this.applytype = applytype.toString();
    return this;
  }

  @Override
  public String toString() {
    return "InvProductAttr{" +
      "name='" + name + '\'' +
      ", value='" + value + '\'' +
      ", caption='" + caption + '\'' +
      ", productId=" + productId +
      ", timestamp='" + timestamp + '\'' +
      ", applytype='" + applytype + '\'' +
      '}';
  }

  @SuppressWarnings("unchecked")
  public void updateAttribute_value_byName(String name, String value, Long productId) {
    Session session = TcInvest.tcInvestDbConnection.getSession();

    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvGlobalAttr> query = session.createQuery(
      "UPDATE InvProductAttr i\n" +
        "    SET i.value=:value\n" +
        "    where i.name=:name AND i.productId=:productId"
    );

    query.setParameter("value", value);
    query.setParameter("name", name);
    query.setParameter("productId", productId);

    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void delete() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvGlobalAttr> query = session.createQuery(
      "DELETE FROM InvProductAttr WHERE name=:name AND productId = :productId"
    );
    query.setParameter("name", name);
    query.setParameter("productId", productId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void delete_by_id() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvGlobalAttr> query = session.createQuery(
      "DELETE FROM InvProductAttr WHERE id=:id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void insert() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    session.save(this);
    session.getTransaction().commit();
  }

  public void deleteProductAttrsByCaption() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvGlobalAttr> query = session.createQuery(
      "DELETE FROM InvProductAttr WHERE name=:name AND productId = :productId AND caption=:caption"
    );
    query.setParameter("name", name);
    query.setParameter("productId", productId);
    query.setParameter("caption", caption);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
