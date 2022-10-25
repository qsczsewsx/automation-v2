package com.automation.common.kafka_command;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
public class BaseCmd implements Serializable {
  private String id;
  private String traceID;
  private String sender;
  private Date createdDate;
}
