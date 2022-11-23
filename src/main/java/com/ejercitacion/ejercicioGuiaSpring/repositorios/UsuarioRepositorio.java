/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ejercitacion.ejercicioGuiaSpring.repositorios;

import com.ejercitacion.ejercicioGuiaSpring.entidades.Periodista;
import com.ejercitacion.ejercicioGuiaSpring.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
    
    @Query("SELECT u FROM Usuario u WHERE u.email=:email")
    public Usuario buscarPorEmail(@Param("email") String email);
    
    @Query("SELECT u.nombreUsuario FROM Usuario u")
    public List<String> listarNombres();
    
    @Query("SELECT u.email FROM Usuario u")
    public List<String> listarEmails();
    
    @Query("SELECT u FROM Usuario u WHERE u.rol='PERIODISTA' AND u.activo=true")
    public List<Usuario> buscarPeriodistasAlta();
    
    @Query("SELECT u FROM Usuario u WHERE rol='PERIODISTA' AND u.activo=false")
    public List<Usuario> buscarPeriodistasBaja();
    
    @Query("SELECT p FROM Usuario p WHERE p.id=:id")
    public Periodista buscarPeriodista(@Param("id") String id);
}
