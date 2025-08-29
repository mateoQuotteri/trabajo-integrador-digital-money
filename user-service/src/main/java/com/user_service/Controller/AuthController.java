package com.user_service.Controller;

import com.user_service.Dtos.RegisterRequest;
import com.user_service.Dtos.RegisterResponse;
import com.user_service.Entity.Usuario;
import com.user_service.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth") 
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
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

        } catch (IllegalArgumentException e) {
            // Manejar errores específicos de validación
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Log del error para debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }
}