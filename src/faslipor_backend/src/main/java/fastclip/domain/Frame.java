package fastclip.domain;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class Frame {
    /*frame:{String gid,String type, style:{int left,int top,int radius}*/
    public String type;
    public List<JSONObject> params;
}
