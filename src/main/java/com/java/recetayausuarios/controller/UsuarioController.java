
package com.java.recetayausuarios.controller;

import com.java.recetayausuarios.model.Usuario;
import com.java.recetayausuarios.service.UsuarioService;
import com.java.recetayausuarios.security.JwtUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtUtils jwtUtils;

    public UsuarioController(UsuarioService usuarioService,
                             JwtUtils jwtUtils) {
        this.usuarioService = usuarioService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario) {

        Usuario nuevoUsuario = usuarioService.guardar(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscaPorId(@PathVariable Long id) {

        return usuarioService.buscaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<Usuario> buscarPorCorreo(
            @PathVariable String correo) {

        return usuarioService.buscarPorCorreo(correo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {

        return usuarioService.actualizar(id, usuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (usuarioService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario datos) {

        // Validar correo y contraseña
        if (datos == null
                || datos.getCorreo() == null
                || datos.getCorreo().isBlank()
                || datos.getPassword() == null
                || datos.getPassword().isBlank()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                        "mensaje", "Correo y contraseña son obligatorios"
                    ));
        }

        Optional<Usuario> userOpt =
                usuarioService.buscarPorCorreo(datos.getCorreo());

        if (userOpt.isEmpty()
                || !usuarioService.validarPassword(
                        datos.getPassword(),
                        userOpt.get().getPassword())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Credenciales inválidas"));
        }

        Usuario user = userOpt.get();

        String token = jwtUtils.generarToken(
                user.getId(),
                user.getCorreo(),
                user.getRol().getNombre()
        );

        return ResponseEntity.ok(Map.of(
                "mensaje", "Login correcto",
                "token", token,
                "id", user.getId(),
                "nombre", user.getNombre(),
                "rol", user.getRol().getNombre()
        ));
    }
}
