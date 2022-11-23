/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejercitacion.ejercicioGuiaSpring.controladores;

import com.ejercitacion.ejercicioGuiaSpring.entidades.Noticia;
import com.ejercitacion.ejercicioGuiaSpring.entidades.Usuario;
import com.ejercitacion.ejercicioGuiaSpring.servicios.NoticiaServicio;
import com.ejercitacion.ejercicioGuiaSpring.servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    NoticiaServicio notserv;
    
    @Autowired
    UsuarioServicio usserv;

    @GetMapping("/dashboard")
    public String modificaciones() {
        return "index_Modificador.html";
    }

    @GetMapping("/crear")
    public String crearNoticia() {
        return "noticia_crear.html";
    }

    @PostMapping("/crear_")
    public String crearNoticia_(@RequestParam String titulo, @RequestParam String cuerpo, @RequestParam String id, ModelMap modelo) {
        try {
            notserv.crearNoticia(titulo, cuerpo, id);
            modelo.put("exito", "La noticia se creó exitosamente");
            return "index_Modificador.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "noticia_crear.html";
        }
    }

    @GetMapping("/listaModificar")
    public String listaModificar(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuario.getRol().toString().equals("ADMINISTRADOR")) {
            List<Noticia> noticias = notserv.listarTodo();
            modelo.addAttribute("noticias", noticias);
            return "noticia_listaModificar.html";
        } else {
            List<Noticia> noticias=notserv.listarPorPeriodista(usuario.getId());
            modelo.addAttribute("noticias", noticias);
            return "noticia_listaModificar.html";
        }
    }

    @GetMapping("/modificarTitulo/{id}")
    public String modificarTitulo(@PathVariable String id, ModelMap modelo) {
        modelo.put("noticia", notserv.getOne(id));
        return "noticia_modificarTitulo.html";
    }

    @GetMapping("/modificarCuerpo/{id}")
    public String modificarCuerpo(@PathVariable String id, ModelMap modelo) {
        modelo.put("noticia", notserv.getOne(id));
        return "noticia_modificarCuerpo.html";
    }

    @PostMapping("/modificarTitulo/{id}")
    public String modificarTitulo_(@PathVariable String id, @RequestParam String tituloNuevo, ModelMap modelo) {
        try {
            notserv.modificarTitulo(id, tituloNuevo);
            modelo.put("exito", "El titulo se modificó exitosamente");
            return "index_Modificador.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "noticia_modificarTitulo.html";
        }
    }

    @PostMapping("/modificarCuerpo/{id}")
    public String modificarCuerpo_(@PathVariable String id, @RequestParam String cuerpo, ModelMap modelo) {
        try {
            notserv.modificarCuerpo(id, cuerpo);
            modelo.put("exito", "El cuerpo se modificó exitosamente");
            return "index_Modificador.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "noticia_modificarCuerpo.html";
        }
    }

    @GetMapping("/darBaja")
    public String darBajaNoticia(ModelMap modelo) {
        List<Noticia> noticias = notserv.listarTodoAlta();
        modelo.addAttribute("noticias", noticias);
        modelo.put("action", "baja");
        return "noticia_darBajaAlta.html";
    }

    @GetMapping("/darAlta")
    public String darAltaNoticia(ModelMap modelo) {
        List<Noticia> noticias = notserv.listarTodoBaja();
        modelo.addAttribute("noticias", noticias);
        modelo.put("action", "alta");
        return "noticia_darBajaAlta.html";
    }

    @PostMapping("/darBaja_")
    public String darBajaNoticia_(@RequestParam String titulo, ModelMap modelo) {
        try {
            notserv.darDeBaja(titulo);
            modelo.put("exito", "Noticia dada de baja exitosamente");
            return "index_Modificador.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "noticia_darBajaAlta.html";
        }
    }

    @PostMapping("/darAlta_")
    public String darAltaNoticia_(@RequestParam String titulo, ModelMap modelo) {
        try {
            notserv.darDeAlta(titulo);
            modelo.put("exito", "Noticia dada de alta exitosamente");
            return "index_Modificador.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "noticia_darBajaAlta.html";
        }
    }

    @GetMapping("/eliminar")
    public String eliminar(ModelMap modelo) {
        List<Noticia> noticias = notserv.listarTodo();
        modelo.addAttribute("noticias", noticias);
        return "noticia_eliminar.html";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarNoticia(@PathVariable String id, ModelMap modelo) {
        Noticia noticia = notserv.getOne(id);
        modelo.put("noticia", noticia);
        return "noticia_eliminarConfirmacion.html";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarNoticia_(@PathVariable String id, ModelMap modelo) {
        try {
            notserv.eliminarNoticia(id);
            modelo.put("exito", "La noticia se eliminó correctamente");
            return "index_Modificador.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "noticia_eliminar.html";
        }
    }
    
    @GetMapping("/fijarSueldo")
    public String fijarSueldo(ModelMap modelo) {
        List<Usuario> usuarios=usserv.listarPeriodistasAlta();
        modelo.addAttribute("usuarios", usuarios);
        return "seleccionFijarSueldo.html";
    }
    
    @PostMapping("/fijarSueldo")
    public String fijarSueldo_(@RequestParam String id, @RequestParam Integer sueldo, ModelMap modelo) {
        try {
            usserv.fijarSueldo(id, sueldo);
            modelo.put("exito", "El sueldo se modificó correctamente");
            return "index_Modificador.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "seleccionFijarSueldo.html";
        }
    }
    
    @GetMapping("/darDeBajaPeriodista")
    public String darBajaPeriodista(ModelMap modelo) {
        List<Usuario> usuarios = usserv.listarPeriodistasAlta();
        modelo.addAttribute("usuarios", usuarios);
        modelo.put("action", "baja");
        return "darBajaAltaPeriodistas.html";
    }
    
    @GetMapping("/darDeAltaPeriodista")
    public String darDeAltaPeriodista(ModelMap modelo) {
        List<Usuario> usuarios = usserv.listarPeriodistasBaja();
        modelo.addAttribute("usuarios", usuarios);
        modelo.put("action", "alta");
        return "darBajaAltaPeriodistas.html";
    }
    
    @PostMapping("/darDeBajaPeriodista_")
    public String darDeBajaeriodista_(@RequestParam String id, ModelMap modelo) {
        try {
            usserv.darDeBajaPeriodista(id);
            modelo.put("exito", "Periodista dado de baja exitosamente");
            return "index_Modificador.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "darBajaAltaPeriodistas.html";
        }
    }

    @PostMapping("/darDeAltaPeriodista_")
    public String darDeAltaPeriodista_(@RequestParam String id, ModelMap modelo) {
        try {
            usserv.darDeAltaPeriodista(id);
            modelo.put("exito", "Periodista dado de alta exitosamente");
            return "index_Modificador.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "darBajaAltaPeriodistas.html";
        }
    }
    
}
