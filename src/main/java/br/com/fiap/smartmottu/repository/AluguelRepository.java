package br.com.fiap.smartmottu.repository;

import br.com.fiap.smartmottu.entity.Aluguel;
import br.com.fiap.smartmottu.entity.enuns.StatusAluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {

    @Query("SELECT a FROM Aluguel a WHERE a.usuario.idUsuario = :usuarioId AND a.statusAluguel = :status")
    Optional<Aluguel> findAluguelAtivoByUsuarioId(
            @Param("usuarioId") Long usuarioId,
            @Param("status") StatusAluguel status
    );

    boolean existsByUsuario_IdUsuario(Long usuarioId);

}