package in.astro.service.impl;

import in.astro.dto.CartDto;
import in.astro.dto.CategoryDto;
import in.astro.dto.CategoryResponse;
import in.astro.entity.Category;
import in.astro.exceptions.APIException;
import in.astro.repository.CategoryRepository;
import in.astro.service.ICategoryService;
import in.astro.service.IProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private IProductService productService;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryDto createCategory(Category category) {
        Category categoryDB = categoryRepo.findByCategoryName(category.getCategoryName());
        if (categoryDB != null) {
            throw new APIException("Category with the name '" + category.getCategoryName() + "' already exists !!!");
        }
        categoryDB = categoryRepo.save(category);
        return modelMapper.map(categoryDB, CategoryDto.class);
    }

    @Override
    public CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> pageCategories = categoryRepo.findAll(pageDetails);
        List<Category> categories = pageCategories.getContent();

        if (categories.size() == 0) {
            throw new APIException("No category is created till now");
        }
        List<CategoryDto> categoryDTOs = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOs);
        categoryResponse.setPageNumber(pageCategories.getNumber());
        categoryResponse.setPageSize(pageCategories.getSize());
        categoryResponse.setTotalElements(pageCategories.getTotalElements());
        categoryResponse.setTotalPages(pageCategories.getTotalPages());
        categoryResponse.setLastPage(pageCategories.isLast());

        return categoryResponse;
    }

    @Override
    public CategoryDto updateCategory(Category category, Long categoryId) {
        Category savedCategory = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new APIException("Category with id '" + categoryId + "' not found"));
        category.setCategoryId(categoryId);
        savedCategory =  categoryRepo.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new APIException("Category with id '" + categoryId + "' not found"));
        category.getProducts().forEach(product -> productService.deleteProduct(product.getProductId()));
        categoryRepo.delete(category);
        return "Category with id '" + categoryId + "' deleted successfully";
    }
}
