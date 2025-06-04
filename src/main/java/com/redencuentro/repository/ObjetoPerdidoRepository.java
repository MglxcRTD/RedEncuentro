package com.redencuentro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.redencuentro.entity.ObjetoPerdido;
import com.redencuentro.entity.Usuario;

public interface ObjetoPerdidoRepository extends JpaRepository<ObjetoPerdido, Long> {

    // Busca objetos perdidos por usuario y estado
    List<ObjetoPerdido> findByUsuarioAndEstado(Usuario usuario, String estado);
}
