package in.astro.service.impl;

import in.astro.dto.CartDto;
import in.astro.dto.ProductDTO;
import in.astro.entity.Cart;
import in.astro.entity.CartItem;
import in.astro.entity.Product;
import in.astro.exceptions.APIException;
import in.astro.exceptions.ResourceNotFoundException;
import in.astro.repository.CartItemRepository;
import in.astro.repository.CartRepository;
import in.astro.repository.ProductRepository;
import in.astro.service.ICartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService implements ICartService {
    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CartDto addProductToCart(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart");
        }
        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }
        CartItem newCartItem = new CartItem();

        product.setQuantity(product.getQuantity() - quantity);
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
        cartItemRepo.save(newCartItem);
        CartDto cartDTO = modelMapper.map(cart, CartDto.class);

        List<ProductDTO> productDTOs = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

        cartDTO.setProducts(productDTOs);

        return cartDTO;
    }

    @Override
    public List<CartDto> getAllCarts() {
        List<Cart> carts = cartRepo.findAll();

        if (carts.size() == 0) {
            throw new APIException("No cart exists");
        }

        List<CartDto> cartDTOs = carts.stream().map(cart -> {
            CartDto cartDTO = modelMapper.map(cart, CartDto.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        return cartDTOs;
    }

    @Override
    public CartDto getCart(String emailId, Long cartId) {
        Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        CartDto cartDTO = modelMapper.map(cart, CartDto.class);

        List<ProductDTO> products = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

        cartDTO.setProducts(products);

        return cartDTO;
    }

    @Override
    public CartDto updateProductQuantityInCart(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }
        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
        product.setQuantity(product.getQuantity() + cartItem.getQuantity() - quantity);

        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setQuantity(quantity);
        cartItem.setDiscount(product.getDiscount());
        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * quantity));
        cartItem = cartItemRepo.save(cartItem);
        CartDto cartDTO = modelMapper.map(cart, CartDto.class);
        List<ProductDTO> productDTOs = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
        cartDTO.setProducts(productDTOs);
        return cartDTO;
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
        }
        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
        cartItem.setProductPrice(product.getSpecialPrice());
        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));
        cartItem = cartItemRepo.save(cartItem);
    }

    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }
        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
        Product product = cartItem.getProduct();
        product.setQuantity(product.getQuantity() + cartItem.getQuantity());
        cartItemRepo.deleteCartItemByProductIdAndCartId(cartId, productId);
        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";
    }
}
