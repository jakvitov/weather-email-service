package cz.jakvitov.wes.persistence.repository;

import cz.jakvitov.wes.persistence.entity.MonitoredError;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoredErrorRepository extends JpaRepository<MonitoredError, Long>{}
