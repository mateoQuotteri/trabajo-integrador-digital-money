package Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * Entidad SesionUsuario - Maneja las sesiones activas de los usuarios
 *
 * PROBLEMA QUE RESUELVE:
 * Los tokens JWT son "stateless" por diseño, lo que significa que una vez
 * emitidos, no se pueden invalidar directamente. Esta entidad permite
 * implementar logout efectivo y control granular de sesiones.
 */
@Entity
@Table(name = "sesiones_usuario", indexes = {
        @Index(name = "idx_token_hash", columnList = "tokenHash"),
        @Index(name = "idx_usuario_sesion", columnList = "usuario_id"),
        @Index(name = "idx_fecha_expiracion", columnList = "fechaExpiracion"),
        @Index(name = "idx_activa", columnList = "activa")
})
public class SesionUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Usuario propietario de la sesión
     * FetchType.LAZY: no carga el usuario automáticamente para optimización
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    /**
     * Hash SHA-256 del token JWT para seguridad
     * IMPORTANTE: No guardamos el token completo por seguridad,
     * solo guardamos su hash para poder identificarlo
     */
    @Column(nullable = false, unique = true, length = 255)
    private String tokenHash;

    /**
     * Timestamp automático de creación
     * Se setea automáticamente cuando se crea la entidad
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Fecha de expiración del token
     * Debe coincidir con la expiración configurada en JWT
     */
    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    /**
     * Estado de la sesión
     * true: sesión válida, false: sesión invalidada (logout)
     */
    @Column(nullable = false)
    private Boolean activa = true;

    /**
     * IP desde donde se inició la sesión
     * Útil para auditoría de seguridad y detección de accesos sospechosos
     */
    @Column(length = 45) // IPv6 máximo: 39 caracteres
    private String ipAddress;

    /**
     * User Agent del navegador/aplicación
     * Permite identificar el tipo de dispositivo/navegador y detectar bots
     */
    @Column(length = 500)
    private String userAgent;

    // ===============================================
    // CONSTRUCTORES
    // ===============================================

    /**
     * Constructor vacío requerido por JPA
     */
    public SesionUsuario() {}

    /**
     * Constructor principal para crear una nueva sesión
     *
     * @param usuario Usuario propietario de la sesión
     * @param tokenHash Hash SHA-256 del token JWT
     * @param fechaExpiracion Cuándo expira el token
     */
    public SesionUsuario(Usuario usuario, String tokenHash, LocalDateTime fechaExpiracion) {
        this.usuario = usuario;
        this.tokenHash = tokenHash;
        this.fechaExpiracion = fechaExpiracion;
        this.activa = true;
    }

    /**
     * Constructor completo con información de contexto
     */
    public SesionUsuario(Usuario usuario, String tokenHash, LocalDateTime fechaExpiracion,
                         String ipAddress, String userAgent) {
        this.usuario = usuario;
        this.tokenHash = tokenHash;
        this.fechaExpiracion = fechaExpiracion;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.activa = true;
    }

    // ===============================================
    // MÉTODOS DE NEGOCIO
    // ===============================================

    /**
     * Invalida la sesión (logout)
     * Marca la sesión como inactiva sin borrarla de la BD
     * Esto mantiene el historial de sesiones para auditoría
     */
    public void invalidar() {
        this.activa = false;
    }

    /**
     * Verifica si la sesión es válida actualmente
     * Una sesión es válida si:
     * 1. Está marcada como activa
     * 2. No ha expirado según la fecha
     *
     * @return true si la sesión puede usarse, false si no
     */
    public boolean esValida() {
        return activa && fechaExpiracion.isAfter(LocalDateTime.now());
    }

    /**
     * Verifica si la sesión ha expirado por tiempo
     * Útil para limpiezas automáticas
     *
     * @return true si la fecha de expiración ya pasó
     */
    public boolean estaExpirada() {
        return fechaExpiracion.isBefore(LocalDateTime.now());
    }

    /**
     * Calcula los minutos restantes antes de la expiración
     * Útil para mostrar al usuario cuánto tiempo le queda
     *
     * @return minutos hasta expiración, 0 si ya expiró
     */
    public long getMinutosRestantes() {
        if (estaExpirada()) {
            return 0;
        }
        return java.time.Duration.between(LocalDateTime.now(), fechaExpiracion).toMinutes();
    }

    /**
     * Obtiene información resumida del dispositivo
     * Parsea el User Agent para extraer info básica
     *
     * @return descripción del dispositivo (ej: "Chrome en Windows")
     */
    public String getDescripcionDispositivo() {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Dispositivo desconocido";
        }

        // Lógica básica para extraer info del User Agent
        if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        } else if (userAgent.contains("Mobile")) {
            return "Dispositivo móvil";
        } else {
            return "Navegador desconocido";
        }
    }

    // ===============================================
    // GETTERS Y SETTERS
    // ===============================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}