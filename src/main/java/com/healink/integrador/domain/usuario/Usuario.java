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
import com.healink.integrador.domain.rol.Rol;
import com.healink.integrador.enums.TipoIdentificacion;

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

    @Column(name = "esta_activo")
    private Boolean estaActivo = true;

    // Asegúrate de tener esta relación en lugar de rolId
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id")
    private Rol rol;
    // @Column(name = "rol_id", nullable = false)
    // private Long rolId;

    // Métodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.getNombre().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return this.clave;
    }

    @Override
    public String getUsername() {
        // Combinación de tipo+identificación como "username" único
        return (tipoIdentificacion != null ? tipoIdentificacion.getValue() : "") + ":" + identificacion;
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
        return this.estaActivo;
    }

}