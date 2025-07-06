package fastclip.domain;

import java.util.Objects;

public class House {
    public String rid;
    public String name;
    public boolean state;
    public String description;
    public int stats;
    public int limit;
    public boolean lock=false;

    public boolean equals(House o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        House house = (House) o;
        return state == house.state && stats == house.stats && limit == house.limit && rid.equals(house.rid) && name.equals(house.name) && description.equals(house.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rid, name, state, description, stats, limit);
    }
}
