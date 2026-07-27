package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.entity.Auditoria;
import org.jcdev.stockflow.backend.repository.AuditoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaService(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    //obtener las auditorias
    public List<Auditoria> obtenerAuditorias() {
        return auditoriaRepository.findAll();
    }

    //obtener las auditorias por usuarios
    public List<Auditoria> obtenerAuditoriasByUsuarioId(Long idUsuario) {
        return auditoriaRepository.findByUsuarioId(idUsuario);
    }
}
