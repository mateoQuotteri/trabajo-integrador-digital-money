package com.example.account_service.Controller;

import com.example.account_service.Dto.CuentaResponse;
import com.example.account_service.Dto.PatchCuentaRequest;
import com.example.account_service.Dto.TransaccionResponse;
import com.example.account_service.Exception.CuentaNotFoundException;
import com.example.account_service.Service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "*")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCuenta(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        try {
            CuentaResponse response = cuentaService.obtenerCuenta(id, userId);
            return ResponseEntity.ok(response);
        } catch (CuentaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<?> obtenerTransacciones(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        try {
            List<TransaccionResponse> response = cuentaService.obtenerTransacciones(id, userId);
            return ResponseEntity.ok(response);
        } catch (CuentaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarCuenta(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody PatchCuentaRequest request) {
        try {
            CuentaResponse response = cuentaService.actualizarCuenta(id, userId, request.getAlias());
            return ResponseEntity.ok(response);
        } catch (CuentaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> crearCuenta(@RequestHeader("X-User-Id") Long userId) {
        try {
            CuentaResponse response = cuentaService.crearCuenta(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
