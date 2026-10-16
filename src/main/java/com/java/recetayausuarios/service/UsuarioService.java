package com.java.recetayausuarios.service;

import com.java.recetayausuarios.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    Usuario guardar(Usuario usuario);

    List<Usuario> obtenerTodos();

    Optional<Usuario> buscaPorId(Long id);

    Optional<Usuario> buscarPorCorreo(String correo);

    Optional<Usuario> actualizar(Long id, Usuario datos);

    boolean eliminar(Long id);

    boolean validarPassword(String raw, String encoded);
}