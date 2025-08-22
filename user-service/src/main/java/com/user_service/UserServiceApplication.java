package com.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * Aplicación principal del User Service:
 *
 * Esta clase inicializa el microservicio de usuarios que se encarga de:
 * - Registro y autenticación de usuarios
 * - Gestión de sesiones con JWT
 * - Operaciones CRUD del perfil de usuario
 */
@SpringBootApplication
@EnableDiscoveryClient

public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
}