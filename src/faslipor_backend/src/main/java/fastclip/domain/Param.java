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
    //成员列表
    public List<User> userList;
    public List<Layout> layouts;
    public List<House> list;
    public Result result;
    public Param(){
        this.state=200;
    }
}
