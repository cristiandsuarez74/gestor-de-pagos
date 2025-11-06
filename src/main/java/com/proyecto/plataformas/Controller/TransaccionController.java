package com.proyecto.plataformas.Controller;

import com.proyecto.plataformas.Model.TransaccionEntity;
import com.proyecto.plataformas.Services.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;
import java.math.BigDecimal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/tr")
public class TransaccionController {
    private final TransaccionService service;
    @GetMapping
    public Mono<ResponseEntity<Flux<TransaccionEntity>>> get(){
        return Mono.just(ResponseEntity.ok(service.traer()));
    }

    @PostMapping
    public Mono<ResponseEntity<TransaccionEntity>> obt(@RequestBody Mono<TransaccionEntity> tran){
        return service.obtener(tran)
                .map(t -> ResponseEntity.status(HttpStatus.CREATED).body(t));
    }

    @PostMapping("/transferir")
    public Mono<ResponseEntity<TransaccionEntity>> transferir(@RequestBody Map<String, Object> request) {
        Long idCuentaOrigen;
        Long idCuentaDestino;
        BigDecimal monto;

        try {
            Object origenObj = request.get("idCuentaOrigen");
            Object destinoObj = request.get("idCuentaDestino");
            Object montoObj = request.get("monto");

            if (origenObj == null || destinoObj == null || montoObj == null) {
                return Mono.just(ResponseEntity.badRequest().build()); // Missing required fields
            }

            idCuentaOrigen = Long.valueOf(origenObj.toString());
            idCuentaDestino = Long.valueOf(destinoObj.toString());
            monto = new BigDecimal(montoObj.toString());
        } catch (NumberFormatException e) {
            return Mono.just(ResponseEntity.badRequest().body(null)); // Invalid number format
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()); // Other parsing errors
        }

        return service.realizarTransferencia(idCuentaOrigen, idCuentaDestino, monto)
                .map(transaccion -> ResponseEntity.ok(transaccion))
                .onErrorResume(e -> {
                    // Manejo de errores específicos
                    if (e.getMessage().contains("no encontradas")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                    } else if (e.getMessage().contains("Saldo insuficiente")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }


    @PutMapping("{id}")
    public Mono<ResponseEntity<TransaccionEntity>> update(@PathVariable Long id, @RequestBody TransaccionEntity transaccion){
        return service.update(id, transaccion)
                .map(ResponseEntity::ok);
    }
}
