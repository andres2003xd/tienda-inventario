package com.tiendainventario.controller;

import com.tiendainventario.model.Producto;
import com.tiendainventario.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    // Método para respuestas de error
    private Map<String, Object> crearRespuestaError(String mensaje, Object detalles) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "error");
        respuesta.put("mensaje", mensaje);
        respuesta.put("detalles", detalles);
        return respuesta;
    }

    // Método para ejemplo de producto
    private Map<String, Object> generarEjemploProducto() {
        Map<String, Object> ejemplo = new LinkedHashMap<>();
        ejemplo.put("nombre", "Smartphone X");
        ejemplo.put("descripcion", "Teléfono inteligente de última generación");
        ejemplo.put("precio", 1999999.99);
        ejemplo.put("stock", 50);
        ejemplo.put("proveedor", Collections.singletonMap("id", 1));
        ejemplo.put("categoria", Collections.singletonMap("id", 1));
        return ejemplo;
    }

    private String formatearPrecioCOP(Double precio) {
        return String.format("$%,.2f COP", precio);
    }

    // CREAR PRODUCTO
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody(required = false) Map<String, Object> requestBody) {
        if (requestBody == null) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("----DEBE ENVIAR LOS DATOS DEL PRODUCTO CON SUS CAMPOS OBLIGATORIOS----",
                            Collections.singletonMap("ejemplo", generarEjemploProducto())
                    )
            );
        }

        // Validar campos
        Set<String> camposPermitidos = Set.of("nombre", "descripcion", "precio", "stock", "proveedor", "categoria");
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
                    "nombre: 'Smartphone X'",
                    "descripcion: 'Teléfono inteligente de última generación'",
                    "precio: 1999999.99",
                    "stock: 50",
                    "proveedor: {id: 1}",
                    "categoria: {id: 1}"
            ));
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("LOS NOMBRES DE LOS CAMPOS NO COINCIDEN CON LOS ESPERADOS", detalles)
            );
        }

        // Convertir y validar datos
        Producto producto;
        try {
            producto = new ObjectMapper().convertValue(requestBody, Producto.class);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Error en el formato de los datos",
                            Collections.singletonMap("EJEMPLO: ", generarEjemploProducto()))
            );
        }

        // Validaciones
        Map<String, String> errores = new LinkedHashMap<>();

        // Validar nombre
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            errores.put("nombre", "Campo obligatorio, poner un nombre válido");
        } else if (!producto.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s-]{2,50}$")) {
            errores.put("nombre", "Solo puede contener letras, números, espacios y guiones (2-50 caracteres)");
        }

        // Validar descripción
        if (producto.getDescripcion() == null || producto.getDescripcion().trim().isEmpty()) {
            errores.put("descripcion", "Campo obligatorio, poner una descripción válida");
        } else if (producto.getDescripcion().length() < 5 || producto.getDescripcion().length() > 200) {
            errores.put("descripcion", "Debe tener entre 5 y 200 caracteres");
        }

        // Validar precio
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            errores.put("precio", "Debe ser un valor mayor a 0");
        }

        // Validar stock
        if (producto.getStock() == null || producto.getStock() < 0) {
            errores.put("stock", "No puede ser un valor negativo");
        }

        // Validar proveedor
        if (producto.getProveedor() == null || producto.getProveedor().getId() == null) {
            errores.put("proveedor", "Debe especificar un proveedor válido");
        } else if (!proveedorRepository.existsById(producto.getProveedor().getId())) {
            errores.put("proveedor", "No existe un proveedor con ID " + producto.getProveedor().getId());
        }

        // Validar categoría
        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            errores.put("categoria", "Debe especificar una categoría válida");
        } else if (!categoriaRepository.existsById(producto.getCategoria().getId())) {
            errores.put("categoria", "No existe una categoría con ID " + producto.getCategoria().getId());
        }

        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Error en los datos del producto", errores)
            );
        }

        // Asignar relaciones completas
        proveedorRepository.findById(producto.getProveedor().getId()).ifPresent(producto::setProveedor);
        categoriaRepository.findById(producto.getCategoria().getId()).ifPresent(producto::setCategoria);

        Producto nuevoProducto = productoRepository.save(producto);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "éxito");
        respuesta.put("mensaje", "Producto creado correctamente");
        respuesta.put("data", Map.of(
                "id", nuevoProducto.getId(),
                "nombre", nuevoProducto.getNombre(),
                "precio", formatearPrecioCOP(nuevoProducto.getPrecio()),
                "stock", nuevoProducto.getStock(),
                "proveedor", nuevoProducto.getProveedor().getId(),
                "categoria", nuevoProducto.getCategoria().getId()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // LISTAR PRODUCTOS
    @GetMapping
    public ResponseEntity<?> listarProductos() {
        List<Producto> productos = productoRepository.findAll();

        if (productos.isEmpty()) {
            Map<String, Object> respuesta = new LinkedHashMap<>();
            respuesta.put("status", "info");
            respuesta.put("mensaje", "No hay productos registrados");
            respuesta.put("sugerencia", "Use POST /api/productos para crear un nuevo producto");
            return ResponseEntity.ok(respuesta);
        }

        List<Map<String, Object>> productosFormateados = new ArrayList<>();
        for (Producto p : productos) {
            Map<String, Object> productoMap = new LinkedHashMap<>();
            productoMap.put("id", p.getId());
            productoMap.put("nombre", p.getNombre());
            productoMap.put("descripcion", p.getDescripcion());
            productoMap.put("precio", formatearPrecioCOP(p.getPrecio()));
            productoMap.put("stock", p.getStock());
            productoMap.put("proveedor", p.getProveedor().getId());
            productoMap.put("categoria", p.getCategoria().getId());
            productosFormateados.add(productoMap);
        }

        return ResponseEntity.ok(productosFormateados);
    }

    // OBTENER PRODUCTO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProducto(@PathVariable Long id) {
        Optional<Producto> producto = productoRepository.findById(id);

        if (producto.isEmpty()) {
            Map<String, Object> detalles = new LinkedHashMap<>();
            detalles.put("error", "No existe un producto con ID " + id);
            detalles.put("sugerencia", "Verifique el ID o consulte la lista de productos en GET /api/productos");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Producto no encontrado", detalles));
        }

        Producto p = producto.get();
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("id", p.getId());
        respuesta.put("nombre", p.getNombre());
        respuesta.put("descripcion", p.getDescripcion());
        respuesta.put("precio", formatearPrecioCOP(p.getPrecio()));
        respuesta.put("stock", p.getStock());
        respuesta.put("proveedor", p.getProveedor().getId());
        respuesta.put("categoria", p.getCategoria().getId());

        return ResponseEntity.ok(respuesta);
    }

    // ACTUALIZAR PRODUCTO
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> requestBody) {

        if (requestBody == null || requestBody.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("ERROR: NO SE ENVIÓ NINGÚN DATO PARA ACTUALIZAR",
                            Map.of(
                                    "solución", "Debes incluir los campos a actualizar",
                                    "ejemplo", generarEjemploProducto()
                            )
                    )
            );
        }

        Optional<Producto> productoOptional = productoRepository.findById(id);
        if (productoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    crearRespuestaError("PRODUCTO NO ENCONTRADO",
                            Map.of(
                                    "alerta", "No existe un producto con ID " + id,
                                    "solución", "Verifica el ID o consulta la lista de productos",
                                    "Consulta en", "GET /api/productos"
                            )
                    )
            );
        }

        Producto productoExistente = productoOptional.get();
        boolean hayCambios = false;
        Map<String, String> errores = new LinkedHashMap<>();

        // Validar y actualizar cada campo
        if (requestBody.containsKey("nombre")) {
            String nombre = (String) requestBody.get("nombre");
            if (nombre == null || nombre.trim().isEmpty()) {
                errores.put("nombre", "El nombre no puede estar vacío");
            } else if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s-]{2,50}$")) {
                errores.put("nombre", "Solo puede contener letras, números, espacios y guiones (2-50 caracteres)");
            } else if (!nombre.equals(productoExistente.getNombre())) {
                productoExistente.setNombre(nombre);
                hayCambios = true;
            }
        }

        if (requestBody.containsKey("descripcion")) {
            String descripcion = (String) requestBody.get("descripcion");
            if (descripcion == null || descripcion.trim().isEmpty()) {
                errores.put("descripcion", "La descripción no puede estar vacía");
            } else if (descripcion.length() < 5 || descripcion.length() > 200) {
                errores.put("descripcion", "Debe tener entre 5 y 200 caracteres");
            } else if (!descripcion.equals(productoExistente.getDescripcion())) {
                productoExistente.setDescripcion(descripcion);
                hayCambios = true;
            }
        }

        if (requestBody.containsKey("precio")) {
            try {
                Double precio = ((Number) requestBody.get("precio")).doubleValue();
                if (precio <= 0) {
                    errores.put("precio", "Debe ser un valor mayor a 0");
                } else if (!precio.equals(productoExistente.getPrecio())) {
                    productoExistente.setPrecio(precio);
                    hayCambios = true;
                }
            } catch (Exception e) {
                errores.put("precio", "Formato inválido para el precio");
            }
        }

        if (requestBody.containsKey("stock")) {
            try {
                Integer stock = (Integer) requestBody.get("stock");
                if (stock < 0) {
                    errores.put("stock", "No puede ser un valor negativo");
                } else if (!stock.equals(productoExistente.getStock())) {
                    productoExistente.setStock(stock);
                    hayCambios = true;
                }
            } catch (Exception e) {
                errores.put("stock", "Formato inválido para el stock");
            }
        }

        if (requestBody.containsKey("proveedor")) {
            try {
                Map<String, Object> proveedorMap = (Map<String, Object>) requestBody.get("proveedor");
                if (proveedorMap == null || proveedorMap.get("id") == null) {
                    errores.put("proveedor", "Debe especificar un proveedor válido");
                } else {
                    Long proveedorId = ((Number) proveedorMap.get("id")).longValue();
                    if (!proveedorRepository.existsById(proveedorId)) {
                        errores.put("proveedor", "No existe un proveedor con ID " + proveedorId);
                    } else if (!proveedorId.equals(productoExistente.getProveedor().getId())) {
                        proveedorRepository.findById(proveedorId).ifPresent(productoExistente::setProveedor);
                        hayCambios = true;
                    }
                }
            } catch (Exception e) {
                errores.put("proveedor", "Formato inválido para el proveedor");
            }
        }

        if (requestBody.containsKey("categoria")) {
            try {
                Map<String, Object> categoriaMap = (Map<String, Object>) requestBody.get("categoria");
                if (categoriaMap == null || categoriaMap.get("id") == null) {
                    errores.put("categoria", "Debe especificar una categoría válida");
                } else {
                    Long categoriaId = ((Number) categoriaMap.get("id")).longValue();
                    if (!categoriaRepository.existsById(categoriaId)) {
                        errores.put("categoria", "No existe una categoría con ID " + categoriaId);
                    } else if (!categoriaId.equals(productoExistente.getCategoria().getId())) {
                        categoriaRepository.findById(categoriaId).ifPresent(productoExistente::setCategoria);
                        hayCambios = true;
                    }
                }
            } catch (Exception e) {
                errores.put("categoria", "Formato inválido para la categoría");
            }
        }

        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Error en los datos del producto", errores)
            );
        }

        if (!hayCambios) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("No se realizaron cambios",
                            Map.of("mensaje", "Los valores enviados son iguales a los actuales"))
            );
        }

        Producto productoActualizado = productoRepository.save(productoExistente);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "éxito");
        respuesta.put("mensaje", "Producto actualizado correctamente");
        respuesta.put("data", Map.of(
                "id", productoActualizado.getId(),
                "nombre", productoActualizado.getNombre(),
                "precio", formatearPrecioCOP(productoActualizado.getPrecio()),
                "stock", productoActualizado.getStock()
        ));
        return ResponseEntity.ok(respuesta);
    }

    // ELIMINAR PRODUCTO
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Producto no encontrado",
                            Map.of("error", "No existe producto con ID " + id)));
        }

        if (detalleVentaRepository.existsByProductoId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(crearRespuestaError("No se puede eliminar el producto",
                            Map.of(
                                    "razón", "El producto está asociado a ventas",
                                    "solución", "Elimine primero los detalles de venta relacionados"
                            )));
        }

        productoRepository.deleteById(id);
        return ResponseEntity.ok(Map.of(
                "status", "éxito",
                "mensaje", "Producto eliminado correctamente",
                "id_eliminado", id
        ));
    }
}