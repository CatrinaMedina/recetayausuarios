
package com.java.recetayausuarios.service;

import com.java.recetayausuarios.model.Usuario;
import com.java.recetayausuarios.model.Rol;
import com.java.recetayausuarios.repository.UsuarioRepository;
import com.java.recetayausuarios.repository.RolRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Locale;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository repository,
                              RolRepository rolRepository,
                              PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario guardar(Usuario usuario) {

        validarUsuario(usuario);

        if (repository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El correo ya está registrado");
        }

        Rol rol = buscarRol(usuario.getRol().getId());

        usuario.setId(null);
        usuario.setRol(rol);
        usuario.setPassword(
            passwordEncoder.encode(usuario.getPassword()));

        return repository.save(usuario);
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public Optional<Usuario> buscaPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return repository.findByCorreo(correo);
    }

    @Override
    public Optional<Usuario> actualizar(Long id, Usuario datos) {

        validarUsuario(datos);

        Optional<Usuario> usuarioCorreo =
                repository.findByCorreo(datos.getCorreo());

        if (usuarioCorreo.isPresent()
                && !usuarioCorreo.get().getId().equals(id)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El correo ya está registrado");
        }

        Rol rol = buscarRol(datos.getRol().getId());

        return repository.findById(id)
            .map(usuario -> {
                usuario.setNombre(datos.getNombre());
                usuario.setCorreo(datos.getCorreo());
                usuario.setRol(rol);
                usuario.setPassword(
                    passwordEncoder.encode(datos.getPassword()));

                return repository.save(usuario);
            });
    }

    @Override
    public boolean eliminar(Long id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public boolean validarPassword(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }

    private void validarUsuario(Usuario usuario) {

        if (usuario == null
                || usuario.getNombre() == null
                || usuario.getNombre().isBlank()
                || usuario.getCorreo() == null
                || usuario.getCorreo().isBlank()
                || usuario.getPassword() == null
                || usuario.getPassword().isBlank()
                || usuario.getRol() == null
                || usuario.getRol().getId() == null) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Nombre, correo, contraseña y rol son obligatorios");
        }

        String correo = usuario.getCorreo()
                .trim()
                .toLowerCase(Locale.ROOT);

        usuario.setCorreo(correo);

        if (!correo.matches(
                "^[\\w.+-]+@(gmail\\.com|duocuc\\.cl|hotmail\\.com|recetaya\\.cl)$")) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Solo se permiten correos @gmail.com, @duocuc.cl, @hotmail.com o @recetaya.cl");
        }

        if (usuario.getPassword().length() < 8) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La contraseña debe tener al menos 8 caracteres");
        }
    }

    private Rol buscarRol(Long id) {

        return rolRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Rol no encontrado"));
    }
}
