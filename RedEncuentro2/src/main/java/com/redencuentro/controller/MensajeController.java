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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.redencuentro.entity.Mensaje;
import com.redencuentro.repository.MensajeRepository;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/mensajes")
public class MensajeController {

    private final MensajeRepository mensajeRepository;

    public MensajeController(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    @GetMapping
    public String mostrarMensajeria(
        @RequestParam(name = "usuario", required = false) String usuarioActual,
        @RequestParam(name = "chat", required = false) String currentChat,
        Model model
    ) {
        if (usuarioActual == null || usuarioActual.isEmpty()) {
            usuarioActual = "Yo"; // o redirigir a login, según tu lógica real
        }

        // Obtener contactos reales
        List<String> contactos = mensajeRepository.encontrarContactosDelUsuario(usuarioActual);

        // Cargar mensajes solo si hay un chat seleccionado
        List<Mensaje> mensajes = (currentChat == null || currentChat.isEmpty())
                                 ? List.of()
                                 : mensajeRepository.encontrarMensajesEntreUsuarios(usuarioActual, currentChat);

        // Para mostrar el último mensaje de cada contacto (puedes refinar esta parte)
        Map<String, String> lastMessages = new HashMap<>();
        for (String contacto : contactos) {
            List<Mensaje> msgs = mensajeRepository.encontrarMensajesEntreUsuarios(usuarioActual, contacto);
            String last = msgs.isEmpty() ? "" : msgs.get(msgs.size() - 1).getContenido();
            lastMessages.put(contacto, last);
        }

        // Puedes tener un método para obtener avatares si quieres, o usar placeholders dinámicos
        Map<String, String> avatares = new HashMap<>();
        for (String contacto : contactos) {
            avatares.put(contacto, "https://ui-avatars.com/api/?name=" + contacto); // avatar simple con iniciales
        }

        model.addAttribute("avatares", avatares);
        model.addAttribute("currentChat", currentChat);
        model.addAttribute("contactos", contactos);
        model.addAttribute("lastMessages", lastMessages);
        model.addAttribute("mensajes", mensajes);
        model.addAttribute("usuarioActual", usuarioActual);
        model.addAttribute("modalCurrent", currentChat);

        return "mensajes";
    }


    @PostMapping("/api")
    @ResponseBody
    @CrossOrigin(origins = "*")
    @Transactional  // recomendable para asegurar transacción
    public Mensaje enviarMensaje(@RequestBody Mensaje mensaje) {
        mensaje.setFechaEnvio(LocalDateTime.now());

        Mensaje guardado = mensajeRepository.save(mensaje); // guardas
        mensajeRepository.flush(); // aquí fuerzas que Hibernate haga commit y sincronice con BD inmediatamente

        System.out.println("Mensaje guardado con ID: " + guardado.getId());
        return guardado;
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
