package com.healink.integrador.domain.entidades_salud;

import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ENTIDADES_SALUD")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class EntidadSalud extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razon_social")
    private String razonSocial;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadSalud")
    private List<Usuario> usuarios = new ArrayList<>();
}
