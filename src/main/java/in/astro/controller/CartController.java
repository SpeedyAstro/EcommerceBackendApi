package in.astro.controller;

import in.astro.dto.CartDto;
import in.astro.service.ICartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@Tag(name = "Cart Controller", description = "[🛒] Cart Controller")
public class CartController {
    @Autowired
    private ICartService cartService;

    @PostMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
    @Operation(summary = "Add Product to Cart", description = "[🛒] Add Product to Cart")
    public ResponseEntity<CartDto> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Integer quantity) {
        CartDto cartDTO = cartService.addProductToCart(cartId, productId, quantity);
        return new ResponseEntity<CartDto>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/admin/carts")
    @Operation(summary = "Get Carts", description = "[🛍️] Get All Carts")
    public ResponseEntity<List<CartDto>> getCarts() {
        List<CartDto> cartDTOs = cartService.getAllCarts();
        return new ResponseEntity<List<CartDto>>(cartDTOs, HttpStatus.FOUND);
    }

    @GetMapping("/public/users/{emailId}/carts/{cartId}")
    @Operation(summary = "Get Cart By User", description = "[🛒] Get Cart By emailId and cartId")
    public ResponseEntity<CartDto> getCartById(@PathVariable String emailId, @PathVariable Long cartId) {
        CartDto cartDTO = cartService.getCart(emailId, cartId);
        return new ResponseEntity<CartDto>(cartDTO, HttpStatus.FOUND);
    }

    @PutMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
    @Operation(summary = "Update Cart Product Quantity", description = "[📝] Update Cart Product Quantity")
    public ResponseEntity<CartDto> updateCartProduct(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Integer quantity) {
        CartDto cartDTO = cartService.updateProductQuantityInCart(cartId, productId, quantity);
        return new ResponseEntity<CartDto>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/public/carts/{cartId}/product/{productId}")
    @Operation(summary = "Delete Product From Cart", description = "[🗑️] Delete Product From Cart")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }

}
