package com.healink.integrador.campana;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.healink.integrador.domain.campana.Campana;
import com.healink.integrador.domain.campana.CampanaRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class CampanaTest {
    @Autowired
    private CampanaRepository campanaRespository;

    // Configuraciones

    private Campana campana = null;

    @Test
    @Transactional
    void testGuardarNuevaCampana() {
        campana = new Campana();

        System.out.println(campana);
        Campana resultado = campanaRespository.save(campana);

        assertNotNull(resultado);
    }

}
