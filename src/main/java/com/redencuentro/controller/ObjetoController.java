package com.redencuentro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.redencuentro.entity.ObjetoPerdido;
import com.redencuentro.repository.ObjetoPerdidoRepository;

@Controller
public class ObjetoController {

	@Autowired
	private ObjetoPerdidoRepository repositorio;

	@GetMapping("/")
	public String inicio(Model model) {
		model.addAttribute("objetos", repositorio.findAll());
		return "inicio";
	}

	@GetMapping("/nuevo")
	public String mostrarFormulario(Model model) {
		model.addAttribute("objeto", new ObjetoPerdido());
		return "formulario";
	}

	@PostMapping("/guardar")
	public String guardarObjeto(@ModelAttribute ObjetoPerdido objeto, RedirectAttributes redirectAttrs) {
	    try {
	        repositorio.save(objeto);
	        redirectAttrs.addFlashAttribute("mensaje", "Objeto guardado correctamente!");
	    } catch (Exception e) {
	        redirectAttrs.addFlashAttribute("mensaje", "Error al guardar el objeto: " + e.getMessage());
	    }
	    return "redirect:/nuevo";
	}

	


}
