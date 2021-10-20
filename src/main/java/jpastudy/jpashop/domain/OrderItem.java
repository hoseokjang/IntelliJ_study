package jpastudy.jpashop.domain;

import jpastudy.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter @Setter
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    // Order와 N:1 관계
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    // Item과 N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 주문가격
    private int orderPrice;

    // 주문수량
    private int count;

    // * 비지니스 로직 * //
    // 생성 Method
    public static OrderItem createOrderItem(Item item, int count, int orderPrice)
    {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        item.removeStock(count); // 주문을 생성했으니 상품의 수량 감소
        return orderItem;
    }
    // 주문 취소 Method
    public void cancel()
    {
        item.addStock(this.count);
    }
    // 주문 상품 전체 가격 조회
    public int getTotalPrice()
    {
        return this.getOrderPrice() * this.getCount();
    }
}
