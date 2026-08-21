package org.example.hackathon_de01.repository;

import org.example.hackathon_de01.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
// CategoryRepository
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
