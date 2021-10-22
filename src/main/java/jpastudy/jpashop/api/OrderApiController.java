package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.*;
import jpastudy.jpashop.repository.OrderRepository;
import jpastudy.jpashop.repository.order.query.OrderQueryDto;
import jpastudy.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

/*
  One-To-Many 관계 성능 최적화
  Order -> OrderItem -> Item
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

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
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2()
    {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        return orders.stream()
                .map(order -> new OrderDto(order))
                .collect(toList());
    }
    // -- 응답과 요청에 사용할 DTO Inner Class 선언 -- //
    @Data
    static class OrderItemDto {
        private String itemName; //상품 명
        private int orderPrice; //주문 가격
        private int count; //주문 수량
        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
    // order 결과를 담아놓은 DTO
    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;
        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getOrderStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream() // Stream<OrderItem>
                    .map(orderItem -> new OrderItemDto(orderItem)) // Stream<OrderItemDto>
                    .collect(toList()); // List<OrderItemDto>
        }
    }
    /*
        v3 : fetch join 최적화
        문제점 : *ToMany 의존관계 객체들의 페이징 처리가 불가
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        orders.forEach(order -> System.out.println(order));
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }
    /*
        v3.1 : fetch join의 페이징 처리 문제 해결
        batch_fetch_size를 정해줘서 데이터 개수를 몇 개 가져올지 정해줌 (100~1000을 권장)
        // version3에서는 쿼리가 7번 날아갔지만 version3.1에서는 쿼리가 3번만 날아감
     */
    @GetMapping("/api/v3_1/orders")
    public List<OrderDto> orderV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                       @RequestParam(value = "limit", defaultValue = "100") int limit)
    {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> orderDtoList = orders.stream() // Stream<Order>
                .map(order -> new OrderDto(order)) // Stream<OrderDto>
                .collect(toList()); // List<OrderDto>
        return orderDtoList; // ctrl + alt + v로 변수로 생성 가능
    }
    /*
     * V4 : 쿼리를 수행할 때 Dto를 저장했기 때문에 그대로 사용하면 된다. (return Type도 Dto로 받으면된다)
     */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrdersQueryDtos();
    }
    /*
     * V5 : 쿼리를 수행할 때 Dto를 저장했기 때문에 그대로 사용하면 된다
     * 쿼리 횟수를 줄이기 위해 스트림의 Groupby 기능 사용한 메소드 호출
     */
    @GetMapping("/api/v5/orders")
    public  List<OrderQueryDto> orderV5()
    {
        return orderQueryRepository.findOrdersQueryDtos_optimize_before();
    }
    @GetMapping("/api/v5_1/orders")
    public List<OrderQueryDto> ordersV5_1() {
        return orderQueryRepository.findOrdersQueryDtos_optimize_after();
    }
}
