package com.tcbs.automation.tcinvest.cogs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INV_ORDER_ATTR")
public class InvOrderAttr implements Serializable {
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

  @Column(name = "ORDER_ID")
  private Long orderId;
}
