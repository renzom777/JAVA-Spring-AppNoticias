/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejercitacion.ejercicioGuiaSpring.controladores;

import com.ejercitacion.ejercicioGuiaSpring.entidades.Usuario;
import com.ejercitacion.ejercicioGuiaSpring.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    UsuarioServicio usserv;

    @GetMapping("/")
    public String index() {
        return "index0.html";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro.html";
    }

    @PostMapping("/registro")
    public String registro_(@RequestParam String nombreUsuario, @RequestParam String email, @RequestParam String password, 
            @RequestParam String password2, ModelMap modelo, MultipartFile archivo) {
        try {
            usserv.registrarUsuario(archivo, nombreUsuario, password, password2, email);
            modelo.put("exito", "Usuario registrado");
            modelo.put("usuario", usserv.buscarPorEmail(email));
            return "index.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombreUsuario", nombreUsuario);
            modelo.put("email", email);
            return "registro.html";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o Contraseña invalidos!");
        }
        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMINISTRADOR', 'ROLE_PERIODISTA')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado.getRol().toString().equals("ADMINISTRADOR") || (logueado.getRol().toString().equals("PERIODISTA") && logueado.isActivo())) {
            return "redirect:/admin/dashboard";
        }
        modelo.put("usuario", usserv.getOne(logueado.getId()));
        return "index.html";

    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMINISTRADOR', 'ROLE_PERIODISTA')")
    @GetMapping("/inicio/inicio")
    public String inicio_(ModelMap modelo, HttpSession session) {
        Usuario usuario=(Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usserv.getOne(usuario.getId()));
        return "index.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMINISTRADOR', 'ROLE_PERIODISTA')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session){
        Usuario usuario=(Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usserv.getOne(usuario.getId()));
        return "usuario_modificaciones.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMINISTRADOR', 'ROLE_PERIODISTA')")
    @GetMapping("/perfil/modificarNombre")
    public String modificarNombre(ModelMap modelo, HttpSession session) {
        Usuario usuario=(Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usserv.getOne(usuario.getId()));
        return "usuario_modificarNombre.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMINISTRADOR', 'ROLE_PERIODISTA')")
    @PostMapping("/perfil/modificarNombre/{id}")
    public String modificarNombre_(@PathVariable String id, @RequestParam String nombreUsuario, ModelMap modelo) {
        try {
            usserv.actualizarNombre(nombreUsuario, id);
            modelo.put("exito", "Usuario actualizado correctamente");
            modelo.put("usuario", usserv.getOne(id));
            return "index.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "usuario_modificarNombre.html";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMINISTRADOR', 'ROLE_PERIODISTA')")
    @GetMapping("/perfil/modificarContrasenia")
    public String modificarContrasenia(ModelMap modelo, HttpSession session) {
        Usuario usuario=(Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usserv.getOne(usuario.getId()));
        return "usuario_modificarContrasenia.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMINISTRADOR', 'ROLE_PERIODISTA')")
    @PostMapping("/perfil/modificarContrasenia/{id}")
    public String modificarContrasenia_(@PathVariable String id, @RequestParam String password, @RequestParam String password2, ModelMap modelo) {
        try {
            usserv.actualizarContrasenia(password, password2, id);
            modelo.put("exito", "Usuario actualizado correctamente");
            modelo.put("usuario", usserv.getOne(id));
            return "index.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "usuario_modificarContrasenia.html";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMINISTRADOR', 'ROLE_PERIODISTA')")
    @GetMapping("/perfil/modificarFoto")
    public String modificarFoto(ModelMap modelo, HttpSession session) {
        Usuario usuario=(Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usserv.getOne(usuario.getId()));
        return "usuario_modificarFoto.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMINISTRADOR', 'ROLE_PERIODISTA')")
    @PostMapping("/perfil/modificarFoto/{id}")
    public String modificarFoto_(@PathVariable String id, MultipartFile archivo, ModelMap modelo) {
        try {
            usserv.actualizarFoto(archivo, id);
            modelo.put("exito", "Usuario actualizado correctamente");
            modelo.put("usuario", usserv.getOne(id));
            return "index.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "usuario_modificarFoto.html";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMINISTRADOR', 'ROLE_PERIODISTA')")
    @GetMapping("/perfil/modificarEmail")
    public String modificarEmail(ModelMap modelo, HttpSession session) {
        Usuario usuario=(Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usserv.getOne(usuario.getId()));
        return "usuario_modificarEmail.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMINISTRADOR', 'ROLE_PERIODISTA')")
    @PostMapping("/perfil/modificarEmail/{id}")
    public String modificarEmail_(@PathVariable String id, @RequestParam String email, ModelMap modelo) {
        try {
            usserv.actualizarEmail(email, id);
            modelo.put("exito", "Usuario actualizado correctamente");
            modelo.put("usuario", usserv.getOne(id));
            return "index.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "usuario_modificarEmail.html";
        }
    }
    

}
