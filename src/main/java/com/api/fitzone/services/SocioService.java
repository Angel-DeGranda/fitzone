package com.api.fitzone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.fitzone.models.SocioModel;
import com.api.fitzone.repositories.ISocioRepository;

@Service
public class SocioService {

    @Autowired
    private ISocioRepository socioRepository;

    @Transactional(readOnly = true)
    public SocioModel obtenerYValidarSocioVigente(String idSocio) {
        if (idSocio == null || idSocio.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del socio no puede estar vacío.");
        }

        SocioModel socio = socioRepository.findById(idSocio)
                .orElseThrow(() -> new RuntimeException("SOCIO_NO_ENCONTRADO"));

        if (!socio.isMembresiaVigente()) {
            throw new RuntimeException("MEMBRESIA_VENCIDA");
        }

        return socio;
    }
}