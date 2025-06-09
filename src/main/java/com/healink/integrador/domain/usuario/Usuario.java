package com.healink.integrador.domain.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.entidades_salud.EntidadSalud;
import com.healink.integrador.domain.rol.Rol;

@Entity
@Table(name = "USUARIOS", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "identificacion", "correo"
        }),
})
@Getter
@Setter
// Excluir clave de toString por seguridad
@ToString(exclude = "clave")
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends EntidadAuditable implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_identificacion", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private TipoIdentificacion tipoIdentificacion;

    @Column(name = "identificacion", nullable = false)
    private String identificacion;

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "correo", unique = true)
    private String correo;

    @Column(name = "clave", nullable = false)
    private String clave;

    @Column(name = "celular")
    private String celular;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.ACTIVO;

    // Asegúrate de tener esta relación en lugar de rolId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Rol rol;

    @Column(name = "rol_id", nullable = false)
    private Long rolId;

    @Column(name = "entidad_salud_id")
    private Long entidadSaludId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entidad_salud_id", referencedColumnName = "id", insertable = false, updatable = false)
    private EntidadSalud entidadSalud;

    // Métodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (rol != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + rol.getNombre().toUpperCase()));
        }
        // Si el rol no está cargado, usar un rol por defecto basado en rolId
        if (rolId != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_USER_" + rolId));
        }
        // Último recurso: rol genérico
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.clave;
    }

    @Override
    public String getUsername() {
        return tipoIdentificacion + ":" + identificacion;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.estado == Estado.ACTIVO;
    }

}