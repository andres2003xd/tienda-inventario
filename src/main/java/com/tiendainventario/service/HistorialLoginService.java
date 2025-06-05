package com.tiendainventario.service;

import com.tiendainventario.model.HistorialLogin;
import com.tiendainventario.repository.HistorialLoginRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HistorialLoginService extends BaseService<HistorialLogin, Long, HistorialLoginRepository> {

    public HistorialLoginService(HistorialLoginRepository repository) {
        super(repository);
    }

    @Transactional
    public HistorialLogin crearHistorial(HistorialLogin historialLogin) {
        // Crea un nuevo historial de login
        return repository.save(historialLogin);
    }

    @Transactional
    public HistorialLogin actualizarHistorial(Long id, HistorialLogin historialNuevo) {
        HistorialLogin historialLogin = findById(id); // Usa la verificación proporcionada por BaseService
        historialLogin.setFechaLogin(historialNuevo.getFechaLogin());
        historialLogin.setIp(historialNuevo.getIp());
        historialLogin.setDispositivo(historialNuevo.getDispositivo());
        historialLogin.setUsuario(historialNuevo.getUsuario());
        return repository.save(historialLogin);
    }

    @Override
    protected String getEntityName() {
        return "HistorialLogin";
    }
}