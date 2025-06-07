package com.tiendainventario.controller;

import com.tiendainventario.model.Usuario;
import com.tiendainventario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id);

        // Estructura personalizada de la respuesta
        return ResponseEntity.ok(
                Map.of(
                        "id", usuario.getId(),
                        "rol", Map.of(
                                "id", usuario.getRol().getId(),
                                "tipoRol", usuario.getRol().getTipoRol(),
                                "descripcion", usuario.getRol().getDescripcion()
                        ),
                        "username", usuario.getUsername(),
                        "password", usuario.getPassword(),
                        "email", usuario.getEmail(),
                        "activo", usuario.getActivo(),
                        "fechaCreacion", usuario.getFechaCreacion()
                )
        );
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearUsuario(@RequestBody Usuario usuario) {
        Usuario creado = usuarioService.crearUsuario(usuario);

        // Modificar la estructura de respuesta JSON
        return ResponseEntity.ok(
                Map.of(
                        "rol", Map.of("id", creado.getRol().getId()),
                        "username", creado.getUsername(),
                        "password", creado.getPassword(),
                        "email", creado.getEmail(),
                        "activo", creado.getActivo(),
                        "fechaCreacion", creado.getFechaCreacion()
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}