package com.java.recetayausuarios.service;

import com.java.recetayausuarios.model.Rol;

import java.util.List;
import java.util.Optional;

public interface RolService {

    Rol guardar(Rol rol);

    List<Rol> obtenerTodos();

    Optional<Rol> buscaPorId(Long id);

    Optional<Rol> actualizar(Long id, Rol datos);

    boolean eliminar(Long id);
}