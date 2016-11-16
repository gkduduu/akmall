package android.ak.com.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by gkduduu on 2016-11-15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Info {
    public String coupon_price;
    public String coupon_rate_amt;
    public String coupon_rate_amt_yn;
    public String dc_prom_rate_amt;
    public String dc_prom_rate_amt_yn;
    public String final_price;
    public String lumpPayDcYN;
    public String nointMonthCnt;
    public String reserve_price;
    public String sale_dc_price;
    public String sale_price;
    public String sale_rate;

}
