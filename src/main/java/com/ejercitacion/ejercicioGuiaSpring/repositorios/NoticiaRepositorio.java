/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejercitacion.ejercicioGuiaSpring.repositorios;

import com.ejercitacion.ejercicioGuiaSpring.entidades.Noticia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticiaRepositorio extends JpaRepository<Noticia, String>{
    
    @Query("SELECT n FROM Noticia n WHERE n.titulo LIKE %:titulo% AND n.alta=true")
    public List<Noticia> buscarPorParteDeTitulo(@Param("titulo") String titulo);
    
    @Query("SELECT n FROM Noticia n WHERE n.cuerpo LIKE %:cuerpo% AND n.alta=true")
    public List<Noticia> buscarPorCuerpo(@Param("cuerpo") String cuerpo);
    
    @Query("SELECT n FROM Noticia n WHERE n.titulo=:titulo")
    public Noticia buscarPorTitulo(@Param("titulo") String titulo);
    
    @Query("SELECT n FROM Noticia n WHERE n.alta=true")
    public List<Noticia> buscarTodoAlta();
    
    @Query("SELECT n FROM Noticia n WHERE n.alta=false")
    public List<Noticia> buscarTodoBaja();
    
    @Query("SELECT n FROM Noticia n WHERE n.creador.id=:id AND n.alta=true")
    public List<Noticia> buscarPorPeriodista(@Param("id") String id);
}
