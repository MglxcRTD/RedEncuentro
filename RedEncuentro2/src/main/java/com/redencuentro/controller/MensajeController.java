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
        avatares.put("Juan", "https://randomuser.me/api/portraits/men/10.jpg");
        avatares.put("María", "https://randomuser.me/api/portraits/women/20.jpg");
        avatares.put("Carlos", "https://randomuser.me/api/portraits/men/30.jpg");

        String currentChat = "Juan"; // Puedes ajustarlo
        String usuarioActual = "Yo";

        // Lista de mensajes entre 'Yo' y el chat actual
        List<Mensaje> mensajes = mensajeRepository.encontrarMensajesEntreUsuarios(usuarioActual, currentChat);

        // Últimos mensajes por contacto
        Map<String, String> lastMessages = new HashMap<>();
        for (String contacto : avatares.keySet()) {
            List<Mensaje> msgs = mensajeRepository.encontrarMensajesEntreUsuarios(usuarioActual, contacto);
            String last = msgs.isEmpty() ? "" : msgs.get(msgs.size() - 1).getContenido();
            lastMessages.put(contacto, last);
        }

        // Datos del perfil
        Map<String, Map<String, String>> profileData = new HashMap<>();

        Map<String, String> p1 = new HashMap<>();
        p1.put("stars", "4");
        p1.put("created", "15/03/2019");
        p1.put("objectDesc", "Buscando bicicleta de montaña en buen estado.");
        profileData.put("Juan", p1);

        Map<String, String> p2 = new HashMap<>();
        p2.put("stars", "5");
        p2.put("created", "22/11/2021");
        p2.put("objectDesc", "Ofrece clases de guitarra para principiantes.");
        profileData.put("María", p2);

        Map<String, String> p3 = new HashMap<>();
        p3.put("stars", "3");
        p3.put("created", "10/07/2018");
        p3.put("objectDesc", "Vendo cámara réflex usada, excelente estado.");
        profileData.put("Carlos", p3);

        // Añadir al modelo
        model.addAttribute("avatares", avatares);
        model.addAttribute("currentChat", currentChat);
        model.addAttribute("contactos", avatares.keySet());
        model.addAttribute("lastMessages", lastMessages);
        model.addAttribute("mensajes", mensajes);
        model.addAttribute("usuarioActual", usuarioActual);
        model.addAttribute("modalCurrent", currentChat);
        model.addAttribute("profileData", profileData);

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
