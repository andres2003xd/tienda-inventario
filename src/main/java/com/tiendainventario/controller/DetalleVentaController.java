package com.tiendainventario.controller;

import com.tiendainventario.model.DetalleVenta;
import com.tiendainventario.model.Producto;
import com.tiendainventario.model.Venta;
import com.tiendainventario.repository.DetalleVentaRepository;
import com.tiendainventario.repository.ProductoRepository;
import com.tiendainventario.repository.VentaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/detallesventas")
public class DetalleVentaController {


    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private VentaRepository ventaRepository;

    // Método para respuestas de error
    private Map<String, Object> crearRespuestaError(String mensaje, Object detalles) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "error");
        respuesta.put("mensaje", mensaje);
        respuesta.put("detalles", detalles);
        return respuesta;
    }

    // Método para ejemplo de detalle de venta
    private Map<String, Object> generarEjemploDetalleVenta() {
        Map<String, Object> ejemplo = new LinkedHashMap<>();
        ejemplo.put("cantidad", 2);
        ejemplo.put("precioUnitario", 50000.0);
        ejemplo.put("producto", Collections.singletonMap("id", 1));
        ejemplo.put("venta", Collections.singletonMap("id", 1));
        return ejemplo;
    }

    // CREAR
    @PostMapping
    public ResponseEntity<?> crearDetalleVenta(@RequestBody(required = false) Map<String, Object> requestBody) {
        if (requestBody == null) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("----DEBE ENVIAR LOS DATOS DEL DETALLE DE VENTA CON SUS CAMPOS OBLIGATORIOS----",
                            Collections.singletonMap("ejemplo", generarEjemploDetalleVenta())
                    )
            );
        }

        // Validar campos
        Set<String> camposPermitidos = Set.of("cantidad", "precioUnitario", "producto", "venta");
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
                    "cantidad: 2",
                    "precioUnitario: 50000.0",
                    "producto: {id: 1}",
                    "venta: {id: 1}"
            ));
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("LOS NOMBRES DE LOS CAMPOS NO COINCIDEN CON LOS ESPERADOS", detalles)
            );
        }

        // Convertir y validar datos
        DetalleVenta detalleVenta;
        try {
            detalleVenta = new ObjectMapper().convertValue(requestBody, DetalleVenta.class);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Error en el formato de los datos",
                            Collections.singletonMap("EJEMPLO: ", generarEjemploDetalleVenta()))
            );
        }

        // Validaciones
        Map<String, String> errores = new LinkedHashMap<>();
        if (detalleVenta.getCantidad() == null || detalleVenta.getCantidad() <= 0) {
            errores.put("cantidad", "Debe ser mayor a 0");
        }

        if (detalleVenta.getPrecioUnitario() == null || detalleVenta.getPrecioUnitario() <= 0) {
            errores.put("precioUnitario", "Debe ser mayor a 0");
        }

        // Validar producto
        if (detalleVenta.getProducto() == null || detalleVenta.getProducto().getId() == null) {
            errores.put("producto", "Campo obligatorio");
        } else if (!productoRepository.existsById(detalleVenta.getProducto().getId())) {
            errores.put("producto", "No existe un producto con ID " + detalleVenta.getProducto().getId());
        }

        // Validar venta
        if (detalleVenta.getVenta() == null || detalleVenta.getVenta().getId() == null) {
            errores.put("venta", "Campo obligatorio");
        } else if (!ventaRepository.existsById(detalleVenta.getVenta().getId())) {
            errores.put("venta", "No existe una venta con ID " + detalleVenta.getVenta().getId());
        }

        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Error en los datos del detalle de venta", errores)
            );
        }

        // Asignar relaciones
        productoRepository.findById(detalleVenta.getProducto().getId()).ifPresent(detalleVenta::setProducto);
        ventaRepository.findById(detalleVenta.getVenta().getId()).ifPresent(detalleVenta::setVenta);

        // Calcular subtotal
        detalleVenta.setSubtotal(detalleVenta.getCantidad() * detalleVenta.getPrecioUnitario());

        DetalleVenta nuevoDetalle = detalleVentaRepository.save(detalleVenta);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "éxito");
        respuesta.put("mensaje", "Detalle de venta creado correctamente");
        respuesta.put("id", nuevoDetalle.getId());
        respuesta.put("subtotal", nuevoDetalle.getSubtotal());
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDetalleVenta(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> requestBody) {

        if (requestBody == null || requestBody.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("ERROR: NO SE ENVIÓ NINGÚN DATO PARA ACTUALIZAR",
                            Map.of(
                                    "solución", "Debes incluir los campos a actualizar",
                                    "ejemplo", generarEjemploDetalleVenta()
                            )
                    )
            );
        }

        Optional<DetalleVenta> detalleOptional = detalleVentaRepository.findById(id);
        if (detalleOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    crearRespuestaError("DETALLE DE VENTA NO ENCONTRADO",
                            Map.of(
                                    "alerta", "No existe un detalle de venta con ID " + id,
                                    "solución", "Verifica el ID o consulta la venta asociada",
                                    "consulta_en", "GET /api/detallesventa/" + id
                            )
                    )
            );
        }

        DetalleVenta detalleExistente = detalleOptional.get();

        Set<String> camposPermitidos = Set.of("cantidad", "precioUnitario", "producto", "venta");
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

        boolean hayCambios = false;
        Map<String, String> erroresValidacion = new LinkedHashMap<>();

        // Validar y actualizar cantidad
        if (requestBody.containsKey("cantidad")) {
            Object cantidadObj = requestBody.get("cantidad");
            Integer cantidad = null;

            if (cantidadObj instanceof Integer) {
                cantidad = (Integer) cantidadObj;
            } else {
                erroresValidacion.put("cantidad", "La cantidad debe ser un número entero.");
            }

            if (cantidad != null) {
                if (cantidad <= 0) {
                    erroresValidacion.put("cantidad", "La cantidad debe ser mayor a 0.");
                } else if (!cantidad.equals(detalleExistente.getCantidad())) {
                    detalleExistente.setCantidad(cantidad);
                    hayCambios = true;
                }
            }
        }

        // Validar y actualizar precio unitario
        if (requestBody.containsKey("precioUnitario")) {
            Object precioObj = requestBody.get("precioUnitario");
            Double precioUnitario = null;

            if (precioObj instanceof Double || precioObj instanceof Integer) {
                precioUnitario = ((Number) precioObj).doubleValue();
            } else {
                erroresValidacion.put("precioUnitario", "El precio unitario debe ser un número válido.");
            }

            if (precioUnitario != null) {
                if (precioUnitario <= 0) {
                    erroresValidacion.put("precioUnitario", "El precio unitario debe ser mayor a 0.");
                } else if (!precioUnitario.equals(detalleExistente.getPrecioUnitario())) {
                    detalleExistente.setPrecioUnitario(precioUnitario);
                    hayCambios = true;
                }
            }
        }

        // Validar y actualizar producto
        if (requestBody.containsKey("producto")) {
            try {
                Map<String, Object> productoMap = (Map<String, Object>) requestBody.get("producto");
                if (productoMap == null || productoMap.get("id") == null) {
                    erroresValidacion.put("producto", "El producto debe incluir un ID válido.");
                } else {
                    Long productoId = ((Number) productoMap.get("id")).longValue();
                    if (!productoRepository.existsById(productoId)) {
                        erroresValidacion.put("producto", "No existe un producto con ID " + productoId + ".");
                    } else if (!productoId.equals(detalleExistente.getProducto().getId())) {
                        productoRepository.findById(productoId).ifPresent(detalleExistente::setProducto);
                        hayCambios = true;
                    }
                }
            } catch (Exception e) {
                erroresValidacion.put("producto", "Formato incorrecto para el producto.");
            }
        }

        // Validar y actualizar venta
        if (requestBody.containsKey("venta")) {
            try {
                Map<String, Object> ventaMap = (Map<String, Object>) requestBody.get("venta");
                if (ventaMap == null || ventaMap.get("id") == null) {
                    erroresValidacion.put("venta", "La venta debe incluir un ID válido.");
                } else {
                    Long ventaId = ((Number) ventaMap.get("id")).longValue();
                    if (!ventaRepository.existsById(ventaId)) {
                        erroresValidacion.put("venta", "No existe una venta con ID " + ventaId + ".");
                    } else if (!ventaId.equals(detalleExistente.getVenta().getId())) {
                        ventaRepository.findById(ventaId).ifPresent(detalleExistente::setVenta);
                        hayCambios = true;
                    }
                }
            } catch (Exception e) {
                erroresValidacion.put("venta", "Formato incorrecto para la venta.");
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

        // Recalcular subtotal si hubo cambios en cantidad o precio
        if (hayCambios && (requestBody.containsKey("cantidad") || requestBody.containsKey("precioUnitario"))) {
            detalleExistente.setSubtotal(detalleExistente.getCantidad() * detalleExistente.getPrecioUnitario());
        }

        DetalleVenta detalleActualizado = detalleVentaRepository.save(detalleExistente);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "éxito");
        respuesta.put("mensaje", "Detalle de venta actualizado correctamente");
        respuesta.put("id", detalleActualizado.getId());
        respuesta.put("subtotal", detalleActualizado.getSubtotal());

        return ResponseEntity.ok(respuesta);
    }

    // VER LISTA
    @GetMapping
    public ResponseEntity<?> listarTodosDetallesVenta() {
        List<DetalleVenta> detalles = detalleVentaRepository.findAll();

        if (detalles.isEmpty()) {
            Map<String, Object> respuesta = new LinkedHashMap<>();
            respuesta.put("status", "info");
            respuesta.put("mensaje", "No hay detalles de venta registrados");
            respuesta.put("sugerencia", "Use POST /api/detallesventas para crear un nuevo detalle");
            return ResponseEntity.ok(respuesta);
        }

        List<Map<String, Object>> detallesFormateados = new ArrayList<>();
        for (DetalleVenta dv : detalles) {
            Map<String, Object> detalleMap = new LinkedHashMap<>();
            detalleMap.put("cantidad", dv.getCantidad());
            detalleMap.put("precioUnitario", dv.getPrecioUnitario());
            detalleMap.put("subtotal", dv.getSubtotal());

            // Información básica del producto
            Map<String, Object> productoInfo = new LinkedHashMap<>();
            productoInfo.put("id", dv.getProducto().getId());
            productoInfo.put("nombre", dv.getProducto().getNombre());
            productoInfo.put("precio", dv.getProducto().getPrecio());
            detalleMap.put("producto", productoInfo);

            // Información básica de la venta
            Map<String, Object> ventaInfo = new LinkedHashMap<>();
            ventaInfo.put("id", dv.getVenta().getId());
            ventaInfo.put("fecha", dv.getVenta().getFecha());
            ventaInfo.put("total", dv.getVenta().getTotal());

            // Información básica del cliente en la venta
            if(dv.getVenta().getCliente() != null) {
                Map<String, Object> clienteInfo = new LinkedHashMap<>();
                clienteInfo.put("id", dv.getVenta().getCliente().getId());
                clienteInfo.put("nombre", dv.getVenta().getCliente().getNombre());
                ventaInfo.put("cliente", clienteInfo);
            }

            detalleMap.put("venta", ventaInfo);

            detallesFormateados.add(detalleMap);
        }

        return ResponseEntity.ok(Map.of(
                "ID_DETALLE", detalles.size(),
                "DATOS",detallesFormateados

        ));
    }

    // VER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDetalleVenta(@PathVariable Long id) {
        Optional<DetalleVenta> detalleVenta = detalleVentaRepository.findById(id);

        if (detalleVenta.isEmpty()) {
            Map<String, Object> detalles = new LinkedHashMap<>();
            detalles.put("error", "No existe un detalle de venta con ID " + id);
            detalles.put("sugerencia", "Verifique el ID o consulte la lista de detalles de venta");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Detalle de venta no encontrado", detalles));
        }

        DetalleVenta dv = detalleVenta.get();
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("id", dv.getId());
        respuesta.put("cantidad", dv.getCantidad());
        respuesta.put("precioUnitario", dv.getPrecioUnitario());
        respuesta.put("subtotal", dv.getSubtotal());

        // Información extendida del producto
        Map<String, Object> productoInfo = new LinkedHashMap<>();
        productoInfo.put("id", dv.getProducto().getId());
        productoInfo.put("nombre", dv.getProducto().getNombre());
        productoInfo.put("descripcion", dv.getProducto().getDescripcion());
        productoInfo.put("precio", dv.getProducto().getPrecio());
        productoInfo.put("stock", dv.getProducto().getStock());
        productoInfo.put("proveedor", dv.getProducto().getProveedor().getId());
        productoInfo.put("categoria", dv.getProducto().getCategoria().getId());
        respuesta.put("producto", productoInfo);

        // Información extendida de la venta
        Map<String, Object> ventaInfo = new LinkedHashMap<>();
        ventaInfo.put("id", dv.getVenta().getId());
        ventaInfo.put("fecha", dv.getVenta().getFecha());
        ventaInfo.put("total", dv.getVenta().getTotal());
        respuesta.put("venta", ventaInfo);

        // Información del cliente si existe
        if(dv.getVenta().getCliente() != null) {
            Map<String, Object> clienteInfo = new LinkedHashMap<>();
            clienteInfo.put("id", dv.getVenta().getCliente().getId());
            clienteInfo.put("nombre", dv.getVenta().getCliente().getNombre());
            clienteInfo.put("email", dv.getVenta().getCliente().getEmail());
            ventaInfo.put("cliente", clienteInfo);
        }



        return ResponseEntity.ok(Map.of(
                "DATOS", respuesta
        ));
    }

    // ELIMINAR DETALLE DE VENTA
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminarDetalleVenta(@PathVariable Long id) {
        if (!detalleVentaRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Detalle no encontrado",
                            Map.of("error", "No existe detalle con ID " + id)));
        }

        detalleVentaRepository.deleteById(id);
        return ResponseEntity.ok(Map.of(
                "status", "éxito",
                "mensaje", "Detalle de venta eliminado correctamente",
                "id_eliminado", id
        ));
    }
}