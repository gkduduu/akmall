package android.ak.com.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by gkduduu on 2016-11-15.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageDatas {
    public String akplaza_yn;
    public String buy_age_code;
    public String comment_avg;
    public String comment_cnt;
    public String comment_content;
    public String disp_ctg_id;
    public String final_price;
    public String free_deliv_yn;
    public String goods_id;
    public String goods_kind_code;
    public String goods_name;
    public String launch_yn;
    public String rental_month_price;
    public String rental_months;
    public String sale_dc_price;
    public String sale_price;
    public String selling_point_back;
    public String selling_point_front;
    public String smart_pick_yn;
    public String totalcnt;
    public String upper_vendor_id;
    public String upper_vendor_name;
    public String vendor_id;
    public String vendor_name;
    public String getImagePath;
    public String getPlazaName;
    public String isDeptVendor;
    public Info info;
}
