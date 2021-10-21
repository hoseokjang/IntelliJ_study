package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.Order;
import jpastudy.jpashop.domain.OrderItem;
import jpastudy.jpashop.domain.OrderSearch;
import jpastudy.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
  One-To-Many 관계 성능 최적화
  Order -> OrderItem -> Item
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    /*
        v1 : Entity를 API에 직접 노출하는 방식
        N+1 문제 발생함
        Order 1번 + Member, Delivery N번
        OrderItem N번 (Order의 Raw수)
        Item N번 (OrderItem의 Raw수)
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems(); // ToMany
            orderItems.stream().forEach(orderItem -> orderItem.getItem().getName()); //Lazy 강제초기화
        }
        return all;
    }
    /*
        v2 : Entity를 Dto로 변환해서 노출하는 방식
        N+1 문제는 여전히 존재
     */
}
