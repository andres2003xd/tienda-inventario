package com.tiendainventario.controller;

import com.tiendainventario.model.Proveedor;
import com.tiendainventario.repository.ProveedorRepository;
import com.tiendainventario.repository.ProductoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Metodo para generar ejemplo de proveedor
    private Map<String, Object> generarEjemploProveedor() {
        Map<String, Object> ejemplo = new LinkedHashMap<>();
        ejemplo.put("nombre", "Mrbeast");
        ejemplo.put("telefono", "3001234567");
        ejemplo.put("email", "contacto@ejemplo.com");
        return ejemplo;
    }

    // Metodo para respuestas de error
    private Map<String, Object> crearRespuestaError(String mensaje, Object detalles) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "error");
        respuesta.put("mensaje", mensaje);
        respuesta.put("detalles", detalles);
        return respuesta;
    }

    // CREAR
    @PostMapping
    public ResponseEntity<?> crearProveedor(@RequestBody(required = false) Map<String, Object> requestBody) {
        if (requestBody == null) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("DEBE ENVIAR LOS DATOS DEL PROVEEDOR CON SUS CAMPOS OBLIGATORIOS",
                            Collections.singletonMap("ejemplo", generarEjemploProveedor())
                    )
            );
        }

        // Verificar nombres de campos correctos
        Set<String> camposEsperados = new HashSet<>(Arrays.asList("nombre", "telefono", "email"));
        Map<String, String> erroresCampos = new LinkedHashMap<>();

        for (String campo : requestBody.keySet()) {
            if (!camposEsperados.contains(campo)) {
                erroresCampos.put(campo, "NOMBRE DE CAMPO INCORRECTO");
            }
        }

        if (!erroresCampos.isEmpty()) {
            Map<String, Object> detalles = new LinkedHashMap<>();
            detalles.put("CAMPOS INCORRECTOS", erroresCampos);
            detalles.put("EJEMPLO CORRECTO", generarEjemploProveedor());
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("LOS NOMBRES DE LOS CAMPOS NO COINCIDEN CON LOS ESPERADOS", detalles)
            );
        }

        Proveedor proveedor;
        try {
            proveedor = new ObjectMapper().convertValue(requestBody, Proveedor.class);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Error en el formato de los datos",
                            Collections.singletonMap("ejemplo", generarEjemploProveedor()))
            );
        }

        // Validaciones
        Map<String, String> errores = new LinkedHashMap<>();
        if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
            errores.put("nombre", "Campo obligatorio, debe contener entre 2 y 50 caracteres");
        } else if (!proveedor.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}$")) {
            errores.put("nombre", "Solo puede contener letras y espacios (2-50 caracteres)");
        }

        if (proveedor.getTelefono() == null || proveedor.getTelefono().trim().isEmpty()) {
            errores.put("telefono", "Campo obligatorio");
        } else if (!proveedor.getTelefono().matches("^[0-9]{7,15}$")) {
            errores.put("telefono", "Debe contener solo números (7-15 dígitos)");
        }

        if (proveedor.getEmail() == null || proveedor.getEmail().trim().isEmpty()) {
            errores.put("email", "Campo obligatorio");
        } else if (!proveedor.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errores.put("email", "Formato de email inválido");
        }

        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Error en los datos del proveedor", errores)
            );
        }

        Proveedor nuevoProveedor = proveedorRepository.save(proveedor);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "éxito");
        respuesta.put("mensaje", "Proveedor creado correctamente");
        respuesta.put("id", nuevoProveedor.getId());
        respuesta.put("nombre", nuevoProveedor.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // VER LISTA
    @GetMapping
    public ResponseEntity<?> listarProveedores() {
        List<Proveedor> proveedores = proveedorRepository.findAll();

        if (proveedores.isEmpty()) {
            Map<String, Object> respuesta = new LinkedHashMap<>();
            respuesta.put("status", "info");
            respuesta.put("mensaje", "No hay proveedores registrados");
            respuesta.put("sugerencia", "Use POST /api/proveedores para crear un nuevo proveedor");
            return ResponseEntity.ok(respuesta);
        }

        List<Map<String, Object>> proveedoresFormateados = new ArrayList<>();
        for (Proveedor p : proveedores) {
            Map<String, Object> proveedorMap = new LinkedHashMap<>();
            proveedorMap.put("id", p.getId());
            proveedorMap.put("nombre", p.getNombre());
            proveedorMap.put("telefono", p.getTelefono());
            proveedorMap.put("email", p.getEmail());
            proveedoresFormateados.add(proveedorMap);
        }

        return ResponseEntity.ok(proveedoresFormateados);
    }

    // VER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProveedor(@PathVariable Long id) {
        Optional<Proveedor> proveedor = proveedorRepository.findById(id);

        if (proveedor.isEmpty()) {
            Map<String, Object> detalles = new LinkedHashMap<>();
            detalles.put("error", "No existe un proveedor con ID " + id);
            detalles.put("sugerencia", "Verifique el ID o consulte la lista de proveedores en GET /api/proveedores");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Proveedor no encontrado", detalles));
        }

        Proveedor p = proveedor.get();
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("id", p.getId());
        respuesta.put("nombre", p.getNombre());
        respuesta.put("telefono", p.getTelefono());
        respuesta.put("email", p.getEmail());

        return ResponseEntity.ok(respuesta);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProveedor(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> requestBody) {

        if (requestBody == null || requestBody.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("ERROR: NO SE ENVIÓ NINGÚN DATO PARA ACTUALIZAR",
                            Map.of(
                                    "solución", "Debes incluir los campos a actualizar",
                                    "ejemplo", generarEjemploProveedor()
                            )
                    )
            );
        }

        Optional<Proveedor> proveedorOptional = proveedorRepository.findById(id);
        if (proveedorOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    crearRespuestaError("PROVEEDOR NO ENCONTRADO",
                            Map.of(
                                    "alerta", "No existe un proveedor con ID " + id,
                                    "solución", "Verifica el ID o consulta la lista de proveedores",
                                    "Consulta en", "GET /api/proveedores"
                            )
                    )
            );
        }

        Proveedor proveedorExistente = proveedorOptional.get();

        Set<String> camposPermitidos = Set.of("nombre", "telefono", "email");
        Map<String, String> erroresCampo = new LinkedHashMap<>();

        for (String campo : requestBody.keySet()) {
            if (!camposPermitidos.contains(campo)) {
                erroresCampo.put(campo, "Campo no permitido");
            }
        }

        if (!erroresCampo.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("CAMPOS NO VÁLIDOS",
                            Map.of(
                                    "Campos permitidos", camposPermitidos,
                                    "Campos incorrectos", erroresCampo
                            )
                    )
            );
        }

        ObjectMapper mapper = new ObjectMapper();
        Proveedor proveedorActualizado = mapper.convertValue(requestBody, Proveedor.class);

        boolean hayCambios = false;
        Map<String, String> erroresValidacion = new LinkedHashMap<>();

        // Validar y actualizar nombre
        if (requestBody.containsKey("nombre")) {
            String nombre = proveedorActualizado.getNombre();
            if (nombre == null || nombre.trim().isEmpty()) {
                erroresValidacion.put("nombre", "El nombre no puede estar vacío.");
            } else if (nombre.trim().length() < 2 || nombre.trim().length() > 50) {
                erroresValidacion.put("nombre", "El nombre debe tener entre 2 y 50 caracteres.");
            } else if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}$")) {
                erroresValidacion.put("nombre", "El nombre solo puede contener letras y espacios.");
            } else if (!nombre.equals(proveedorExistente.getNombre())) {
                proveedorExistente.setNombre(nombre);
                hayCambios = true;
            }
        }

        // Validar y actualizar teléfono
        if (requestBody.containsKey("telefono")) {
            String telefono = proveedorActualizado.getTelefono();
            if (telefono == null || telefono.trim().isEmpty()) {
                erroresValidacion.put("telefono", "El teléfono no puede estar vacío.");
            } else if (!telefono.matches("^[0-9]{7,15}$")) {
                erroresValidacion.put("telefono", "Debe contener solo números (7-15 dígitos).");
            } else if (!telefono.equals(proveedorExistente.getTelefono())) {
                proveedorExistente.setTelefono(telefono);
                hayCambios = true;
            }
        }

        // Validar y actualizar email
        if (requestBody.containsKey("email")) {
            String email = proveedorActualizado.getEmail();
            if (email == null || email.trim().isEmpty()) {
                erroresValidacion.put("email", "El email no puede estar vacío.");
            } else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                erroresValidacion.put("email", "Formato de email inválido.");
            } else if (!email.equals(proveedorExistente.getEmail())) {
                proveedorExistente.setEmail(email);
                hayCambios = true;
            }
        }

        if (!erroresValidacion.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Errores en la validación de los datos", erroresValidacion)
            );
        }

        if (!hayCambios) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("No se realizaron cambios", Map.of(
                            "mensaje", "Los valores enviados son iguales a los actuales."
                    ))
            );
        }

        Proveedor proveedorGuardado = proveedorRepository.save(proveedorExistente);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "éxito");
        respuesta.put("mensaje", "Proveedor actualizado correctamente.");
        respuesta.put("id", proveedorGuardado.getId());

        return ResponseEntity.ok(respuesta);
    }

    // ELIMINAR PROVEEDOR
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminarProveedor(@PathVariable Long id) {
        if (!proveedorRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Proveedor no encontrado",
                            Map.of("error", "No existe proveedor con ID " + id)));
        }

        if (productoRepository.existsByProveedorId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(crearRespuestaError("No se puede eliminar el proveedor",
                            Map.of(
                                    "razón", "El proveedor tiene productos asociados",
                                    "solución", "Elimine o actualice primero los productos relacionados"
                            )));
        }

        proveedorRepository.deleteById(id);
        return ResponseEntity.ok(Map.of(
                "status", "éxito",
                "mensaje", "Proveedor eliminado correctamente",
                "id_eliminado", id
        ));
    }
}