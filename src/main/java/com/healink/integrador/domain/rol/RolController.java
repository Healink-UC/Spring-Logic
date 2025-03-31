package com.healink.integrador.domain.rol;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.core.controller.ControladorGenerico;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "API para gestión de roles")
public class RolController extends ControladorGenerico<Rol, RolDTO> {

    private final RolService rolService;

    public RolController(RolService rolService, RolMapper rolMapper) {
        super(rolService, rolMapper);
        this.rolService = rolService;
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar rol por nombre", description = "Obtiene un rol por su nombre")
    public ResponseEntity<RolDTO> buscarPorNombre(@PathVariable String nombre) {
        return rolService.buscarPorNombre(nombre)
                .map(rol -> ResponseEntity.ok(mapeador.aDTO(rol)))
                .orElse(ResponseEntity.notFound().build());
    }

}
