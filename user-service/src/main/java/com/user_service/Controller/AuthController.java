package com.user_service.Controller;

import com.user_service.Dtos.RegisterRequest;
import com.user_service.Dtos.RegisterResponse;
import com.user_service.Entity.Usuario;
import com.user_service.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            UsuarioService usuarioService = null;
            Usuario usuarioCreado = usuarioService.registrarUsuario(
                    request.getNombre(),
                    request.getApellido(),
                    request.getDni(),
                    request.getEmail(),
                    request.getTelefono(),
                    request.getPassword()
            );

            // Crear respuesta sin datos sensibles
            RegisterResponse response = new RegisterResponse(
                    usuarioCreado.getId(),
                    usuarioCreado.getEmail(),
                    usuarioCreado.getCvu(),
                    usuarioCreado.getAlias()
            );

            return ResponseEntity.ok(response);

        }catch (Exception e) {
            // Error interno del servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
}
