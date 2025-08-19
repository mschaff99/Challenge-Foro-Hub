package com.alura.forohub.controller;

import com.alura.forohub.domain.usuario.DatosAutenticacionUsuario;
import com.alura.forohub.domain.usuario.Usuario;
import com.alura.forohub.infra.security.DatosJWTToken;
import com.alura.forohub.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity autenticarUsuario(@RequestBody DatosAutenticacionUsuario datosAutenticacionUsuario) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(
                    datosAutenticacionUsuario.login(),
                    datosAutenticacionUsuario.clave()
            );
            var authentication = authenticationManager.authenticate(authToken);
            
            var JWTtoken = tokenService.generarToken((Usuario) authentication.getPrincipal());
            return ResponseEntity.ok(new DatosJWTToken(JWTtoken));
        } catch (Exception e) {
            System.out.println("Error en autenticación: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error en autenticación: " + e.getMessage());
        }
    }
}
