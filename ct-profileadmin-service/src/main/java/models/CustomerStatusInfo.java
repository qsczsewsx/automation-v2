package models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerStatusInfo {
  private List<CustomerStatuses> customerStatuses;
  private Message response;
}
