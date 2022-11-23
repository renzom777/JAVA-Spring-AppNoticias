/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejercitacion.ejercicioGuiaSpring.controladores;

import com.ejercitacion.ejercicioGuiaSpring.entidades.Noticia;
import com.ejercitacion.ejercicioGuiaSpring.servicios.NoticiaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMINISTRADOR', 'ROLE_PERIODISTA')")
@RequestMapping("/noticia")
public class NoticiaControlador {

    @Autowired
    NoticiaServicio notserv;
    
    @GetMapping("/mostrar/{id}")
    public String mostrarNoticia(@PathVariable String id, ModelMap modelo) {
        modelo.put("noticia", notserv.getOne(id));
        return "noticia_mostrarNoticia.html";
    }

    @GetMapping("/mostrarTodo")
    public String mostrarTodo(ModelMap modelo) {
        List<Noticia> noticias = notserv.listarTodoAlta();
        modelo.addAttribute("noticias", noticias);
        return "noticia_titulosBuscados.html";
    }

    @GetMapping("/buscarTitulo")
    public String buscarTitulo() {
        return "noticia_buscarPorTitulo.html";
    }

    @PostMapping("/buscarTitulo_")
    public String buscarTitulo_(@RequestParam String titulo, ModelMap modelo) {

        try {
            List<Noticia> noticias = notserv.buscarPorTitulo(titulo);
            modelo.addAttribute("noticias", noticias);
            return "noticia_titulosBuscados.html";

        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "noticia_buscarPorTitulo.html";
        }

    }
    
    @GetMapping("/buscarCuerpo")
    public String buscarCuerpo() {
        return "noticia_buscarPorCuerpo.html";
    }

    @PostMapping("/buscarCuerpo_")
    public String buscarCuerpo_(@RequestParam String cuerpo, ModelMap modelo) {
        try {

            List<Noticia> noticias = notserv.buscarPorCuerpo(cuerpo);
            modelo.addAttribute("noticias", noticias);
            return "noticia_titulosBuscados.html";

        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "noticia_buscarPorCuerpo.html";
        }
    }

    
}
