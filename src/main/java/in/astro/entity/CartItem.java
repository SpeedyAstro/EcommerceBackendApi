package in.astro.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {
/*
    Each cart item represents a product in the cart.
    It's best to have a separate entity for cart items because it allows you to store additional information about the product in the cart.
    i.e. quantity, discount, product price, etc.
 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
    private double discount;
    private double productPrice;
}
