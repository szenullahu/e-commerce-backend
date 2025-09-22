package ch.sze.ecommerce.service;

import ch.sze.ecommerce.entity.Category;
import ch.sze.ecommerce.entity.Product;
import ch.sze.ecommerce.entity.dto.ProductDTO;
import ch.sze.ecommerce.repository.CategoryRepo;
import ch.sze.ecommerce.repository.ProductRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProduct(UUID id) {
        return productRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    public Product createProduct(ProductDTO dto) {
        if (productRepo.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Product name already exists");
        }
        Category category = categoryRepo.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImage(dto.getImage());
        product.setCategory(category);
        return productRepo.save(product);
    }

    public Product updateProduct(UUID id, ProductDTO dto) {
        Product product = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setImage(dto.getImage());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        Category category = categoryRepo.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        return productRepo.save(product);
    }

    public void deleteProduct(UUID id) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        categoryRepo.delete(category);
    }
}
