package com.proyecto.plataformas.Repositorys;

import com.proyecto.plataformas.Model.TransaccionEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TransaccionRepository extends ReactiveCrudRepository<TransaccionEntity, Long> {
}
