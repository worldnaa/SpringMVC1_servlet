package hello.servlet.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
 */
public class MemberRepository {

    // static으로 만들면 MemberRepository가 많이 생겨도 아래 필드는 1개만 존재한다
    // 싱글톤으로 하면 static 없어도 1개만 존재하는 것이 보장이 되기에 static 생략 가능

    private static Map<Long, Member> store = new HashMap<>(); // key=id / value=Member
    private static long sequence = 0L;                        // id를 저장할 시퀀스 (1씩 증가)

    private static final MemberRepository instance = new MemberRepository(); // 싱글톤

    // 무조건 아래 메서드를 통해서만 인스턴스를 생성할 수 있음 (최초 1개만)
    public static MemberRepository getInstance() {
        return instance;
    }

    private MemberRepository() {
    }

    public Member save(Member member) {
        member.setId(++sequence);          // 시퀀스 값을 증가시켜 id에 저장
        store.put(member.getId(), member); // Map 형태인 store에 id를 키로, member를 값으로 삽입
        return member;
    }

    public Member findById(Long id) {
        return store.get(id); // key 값인 id를 넘겨주면 value인 Member 객체 반환
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values()); // store 자체를 보호하기 위해 ArrayList에 담아줌
    }

    public void clearStore() {
        store.clear(); // store 값 초기화 (테스트에서만 사용)
    }

}
