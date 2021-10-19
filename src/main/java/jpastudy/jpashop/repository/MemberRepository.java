package jpastudy.jpashop.repository;

import jpastudy.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor // final을 받는 인자를 자동으로 초기화 해준다.
public class MemberRepository {
    // @PersistenceContext // @Autowired를 써도 된다
    private final EntityManager em;

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
        return em.createQuery("select m from Member m where m.name=:name", Member.class).setParameter("name",name).getResultList();
    }

}
