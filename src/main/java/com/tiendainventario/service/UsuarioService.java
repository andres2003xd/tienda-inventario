package com.tiendainventario.service;

import com.tiendainventario.model.Usuario;
import com.tiendainventario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService extends BaseService<Usuario, Long, UsuarioRepository> {

    public UsuarioService(UsuarioRepository repository) {
        super(repository);
    }

    @Transactional
    public Usuario crearUsuario(Usuario usuario) {
        return repository.save(usuario);
    }

    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuario = findById(id);
        usuario.setUsername(usuarioActualizado.getUsername());
        usuario.setPassword(usuarioActualizado.getPassword());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setActivo(usuarioActualizado.getActivo());
        usuario.setRol(usuarioActualizado.getRol());
        return repository.save(usuario);
    }

    @Override
    protected String getEntityName() {
        return "Usuario";
    }

}