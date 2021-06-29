package parameters;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Person implements Serializable {
    private LocalDateTime birthday; //Поле не может быть null
    private Color hairColor; //Поле не может быть null
    private Location location; //Поле может быть null

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
