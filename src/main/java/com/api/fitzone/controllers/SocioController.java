package com.api.fitzone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.fitzone.models.SocioModel;
import com.api.fitzone.services.SocioService;

@RestController
@RequestMapping("/api/socios")
public class SocioController {

    @Autowired
    private SocioService socioService;

    @GetMapping("/{idSocio}/verificar")
    public ResponseEntity<?> verificarSocio(@PathVariable String idSocio) {
        try {
            SocioModel socio = socioService.obtenerYValidarSocioVigente(idSocio);
            return ResponseEntity.ok(socio);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}