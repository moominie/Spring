package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member); // 회원을 저장소에 저장
    Optional<Member> findById(Long id); // id로 찾기
    Optional<Member> findByName(String name); // name으로 찾기
    List<Member> findAll(); // 지금까지 저장한 모든 회원을 List로 반환

}
