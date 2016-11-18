package android.ak.com.akmall.utils.json;

import android.ak.com.akmall.utils.json.result.BestResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by gkduuu on 2016-11-15.
 * 서버 결과 파싱해줌
 */
public class Parser {

    //베스트 파
    public static BestResult parsingBestProduct(String response) {
        BestResult result = new BestResult();
        try {
            JSONObject res = new JSONObject(response);
            result = new ObjectMapper().readValue(res.getString("productList"),BestResult.class);
        }catch(IOException e) {
            e.getMessage();
        }catch(JSONException e){
            e.getMessage();
        }
        return result;
    }

//    public UseInfoDetailResult parsingUseInfoDetail(String data) {
//        UseInfoDetailResult rs = new UseInfoDetailResult();
//        try {
//            rs = new ObjectMapper().readValue(data, UseInfoDetailResult.class);
//            JSONObject json = new JSONObject(data);
//            List<rMapResult> rmapList = new ArrayList<>();
//            for (int i = 0; i < json.getJSONArray("bankLst").length(); i++) {
//                rMapResult rmap = new ObjectMapper().readValue(json.getJSONArray("bankLst").get(i).toString(), rMapResult.class);
//                rmapList.add(rmap);
//            }
//            rs.rMapList = rmapList;
//        } catch (JsonParseException e) {
//            e.printStackTrace();
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return rs;
//    }

}
