package com.java.recetayausuarios.service;

import com.java.recetayausuarios.model.Rol;
import com.java.recetayausuarios.repository.RolRepository;
import com.java.recetayausuarios.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    private final RolRepository repository;
    private final UsuarioRepository usuarioRepository;

    public RolServiceImpl(RolRepository repository,
                          UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Rol guardar(Rol rol) {

        if (rol.getNombre() == null || rol.getNombre().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El nombre del rol es obligatorio");
        }

        if (repository.findByNombre(rol.getNombre()).isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "El rol ya existe");
        }

        rol.setId(null);
        return repository.save(rol);
    }

    @Override
    public List<Rol> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public Optional<Rol> buscaPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Rol> actualizar(Long id, Rol datos) {

        if (datos.getNombre() == null || datos.getNombre().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El nombre del rol es obligatorio");
        }

        Optional<Rol> existente = repository.findByNombre(datos.getNombre());

        if (existente.isPresent() && !existente.get().getId().equals(id)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "El rol ya existe");
        }

        return repository.findById(id)
            .map(rol -> {
                rol.setNombre(datos.getNombre());
                return repository.save(rol);
            });
    }

    @Override
    public boolean eliminar(Long id) {

        if (!repository.existsById(id)) {
            return false;
        }

        if (usuarioRepository.existsByRol_Id(id)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "No se puede eliminar un rol con usuarios asociados");
        }

        repository.deleteById(id);
        return true;
    }
}