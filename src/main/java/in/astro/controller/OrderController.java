package in.astro.controller;

import in.astro.config.AppConstants;
import in.astro.dto.OrderDTO;
import in.astro.dto.OrderResponse;
import in.astro.service.IOrderService;
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
public class OrderController {
    @Autowired
    public IOrderService orderService;

    @PostMapping("/public/users/{emailId}/carts/{cartId}/payments/{paymentMethod}/order")
    @Operation(summary = "Order Products", description = "[📦] Order Products")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String emailId, @PathVariable Long cartId, @PathVariable String paymentMethod) {
        OrderDTO order = orderService.placeOrder(emailId, cartId, paymentMethod);

        return new ResponseEntity<OrderDTO>(order, HttpStatus.CREATED);
    }

    @GetMapping("/admin/orders")
    @Operation(summary = "Get Orders", description = "[📦] Get All Orders")
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        OrderResponse orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.OK);
    }

    @GetMapping("public/users/{emailId}/orders")
    @Operation(summary = "Get Orders By User", description = "[📦] Get Orders By User emailId")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable String emailId) {
        List<OrderDTO> orders = orderService.getOrdersByUser(emailId);

        return new ResponseEntity<List<OrderDTO>>(orders, HttpStatus.OK);
    }

    @GetMapping("public/users/{emailId}/orders/{orderId}")
    @Operation(summary = "Get Order By User", description = "[📦] Get Order By User emailId and orderId")
    public ResponseEntity<OrderDTO> getOrderByUser(@PathVariable String emailId, @PathVariable Long orderId) {
        OrderDTO order = orderService.getOrder(emailId, orderId);

        return new ResponseEntity<OrderDTO>(order, HttpStatus.OK);
    }

    @PutMapping("admin/users/{emailId}/orders/{orderId}/orderStatus/{orderStatus}")
    @Operation(summary = "Update Order Status", description = "[🔄] Update Order Status")
    public ResponseEntity<OrderDTO> updateOrderByUser(@PathVariable String emailId, @PathVariable Long orderId, @PathVariable String orderStatus) {
        OrderDTO order = orderService.updateOrder(emailId, orderId, orderStatus);

        return new ResponseEntity<OrderDTO>(order, HttpStatus.OK);
    }
}
