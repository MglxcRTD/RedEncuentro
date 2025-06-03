package com.redencuentro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "preguntas_lote")
public class PreguntasLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación: cada PreguntasLote pertenece a un único ObjetoPerdido
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objeto_id", nullable = false, unique = true)
    private ObjetoPerdido objeto; 

    @Column(columnDefinition = "TEXT", nullable = false)
    private String pregunta1;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String pregunta2;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String pregunta3;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ObjetoPerdido getObjeto() { return objeto; }
    public void setObjeto(ObjetoPerdido objeto) { this.objeto = objeto; }

    public String getPregunta1() { return pregunta1; }
    public void setPregunta1(String pregunta1) { this.pregunta1 = pregunta1; }

    public String getPregunta2() { return pregunta2; }
    public void setPregunta2(String pregunta2) { this.pregunta2 = pregunta2; }

    public String getPregunta3() { return pregunta3; }
    public void setPregunta3(String pregunta3) { this.pregunta3 = pregunta3; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}

