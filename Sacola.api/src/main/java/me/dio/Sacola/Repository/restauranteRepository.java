package me.dio.Sacola.Repository;

import me.dio.Sacola.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface restauranteRepository extends JpaRepository<Restaurante, Long> {
}
