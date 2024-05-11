package in.astro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String productName;
    private String description;
    private String brand;
    private Long categoryId;
    private double price;
    private Integer quantity;
    private double discount;
    private String imageUrl;
}
