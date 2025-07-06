package fastclip.domain;

import java.util.List;



//整体存到redis
public class Param {
    public int state;
    public String rid;
    public String uid;
    public String roomName;
    public String description;
    public House room;
    public Usr user;
    //成员列表
    public List<Usr> userList;
    public List<Layout> layout;
    public List<House> list;
    public Result result;
    public Param(){
        this.state=200;
        //this.user=new User();
    }
}
