package fastclip.domain;

import java.util.List;

public class Result {
    public String event;  //sync
    public Param params;
    public int state=200;
    public Result(){
        this.params=new Param();
    }
    public List<House> data;
}
