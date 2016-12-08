package com.ak.android.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by 하영 on 2016-11-28.
 * gkduduu@naver.com
 * what is? :
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProdList {
    public ProductResult bestProdList;
    public ProductResult newProdList;
    public ProductResult themeList;
    public int pageIdx;
    public int totalcnt;
    public List<PageDatas> pageDatas;
}
