package jpastudy.jpashop.domain.item;

import jpastudy.jpashop.domain.OrderItem;
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
    // 재고 수량 1개 증가
    public void addStock(int quantity)
    {
        this.stockQuantity += quantity;
    }
    // 재고 수량 1개 감소
    public void removeStock(int quantity)
    {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0)
        {

        }
        this.stockQuantity = restStock;
    }
}
