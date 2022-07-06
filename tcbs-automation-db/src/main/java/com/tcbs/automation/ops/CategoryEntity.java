package com.tcbs.automation.ops;

import lombok.*;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.ops.OPS.opsConnection;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CATEGORY")
public class CategoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "CATEGORY_NAME")
  private String categoryName;

  @Column(name = "TCBSID")
  private String tcbsid;

  @Column(name = "CATEGORY_DEFAULT")
  private String categoryDefault;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  @Column(name = "LIST_STOCKS_CODE")
  private String listStocksCode;

  @Column(name = "CATEGORY_TYPE")
  private String categoryType;

  @Column(name = "METADATA")
  private String metadata;


  public static CategoryEntity getByID(int id) {
    opsConnection.getSession().clear();
    opsConnection.getSession().beginTransaction();

    Query<CategoryEntity> query = opsConnection.getSession().createQuery("SELECT c FROM CategoryEntity c WHERE c.id = :id");
    query.setParameter("id", id);

    CategoryEntity categoryEntity = query.getSingleResult();
    opsConnection.getSession().getTransaction().commit();
    return categoryEntity;
  }

  public static List<CategoryEntity> getByIdIn(List<Integer> ids) {
    opsConnection.getSession().clear();
    opsConnection.getSession().beginTransaction();

    Query<CategoryEntity> query = opsConnection.getSession().createQuery("SELECT c FROM CategoryEntity c WHERE c.id IN :ids");
    query.setParameter("ids", ids);

    List<CategoryEntity> categoryEntities = query.getResultList();
    opsConnection.getSession().getTransaction().commit();
    return categoryEntities;
  }

  public static List<CategoryEntity> getByTcbsID(String tcbsID) {
    opsConnection.getSession().clear();
    opsConnection.getSession().beginTransaction();

    Query<CategoryEntity> query = opsConnection.getSession().createQuery("SELECT c FROM CategoryEntity c WHERE c.tcbsid = :tcbsid");
    query.setParameter("tcbsid", tcbsID);

    List<CategoryEntity> categoryEntities = query.getResultList();
    opsConnection.getSession().getTransaction().commit();
    return categoryEntities;
  }

  public static List<CategoryEntity> getByTcbsIDAndType(String tcbsID) {
    opsConnection.getSession().clear();
    opsConnection.getSession().beginTransaction();

    Query<CategoryEntity> query = opsConnection.getSession().createQuery("SELECT c FROM CategoryEntity c WHERE c.tcbsid = :tcbsid AND c.categoryType IN ('WL','DWL')");
    query.setParameter("tcbsid", tcbsID);

    List<CategoryEntity> categoryEntities = query.getResultList();
    opsConnection.getSession().getTransaction().commit();
    return categoryEntities;
  }
}
