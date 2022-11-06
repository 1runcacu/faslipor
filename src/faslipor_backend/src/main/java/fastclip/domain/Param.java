package fastclip.domain;

import java.util.List;

public class Param {
    public int state;
    public String rid;
    public String uid;
    public String roomName;
    public String description;
    public House room;
    public User user;
    public List<House> list;
    public Result result;
    public Param(){
        this.state=200;
    }
}
