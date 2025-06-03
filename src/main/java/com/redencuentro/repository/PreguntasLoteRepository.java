// src/main/java/com/redencuentro/repository/PreguntasLoteRepository.java
package com.redencuentro.repository;

import com.redencuentro.entity.PreguntasLote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreguntasLoteRepository extends JpaRepository<PreguntasLote, Long> {
    // Podrías agregar, por ejemplo:
    // Optional<PreguntasLote> findByObjetoId(Long objetoId);
}
