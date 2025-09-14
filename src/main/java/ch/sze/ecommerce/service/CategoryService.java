package ch.sze.ecommerce.service;

import ch.sze.ecommerce.entity.Category;
import ch.sze.ecommerce.entity.dto.CategoryDTO;
import ch.sze.ecommerce.repository.CategoryRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    public Category getCategory(UUID uuid) {
        return categoryRepo.findById(uuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    public Category createCategory(Category category) {
        if (categoryRepo.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category name already exists");
        }
        return categoryRepo.save(category);
    }

    public Category updateCategory(UUID id, CategoryDTO dto) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        category.setName(dto.getName());
        return categoryRepo.save(category);
    }

    public void deleteCategory(UUID id) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        categoryRepo.delete(category);
    }
}
