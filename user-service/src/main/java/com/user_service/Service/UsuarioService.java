package com.user_service.Service;

import com.user_service.Entity.Usuario;
import com.user_service.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Lista de palabras para generar alias
    private static final List<String> PALABRAS = Arrays.asList(
            "sol", "luna", "estrella", "mar", "rio", "monte", "valle", "bosque",
            "flor", "arbol", "piedra", "viento", "fuego", "agua", "tierra", "cielo"
    );

    private final Random random = new SecureRandom();

    public Usuario registrarUsuario(String nombre, String apellido, String dni,
                                    String email, String telefono, String password) {

        // Validar que email no exista
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Validar que DNI no exista
        if (usuarioRepository.existsByDni(dni)) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }

        // Generar CVU y alias únicos
        String cvu = generarCVUUnico();
        String alias = generarAliasUnico();

        // Crear usuario con contraseña hasheada
        Usuario usuario = new Usuario(nombre, apellido, dni, email, telefono,
                passwordEncoder.encode(password));

        // Necesitarás agregar setters para CVU y alias en la entidad Usuario
        // o modificar el constructor

        return usuarioRepository.save(usuario);
    }

    private String generarCVUUnico() {
        String cvu;
        do {
            // Generar CVU de 22 dígitos
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 22; i++) {
                sb.append(random.nextInt(10));
            }
            cvu = sb.toString();
        } while (usuarioRepository.existsByCvu(cvu));

        return cvu;
    }

    private String generarAliasUnico() {
        String alias;
        do {
            alias = PALABRAS.get(random.nextInt(PALABRAS.size())) + "." +
                    PALABRAS.get(random.nextInt(PALABRAS.size())) + "." +
                    PALABRAS.get(random.nextInt(PALABRAS.size()));
        } while (usuarioRepository.existsByAlias(alias));

        return alias;
    }
}