package in.astro.service;

import in.astro.dto.OrderDTO;
import in.astro.dto.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderDTO placeOrder(String emailId, Long cartId, String paymentMethod);

    OrderDTO getOrder(String emailId, Long orderId);

    List<OrderDTO> getOrdersByUser(String emailId);

    OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    OrderDTO updateOrder(String emailId, Long orderId, String orderStatus);
}
