package com.redencuentro.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id_usuario;

    private String nombre;

    @Column(unique = true)
    private String email;

    @Column(name = "contraseña")  // Mantiene la ñ en la DB
    private String contrasena;      // Sin ñ en Java

    // Getters y setters
    public Long getId() { return id_usuario; }

    public void setId(Long id) { this.id_usuario = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }

    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
