package com.healink.integrador.domain.entidades_salud;

import com.healink.integrador.core.controller.ControladorGenerico;
import com.healink.integrador.domain.embajadores.Embajador;
import com.healink.integrador.domain.embajadores.EmbajadorDTO;
import com.healink.integrador.domain.embajadores.EmbajadorMapper;
import com.healink.integrador.domain.embajadores.EmbajadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/entidades-salud")
@Tag(name = "EntidadesSalud", description = "API para gestión de entidades de salud")
public class EntidadSaludController extends ControladorGenerico<EntidadSalud, EntidadSaludDTO> {

    private final EntidadSaludService entidadSaludService;
    private final EmbajadorService embajadorService;
    private final EmbajadorMapper embajadorMapper;

    public EntidadSaludController(EntidadSaludService entidadSaludService, EntidadSaludMapper entidadSaludMapper,
                                 EmbajadorService embajadorService, EmbajadorMapper embajadorMapper) {
        super(entidadSaludService, entidadSaludMapper);
        this.entidadSaludService = entidadSaludService;
        this.embajadorService = embajadorService;
        this.embajadorMapper = embajadorMapper;
    }

    @GetMapping("/usuario/{usuario_id}")
    public ResponseEntity<EntidadSaludDTO> getByUsuarioId(@PathVariable Long usuario_id) {
        return entidadSaludService.findByUsuarioId(usuario_id)
                .map(entidad_salud -> ResponseEntity.ok(mapeador.aDTO(entidad_salud)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/razon-social/{razon_social}")
    public ResponseEntity<EntidadSaludDTO> getByRazonSocial(@PathVariable String razon_social) {
        return entidadSaludService.findByRazonSocial(razon_social)
                .map(entidad_salud -> ResponseEntity.ok(mapeador.aDTO(entidad_salud)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/por-administrador")
    @Operation(summary = "Listar entidades de salud por administrador", 
               description = "Obtiene todas las entidades de salud creadas por un administrador específico usando TipoIdentificacion:Identificacion")
    public ResponseEntity<List<EntidadSaludDTO>> listarPorAdministrador(
            @Parameter(description = "Tipo de identificación del administrador (ej: CC, NIT, CE)", example = "CC")
            @RequestParam String tipoIdentificacion,
            @Parameter(description = "Número de identificación del administrador", example = "12345678")
            @RequestParam String identificacion) {
        
        List<EntidadSalud> entidades = entidadSaludService.buscarPorAdministrador(tipoIdentificacion, identificacion);
        
        if (entidades.isEmpty()) {
            return ResponseEntity.ok(List.of()); // Retorna lista vacía si no encuentra nada
        }
        
        List<EntidadSaludDTO> entidadesDTO = mapeador.aListaDTO(entidades);
        return ResponseEntity.ok(entidadesDTO);
    }

    @GetMapping("/embajadores-nit/{nit}")
    @Operation(summary = "Buscar embajadores por NIT creador", 
               description = "Obtiene todos los embajadores que fueron creados por una entidad con el NIT especificado")
    public ResponseEntity<List<EmbajadorDTO>> getEmbajadoresByNitCreador(@PathVariable String nit) {
        List<Embajador> embajadores = embajadorService.findByNitCreador(nit);
        return ResponseEntity.ok(embajadorMapper.aListaDTO(embajadores));
    }

}
