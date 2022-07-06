package com.tcbs.automation.conditionalorder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ENGINE_ASSISTANCE")
public class EngineAssistance {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private int id;
  @NotNull
  @Column(name = "ORDER_ID")
  private int orderId;
  @NotBlank
  @Column(name = "UUID")
  private String uuid;
  @NotBlank
  @Column(name = "SYMBOL")
  private String symbol;
  @NotBlank
  @Column(name = "CODE")
  private String code;
  @NotNull
  @Column(name = "VALUE")
  private Long value;
  @NotBlank
  @Column(name = "VALUE_TYPE")
  private String valueType;
  @NotBlank
  @Column(name = "METADATA")
  private String metadata;

  public static List<Map<String, Object>> getEngineAssistanceByOrderId(int orderId) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM ENGINE_ASSISTANCE \r\n");
    queryStringBuilder.append(String.format("WHERE ORDER_ID = %2d", orderId));

    List result = new ArrayList<>();
    result = TheConditionalOrder.anattaDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    return result;
  }
}

