package com.tcbs.automation.hfcdata;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "SMY_DWH_PT_MING_NEIYIN")
public class IFSMingEntity {
  @Id
  @NotNull
  @Column(name = "FULL_MING_ID")
  private Integer mingId;
  @Column(name = "FULL_MING")
  private String ming;
  @Column(name = "OVERVIEW")
  private String overview;
  @Column(name = "PREFACE")
  private String preface;
  @Column(name = "DES_COLOR")
  private String desColor;
  @Column(name = "DES_MING")
  private String desMing;

  @Step("Get ming data")
  public static List<IFSMingEntity> getMing(Integer mingId) {
    try {
      Query<IFSMingEntity> query = HfcData.hfcDataDbConnection.getSession()
        .createQuery("from IFSMingEntity a where a.mingId=:fullMingId", IFSMingEntity.class);
      query.setParameter("fullMingId", mingId);
      List<IFSMingEntity> rsSet = query.getResultList();

      return rsSet;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
