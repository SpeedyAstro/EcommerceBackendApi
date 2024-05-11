package in.astro.controller;

import in.astro.config.AppConstants;
import in.astro.dto.ProductDTO;
import in.astro.dto.ProductRequest;
import in.astro.dto.ProductResponse;
import in.astro.entity.Product;
import in.astro.exceptions.APIException;
import in.astro.service.IProductService;
import in.astro.service.impl.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class ProductController {
    @Autowired
    private IProductService productService;

    @PostMapping("/admin/add/product")
    @Operation(summary = "Add Product", description = "Add Product to Category")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductRequest product) {
        if (product.getCategoryId() == null) {
            throw new APIException("Category Id is required");
        }
        ProductDTO savedProduct = productService.addProduct(product.getCategoryId(), product);

        return new ResponseEntity<ProductDTO>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    @Operation(summary = "Get Products", description = "Get All Products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    @Operation(summary = "Get Products By Category", description = "Get Products By Category")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    @Operation(summary = "Get Products By Keyword", description = "Get Products By Keyword")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }
    @PutMapping("/admin/products/{productId}")
    @Operation(summary = "Update Product", description = "Update Product")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody Product product,
                                                    @PathVariable Long productId) {
        ProductDTO updatedProduct = productService.updateProduct(productId, product);

        return new ResponseEntity<ProductDTO>(updatedProduct, HttpStatus.OK);
    }
    @PutMapping("/admin/products/{productId}/image")
    @Operation(summary = "Update Product Image", description = "Update Product Image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.updateProductImage(productId, image);

        return new ResponseEntity<ProductDTO>(updatedProduct, HttpStatus.OK);
    }

    @PostMapping("/admin/products/upload/image")
    @Operation(summary = "Upload Product Image", description = "Upload Product Image")
    public Map<?,?> uploadProductImage(@RequestParam("image") MultipartFile image) throws IOException {
        Map<?,?> map = productService.uploadImage(image);
        return map;
    }

    @DeleteMapping("/admin/products/{productId}")
    @Operation(summary = "Delete Product", description = "Delete Product")
    public ResponseEntity<String> deleteProductByCategory(@PathVariable Long productId) {
        String status = productService.deleteProduct(productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
