package com.tiendainventario.controller;

import com.tiendainventario.model.Categoria;
import com.tiendainventario.repository.CategoriaRepository;
import com.tiendainventario.repository.ProductoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Metodo para respuestas de error
    private Map<String, Object> crearRespuestaError(String mensaje, Object detalles) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "error");
        respuesta.put("mensaje", mensaje);
        respuesta.put("detalles", detalles);
        return respuesta;
    }

    // Metodo para ejemplo de categoría
    private Map<String, Object> generarEjemploCategoria() {
        Map<String, Object> ejemplo = new LinkedHashMap<>();
        ejemplo.put("nombre", "Electrónicos");
        ejemplo.put("descripcion", "Productos electrónicos y dispositivos");
        return ejemplo;
    }

    //CREAR
    @PostMapping
    public ResponseEntity<?> crearCategoria(@RequestBody(required = false) Map<String, Object> requestBody) {
        if (requestBody == null) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("----DEBE ENVIAR LOS DATOS DE LA CATEGORÍA CON SUS CAMPOS OBLIGATORIOS----",
                            Collections.singletonMap("ejemplo", generarEjemploCategoria())
                    )
            );
        }

        // Validar campos
        Set<String> camposPermitidos = Set.of("nombre", "descripcion");
        Map<String, String> erroresCampos = new LinkedHashMap<>();

        for (String campo : requestBody.keySet()) {
            if (!camposPermitidos.contains(campo)) {
                erroresCampos.put(campo, "<-----NOMBRE DE CAMPO INCORRECTO");
            }
        }

        if (!erroresCampos.isEmpty()) {
            Map<String, Object> detalles = new LinkedHashMap<>();
            detalles.put("CAMPOS INCORRECTOS", erroresCampos);
            detalles.put("EJEMPLO CORRECTO", Arrays.asList(
                    "nombre: 'Electrónicos'",
                    "descripcion: 'Productos electrónicos y dispositivos'"
            ));
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("LOS NOMBRES DE LOS CAMPOS NO COINCIDEN CON LOS ESPERADOS", detalles)
            );
        }

        // Convertir y validar datos
        Categoria categoria;
        try {
            categoria = new ObjectMapper().convertValue(requestBody, Categoria.class);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Error en el formato de los datos",
                            Collections.singletonMap("EJEMPLO: ", generarEjemploCategoria()))
            );
        }

        // Validaciones
        Map<String, String> errores = new LinkedHashMap<>();
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            errores.put("nombre", "Campo obligatorio, poner un nombre válido");
        } else if (!categoria.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}$")) {
            errores.put("nombre", "Solo puede contener letras y espacios (2-50 caracteres)");
        }

        if (categoria.getDescripcion() == null || categoria.getDescripcion().trim().isEmpty()) {
            errores.put("descripcion", "Campo obligatorio, poner una descripción válida");
        } else if (categoria.getDescripcion().length() < 5 || categoria.getDescripcion().length() > 200) {
            errores.put("descripcion", "Debe tener entre 5 y 200 caracteres");
        }

        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Error en los datos de la categoría", errores)
            );
        }

        Categoria nuevaCategoria = categoriaRepository.save(categoria);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "éxito");
        respuesta.put("mensaje", "Categoría creada correctamente");
        respuesta.put("id", nuevaCategoria.getId());
        respuesta.put("nombre", nuevaCategoria.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    //VER LISTA
    @GetMapping
    public ResponseEntity<?> listarCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();

        if (categorias.isEmpty()) {
            Map<String, Object> respuesta = new LinkedHashMap<>();
            respuesta.put("status", "info");
            respuesta.put("mensaje", "No hay categorías registradas");
            respuesta.put("sugerencia", "Use POST /api/categorias para crear una nueva categoría");
            return ResponseEntity.ok(respuesta);
        }

        List<Map<String, Object>> categoriasFormateadas = new ArrayList<>();
        for (Categoria c : categorias) {
            Map<String, Object> categoriaMap = new LinkedHashMap<>();
            categoriaMap.put("id", c.getId());
            categoriaMap.put("nombre", c.getNombre());
            categoriaMap.put("descripcion", c.getDescripcion());
            categoriasFormateadas.add(categoriaMap);
        }

        return ResponseEntity.ok(categoriasFormateadas);
    }

    // VER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCategoria(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);

        if (categoria.isEmpty()) {
            Map<String, Object> detalles = new LinkedHashMap<>();
            detalles.put("error", "No existe una categoría con ID " + id);
            detalles.put("sugerencia", "Verifique el ID o consulte la lista de categorías en GET /api/categorias");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Categoría no encontrada", detalles));
        }

        Categoria c = categoria.get();
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("id", c.getId());
        respuesta.put("nombre", c.getNombre());
        respuesta.put("descripcion", c.getDescripcion());

        return ResponseEntity.ok(respuesta);
    }

    //ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCategoria(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> requestBody) {

        if (requestBody == null || requestBody.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("ERROR: NO SE ENVIÓ NINGÚN DATO PARA ACTUALIZAR",
                            Map.of(
                                    "solución", "Debes incluir los campos a actualizar",
                                    "ejemplo", generarEjemploCategoria()
                            )
                    )
            );
        }

        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        if (categoriaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    crearRespuestaError("CATEGORÍA NO ENCONTRADA",
                            Map.of(
                                    "alerta", "No existe una categoría con ID " + id,
                                    "solución", "Verifica el ID o consulta la lista de categorías",
                                    "Consulta en", "GET /api/categorias"
                            )
                    )
            );
        }

        Categoria categoriaExistente = categoriaOptional.get();
        boolean hayCambios = false;
        Map<String, String> errores = new LinkedHashMap<>();

        if (requestBody.containsKey("nombre")) {
            String nombre = (String) requestBody.get("nombre");
            if (nombre == null || nombre.trim().isEmpty()) {
                errores.put("nombre", "El nombre no puede estar vacío");
            } else if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}$")) {
                errores.put("nombre", "Solo puede contener letras y espacios (2-50 caracteres)");
            } else if (!nombre.equals(categoriaExistente.getNombre())) {
                categoriaExistente.setNombre(nombre);
                hayCambios = true;
            }
        }

        if (requestBody.containsKey("descripcion")) {
            String descripcion = (String) requestBody.get("descripcion");
            if (descripcion == null || descripcion.trim().isEmpty()) {
                errores.put("descripcion", "La descripción no puede estar vacía");
            } else if (descripcion.length() < 5 || descripcion.length() > 200) {
                errores.put("descripcion", "Debe tener entre 5 y 200 caracteres");
            } else if (!descripcion.equals(categoriaExistente.getDescripcion())) {
                categoriaExistente.setDescripcion(descripcion);
                hayCambios = true;
            }
        }

        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Error en los datos de la categoría", errores)
            );
        }

        if (!hayCambios) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("No se realizaron cambios",
                            Map.of("mensaje", "Los valores enviados son iguales a los actuales"))
            );
        }

        Categoria categoriaActualizada = categoriaRepository.save(categoriaExistente);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "éxito");
        respuesta.put("mensaje", "Categoría actualizada correctamente");
        respuesta.put("id", categoriaActualizada.getId());
        return ResponseEntity.ok(respuesta);
    }

    // ELIMINAR CATEGORÍA
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        if (!categoriaRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Categoría no encontrada",
                            Map.of("error", "No existe categoría con ID " + id)));
        }

        if (productoRepository.existsByCategoriaId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(crearRespuestaError("No se puede eliminar la categoría",
                            Map.of(
                                    "razón", "La categoría tiene productos asociados",
                                    "solución", "Elimine o actualice primero los productos relacionados"
                            )));
        }

        categoriaRepository.deleteById(id);
        return ResponseEntity.ok(Map.of(
                "status", "éxito",
                "mensaje", "Categoría eliminada correctamente",
                "id_eliminado", id
        ));
    }
}