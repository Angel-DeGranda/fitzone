package com.api.fitzone.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.fitzone.models.InscripcionModel;
import com.api.fitzone.services.InscripcionService;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    @PostMapping
    public ResponseEntity<?> inscribirEnClase(@RequestBody Map<String, String> solicitud) {
        try {
            String idSocio = solicitud.get("idSocio");
            String idClase = solicitud.get("idClase");

            InscripcionModel resultado = inscripcionService.inscribirEnClase(idSocio, idClase);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}