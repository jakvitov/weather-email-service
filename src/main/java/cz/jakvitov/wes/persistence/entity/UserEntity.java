package cz.jakvitov.wes.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String email;

    private String password;

    private LocalDateTime changed;

    private Boolean active;

    private String activationCode;

    private String deactivationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "city_latitude", referencedColumnName = "latitude"),
            @JoinColumn(name = "city_longitude", referencedColumnName = "longitude")
    })
    private CityEntity city;

    public UserEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getChanged() {
        return changed;
    }

    public void setChanged(LocalDateTime changed) {
        this.changed = changed;
    }

    public Boolean getActive() {
        return active;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getDeactivationCode() {
        return deactivationCode;
    }

    public void setDeactivationCode(String deactivationCode) {
        this.deactivationCode = deactivationCode;
    }
}
