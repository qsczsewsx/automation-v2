package models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerStatuses {
  private String tcbsId;
  private String id;
  private String name;
  private String code105C;
  private String email;
  private String phoneNumber;
  private String idNumber;
  private String gender;
  private String flexAccountStatus;
  private String flexAccountActivatedDate;
  private String fundAccountStatus;
  private String fundAccountActivatedDate;
  private String documentStatus;
  private String transferStatus;
  private String birthday;
  private String bookNumber;
  private String docusignStatus;
}
