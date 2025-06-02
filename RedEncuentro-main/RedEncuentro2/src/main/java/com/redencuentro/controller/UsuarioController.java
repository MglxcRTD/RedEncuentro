package com.redencuentro.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.redencuentro.entity.Usuario;
import com.redencuentro.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

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

    @GetMapping("/user-login")
    public String mostrarFormularioLogin() {
        return "login"; // templates/login.html
    }

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

        session.setAttribute("usuario", usuario); // Sesión iniciada
        return "redirect:/"; // A donde quieras que vayan
    }
}