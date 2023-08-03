package cz.jakvitov.wes.persistence.repository;

import cz.jakvitov.wes.persistence.entity.CityEntity;
import cz.jakvitov.wes.persistence.entity.CityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, CityId> {

    @Query("SELECT e FROM CityEntity e WHERE e.name=:cityName AND e.countryISO=:countryISO")
    public Optional<CityEntity> findCityByNameAndCountry(@Param("cityName") String cityName, @Param("countryISO") String countryISO);

}
