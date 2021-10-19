package jpastudy.jpashop.domain;

import jpastudy.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
// springframework import를 추천 -> 할 수 있는 것이 더 많아짐
// Transaction Annotaion이 없으면 Error 발생
class EntityTest {
    @Autowired
    EntityManager em;

    @Test @Rollback(value = false) // true가 default
    public void entity() throws Exception
    {   // Test case는 작업을 하고 Commit 하지 않고 Rollback을 시켜버림 -> Annotaion을 주면 Rollback을 하지말라고 설정할 수 있음.
        // Member 생성
        Member member = new Member();
        member.setName("몽타");
        // Address 생성
        Address address = new Address("서울시","동작구","12345");
        // Member에 Address 연결
        member.setAddress(address);
        // 영속성 컨텍스트에 저장
        em.persist(member);

        // Order 생성
        Order order = new Order();
        order.setMember(member);
        em.persist(order);

        // Delivery 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); // 회원이 가진 address 정보를 가져오는 것이 맞음
        delivery.setStatus(DeliveryStatus.READY);
        // Order & Delivery 연결
        order.setDelivery(delivery);
        // Delivery는 persist할 필요 없음 -> order에서 cascade를 했기 때문에 order만 하면 됨

        // Item Book 생성
        Book book = new Book();
        book.setName("부트책");
        book.setAuthor("김김김");
        book.setIsbn("1234-ab");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        // OrderItem 생성
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(book);
        orderItem.setCount(2);
        orderItem.setOrderPrice(20000);
        // order & OrderItem 연결
        order.addOrderItem(orderItem);
        // 날짜
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ORDER);
        // 영속성 컨텍스트에 저장
        em.persist(orderItem);
    }
}