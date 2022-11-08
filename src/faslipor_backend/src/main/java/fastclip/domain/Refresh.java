package fastclip.domain;


import com.alibaba.fastjson.JSONObject;
import org.json.JSONString;

///event,rid,uid,lid,frame:roomBuffer[rid]
public class Refresh {
    public String event="refresh";
    public String rid;
    public String uid;
    public JSONObject frame;
}
