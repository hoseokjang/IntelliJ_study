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
}
