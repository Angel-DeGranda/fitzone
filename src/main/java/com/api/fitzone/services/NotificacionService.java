package com.api.fitzone.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.api.fitzone.models.ClaseModel;
import com.api.fitzone.models.NotificacionModel;
import com.api.fitzone.models.SocioModel;
import com.api.fitzone.repositories.INotificacionRepository;

@Service
public class NotificacionService {

    @Autowired
    private INotificacionRepository notificacionRepository;

    @Async
    public void enviarAvisoConfirmacion(SocioModel socio, ClaseModel clase) {
        String mensaje = "Hola " + socio.getNombre() + ", tu inscripción a la clase de " 
                + clase.getDisciplina() + " para la fecha " + clase.getFechaClase() + " ha sido confirmada.";
        
        NotificacionModel notificacion = new NotificacionModel(socio, mensaje, "CONFIRMACION", LocalDateTime.now());
        notificacionRepository.save(notificacion);
    }
}