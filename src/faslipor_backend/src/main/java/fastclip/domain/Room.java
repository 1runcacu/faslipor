package fastclip.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Room {
    private String rid;
    private String name;
    private boolean state;
    private String description;
    private int stats;
    private int limit;
    private List<String> nameList;

    public String getId() {
        return rid;
    }

    public void setId(String id) {
        this.rid = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return description;
    }

    public void setMsg(String msg) {
        this.description = msg;
    }

    public List<String> getNameList() {
        return nameList;
    }

    public void addName(String uid) {
        if(nameList==null)
            nameList=new ArrayList<>();
        nameList.add(uid);
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getStats() {
        return stats;
    }

    public void setStats(int stats) {
        this.stats = stats;
    }
}
