package vos.hoteldemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "age")
public class Age {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "age_id")
    private int ageID;

    @Column(name = "name")
    private String name;

    @Column(name = "min_age")
    private int minAge;

    @Column(name = "max_age")
    private int maxAge;

    public Age() {
    }

    public Age(int ageID, String name, int minAge, int maxAge) {
        this.ageID = ageID;
        this.name = name;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public int getAgeID() {
        return ageID;
    }

    public void setAgeID(int ageID) {
        this.ageID = ageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
}
