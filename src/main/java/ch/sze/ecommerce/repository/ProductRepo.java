package ch.sze.ecommerce.repository;

import ch.sze.ecommerce.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity, UUID> {
    boolean existsByName(String name);
}
