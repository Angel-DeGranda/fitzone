package com.api.fitzone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.fitzone.models.NotificacionModel;

@Repository
public interface INotificacionRepository extends JpaRepository<NotificacionModel, Long> {
}