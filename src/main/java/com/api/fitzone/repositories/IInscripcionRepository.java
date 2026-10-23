package com.api.fitzone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.fitzone.models.ClaseModel;
import com.api.fitzone.models.InscripcionModel;
import com.api.fitzone.models.SocioModel;

@Repository
public interface IInscripcionRepository extends JpaRepository<InscripcionModel, Long> {

    boolean existsBySocioAndClaseAndEstatus(SocioModel socio, ClaseModel clase, String estatus);
}