package in.astro.repository;

import in.astro.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.email = ?1 AND o.orderId = ?2")
    Order findOrderByEmailAndOrderId(String email, Long cartId);
    List<Order> findAllByEmail(String emailId);
}
