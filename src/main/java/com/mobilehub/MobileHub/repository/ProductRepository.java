package com.mobilehub.MobileHub.repository;

import com.mobilehub.MobileHub.model.Product;
import com.mobilehub.MobileHub.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByProductNameAndCategoryAndBrand(String productName, String category, String brand);
    Optional<Product> findProductByProductId(@NonNull Long productId);
    Optional<Product> findProductByProductName(@NonNull String productName);
    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByBrandIgnoreCase(String brand);
    List<Product> findByCategoryAndBrandIgnoreCase(String category, String brand);
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    @Query("SELECT p FROM Product p WHERE " +
            "(:category IS NULL OR LOWER(p.category) = LOWER(:category)) AND " +
            "(:brand IS NULL OR LOWER(p.brand) = LOWER(:brand)) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> findProductsByFilters(
            @Param("category") String category,
            @Param("brand") String brand,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );
}
