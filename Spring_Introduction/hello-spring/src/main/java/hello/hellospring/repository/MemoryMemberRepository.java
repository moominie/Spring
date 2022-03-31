package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

// implements interface에서 단축키 option + Enter 쓰면 메서드를 implements할 수 있음
@Repository
public class MemoryMemberRepository implements MemberRepository {
    private static Map<Long, Member> store = new HashMap<>();
    private static Long sequence = 0L; // 키값을 생성해줌

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id)); // null이 나올 수도 있으므로 Optional.ofNullable()로 감싸서 반환
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny(); // loop 돌면서 filter에 맞는 값 있으면 바로 하나를 반환하고 null이면 Optional이 감싸서 반환
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); // 모든 회원 목록을 List로 반환
    }

    public void storeClear() {
        store.clear(); // store를 비워줌
    }
}
