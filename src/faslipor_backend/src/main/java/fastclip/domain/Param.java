package fastclip.domain;

import java.util.List;

public class Param {
    public int state;
    public String rid;
    public String roomName;
    public String description;
    public Room room;
    public User user;
    public List<Room> list;
    public Param(){
        this.state=200;
    }
}
