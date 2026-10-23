package com.api.fitzone.services;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.fitzone.models.ClaseModel;
import com.api.fitzone.repositories.ClaseRepository;

@Service
public class ClaseService {

    @Autowired
    private ClaseRepository claseRepository;

    @Transactional(readOnly = true)
    public ClaseModel obtenerClase(String idClase) {
        return claseRepository.findById(idClase)
                .orElseThrow(() -> new RuntimeException("CLASE_NO_ENCONTRADA"));
    }

    public void validarFecha(ClaseModel clase, String fechaClase) {
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaClase);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new RuntimeException("FECHA_INVALIDA");
        }

        if (!fecha.equals(clase.getFechaClase())) {
            throw new RuntimeException("FECHA_INVALIDA");
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> consultarCupo(String idClase, String fechaClase) {
        ClaseModel clase = obtenerClase(idClase);
        validarFecha(clase, fechaClase);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("idClase", clase.getIdClase());
        respuesta.put("fechaClase", clase.getFechaClase());
        respuesta.put("cupoMaximo", clase.getCupoMaximo());
        respuesta.put("cupoOcupado", clase.getCupoOcupado());
        respuesta.put("cupoDisponible", clase.getCupoDisponible());

        return respuesta;
    }
}
