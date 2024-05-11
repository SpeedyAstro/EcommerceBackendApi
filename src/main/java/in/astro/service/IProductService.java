package in.astro.service;

import in.astro.dto.ProductDTO;
import in.astro.dto.ProductRequest;
import in.astro.dto.ProductResponse;
import in.astro.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IProductService {
    ProductDTO addProduct(Long categoryId, ProductRequest product, MultipartFile image) throws IOException;

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
                                     String sortOrder);

    ProductDTO updateProduct(Long productId, Product product);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

    ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
                                           String sortOrder);

    String deleteProduct(Long productId);

}
