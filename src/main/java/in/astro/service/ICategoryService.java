package in.astro.service;

import in.astro.dto.CartDto;
import in.astro.dto.CategoryDto;
import in.astro.dto.CategoryResponse;
import in.astro.entity.Category;

public interface ICategoryService {
    CategoryDto createCategory(Category category);

    CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDto updateCategory(Category category, Long categoryId);

    String deleteCategory(Long categoryId);
}
