package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;

import java.sql.*;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "TEMPLATE_TRANSFER_IXU")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TemplateTransferIxu {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "TEMPLATE_CODE")
  private String templateCode;
  @Column(name = "TEMPLATE_NOTIFY")
  private String templateNotify;
  @Column(name = "BACK_GROUND_URL")
  private String backGroundUrl;
  @Column(name = "ICON_URL")
  private String iconUrl;
  @Column(name = "PRIORITY")
  private String priority;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "LAST_MODIFY_DATE")
  private Timestamp lastModifyDate;
  @Column(name = "NAME")
  private String name;
  @Column(name = "GROUP_NAME")
  private String groupName;
  @Column(name = "GROUP_PRIORITY")
  private String groupPriority;

  @Step
  public static void saveTemplate(TemplateTransferIxu template) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(template);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void delete(String template) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery("delete from TemplateTransferIxu where templateCode = :template");
    query.setParameter("template", template);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteAll() {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from TemplateTransferIxu where id is not null");
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<TemplateTransferIxu> getTransferIxuTemplateByTemplateCode(String templateCode) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query<TemplateTransferIxu> query = ixuDbConnection.getSession().createQuery(
      "from TemplateTransferIxu a where a.templateCode = :templateCode order by a.id desc", TemplateTransferIxu.class);
    query.setParameter("templateCode", templateCode);
    List<TemplateTransferIxu> results = query.getResultList();
    ixuDbConnection.closeSession();
    return results;
  }

  @Step
  public static List<TemplateTransferIxu> getAllTemplatesOrderByPriorityAsc() {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query<TemplateTransferIxu> query = ixuDbConnection.getSession().createQuery(
      "from TemplateTransferIxu a order by a.priority asc", TemplateTransferIxu.class);
    List<TemplateTransferIxu> results = query.getResultList();
    ixuDbConnection.closeSession();
    return results;
  }
}
