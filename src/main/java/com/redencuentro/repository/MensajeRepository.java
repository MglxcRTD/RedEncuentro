package com.redencuentro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.redencuentro.entity.Mensaje;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    @Query("SELECT m FROM Mensaje m WHERE (m.emisor = :usuario1 AND m.receptor = :usuario2) OR (m.emisor = :usuario2 AND m.receptor = :usuario1)")
    List<Mensaje> encontrarMensajesEntreUsuarios(@Param("usuario1") String usuario1, @Param("usuario2") String usuario2);
}
