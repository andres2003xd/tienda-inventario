package com.tiendainventario.controller;

import com.tiendainventario.model.Cliente;
import com.tiendainventario.repository.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    // Metodo para respuestas de error
    private Map<String, Object> crearRespuestaError(String mensaje, Object detalles) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "error");
        respuesta.put("mensaje", mensaje);
        respuesta.put("detalles", detalles);
        return respuesta;
    }

    // Metodo para ejemplo de cliente
    private Map<String, Object> generarEjemploCliente() {
        Map<String, Object> ejemplo = new LinkedHashMap<>();
        ejemplo.put("nombre", "Juan Pérez");
        ejemplo.put("direccion", "Calle 123 #45-67");
        ejemplo.put("telefono", "3001234567");
        ejemplo.put("email", "juan.perez@example.com");
        return ejemplo;
    }

    // CREAR
    @PostMapping
    public ResponseEntity<?> crearCliente(@RequestBody(required = false) Map<String, Object> requestBody) {
        if (requestBody == null) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("----DEBE ENVIAR LOS DATOS DEL CLIENTE CON SUS CAMPOS OBLIGATORIOS----",
                            Collections.singletonMap("ejemplo", generarEjemploCliente())
                    )
            );
        }

        // Validar campos
        Set<String> camposPermitidos = Set.of("nombre", "direccion", "telefono", "email");
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
                    "nombre: 'Juan Pérez'",
                    "direccion: 'Calle 123 #45-67'",
                    "telefono: '3001234567'",
                    "email: 'juan.perez@example.com'"
            ));
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("LOS NOMBRES DE LOS CAMPOS NO COINCIDEN CON LOS ESPERADOS", detalles)
            );
        }

        // Convertir y validar datos
        Cliente cliente;
        try {
            cliente = new ObjectMapper().convertValue(requestBody, Cliente.class);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Error en el formato de los datos",
                            Collections.singletonMap("EJEMPLO: ", generarEjemploCliente()))
            );
        }

        // Validaciones
        Map<String, String> errores = new LinkedHashMap<>();
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            errores.put("nombre", "Campo obligatorio, poner un nombre válido");
        } else if (!cliente.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,100}$")) {
            errores.put("nombre", "Solo puede contener letras y espacios (2-100 caracteres)");
        }

        if (cliente.getDireccion() == null || cliente.getDireccion().trim().isEmpty()) {
            errores.put("direccion", "Campo obligatorio, poner una dirección válida");
        } else if (cliente.getDireccion().length() < 5 || cliente.getDireccion().length() > 200) {
            errores.put("direccion", "Debe tener entre 5 y 200 caracteres");
        }

        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
            errores.put("telefono", "Campo obligatorio");
        } else if (!cliente.getTelefono().matches("^[0-9]{7,15}$")) {
            errores.put("telefono", "Debe contener solo números (7-15 dígitos)");
        }

        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            errores.put("email", "Campo obligatorio");
        } else if (!Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(cliente.getEmail()).matches()) {
            errores.put("email", "Formato de email inválido");
        }

        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Error en los datos del cliente", errores)
            );
        }

        Cliente nuevoCliente = clienteRepository.save(cliente);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "éxito");
        respuesta.put("mensaje", "Cliente creado correctamente");
        respuesta.put("id", nuevoCliente.getId());
        respuesta.put("nombre", nuevoCliente.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    //VER LISTA
    @GetMapping
    public ResponseEntity<?> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();

        if (clientes.isEmpty()) {
            Map<String, Object> respuesta = new LinkedHashMap<>();
            respuesta.put("status", "info");
            respuesta.put("mensaje", "No hay clientes registrados");
            respuesta.put("sugerencia", "Use POST /api/clientes para crear un nuevo cliente");
            return ResponseEntity.ok(respuesta);
        }

        List<Map<String, Object>> clientesFormateados = new ArrayList<>();
        for (Cliente c : clientes) {
            Map<String, Object> clienteMap = new LinkedHashMap<>();
            clienteMap.put("id", c.getId());
            clienteMap.put("nombre", c.getNombre());
            clienteMap.put("direccion", c.getDireccion());
            clienteMap.put("telefono", c.getTelefono());
            clienteMap.put("email", c.getEmail());
            clientesFormateados.add(clienteMap);
        }

        return ResponseEntity.ok(clientesFormateados);
    }


    //VER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCliente(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);

        if (cliente.isEmpty()) {
            Map<String, Object> detalles = new LinkedHashMap<>();
            detalles.put("error", "No existe un cliente con ID " + id);
            detalles.put("sugerencia", "Verifique el ID o consulte la lista de clientes en GET /api/clientes");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Cliente no encontrado", detalles));
        }

        Cliente c = cliente.get();
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("id", c.getId());
        respuesta.put("nombre", c.getNombre());
        respuesta.put("direccion", c.getDireccion());
        respuesta.put("telefono", c.getTelefono());
        respuesta.put("email", c.getEmail());

        return ResponseEntity.ok(respuesta);
    }

    //ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> requestBody) {

        if (requestBody == null || requestBody.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("ERROR: NO SE ENVIÓ NINGÚN DATO PARA ACTUALIZAR",
                            Map.of(
                                    "solución", "Debes incluir los campos a actualizar",
                                    "ejemplo", generarEjemploCliente()
                            )
                    )
            );
        }

        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    crearRespuestaError("CLIENTE NO ENCONTRADO",
                            Map.of(
                                    "alerta", "No existe un cliente con ID " + id,
                                    "solución", "Verifica el ID o consulta la lista de clientes",
                                    "Consulta en", "GET /api/clientes"
                            )
                    )
            );
        }

        Cliente clienteExistente = clienteOptional.get();
        boolean hayCambios = false;
        Map<String, String> errores = new LinkedHashMap<>();

        if (requestBody.containsKey("nombre")) {
            String nombre = (String) requestBody.get("nombre");
            if (nombre == null || nombre.trim().isEmpty()) {
                errores.put("nombre", "El nombre no puede estar vacío");
            } else if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,100}$")) {
                errores.put("nombre", "Solo puede contener letras y espacios (2-100 caracteres)");
            } else if (!nombre.equals(clienteExistente.getNombre())) {
                clienteExistente.setNombre(nombre);
                hayCambios = true;
            }
        }

        if (requestBody.containsKey("direccion")) {
            String direccion = (String) requestBody.get("direccion");
            if (direccion == null || direccion.trim().isEmpty()) {
                errores.put("direccion", "La dirección no puede estar vacía");
            } else if (direccion.length() < 5 || direccion.length() > 200) {
                errores.put("direccion", "Debe tener entre 5 y 200 caracteres");
            } else if (!direccion.equals(clienteExistente.getDireccion())) {
                clienteExistente.setDireccion(direccion);
                hayCambios = true;
            }
        }

        if (requestBody.containsKey("telefono")) {
            String telefono = (String) requestBody.get("telefono");
            if (telefono == null || telefono.trim().isEmpty()) {
                errores.put("telefono", "El teléfono no puede estar vacío");
            } else if (!telefono.matches("^[0-9]{7,15}$")) {
                errores.put("telefono", "Debe contener solo números (7-15 dígitos)");
            } else if (!telefono.equals(clienteExistente.getTelefono())) {
                clienteExistente.setTelefono(telefono);
                hayCambios = true;
            }
        }

        if (requestBody.containsKey("email")) {
            String email = (String) requestBody.get("email");
            if (email == null || email.trim().isEmpty()) {
                errores.put("email", "El email no puede estar vacío");
            } else if (!Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(email).matches()) {
                errores.put("email", "Formato de email inválido");
            } else if (!email.equals(clienteExistente.getEmail())) {
                clienteExistente.setEmail(email);
                hayCambios = true;
            }
        }

        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("Error en los datos del cliente", errores)
            );
        }

        if (!hayCambios) {
            return ResponseEntity.badRequest().body(
                    crearRespuestaError("No se realizaron cambios",
                            Map.of("mensaje", "Los valores enviados son iguales a los actuales"))
            );
        }

        Cliente clienteActualizado = clienteRepository.save(clienteExistente);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "éxito");
        respuesta.put("mensaje", "Cliente actualizado correctamente");
        respuesta.put("id", clienteActualizado.getId());
        return ResponseEntity.ok(respuesta);
    }

    // ELIMINAR CLIENTE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        if (!clienteRepository.existsById(id)) {
            Map<String, Object> detalles = new LinkedHashMap<>();
            detalles.put("error", "No existe un cliente con ID " + id);
            detalles.put("sugerencia", "Verifique el ID o consulte la lista de clientes en GET /api/clientes");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Cliente no encontrado", detalles));
        }


        if (clienteRepository.count() == 1) {
            Map<String, Object> detalles = new LinkedHashMap<>();
            detalles.put("error", "No se puede eliminar el cliente, PORQUE ESTA EN USO");
            detalles.put("sugerencia", "---Primero elimine la Venta---");
            detalles.put("DATO", "Busque en GET /api/ventas para ver la conexion entre clientes y ventas... TAMBIEN PUEDE ELIMINAR O CAMBIAR EL CLIENTE desde PUT /api/ventas/...");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(crearRespuestaError("NO SE PUEDE ELIMINAR EL CLIENTE", detalles));
        }


        clienteRepository.deleteById(id);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("status", "éxito");
        respuesta.put("mensaje", "Cliente eliminado correctamente");
        respuesta.put("id_eliminado", id);
        return ResponseEntity.ok(respuesta);
    }
}