package jpastudy.jpashop.domain.item;

import jpastudy.jpashop.domain.OrderItem;
import jpastudy.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    // OrderItem과 1:N 관계
    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems = new ArrayList<>();

    // 상품가격
    private int price;

    // 상품이름
    private String name;

    // 재고 수량
    private int stockQuantity;

    // * Businese Logic *//
    // 주문이 취소되어 재고 수량 증가
    public void addStock(int quantity)
    {
        this.stockQuantity += quantity;
    }
    // 주문이 체결되어 재고 수량 감소
    public void removeStock(int quantity)
    {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0)
        {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
