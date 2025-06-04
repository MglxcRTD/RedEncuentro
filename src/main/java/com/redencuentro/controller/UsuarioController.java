package com.redencuentro.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.redencuentro.entity.ObjetoPerdido;
import com.redencuentro.entity.Usuario;
import com.redencuentro.repository.ObjetoPerdidoRepository;
import com.redencuentro.repository.UsuarioRepository;
import com.redencuentro.service.CloudinaryService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ObjetoPerdidoRepository objetoPerdidoRepository;
    
    @Autowired
    private CloudinaryService cloudinaryService;

    // Mostrar formulario de registro
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    // Procesar registro
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttrs) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            redirectAttrs.addFlashAttribute("error", "Ya existe un usuario con ese email.");
            return "redirect:/registro";
        }
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuarioRepository.save(usuario);

        redirectAttrs.addFlashAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
        return "redirect:/user-login";
    }

    // Mostrar formulario login
    @GetMapping("/user-login")
    public String mostrarFormularioLogin() {
        return "login";
    }

    // Procesar login
    @PostMapping("/user-login")
    public String loginUsuario(
        @RequestParam String email,
        @RequestParam String contrasena,
        RedirectAttributes redirectAttrs,
        HttpSession session
    ) {
        Optional<Usuario> optUsuario = usuarioRepository.findByEmail(email);
        if (optUsuario.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Correo no encontrado.");
            return "redirect:/user-login";
        }
        Usuario usuario = optUsuario.get();
        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            redirectAttrs.addFlashAttribute("error", "Contraseña incorrecta.");
            return "redirect:/user-login";
        }

        session.setAttribute("usuario", usuario);
        return "redirect:/perfil"; // redirigimos a perfil
    }

    // Mostrar perfil del usuario loggeado (único método /perfil)
    @GetMapping("/perfil")
    public String verPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/user-login";
        }

        List<ObjetoPerdido> objetosPerdidos = objetoPerdidoRepository.findByUsuarioAndEstado(usuario, "perdido");
        List<ObjetoPerdido> objetosEncontrados = objetoPerdidoRepository.findByUsuarioAndEstado(usuario, "encontrado");

        model.addAttribute("usuario", usuario);
        model.addAttribute("objetosPerdidos", objetosPerdidos);
        model.addAttribute("objetosEncontrados", objetosEncontrados);

        return "perfil";
    }
    
    @GetMapping("/ajustes")
    public String mostrarAjustes(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        System.out.println("Usuario en sesión: " + usuario);
        if (usuario == null) {
            System.out.println("Usuario no logueado, redirigiendo a login.");
            return "redirect:/user-login";
        }
        model.addAttribute("usuario", usuario);
        return "ajustes";
    }
    
    @PostMapping("/ajustes/imagen")
    public String actualizarImagenPerfil(@RequestParam("imagen") MultipartFile imagen,
                                         HttpSession session,
                                         RedirectAttributes redirectAttrs) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            redirectAttrs.addFlashAttribute("error", "Debes iniciar sesión.");
            return "redirect:/user-login";
        }

        if (imagen.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Debes seleccionar una imagen.");
            return "redirect:/ajustes";
        }

        try {
            String url = cloudinaryService.subirImagen(imagen);
            usuario.setImagenPerfil(url);
            usuarioRepository.save(usuario);

            // Actualizar sesión
            session.setAttribute("usuario", usuario);

            redirectAttrs.addFlashAttribute("mensaje", "Imagen actualizada correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al subir la imagen.");
            e.printStackTrace();
        }

        return "redirect:/ajustes";
    }



    // Opcional: cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user-login";
    }
}
