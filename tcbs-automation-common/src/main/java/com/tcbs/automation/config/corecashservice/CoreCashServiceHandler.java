package com.tcbs.automation.config.corecashservice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.Period;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import static com.tcbs.automation.functions.PublicConstant.datePickerFormat;

public class CoreCashServiceHandler {
  public static final String CASH_SYMBOL = "CP4A2103";
  public static final String CASH_TYPE = "BondCoupon";
  public static final String CASH_TRANSACTION_CODE = "CCNMB.0000079991.IBONDPRIX.CP4A2103";
  public static final String CASH_AMOUNT = "20000";
  public static final String CASH_DESCRIPTION = "CNMB 0000079991 IBONDPRIX CP4A2103";
  public static final String CASH_TCBSID = "0001010890";
  public static final String CASH_OTHER_TCBSID = "0001014285";
  public static final String CASH_RUBIK_TCBSID = "0001738764";
  public static final String SUCCESS = "Success";
  public static final String PAY_PROCESSING = "Processing";

  public static final String E_CODE_108107 = "108107";
  public static final String E_MESSAGE_108107 = "FLEX 016 -  Vượt quá số dư có thể giải tỏa";
  public static final String E_MESSAGE_108107_FOR_PAY = "FLEX 025 -  Vượt quá số dư được phép rút của tiểu khoản";
  public static final String E_CODE_108135 = "108135";
  public static final String E_CODE_777000 = "777000";
  public static final String E_CODE_777103 = "777103";
  public static final String E_CODE_777112 = "777112";

  public static final String E_MESSAGE_108135 = "FLEX -880117 - Không tồn tại phong tỏa theo cặp ClientCode+ OrderId của tiểu khoản truyền vào";
  public static final String E_CODE_108000 = "108000";
  public static final String E_MESSAGE_108000 = "FLEX un-handle - -90048 - [-90048] - Số hiệu lệnh và Loại sản phẩm theo tiểu khoản không tồn tại!";

  public static final String IXU_AMOUNT = "1000";
  public static final String CASHIXU = "CASHIXU";
  public static final String DESCRIPTION_DEFAULT = "Test by Tung";
  public static final String VARIATION_TRANSCODE_TRUE = "variationTransCode=1";
  public static final String VARIATION_TRANSCODE_FALSE = "variationTransCode=0";

  public static final String BANK_CODE = "01310001";
  public static final String BANK_NAME = "NGAN HANG TMCP KY THUONG VIET NAM";
  public static final String OWNER_NAME = "KHACH HANG 33336666";
  public static final String BANK_ACCOUNT_NUMBER = "19033336666021";
  public static final String PAY_AMOUNT = "5000";
  public static final String CONTRACT_CODE_HD001 = "HD001";
  public static final String CONTRACT_CODE_HD002 = "HD002";
  public static final String CONTRACT_CODE_HD003 = "HD003";

  public static String transactionCodeRandom() {
    return "IXU" + String.format("%07d", ThreadLocalRandom.current().nextInt(100000, 9000000 + 1));
  }

  public static String dateStringPeriod(int period) throws ParseException {
    SimpleDateFormat dateIsoFormatString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    Date fromDate = dateIsoFormatString.parse(Instant.now().plus(Period.ofDays(period)).toString());
    return datePickerFormat.format(fromDate);
  }
}
