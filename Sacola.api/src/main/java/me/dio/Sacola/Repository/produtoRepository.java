package me.dio.Sacola.Repository;

import me.dio.Sacola.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface produtoRepository extends JpaRepository<Produto, Long> {
}
