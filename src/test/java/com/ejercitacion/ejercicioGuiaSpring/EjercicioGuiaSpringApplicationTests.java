package com.ejercitacion.ejercicioGuiaSpring;

import com.ejercitacion.ejercicioGuiaSpring.entidades.Usuario;
import com.ejercitacion.ejercicioGuiaSpring.repositorios.UsuarioRepositorio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class EjercicioGuiaSpringApplicationTests {

    @Autowired
    UsuarioRepositorio usrepo;    
    
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    
	@Test
	void contextLoads() {
            Usuario usuario=usrepo.buscarPorEmail("hola");
            System.out.println(usuario.getPassword());
            System.out.println(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("hola"));
	}

}
