package com.tcbs.automation.ixu;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "FILE_STATUS")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileStatusEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Long id;

  @Column(name = "STATE")
  private String state;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "LAST_UPDATED_DATE")
  private Timestamp lastUpdatedDate;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Step
  public static List<FileStatusEntity> getFileStatusFlowName(String fileName) {
    Query<FileStatusEntity> query = ixuDbConnection.getSession().createQuery(
      "from FileStatusEntity where fileName='" + fileName + "'", FileStatusEntity.class);
    List<FileStatusEntity> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  @Step
  public static Map<String, Object> getFileStatusWithCreatedUser(String userCreated, String page, String recordOnPage) {
    Long indexPage = Long.valueOf(page);
    Long offset = Long.valueOf(recordOnPage);
    Map<String, Object> result = new HashMap<>();
    Query<Object> query = ixuDbConnection.getSession().createQuery(
      "select * from ( " +
        "select ROW_NUMBER() OVER (ORDER BY id) AS RowNum, * " +
        "from FileStatusEntity where createdUser=:createdUser ) " +
        " as RowConstrainedResult " +
        "where RowNum >= :index and RowNum <= :last"
    );
    query.setParameter("createdUser", userCreated);
    query.setParameter("index", indexPage * offset);
    query.setParameter("last", indexPage * offset + offset);
    List<Object> rs = query.getResultList();
    List<FileStatusEntity> listFileStatusEntities = new ArrayList<>();
    if (rs != null && !rs.isEmpty()) {
      result.put("page", page);
      result.put("offest", offset);
      int size = rs.size();
      for (int i = 0; i < size; i++) {
        Object[] tmp = (Object[]) rs.get(i);
        FileStatusEntity fileStatusEntity = new FileStatusEntity();
        fileStatusEntity.setId((Long) tmp[1]);
        fileStatusEntity.setCreatedUser((String) tmp[2]);
        fileStatusEntity.setCreatedDate((Timestamp) tmp[3]);
        fileStatusEntity.setDescription((String) tmp[4]);
        fileStatusEntity.setLastUpdatedDate((Timestamp) tmp[5]);
        fileStatusEntity.setFileName((String) tmp[6]);
        listFileStatusEntities.add(fileStatusEntity);
        if (i == (size - 1)) {
          result.put("total", (Long) tmp[0]);
        }
      }
      result.put("list", listFileStatusEntities);
      ixuDbConnection.closeSession();
      return result;
    }
    ixuDbConnection.closeSession();
    return null;
  }

  @Step
  public static void clearFileStatus(Long id) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from FileStatusEntity his where his.id=:id");
    query.setParameter("id", id);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearFileStatusByFileName(String fileName) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from FileStatusEntity his where his.fileName=:fileName");
    query.setParameter("fileName", fileName);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
