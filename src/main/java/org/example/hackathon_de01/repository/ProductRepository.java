package org.example.hackathon_de01.repository;

import org.example.hackathon_de01.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByCategory_NameIgnoreCase(String categoryName);
    Optional<Product> findByNameIgnoreCase(String name);

}
