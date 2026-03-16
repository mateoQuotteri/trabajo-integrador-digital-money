package com.example.account_service.Service;

import com.example.account_service.Dto.CuentaResponse;
import com.example.account_service.Dto.TransaccionResponse;
import com.example.account_service.Entity.Cuenta;
import com.example.account_service.Entity.Transaccion;
import com.example.account_service.Exception.CuentaNotFoundException;
import com.example.account_service.Repository.CuentaRepository;
import com.example.account_service.Repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private TransaccionRepository transaccionRepository;

    private final Random random = new SecureRandom();

    public CuentaResponse obtenerCuenta(Long cuentaId, Long userId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con id: " + cuentaId));

        if (!cuenta.getUserId().equals(userId)) {
            throw new SecurityException("No tienes permiso para acceder a esta cuenta");
        }

        return toResponse(cuenta);
    }

    public List<TransaccionResponse> obtenerTransacciones(Long cuentaId, Long userId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con id: " + cuentaId));

        if (!cuenta.getUserId().equals(userId)) {
            throw new SecurityException("No tienes permiso para acceder a esta cuenta");
        }

        return transaccionRepository.findByCuentaIdOrderByFechaDesc(cuentaId)
                .stream()
                .map(this::toTransaccionResponse)
                .toList();
    }

    public CuentaResponse crearCuenta(Long userId) {
        if (cuentaRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("El usuario ya tiene una cuenta asociada");
        }

        Cuenta cuenta = new Cuenta(userId, generarCVU(), generarAlias());
        cuentaRepository.save(cuenta);
        return toResponse(cuenta);
    }

    public CuentaResponse actualizarCuenta(Long cuentaId, Long userId, String nuevoAlias) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con id: " + cuentaId));

        if (!cuenta.getUserId().equals(userId)) {
            throw new SecurityException("No tienes permiso para modificar esta cuenta");
        }
        if (nuevoAlias == null || nuevoAlias.isBlank()) {
            throw new IllegalArgumentException("El alias no puede estar vacío");
        }
        if (cuentaRepository.existsByAlias(nuevoAlias)) {
            throw new IllegalArgumentException("El alias ya está en uso");
        }

        cuenta.setAlias(nuevoAlias);
        cuentaRepository.save(cuenta);
        return toResponse(cuenta);
    }

    private String generarCVU() {
        String cvu;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 22; i++) {
                sb.append(random.nextInt(10));
            }
            cvu = sb.toString();
        } while (cuentaRepository.existsByCvu(cvu));
        return cvu;
    }

    private String generarAlias() {
        String[] palabras = {"sol", "luna", "rio", "mar", "pez", "vez", "paz", "luz", "pan", "red"};
        String alias;
        do {
            alias = palabras[random.nextInt(palabras.length)] + "." +
                    palabras[random.nextInt(palabras.length)] + "." +
                    palabras[random.nextInt(palabras.length)];
        } while (cuentaRepository.existsByAlias(alias));
        return alias;
    }

    private CuentaResponse toResponse(Cuenta cuenta) {
        return new CuentaResponse(
                cuenta.getId(),
                cuenta.getUserId(),
                cuenta.getSaldo(),
                cuenta.getCvu(),
                cuenta.getAlias(),
                cuenta.getFechaCreacion()
        );
    }

    private TransaccionResponse toTransaccionResponse(Transaccion t) {
        return new TransaccionResponse(
                t.getId(),
                t.getMonto(),
                t.getTipo(),
                t.getDescripcion(),
                t.getFecha()
        );
    }
}
