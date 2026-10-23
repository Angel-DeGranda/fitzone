package com.api.fitzone.events;

import com.api.fitzone.models.ClaseModel;
import com.api.fitzone.models.SocioModel;

public record InscripcionRealizadaEvent(Long folioInscripcion, SocioModel socio, ClaseModel clase) {
}
