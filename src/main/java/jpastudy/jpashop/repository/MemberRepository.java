package jpastudy.jpashop.repository;

import jpastudy.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {
    @PersistenceContext // @Autowired를 써도 된다.
    private EntityManager em;

    // 등록
    public void save(Member member)
    {
        em.persist(member);
    }
    // id로 member 1개 조회
    public Member findOne(Long id)
    {
        return em.find(Member.class, id);
    }
    // 전체 조회
    public List<Member> findAll()
    {   // TypedQuery.return type 설정
        return em.createQuery("select * from Member m", Member.class).getResultList();
    }
    public List<Member> findByName(String name)
    {   // TypedQuery.파라미터 설정.return type 설정
        return em.createQuery("select * from Member m where m.name=:name", Member.class).setParameter("name",name).getResultList();
    }

}
