package com.ak.android.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by 하영 on 2016-12-06.
 * gkduduu@naver.com
 * what is? : 메인팝업시 리스트들 인포
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BannerInfo implements Serializable {
    public String trkking_code;
    public String banner_tip;
    public String banner_url;
    public String file_id;
    public String mobile_popup_id;
    public String banner_target_div_code;
    public String content_url;

    public String getTrkking_code() {
        return trkking_code;
    }

    public void setTrkking_code(String trkking_code) {
        this.trkking_code = trkking_code;
    }

    public String getBanner_tip() {
        return banner_tip;
    }

    public void setBanner_tip(String banner_tip) {
        this.banner_tip = banner_tip;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getMobile_popup_id() {
        return mobile_popup_id;
    }

    public void setMobile_popup_id(String mobile_popup_id) {
        this.mobile_popup_id = mobile_popup_id;
    }

    public String getBanner_target_div_code() {
        return banner_target_div_code;
    }

    public void setBanner_target_div_code(String banner_target_div_code) {
        this.banner_target_div_code = banner_target_div_code;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }
}
