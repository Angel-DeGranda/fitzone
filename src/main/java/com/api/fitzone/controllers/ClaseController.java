package com.api.fitzone.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.fitzone.services.ClaseService;

@RestController
@RequestMapping("/api/clases")
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    @GetMapping("/{idClase}/cupo")
    public ResponseEntity<?> consultarCupo(@PathVariable String idClase, @RequestParam String fechaClase) {
        try {
            Map<String, Object> cupoInfo = claseService.consultarCupo(idClase, fechaClase);
            return ResponseEntity.ok(cupoInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}