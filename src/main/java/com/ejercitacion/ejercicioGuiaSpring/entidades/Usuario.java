/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ejercitacion.ejercicioGuiaSpring.entidades;

import com.ejercitacion.ejercicioGuiaSpring.enumeraciones.Roles;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class Usuario implements Serializable {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @NonNull
    private String nombreUsuario;
    @NonNull
    private String password;
    @NonNull
    private String email;
    @NonNull
    private LocalDate fechaAlta;
    @Enumerated(EnumType.STRING)
    @NonNull
    private Roles rol;
    @NonNull
    private boolean activo;
    @OneToOne
    private Imagen imagen;
}
