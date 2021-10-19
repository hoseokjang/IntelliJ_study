package jpastudy.jpashop.service;

import jpastudy.jpashop.domain.Member;
import jpastudy.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입()
    {
        //Given
        Member member = new Member();
        member.setName("몽타");
        //When
        Long saveId = memberService.join(member);
        //Then
        assertEquals(member, memberRepository.findOne(saveId));
    }
    @Test
    public void 중복회원_확인()
    {
        //Given
        Member member1 = new Member();
        Member member2 = new Member();

        member1.setName("boot");
        member2.setName("boot");

        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () ->
        {
            // When
            memberService.join(member1);
            memberService.join(member2);
        });
        // Then
        // 예외 메세지 비교
        assertEquals("이미 존재하는 회원입니다.", exception.getMessage()); // message에 공백이 있어도 다르다고 인식해서 test 실패로 나옴
    }

}