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
        return repository.save(historialLogin);
    }

    @Transactional
    public HistorialLogin actualizarHistorial(Long id, HistorialLogin historialNuevo) {
        HistorialLogin historialLogin = findById(id);
        historialLogin.setFechaLogin(historialNuevo.getFechaLogin());
        historialLogin.setIp(historialNuevo.getIp());
        historialLogin.setDispositivo(historialNuevo.getDispositivo());
        historialLogin.setUsuario(historialNuevo.getUsuario());
        return repository.save(historialLogin);
    }

    @Transactional
    public void eliminarById(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("El historial con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }

    @Override
    protected String getEntityName() {
        return "HistorialLogin";
    }
}