package in.astro.controller;

import in.astro.config.AppConstants;
import in.astro.dto.CartDto;
import in.astro.dto.CategoryDto;
import in.astro.dto.CategoryResponse;
import in.astro.entity.Category;
import in.astro.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@Tag(name = "Category Controller", description = "[Create Category, Get Categories, Update Category, Delete Category]")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping("/admin/category")
    @Operation(summary = "Create Category", description = "Create Category")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody Category category) {
        CategoryDto savedCategoryDTO = categoryService.createCategory(category);

        return new ResponseEntity<CategoryDto>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/categories")
    @Operation(summary = "Get Categories", description = "Get All Categories")
    public ResponseEntity<CategoryResponse> getCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        CategoryResponse categoryResponse = categoryService.getCategories(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{categoryId}")
    @Operation(summary = "Update Category", description = "Update Category")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody Category category,
                                                  @PathVariable Long categoryId) {
        CategoryDto categoryDTO = categoryService.updateCategory(category, categoryId);

        return new ResponseEntity<CategoryDto>(categoryDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    @Operation(summary = "Delete Category", description = "Delete Category")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        String status = categoryService.deleteCategory(categoryId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
