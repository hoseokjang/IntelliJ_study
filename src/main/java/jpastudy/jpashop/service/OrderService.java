package jpastudy.jpashop.service;

import jpastudy.jpashop.domain.*;
import jpastudy.jpashop.domain.item.Item;
import jpastudy.jpashop.repository.ItemRepository;
import jpastudy.jpashop.repository.MemberRepository;
import jpastudy.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Transactional
    // 주문
    public Long order(Long memberId, Long itemId, int count)
    {
        // Member 조회
        Member member = memberRepository.findOne(memberId);
        // Item 조회
        Item item = itemRepository.findOne(itemId);
        // Delivery 생성
        Delivery delivery = new Delivery();
        delivery.setStatus(DeliveryStatus.READY);
        delivery.setAddress(member.getAddress());
        // OrderItem 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        // Order 생성
        Order order = Order.createOrder(member, delivery, orderItem);
        orderRepository.save(order);
        return order.getId();
    }
    @Transactional
    // 주문취소 처리
    public void cancelOrder(Long orderId)
    {
        // 취소할 주문 정보 얻어오기
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
    }
    @Transactional
    // 주문 검색
    public List<Order> findOrders(OrderSearch orderSearch)
    {
        return orderRepository.findAll(orderSearch);
    }
}
