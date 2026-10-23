package com.api.fitzone.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.fitzone.models.SocioModel;

@Repository
public interface ISocioRepository extends JpaRepository<SocioModel, String> {

    Optional<SocioModel> findByIdSocioAndMembresiaVigenteTrue(String idSocio);

    boolean existsByIdSocioAndMembresiaVigenteTrue(String idSocio);
}