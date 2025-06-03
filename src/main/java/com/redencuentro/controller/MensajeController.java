package com.redencuentro.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.redencuentro.entity.Mensaje;
import com.redencuentro.repository.MensajeRepository;

@Controller
@RequestMapping("/chat")
public class MensajeController {

    private final MensajeRepository mensajeRepository;

    public MensajeController(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    @GetMapping
    public String mostrarMensajeria(Model model) {
    	Map<String, String> avatares = new HashMap<>();
        avatares.put("user1", "/images/avatar1.png");
        avatares.put("user2", "/images/avatar2.png");

        String currentChat = "user1";

        model.addAttribute("avatares", avatares);
        model.addAttribute("currentChat", currentChat);

        return "mensajes";
    }

    @PostMapping("/api")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public Mensaje enviarMensaje(@RequestBody Mensaje mensaje) {
        mensaje.setFechaEnvio(LocalDateTime.now());
        return mensajeRepository.save(mensaje);
    }

    @GetMapping("/api/{usuario1}/{usuario2}")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public List<Mensaje> obtenerMensajes(@PathVariable String usuario1, @PathVariable String usuario2) {
        return mensajeRepository.encontrarMensajesEntreUsuarios(usuario1, usuario2);
    }

    @GetMapping("/api")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public List<Mensaje> obtenerTodosLosMensajes() {
        return mensajeRepository.findAll();
    }
}
