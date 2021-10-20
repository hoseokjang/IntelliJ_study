package jpastudy.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long id;

    // Member와 N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // Delivery와 1:1 관계
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    // OrderItem과 1:N 관계
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 주문 날짜와 시간
    private LocalDateTime orderDate;

    // 주문 상태
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // * 연관 관계 메소드 * //
    public void setMember(Member member)
    {   // Order & Member two way N:1
        this.member = member;
        member.getOrders().add(this);
    }
    public void setDelivery(Delivery delivery)
    {   // Order & Delivery two way 1:1
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    public void addOrderItem(OrderItem orderItem)
    {   // Order & OrderItems two way 1:N
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // * 비즈니스 로직 *//
    // 주문 생성 Method
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems)
    {
        Order order = new Order();
        order.setMember(member); // 주문과 회원 연결
        order.setDelivery(delivery); // 주문과 배송 연결
        for (OrderItem orderItem: orderItems)
        {   // 주문과 주문 상품들 연결
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    // 주문 취소 Method
    public void cancel()
    {
        if (this.delivery.getStatus() == DeliveryStatus.COMP)
        {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }
        this.setOrderStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem: orderItems)
        {
            orderItem.cancel(); // cancel에 item 수량 증가도 들어가 있음
        }
    }
    // 전체 가격 조회
    public int getTotalPrice()
    {
        int totalPrice = 0;
        for(OrderItem orderItem: orderItems)
        {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
