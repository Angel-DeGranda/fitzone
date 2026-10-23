package com.api.fitzone.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.fitzone.models.ClaseModel;
import com.api.fitzone.models.InscripcionModel;
import com.api.fitzone.models.SocioModel;
import com.api.fitzone.repositories.ClaseRepository;
import com.api.fitzone.repositories.IInscripcionRepository;

@Service
public class InscripcionService {

    @Autowired
    private SocioService socioService;

    @Autowired
    private ClaseService claseService;

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private IInscripcionRepository inscripcionRepository;

    @Autowired
    private NotificacionService notificacionService;

    @Transactional
    public InscripcionModel inscribirEnClase(String idSocio, String idClase) {
        // 1. Validar membresía del socio
        SocioModel socio = socioService.obtenerYValidarSocioVigente(idSocio);

        // 2. Validar existencia de la clase
        ClaseModel clase = claseService.obtenerClase(idClase);

        // 3. Validar cupo disponible (máximo 20)
        if (clase.getCupoDisponible() <= 0) {
            throw new RuntimeException("CLASE_SIN_CUPO");
        }

        // 4. Validar si ya está inscrito
        boolean yaInscrito = inscripcionRepository.existsBySocioAndClaseAndEstatus(socio, clase, "CONFIRMADA");
        if (yaInscrito) {
            throw new RuntimeException("SOCIO_YA_INSCRITO");
        }

        // 5. Incrementar cupo de la clase
        clase.setCupoOcupado(clase.getCupoOcupado() + 1);
        claseRepository.save(clase);

        // 6. Registrar la inscripción
        InscripcionModel nuevaInscripcion = new InscripcionModel(socio, clase, LocalDateTime.now(), "CONFIRMADA");
        InscripcionModel inscripcionGuardada = inscripcionRepository.save(nuevaInscripcion);

        // 7. Notificación asíncrona (unidireccional)
        notificacionService.enviarAvisoConfirmacion(socio, clase);

        return inscripcionGuardada;
    }
}