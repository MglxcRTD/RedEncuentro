package com.redencuentro.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Mensaje {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String emisor;
	private String receptor;

	@Column(length = 2000)
	private String contenido;

	private boolean esAudio;

	private LocalDateTime fechaEnvio;

	public Mensaje() {
	}

	public Mensaje(String emisor, String receptor, String contenido, boolean esAudio, LocalDateTime fechaEnvio) {
		this.emisor = emisor;
		this.receptor = receptor;
		this.contenido = contenido;
		this.esAudio = esAudio;
		this.fechaEnvio = fechaEnvio;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmisor() {
		return emisor;
	}

	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	public String getReceptor() {
		return receptor;
	}

	public void setReceptor(String receptor) {
		this.receptor = receptor;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public boolean isEsAudio() {
		return esAudio;
	}

	public void setEsAudio(boolean esAudio) {
		this.esAudio = esAudio;
	}

	public LocalDateTime getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(LocalDateTime fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	
}
