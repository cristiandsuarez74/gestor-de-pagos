package com.proyecto.plataformas.Controller;

import com.proyecto.plataformas.Model.CuentaEntity;
import com.proyecto.plataformas.Services.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {
    private final CuentaService service;
    @GetMapping
    public Mono<ResponseEntity<Flux<CuentaEntity>>> get(){
        return Mono.just(ResponseEntity.ok(service.traer()));

    }
    @PostMapping
    public Mono<ResponseEntity<CuentaEntity>> crearOActualizar(@RequestBody Mono<CuentaEntity> cuentaMono){
        return cuentaMono.flatMap(service::guardar)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c));
    }


    @PutMapping("{id}")
    public Mono<ResponseEntity<CuentaEntity>> update(@PathVariable Long id, @RequestBody CuentaEntity cuenta){
        return service.update(id, cuenta)
                .map(ResponseEntity::ok);
    }
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> delate(@PathVariable Long id){
        return service.eliminar(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

}
