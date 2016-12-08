package com.ak.android.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 하영 on 2016-12-06.
 * gkduduu@naver.com
 * what is? : 메인팝업
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MainPopupResult implements Serializable {
    public String popup_width;
    public String banner_type_code;
    public String cookie_un_disp_term_apply_yn;
    public String popup_div_code;
    public String popup_height;
    public int mobile_popup_id;
    public String updown_scroll_yn;
    public String popup_content;
    public List<BannerInfo> file;

    public String getPopup_width() {
        return popup_width;
    }

    public void setPopup_width(String popup_width) {
        this.popup_width = popup_width;
    }

    public String getBanner_type_code() {
        return banner_type_code;
    }

    public void setBanner_type_code(String banner_type_code) {
        this.banner_type_code = banner_type_code;
    }

    public String getCookie_un_disp_term_apply_yn() {
        return cookie_un_disp_term_apply_yn;
    }

    public void setCookie_un_disp_term_apply_yn(String cookie_un_disp_term_apply_yn) {
        this.cookie_un_disp_term_apply_yn = cookie_un_disp_term_apply_yn;
    }

    public String getPopup_div_code() {
        return popup_div_code;
    }

    public void setPopup_div_code(String popup_div_code) {
        this.popup_div_code = popup_div_code;
    }

    public String getPopup_height() {
        return popup_height;
    }

    public void setPopup_height(String popup_height) {
        this.popup_height = popup_height;
    }

    public int getMobile_popup_id() {
        return mobile_popup_id;
    }

    public void setMobile_popup_id(int mobile_popup_id) {
        this.mobile_popup_id = mobile_popup_id;
    }

    public String getUpdown_scroll_yn() {
        return updown_scroll_yn;
    }

    public void setUpdown_scroll_yn(String updown_scroll_yn) {
        this.updown_scroll_yn = updown_scroll_yn;
    }

    public String getPopup_content() {
        return popup_content;
    }

    public void setPopup_content(String popup_content) {
        this.popup_content = popup_content;
    }

    public List<BannerInfo> getFile() {
        return file;
    }

    public void setFile(List<BannerInfo> file) {
        this.file = file;
    }
}
