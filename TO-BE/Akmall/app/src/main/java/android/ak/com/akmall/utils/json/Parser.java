package android.ak.com.akmall.utils.json;

/**
 * Created by gkduuu on 2016-11-15.
 * 서버 결과 파싱해줌
 */
public class Parser {

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
