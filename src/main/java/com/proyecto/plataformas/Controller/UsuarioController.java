package com.proyecto.plataformas.Controller;


import com.proyecto.plataformas.Model.UsuarioEntity;
import com.proyecto.plataformas.Services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuario;
    @GetMapping
    public Mono<ResponseEntity<Flux<UsuarioEntity>>> get(){
        return Mono.just(ResponseEntity.ok(usuario.traer()));
    }
    @PostMapping
    public Mono<ResponseEntity<UsuarioEntity>> obt(@RequestBody Mono<UsuarioEntity> user){
        return usuario.obtener(user)
                .map(u -> ResponseEntity.status(HttpStatus.CREATED).body(u));
    }
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> delate(@PathVariable Long id){
        return usuario.eliminar(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<UsuarioEntity>> update(@PathVariable Long id, @RequestBody UsuarioEntity user){
        return usuario.update(id, user)
                .map(ResponseEntity::ok);
    }


}
