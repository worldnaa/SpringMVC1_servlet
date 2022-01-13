package hello.servlet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemberRepositoryTest { //jnuit5의 Test 부터는 public 생략가능

    MemberRepository memberRepository = MemberRepository.getInstance();

    @AfterEach
    void afterEach() {
        memberRepository.clearStore(); //테스트 끝날 때 마다 초기화
    }

    @Test
    void save() {
        //given (이런게 주어졌을때)
        Member member = new Member("hello", 20);

        //when (이런걸 실행했을때)
        Member saveMember = memberRepository.save(member);

        //then (이런 결과가 나와야돼)
        Member findMember = memberRepository.findById(saveMember.getId());
        assertThat(findMember).isEqualTo(saveMember);//찾아온 멤버와 저장된 멤버가 같아야함
    }

    @Test
    void findAll() {
        //given
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 30);

        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> result = memberRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(member1, member2); //result에 멤버1,2 객체가 있는지
    }
}
