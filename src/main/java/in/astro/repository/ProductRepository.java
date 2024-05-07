package in.astro.repository;

import in.astro.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    boolean existsByProductName(String productName);
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = ?1")
    Page<Product> findByCategory(Long categoryId, Pageable pageDetails);

    Page<Product> findByProductNameLike(String keyword, Pageable pageDetails);

}
