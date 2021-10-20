package jpastudy.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {
    // 회원 이름으로 검색을 하겠다.
    private String memberName;
    // 주문 상태로 검색을 하겠다. (Order와 Cancel 2개가 있음)
    private OrderStatus orderStatus;
}
