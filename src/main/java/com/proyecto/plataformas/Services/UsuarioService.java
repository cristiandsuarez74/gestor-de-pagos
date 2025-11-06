package com.proyecto.plataformas.Services;

import com.proyecto.plataformas.Model.UsuarioEntity;
import com.proyecto.plataformas.Repositorys.UsuarioRepository;
import com.proyecto.plataformas.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@RequiredArgsConstructor
@Service
public class UsuarioService {
    private final UsuarioRepository user;

    public Flux<UsuarioEntity> traer(){
        return user.findAll();
    }
    public Mono<UsuarioEntity> obtener(Mono<UsuarioEntity> usuario){
        return usuario.flatMap(user::save);
    }
    public Mono<Void> eliminar(Long id){
        return user.deleteById(id);
    }
    public Mono<UsuarioEntity> update(Long id, UsuarioEntity usuario) {
        return user.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Agente no encontrado con id: " + id)))
                .flatMap(enti -> {
                    enti.setCorreo(usuario.getCorreo());
                    enti.setNombreCompleto(usuario.getNombreCompleto());
                    enti.setTelefono(usuario.getTelefono());
                    enti.setNumeroDocumento(usuario.getNumeroDocumento());
                    return user.save(enti);

                });
    }
    }








