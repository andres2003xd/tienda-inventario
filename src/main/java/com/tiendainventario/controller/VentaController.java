package com.tiendainventario.controller;

import com.tiendainventario.model.Venta;
import com.tiendainventario.repository.ClienteRepository;
import com.tiendainventario.repository.VentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Método para generar ejemplo de venta
    private Map<String, Object> generarEjemploVenta() {
        Map<String, Object> ejemplo = new LinkedHashMap<>();
        ejemplo.put("total", 50000.0);
        ejemplo.put("cliente", Collections.singletonMap("id", 1));
        return ejemplo;
    }

    // Método para respuestas de error
    private Map<String, Object> crearRespuestaError(String mensaje, Object detalles) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "error");
        respuesta.put("mensaje", mensaje);
        respuesta.put("detalles", detalles);
        return respuesta;
    }

    //CREAR
    @PostMapping
    public ResponseEntity<?> crearVenta(@RequestBody(required = false) Map<String, Object> requestBody) {
        if (requestBody == null) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("----DEBE ENVIAR LOS DATOS DE LA VENTA CON SUS CAMPOS OBLIGATORIOS----",
                            Collections.singletonMap("ejemplo", generarEjemploVenta())
                    )
            );
        }

        // Verificar nombres de campos correctos
        Set<String> camposEsperados = new HashSet<>(Arrays.asList("total", "cliente"));
        Map<String, String> erroresCampos = new LinkedHashMap<>();

        for (String campo : requestBody.keySet()) {
            if (!camposEsperados.contains(campo)) {
                erroresCampos.put(campo, "<-----NOMBRE DE CAMPO INCORRECTO");
            }
        }

        if (!erroresCampos.isEmpty()) {
            Map<String, Object> detalles = new LinkedHashMap<>();
            detalles.put("CAMPOS INCORRECTOS", erroresCampos);
            detalles.put("EJEMPLO CORRECTO", Arrays.asList(
                    "total: 50000.0",
                    "cliente: {id: 1}"
            ));
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("LOS NOMBRES DE LOS CAMPOS NO COINCIDEN CON LOS ESPERADOS", detalles)
            );
        }

        Venta venta;
        try {
            venta = new ObjectMapper().convertValue(requestBody, Venta.class);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Error en el formato de los datos",
                            Collections.singletonMap("EJEMPLO: ", generarEjemploVenta()))
            );
        }

        // Validaciones
        Map<String, String> errores = new LinkedHashMap<>();
        if (venta.getTotal() == null || venta.getTotal() <= 0) {
            errores.put("total", "Debe ser mayor a 0");
        }

        // Validar cliente
        if (venta.getCliente() == null || venta.getCliente().getId() == null) {
            errores.put("cliente", "Campo obligatorio");
        } else if (!clienteRepository.existsById(venta.getCliente().getId())) {
            errores.put("cliente", "No existe un cliente con ID " + venta.getCliente().getId());
        }

        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Error en los datos de la venta", errores)
            );
        }

        // Asignar cliente si existe
        clienteRepository.findById(venta.getCliente().getId()).ifPresent(venta::setCliente);
        venta.setFecha(LocalDateTime.now());

        Venta nuevaVenta = ventaRepository.save(venta);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "éxito");
        respuesta.put("mensaje", "Venta creada correctamente");
        respuesta.put("id", nuevaVenta.getId());
        respuesta.put("total", nuevaVenta.getTotal());
        respuesta.put("fecha", nuevaVenta.getFecha());
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    //VER LISTA
    @GetMapping
    public ResponseEntity<?> listarVentas() {
        List<Venta> ventas = ventaRepository.findAll();

        if (ventas.isEmpty()) {
            Map<String, Object> respuesta = new LinkedHashMap<>();
            respuesta.put("status", "info");
            respuesta.put("mensaje", "No hay ventas registradas");
            respuesta.put("sugerencia", "Use POST /api/ventas para crear una nueva venta");
            return ResponseEntity.ok(respuesta);
        }

        List<Map<String, Object>> ventasFormateadas = new ArrayList<>();
        for (Venta v : ventas) {
            Map<String, Object> ventaMap = new LinkedHashMap<>();
            ventaMap.put("id", v.getId());
            ventaMap.put("total", v.getTotal());
            ventaMap.put("fecha", v.getFecha());
            ventaMap.put("cliente", v.getCliente());
            ventasFormateadas.add(ventaMap);
        }

        return ResponseEntity.ok(ventasFormateadas);
    }

    //VER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVenta(@PathVariable Long id) {
        Optional<Venta> venta = ventaRepository.findById(id);

        if (venta.isEmpty()) {
            Map<String, Object> detalles = new LinkedHashMap<>();
            detalles.put("error", "No existe una venta con ID " + id);
            detalles.put("sugerencia", "Verifique el ID o consulte la lista de ventas en GET /api/ventas");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Venta no encontrada", detalles));
        }

        Venta v = venta.get();
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("id", v.getId());
        respuesta.put("total", v.getTotal());
        respuesta.put("fecha", v.getFecha());
        respuesta.put("cliente", v.getCliente());

        return ResponseEntity.ok(respuesta);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVenta(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> requestBody) {

        if (requestBody == null || requestBody.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("ERROR: NO SE ENVIÓ NINGÚN DATO PARA ACTUALIZAR",
                            Map.of(
                                    "solución", "Debes incluir los campos a actualizar",
                                    "ejemplo", generarEjemploVenta()
                            )
                    )
            );
        }

        Optional<Venta> ventaOptional = ventaRepository.findById(id);
        if (ventaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    crearRespuestaError("VENTA NO ENCONTRADA",
                            Map.of(
                                    "alerta", "No existe una venta con ID " + id,
                                    "solución", "Verifica el ID o consulta la lista de ventas",
                                    "Consulta en", "GET /api/ventas"
                            )
                    )
            );
        }

        Venta ventaExistente = ventaOptional.get();

        Set<String> camposPermitidos = Set.of("total", "cliente");
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
        Venta ventaActualizada = mapper.convertValue(requestBody, Venta.class);

        boolean hayCambios = false;
        Map<String, String> erroresValidacion = new LinkedHashMap<>();

        // Validar y actualizar total
        if (requestBody.containsKey("total")) {
            Object totalObj = requestBody.get("total");
            Double total = null;

            if (totalObj instanceof Double || totalObj instanceof Integer) {
                total = ((Number) totalObj).doubleValue();
            } else {
                erroresValidacion.put("total", "El total debe ser un número válido.");
            }

            if (total != null) {
                if (total <= 0) {
                    erroresValidacion.put("total", "El total debe ser mayor a 0.");
                } else if (!total.equals(ventaExistente.getTotal())) {
                    ventaExistente.setTotal(total);
                    hayCambios = true;
                }
            }
        }

        // Validar y actualizar cliente
        if (requestBody.containsKey("cliente")) {
            try {
                Map<String, Object> clienteMap = (Map<String, Object>) requestBody.get("cliente");
                if (clienteMap == null || clienteMap.get("id") == null) {
                    erroresValidacion.put("cliente", "El cliente debe incluir un ID válido.");
                } else {
                    Long clienteId = ((Number) clienteMap.get("id")).longValue();
                    if (!clienteRepository.existsById(clienteId)) {
                        erroresValidacion.put("cliente", "No existe un cliente con ID " + clienteId + ".");
                    } else if (!clienteId.equals(ventaExistente.getCliente().getId())) {
                        clienteRepository.findById(clienteId).ifPresent(ventaExistente::setCliente);
                        hayCambios = true;
                    }
                }
            } catch (Exception e) {
                erroresValidacion.put("cliente", "Formato incorrecto para el cliente.");
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

        Venta ventaGuardada = ventaRepository.save(ventaExistente);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "éxito");
        respuesta.put("mensaje", "Venta actualizada correctamente.");
        respuesta.put("id", ventaGuardada.getId());
        respuesta.put("total", ventaGuardada.getTotal());
        respuesta.put("cliente", ventaGuardada.getCliente());

        return ResponseEntity.ok(respuesta);
    }

    // ELIMINAR VENTA
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminarVenta(@PathVariable Long id) {
        if (!ventaRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Venta no encontrada",
                            Map.of("error", "No existe venta con ID " + id)));
        }

        // Verificar detalles de venta asociados
        if (ventaRepository.existsDetallesByVentaId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(crearRespuestaError("No se puede eliminar la venta",
                            Map.of("razón", "Existen detalles de venta asociados",
                                    "solución", "Elimine primero los detalles relacionados")));
        }

        ventaRepository.deleteById(id);
        return ResponseEntity.ok(Map.of(
                "status", "éxito",
                "mensaje", "Venta eliminada correctamente",
                "id_eliminado", id
        ));
    }
}
