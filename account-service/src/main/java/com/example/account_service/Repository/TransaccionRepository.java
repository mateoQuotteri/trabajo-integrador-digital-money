package com.example.account_service.Repository;

import com.example.account_service.Entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByCuentaIdOrderByFechaDesc(Long cuentaId);
}
