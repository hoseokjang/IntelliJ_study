package jpastudy.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpastudy.jpashop.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order)
    {
        em.persist(order);
    }
    public Order findOne(Long id)
    {
        return em.find(Order.class, id);
    }
    public List<Order> findAll(OrderSearch orderSearch)
    {
        // 1. Factory 생성
        JPAQueryFactory query = new JPAQueryFactory(em);
        // 2. Qorder.Qmember 가져오기
        QOrder order = QOrder.order;
        QMember member = QMember.member;
        return query.select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()),
                    namelike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }
    // order, member, delivery를 가져옴 (OrderApiController)
    public List<Order> findAllWithItem() {
        return em.createQuery(
                        "select distinct o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d" +
                                " join fetch o.orderItems oi" +
                                " join fetch oi.item i", Order.class)
                .getResultList();
    }
    // (OrderSimpleApiController)
    public List<Order> findAllWithMemberDelivery()
    {
        return em.createQuery("select o from Order o join fetch o.member m join fetch o.delivery d", Order.class).getResultList();
    }
    private BooleanExpression statusEq(OrderStatus orderStatus) {
        if(orderStatus == null) {
            return null;
        }
        return QOrder.order.orderStatus.eq(orderStatus);
    }
    private BooleanExpression namelike(String memberName) {
        if(!StringUtils.hasText(memberName)) // StringUtils는 springframework에 있는 package를 사용
        {
            return null;
        }
        return QMember.member.name.contains(memberName);
        // 부분 문자열 검색을 하려면 like가 아닌 contains를 사용하면 됨
    }

}
