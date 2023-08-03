package cz.jakvitov.wes.persistence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

@Entity
public class CityEntity {

    @EmbeddedId
    private CityId cityId;

    private String name;

    private String countryISO;

    @OneToMany(mappedBy = "city")
    private Set<UserEntity> users = new HashSet<>();

    public CityEntity() {
    }

    public String getCountryISO() {
        return countryISO;
    }

    public void setCountryISO(String countryISO) {
        this.countryISO = countryISO;
    }

    public CityId getCityId() {
        return cityId;
    }

    public void setCityId(CityId cityId) {
        this.cityId = cityId;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CityEntity{" +
                "cityId=" + cityId +
                ", name='" + name + '\'' +
                ", countryISO='" + countryISO + '\'' +
                ", users=" + users +
                '}';
    }
}
