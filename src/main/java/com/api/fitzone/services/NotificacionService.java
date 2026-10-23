package com.api.fitzone.services;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import com.api.fitzone.events.InscripcionRealizadaEvent;
import com.api.fitzone.models.ClaseModel;
import com.api.fitzone.models.NotificacionModel;
import com.api.fitzone.models.SocioModel;
import com.api.fitzone.repositories.INotificacionRepository;

@Service
public class NotificacionService {

    private static final int HORAS_ANTES_RECORDATORIO = 2;

    @Autowired
    private INotificacionRepository notificacionRepository;

    @Autowired
    private TaskScheduler taskScheduler;

    @Async
    public void enviarAvisoConfirmacion(SocioModel socio, ClaseModel clase) {
        String mensaje = "Hola " + socio.getNombre() + ", tu inscripción a la clase de "
                + clase.getDisciplina() + " para la fecha " + clase.getFechaClase() + " ha sido confirmada.";

        NotificacionModel notificacion = new NotificacionModel(socio, mensaje, "CONFIRMACION", LocalDateTime.now());
        notificacionRepository.save(notificacion);
    }

    @Async
    @TransactionalEventListener
    public void programarRecordatorio(InscripcionRealizadaEvent evento) {
        SocioModel socio = evento.socio();
        ClaseModel clase = evento.clase();

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioClase = LocalDateTime.of(clase.getFechaClase(), clase.getHoraInicio());
        if (!inicioClase.isAfter(ahora)) {
            return;
        }

        LocalDateTime fechaEnvio = inicioClase.minusHours(HORAS_ANTES_RECORDATORIO);
        if (fechaEnvio.isBefore(ahora)) {
            fechaEnvio = ahora;
        }

        taskScheduler.schedule(() -> enviarRecordatorio(socio, clase),
                fechaEnvio.atZone(ZoneId.systemDefault()).toInstant());
    }

    private void enviarRecordatorio(SocioModel socio, ClaseModel clase) {
        String mensaje = "Hola " + socio.getNombre() + ", te recordamos tu clase de " + clase.getDisciplina()
                + " hoy a las " + clase.getHoraInicio() + " en la sucursal " + clase.getSucursal() + ".";

        NotificacionModel notificacion = new NotificacionModel(socio, mensaje, "RECORDATORIO", LocalDateTime.now());
        notificacionRepository.save(notificacion);
    }
}
