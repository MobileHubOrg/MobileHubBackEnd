package com.mobilehub.MobileHub.service;

import com.mobilehub.MobileHub.dto.ProductDTO;
import com.mobilehub.MobileHub.dto.ProductSummaryDTO;
import com.mobilehub.MobileHub.model.Product;
import com.mobilehub.MobileHub.repository.ProductRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converters;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    final ModelMapper modelMapper ;
    final ProductRepository productRepository;
    public ProductDTO addProduct(ProductDTO productDTO) {
        Optional<Product> existingProduct = productRepository.findByProductNameAndCategoryAndBrand(
                productDTO.getProductName(),
                productDTO.getCategory(),
                productDTO.getBrand()
        );

        if (existingProduct.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product already exists");
        }
        // Преобразуем DTO в сущность

        Product product = modelMapper.map(productDTO, Product.class);

        // Сохраняем продукт в базе данных
        Product savedProduct = productRepository.save(product);

        // Возвращаем сохраненный продукт в виде DTO
        return modelMapper.map(savedProduct, ProductDTO.class);
    }
    public List<ProductSummaryDTO> getAllProducts() {
List<Product> products=productRepository.findAll();
        return products.stream().map(product -> modelMapper.map(product,ProductSummaryDTO.class)).collect(Collectors.toList());
    }

    public ProductSummaryDTO getProductById(Long productId) {
        Optional<Product> existProduct=productRepository.findProductByProductId(productId);
        if (existProduct.isPresent()) {
          Product product =existProduct.get();
            return modelMapper.map(product,ProductSummaryDTO.class);
        }
     else {
        // Обработка ситуации, когда продукт не найден
        throw new NoSuchElementException("Product with ID " + productId + " not found");
    }
    }

    public List<ProductSummaryDTO> filterProductsByCategory(String category) {

        List<Product> products=productRepository.findByCategoryIgnoreCase(category);

        return products.stream().map(product -> modelMapper.map(product,ProductSummaryDTO.class)).collect(Collectors.toList());
    }

    public List<ProductSummaryDTO> filterProductsByBrand(String brand) {
        List<Product> products=productRepository.findByBrandIgnoreCase(brand);

        return products.stream().map(product -> modelMapper.map(product,ProductSummaryDTO.class)).collect(Collectors.toList());
    }

//    public List<ProductSummaryDTO> filterProducts(String category, String brand, Double minPrice, Double maxPrice) {
//        List<Product> products=productRepository.findByCategoryAndBrandIgnoreCase(category,brand);
//
//        return products.stream().map(product -> modelMapper.map(product,ProductSummaryDTO.class)).collect(Collectors.toList());
//    }
public List<ProductSummaryDTO> filterProducts(String category, String brand, Double minPrice, Double maxPrice) {
    List<Product> products = productRepository.findProductsByFilters(category, brand, minPrice, maxPrice);

    return products.stream()
            .map(product -> modelMapper.map(product, ProductSummaryDTO.class))
            .sorted(Comparator.comparing(ProductSummaryDTO::getPrice))
            .collect(Collectors.toList());
}
}
