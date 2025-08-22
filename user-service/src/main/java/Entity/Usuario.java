package Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Usuario - Representa un usuario del sistema Digital Money House
 *
 * Esta entidad maneja únicamente la información de identidad del usuario:
 * - Datos personales (nombre, apellido, DNI)
 * - Credenciales de acceso (email, contraseña)
 * - Control de sesiones y autenticación
 *
 * IMPORTANTE: Esta entidad NO contiene información financiera.
 * Los datos de cuenta (CVU, alias, saldo) están en el Account Service.
 *
 * DISEÑO DE MICROSERVICIOS:
 * - Cada microservicio debe ser dueño de sus propios datos
 * - User Service maneja SOLO identidad y autenticación
 * - Account Service maneja SOLO información financiera
 *
 * Esta separación permite:
 * - Escalabilidad independiente
 * - Fallos aislados por dominio
 * - Equipos trabajando en paralelo
 */
@Entity
@Table(name = "usuarios", indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_dni", columnList = "dni")
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido solo puede contener letras")
    @Column(nullable = false, length = 100)
    private String apellido;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^\\d{7,8}$", message = "El DNI debe tener 7 u 8 dígitos")
    @Column(unique = true, nullable = false, length = 8)
    private String dni;

    @Email(message = "El formato del email es inválido")
    @NotBlank(message = "El email es obligatorio")
    @Size(max = 255, message = "El email no puede exceder 255 caracteres")
    @Column(unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{8,14}$", message = "El formato del teléfono es inválido")
    @Column(length = 20)
    private String telefono;

    /**
     * CRÍTICO PARA SEGURIDAD: @JsonIgnore evita que la contraseña
     * aparezca JAMÁS en responses JSON, incluso por error.
     * Sin esta anotación, podrías accidentalmente exponer contraseñas.
     */
    @JsonIgnore
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean activo = true;

    /**
     * Timestamps automáticos - Hibernate maneja estas fechas
     * @CreationTimestamp: se setea UNA VEZ al crear la entidad
     * @UpdateTimestamp: se actualiza CADA VEZ que se modifica la entidad
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    /**
     * Relación con sesiones de usuario para control de JWT
     * Esto permite:
     * - Logout efectivo (invalidar tokens específicos)
     * - Control de sesiones concurrentes
     * - Auditoría de accesos
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SesionUsuario> sesiones = new ArrayList<>();

    // ===============================================
    // CONSTRUCTORES
    // ===============================================

    /**
     * Constructor vacío requerido por JPA
     */
    public Usuario() {}

    /**
     * Constructor para crear usuario completo
     * No incluye ID porque es auto-generado
     * No incluye fechas porque son automáticas
     */
    public Usuario(String nombre, String apellido, String dni, String email, String telefono, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
        this.activo = true;
    }

    // ===============================================
    // MÉTODOS DE NEGOCIO
    // ===============================================

    /**
     * Obtiene el nombre completo formateado
     * Encapsula la lógica de concatenación
     */
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }

    /**
     * Desactiva el usuario (soft delete)
     * Mejor práctica: nunca borrar usuarios, solo desactivar
     */
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Reactiva un usuario desactivado
     */
    public void activar() {
        this.activo = true;
    }

    /**
     * Valida si el usuario puede iniciar sesión
     * Encapsula reglas de negocio que podrían expandirse
     */
    public boolean puedeIniciarSesion() {
        return this.activo;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * NOTA: Getter de password para uso interno únicamente
     * Nunca se serializa a JSON gracias a @JsonIgnore
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public List<SesionUsuario> getSesiones() {
        return sesiones;
    }

    public void setSesiones(List<SesionUsuario> sesiones) {
        this.sesiones = sesiones;
    }
}