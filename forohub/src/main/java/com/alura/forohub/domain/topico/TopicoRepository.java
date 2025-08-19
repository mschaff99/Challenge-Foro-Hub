package com.alura.forohub.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    
    boolean existsByTituloAndMensaje(String titulo, String mensaje);
    
    @Query("SELECT t FROM Topico t ORDER BY t.fechaCreacion ASC")
    Page<Topico> findAllOrderByFechaCreacionAsc(Pageable pageable);
}
