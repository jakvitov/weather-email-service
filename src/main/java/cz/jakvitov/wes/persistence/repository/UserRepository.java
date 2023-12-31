package cz.jakvitov.wes.persistence.repository;

import cz.jakvitov.wes.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT e FROM UserEntity e WHERE e.active=true")
    public List<UserEntity> findAllActiveUsers();

    public List<UserEntity> findAllByEmail(String email);

    public List<UserEntity> findUserEntityByEmail(String email);

    public List<UserEntity> findUserEntityByDeactivationCode(String deactivationCode);

    public List<UserEntity>  findUserEntityByActivationCode(String activationCode);

}
