package br.com.fiap.smartmottu.repository;


import br.com.fiap.smartmottu.entity.StatusMoto;
import br.com.fiap.smartmottu.entity.enuns.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusMotoRepository extends JpaRepository<StatusMoto, Long> {

    Optional<StatusMoto> findByStatus(StatusEnum status);

}