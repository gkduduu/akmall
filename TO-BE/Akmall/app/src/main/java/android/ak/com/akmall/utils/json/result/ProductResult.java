package android.ak.com.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by gkduduu on 2016. 11. 17..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResult {
    public String pageIdx;
    public String totalcnt;
    public String rowsPerPage;
    public String pageRange;
    public List<PageDatas> pageDatas;
}
