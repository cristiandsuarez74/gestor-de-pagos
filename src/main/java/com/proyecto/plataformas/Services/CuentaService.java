package com.proyecto.plataformas.Services;

import com.proyecto.plataformas.Model.CuentaEntity;
import com.proyecto.plataformas.Repositorys.CuentaRepository;
import com.proyecto.plataformas.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class CuentaService {
    private final CuentaRepository cuentaRepository;

    public Flux<CuentaEntity> traer() {
        return cuentaRepository.findAll();
    }

    public Mono<CuentaEntity> obtener(Long id) { // Changed from String to Long
        return cuentaRepository.findById(id);
    }

    public Mono<CuentaEntity> guardar(CuentaEntity cuenta) {
        return cuentaRepository.save(cuenta);
    }


    public Mono<CuentaEntity> update(Long id, CuentaEntity cuenta) {
        return cuentaRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Cuenta no encontrada con id: " + id)))
                .flatMap(c -> {
                    c.setUsuarioId(cuenta.getUsuarioId());
                    c.setNumeroCuenta(cuenta.getNumeroCuenta());
                    c.setSaldo(cuenta.getSaldo());
                    c.setMoneda(cuenta.getMoneda());

                    return cuentaRepository.save(c);
                });
    }

}
