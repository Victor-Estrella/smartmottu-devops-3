package br.com.fiap.smartmottu.repository;

import br.com.fiap.smartmottu.entity.Moto;
import br.com.fiap.smartmottu.entity.enuns.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotoRepository extends JpaRepository<Moto, Long>, JpaSpecificationExecutor<Moto> {

    List<Moto> findByStatus_Status(StatusEnum status);

    List<Moto> findByUsuario_IdUsuario(Long usuarioId);

}
