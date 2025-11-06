package com.proyecto.plataformas.Repositorys;

import com.proyecto.plataformas.Model.CuentaEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CuentaRepository extends ReactiveCrudRepository<CuentaEntity, Long> {
}
