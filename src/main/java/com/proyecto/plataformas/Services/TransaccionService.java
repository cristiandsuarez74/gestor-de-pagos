package com.proyecto.plataformas.Services;

import com.proyecto.plataformas.Model.CuentaEntity; // Nueva importación
import com.proyecto.plataformas.Model.TransaccionEntity;
import com.proyecto.plataformas.Repositorys.TransaccionRepository;
import com.proyecto.plataformas.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Nueva importación
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal; // Nueva importación


@Service
@RequiredArgsConstructor
public class TransaccionService {
    private final TransaccionRepository transs;
    private final CuentaService cuentaService; // Nueva dependencia

    public Flux<TransaccionEntity> traer() {
        return transs.findAll();
    }

    public Mono<TransaccionEntity> obtener(Mono<TransaccionEntity> enti){
        return enti.flatMap(transs::save);
    }

    @Transactional
    public Mono<TransaccionEntity> realizarTransferencia(Long idCuentaOrigen, Long idCuentaDestino, BigDecimal monto) {
        return Mono.zip(
                cuentaService.obtener(idCuentaOrigen),
                cuentaService.obtener(idCuentaDestino)
            )
            .switchIfEmpty(Mono.error(new RuntimeException("Una o ambas cuentas no encontradas.")))
            .flatMap(tuple -> {
                CuentaEntity cuentaOrigen = tuple.getT1();
                CuentaEntity cuentaDestino = tuple.getT2();

                if (cuentaOrigen == null || cuentaDestino == null) {
                    return Mono.error(new RuntimeException("Una o ambas cuentas no encontradas."));
                }


                if (cuentaOrigen.getSaldo().compareTo(monto) < 0) {
                    return Mono.error(new RuntimeException("Saldo insuficiente en la cuenta de origen."));
                }
                cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(monto));
                cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(monto));

                return Mono.when(
                        cuentaService.guardar(cuentaOrigen),
                        cuentaService.guardar(cuentaDestino)
                    )
                    .then(Mono.defer(() -> {
                        TransaccionEntity nuevaTransaccion = new TransaccionEntity();
                        nuevaTransaccion.setCuentaOrigenId(idCuentaOrigen);
                        nuevaTransaccion.setCuentaDestinoId(idCuentaDestino);
                        nuevaTransaccion.setMonto(monto);
                        nuevaTransaccion.setTipoTransaccion("TRANSFERENCIA"); // Añadido
                        nuevaTransaccion.setEstado("COMPLETADA"); // Añadido
                        // Puedes añadir más campos como fecha, tipo de transacción, etc.
                        return transs.save(nuevaTransaccion);
                    }));
            });
    }


    public Mono<TransaccionEntity> update(Long id, TransaccionEntity transaccion) {
        return transs.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Transaccion no encontrada con id: " + id)))
                .flatMap(t -> {
                    t.setTipoTransaccion(transaccion.getTipoTransaccion());
                    t.setMonto(transaccion.getMonto());
                    t.setEstado(transaccion.getEstado());
                    t.setCuentaDestinoId(transaccion.getCuentaDestinoId());
                    t.setCuentaOrigenId(transaccion.getCuentaOrigenId());
                    return transs.save(t);
                });
    }
    public Mono<Void> eliminar(Long id){
        return transs.deleteById(id);
    }

}

