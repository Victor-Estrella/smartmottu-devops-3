package br.com.fiap.smartmottu.repository;


import br.com.fiap.smartmottu.entity.StatusMoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusMotoRepository extends JpaRepository<StatusMoto, Long> {
}
