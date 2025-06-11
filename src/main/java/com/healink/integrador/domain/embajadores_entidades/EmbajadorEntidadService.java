// package com.healink.integrador.domain.embajadores_entidades;

// import com.healink.integrador.core.service.ServicioGenerico;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.List;

// @Service
// @Transactional
// public class EmbajadorEntidadService extends ServicioGenerico<EmbajadorEntidad> {

//     private final EmbajadorEntidadRepository embajadorEntidadRepository;

//     public EmbajadorEntidadService(EmbajadorEntidadRepository embajadorEntidadRepository) {
//         super(embajadorEntidadRepository);
//         this.embajadorEntidadRepository = embajadorEntidadRepository;
//     }

//     @Transactional(readOnly = true)
//     public List<EmbajadorEntidad> findByEntidadId(Long entidadId) {
//         return embajadorEntidadRepository.findByEntidadId(entidadId);
//     }

//     @Transactional(readOnly = true)
//     public List<EmbajadorEntidad> findByEmbajadorId(Long embajadorId) {
//         return embajadorEntidadRepository.findByEmbajadorId(embajadorId);
//     }

//     @Transactional(readOnly = true)
//     public boolean existsByEmbajadorIdAndEntidadId(Long embajadorId, Long entidadId) {
//         return embajadorEntidadRepository.findByEmbajadorIdAndEntidadId(embajadorId, entidadId).isPresent();
//     }
// }