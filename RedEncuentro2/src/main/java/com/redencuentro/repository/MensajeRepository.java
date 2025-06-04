package com.redencuentro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.redencuentro.entity.Mensaje;
import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    @Query("SELECT m FROM Mensaje m WHERE " +
           "(m.emisor = :user1 AND m.receptor = :user2) OR " +
           "(m.emisor = :user2 AND m.receptor = :user1) " +
           "ORDER BY m.fechaEnvio ASC")
    List<Mensaje> encontrarMensajesEntreUsuarios(@Param("user1") String user1, @Param("user2") String user2);
    @Query("SELECT DISTINCT CASE WHEN m.emisor = :usuario THEN m.receptor ELSE m.emisor END " +
    	       "FROM Mensaje m " +
    	       "WHERE m.emisor = :usuario OR m.receptor = :usuario")
    	List<String> encontrarContactosDelUsuario(@Param("usuario") String usuario);

}
