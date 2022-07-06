package com.tcbs.automation.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseInfo {

  private EnterprisePersonInfo chiefAccountantInfo;

  private List<EnterprisePersonInfo> representativePersons;

  private List<EnterprisePersonInfo> contactPersons;

  private List<EnterprisePersonInfo> authorizedPersons;

}
