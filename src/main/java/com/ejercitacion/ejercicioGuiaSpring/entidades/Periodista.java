/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejercitacion.ejercicioGuiaSpring.entidades;

import com.ejercitacion.ejercicioGuiaSpring.enumeraciones.Roles;
import java.time.LocalDate;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=true)
public class Periodista extends Usuario {

    protected Integer sueldoMensual;

    public Periodista (String nombreUsuario, String password, String email, LocalDate fechaAlta, Roles rol, boolean activo, Integer sueldoMensual) {
        super(nombreUsuario, password, email, fechaAlta, rol, activo);
        this.sueldoMensual = sueldoMensual;
    }
}
