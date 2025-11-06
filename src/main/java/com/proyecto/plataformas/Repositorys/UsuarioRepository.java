package com.proyecto.plataformas.Repositorys;

import com.proyecto.plataformas.Model.UsuarioEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UsuarioRepository extends ReactiveCrudRepository<UsuarioEntity, Long> {
}
