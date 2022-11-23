/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejercitacion.ejercicioGuiaSpring.servicios;

import com.ejercitacion.ejercicioGuiaSpring.entidades.Imagen;
import com.ejercitacion.ejercicioGuiaSpring.entidades.Periodista;
import com.ejercitacion.ejercicioGuiaSpring.entidades.Usuario;
import com.ejercitacion.ejercicioGuiaSpring.enumeraciones.Roles;
import com.ejercitacion.ejercicioGuiaSpring.repositorios.UsuarioRepositorio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    UsuarioRepositorio usrepo;
    
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ImagenServicio imserv;

    @Transactional
    public void registrarUsuario(MultipartFile archivo, String nombreUsuario, String password, String password2, String email) throws Exception {

        validar(nombreUsuario, password, password2, email);
        Imagen imagen=imserv.guardar(archivo);
        
        if (nombreUsuario.toLowerCase().contains(".periodista")) {
            Periodista periodista=new Periodista(nombreUsuario, passwordEncoder.encode(password), email, LocalDate.now(), Roles.PERIODISTA, true, 0);
            periodista.setImagen(imagen);
            usrepo.save(periodista);
        } else if (nombreUsuario.toLowerCase().contains(".admin")){
            Usuario admin=new Usuario(nombreUsuario, passwordEncoder.encode(password), email, LocalDate.now(), Roles.ADMINISTRADOR, true);
            admin.setImagen(imagen);
            usrepo.save(admin);
        } else {
            Usuario usuario=new Usuario(nombreUsuario, passwordEncoder.encode(password), email, LocalDate.now(), Roles.USUARIO, true);
            usuario.setImagen(imagen);
            usrepo.save(usuario);
        }
    }
    
    @Transactional
    public void actualizar(MultipartFile archivo, String idUsuario, String nombreUsuario, String email, String password, String password2) throws Exception {

        validar(nombreUsuario, password, password2, email);

        Optional<Usuario> respuesta = usrepo.findById(idUsuario);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setEmail(email);
            usuario.setPassword(passwordEncoder.encode(password));
            String idImagen = null;
            
            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }
            
            Imagen imagen = imserv.actualizar(archivo, idImagen);
            usuario.setImagen(imagen);
            usrepo.save(usuario);
        }
    }

    public void validar(String nombreUsuario, String password, String password2, String email) throws Exception {

        if (nombreUsuario.isEmpty() || nombreUsuario == null) {
            throw new Exception("El nombre de usuario no puede ser nulo");
        }

        if (password.isEmpty() || password == null) {
            throw new Exception("La contraseña no puede ser nula");
        }

        if (email.isEmpty() || email == null) {
            throw new Exception("La direccion de correo no puede ser nula");
        }
        
        if (!email.contains("@")) {
            throw new Exception("La direccion de correo no tiene la forma correcta");
        }
        
        if (password.length()<6) {
            throw new Exception("La contraseña debe tener 6 o mas caracteres");
        }

        if (!password.equals(password2)) {
            throw new Exception("Ambas contraseñas ingresadas deben coincidir");
        }
        
        if (usrepo.listarEmails().contains(email)) {
            throw new Exception("El email ingresado ya existe");
        }
        
        if (usrepo.listarNombres().contains(nombreUsuario)) {
            throw new Exception("El nombre ingresado ya existe");
        }
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usrepo.buscarPorEmail(email);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p;
            if (usuario.isActivo()) {
                p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            } else {
                p = new SimpleGrantedAuthority("ROLE_USUARIO");
            }
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }
    
    public Usuario getOne(String id) {
        return usrepo.getOne(id);
    }
    
    @Transactional
    public void darDeBajaPeriodista(String id) {
        Usuario usuario=usrepo.getOne(id);
        usuario.setActivo(false);
        usrepo.save(usuario);
    }
    
    @Transactional
    public void darDeAltaPeriodista(String id) {
        Usuario usuario=usrepo.getOne(id);
        usuario.setActivo(true);
        usrepo.save(usuario);
    }
    
    @Transactional
    public void fijarSueldo (String id, Integer sueldo) throws Exception {
        Periodista periodista=usrepo.buscarPeriodista(id);
        if (sueldo==null || sueldo<0) {
            throw new Exception("El sueldo fijado no puede ser nulo o negativo");
        }
        periodista.setSueldoMensual(sueldo);
        usrepo.save(periodista);
    }
    
    public List<Usuario> listarPeriodistasAlta() {
        return usrepo.buscarPeriodistasAlta();
    }
    
    public List<Usuario> listarPeriodistasBaja() {
        return usrepo.buscarPeriodistasBaja();
    }

}
