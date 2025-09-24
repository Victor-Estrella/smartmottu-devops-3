package br.com.fiap.smartmottu.repository;

import br.com.fiap.smartmottu.entity.TipoMoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoMotoRepository extends JpaRepository<TipoMoto, Long> {
}
