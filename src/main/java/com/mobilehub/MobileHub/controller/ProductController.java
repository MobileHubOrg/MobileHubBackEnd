package com.mobilehub.MobileHub.controller;

import com.mobilehub.MobileHub.dto.ProductDTO;
import com.mobilehub.MobileHub.dto.ProductSummaryDTO;
import com.mobilehub.MobileHub.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private  final ProductService productService;
    @PostMapping(path = "/add")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
        System.out.println("in addProduct");
        ProductDTO savedProduct = productService.addProduct(productDTO);
        return ResponseEntity.ok(savedProduct);
    }
    @GetMapping(path = "/products")
    public List<ProductSummaryDTO> getAllProducts() {
        return productService.getAllProducts();
    }
    @GetMapping(path = "/productsById/{productId}")
    public ProductSummaryDTO getProductById( @PathVariable Long productId){
        return productService.getProductById(productId);
    }
    @GetMapping(path = "/productsByCategory/{category}")
    public List<ProductSummaryDTO> filterProductsByCategory(@PathVariable String category){
        return  productService.filterProductsByCategory(category);
    }
    @GetMapping(path = "/productsByBrand/{brand}")
    public List<ProductSummaryDTO> filterProductsByBrand(@PathVariable String brand){
        return  productService.filterProductsByBrand(brand);
    }
    @GetMapping(path = "/filter")
    public List<ProductSummaryDTO> filterProducts( @RequestParam String category,
                                                   @RequestParam String brand,
                                                   @RequestParam Double minPrice,
                                                   @RequestParam Double maxPrice){
        return  productService.filterProducts(category,brand,minPrice,maxPrice);
    }
}
