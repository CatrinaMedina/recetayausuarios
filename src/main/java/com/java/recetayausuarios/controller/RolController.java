
package com.java.recetayausuarios.controller;

import com.java.recetayausuarios.model.Rol;
import com.java.recetayausuarios.service.RolService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @PostMapping
    public ResponseEntity<Rol> guardar(@RequestBody Rol rol) {

        Rol nuevoRol = rolService.guardar(rol);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRol);
    }

    @GetMapping
    public List<Rol> obtenerTodos() {
        return rolService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> buscaPorId(@PathVariable Long id) {

        return rolService.buscaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizar(
            @PathVariable Long id,
            @RequestBody Rol rol) {

        return rolService.actualizar(id, rol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (rolService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
