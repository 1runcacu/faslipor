package fastclip.domain;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class  Frame {
    /*frame:{String gid,String type, style:{int left,int top,int radius}*/
    public String type;
    public String name;
    public JSONObject pixel;
    public String file;
    public String lock;
    public String uid;
    public String date;
    public String message;
    public List<Frame> data;
}
