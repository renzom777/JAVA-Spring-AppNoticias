/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejercitacion.ejercicioGuiaSpring.servicios;

import com.ejercitacion.ejercicioGuiaSpring.entidades.Noticia;
import com.ejercitacion.ejercicioGuiaSpring.entidades.Usuario;
import com.ejercitacion.ejercicioGuiaSpring.repositorios.NoticiaRepositorio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
public class NoticiaServicio {

    @Autowired
    private NoticiaRepositorio notrepo;

    @Autowired
    private UsuarioServicio usserv;
    
    public static Comparator<Noticia> compararFecha = new Comparator<Noticia>() {
        @Override
        public int compare(Noticia n1, Noticia n2) {
            return n2.getFecha().compareTo(n1.getFecha());
        }
    };

    @Transactional
    public void crearNoticia(String titulo, String cuerpo, String id) throws Exception {

        if (titulo == null || titulo == "") {
            throw new Exception("El titulo no puede ser nulo");
        }

        if (cuerpo == null || cuerpo == "") {
            throw new Exception("El cuerpo no puede ser nulo");
        }

        int letrasCuerpo = cuerpo.length();
        String parteCuerpo;
        if (letrasCuerpo <= 100) {
            parteCuerpo = cuerpo.substring(0, letrasCuerpo);
        } else {
            parteCuerpo = cuerpo.substring(0, 100).concat("...");
        }

        Usuario creador = usserv.getOne(id);
        LocalDate fecha = LocalDate.now();
        Noticia noticia = new Noticia(titulo, cuerpo, true, parteCuerpo, fecha, creador);
        notrepo.save(noticia);
    }

    public List<Noticia> buscarPorTitulo(String titulo) throws Exception {

        if (titulo == null || titulo == "") {
            throw new Exception("El titulo no puede ser nulo");
        }

        List<Noticia> noticias = new ArrayList();
        noticias = notrepo.buscarPorParteDeTitulo(titulo);
        noticias.sort(compararFecha);
        return noticias;
    }

    public List<Noticia> buscarPorCuerpo(String cuerpo) throws Exception {

        if (cuerpo == null || cuerpo == "") {
            throw new Exception("El texto no puede ser nulo");
        }

        List<Noticia> noticias = new ArrayList();
        noticias = notrepo.buscarPorCuerpo(cuerpo);
        noticias.sort(compararFecha);
        return noticias;
    }

    @Transactional
    public void modificarTitulo(String Id, String tituloNuevo) throws Exception {

        if (tituloNuevo == null || tituloNuevo == "") {
            throw new Exception("El titulo no puede ser nulo");
        }

        Optional<Noticia> respuesta;
        respuesta = notrepo.findById(Id);
        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();
            noticia.setTitulo(tituloNuevo);
            notrepo.save(noticia);
        }
    }

    @Transactional
    public void modificarCuerpo(String Id, String cuerpoNuevo) throws Exception {

        if (cuerpoNuevo == null || cuerpoNuevo == "") {
            throw new Exception("El cuerpo no puede ser nulo");
        }

        Optional<Noticia> respuesta;
        respuesta = notrepo.findById(Id);
        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();
            noticia.setCuerpo(cuerpoNuevo);
            int letrasCuerpo = cuerpoNuevo.length();
            String parteCuerpo;
            if (letrasCuerpo <= 100) {
                parteCuerpo = cuerpoNuevo.substring(0, letrasCuerpo);
            } else {
                parteCuerpo = cuerpoNuevo.substring(0, 100).concat("...");
            }
            noticia.setParteCuerpo(parteCuerpo);
            notrepo.save(noticia);
        }
    }

    @Transactional
    public void darDeBaja(String titulo) throws Exception {

        if (titulo == null || titulo == "") {
            throw new Exception("El titulo no puede ser nulo");
        }

        Noticia noticia = notrepo.buscarPorTitulo(titulo);

        if (noticia.isAlta()) {
            noticia.setAlta(false);
            notrepo.save(noticia);
        }

    }

    @Transactional
    public void darDeAlta(String titulo) throws Exception {

        if (titulo == null || titulo == "") {
            throw new Exception("El titulo no puede ser nulo");
        }

        Noticia noticia = notrepo.buscarPorTitulo(titulo);

        if (!noticia.isAlta()) {
            noticia.setAlta(true);
            notrepo.save(noticia);
        }

    }

    @Transactional
    @Modifying
    public void eliminarNoticia(String id) {
        notrepo.deleteById(id);
    }

    public Noticia getOne(String id) {
        return notrepo.getOne(id);
    }

    public List<Noticia> listarTodoAlta() {
        List<Noticia> noticias = notrepo.buscarTodoAlta();
        noticias.sort(compararFecha);
        return noticias;
    }

    public List<Noticia> listarTodoBaja() {
        return notrepo.buscarTodoBaja();
    }

    public List<Noticia> listarTodo() {
        List<Noticia> noticias = notrepo.findAll();
        noticias.sort(compararFecha);
        return noticias;
    }

    public List<Noticia> listarPorPeriodista(String id) {
        return notrepo.buscarPorPeriodista(id);
    }

}
