package com.api.fitzone.services;

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

    @Transactional(readOnly = true)
    public Map<String, Object> consultarCupo(String idClase) {
        ClaseModel clase = obtenerClase(idClase);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("idClase", clase.getIdClase());
        respuesta.put("cupoMaximo", clase.getCupoMaximo());
        respuesta.put("cupoOcupado", clase.getCupoOcupado());
        respuesta.put("cupoDisponible", clase.getCupoDisponible());

        return respuesta;
    }
}