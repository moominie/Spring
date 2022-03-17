# 스프링 입문 - 코드로 배우는 스프링 부트, 웹MVC, DB 접근 기술



## 3. 회원 관리 예제 - 백엔드 개발



### 3-1. 비즈니스 요구사항 정리

- 데이터 : 회원ID, 이름
- 기능 : 회원 등록, 조회
- 아직 DB는 선정되지 않은 것으로 가정
  - RDB, NoSQL 등 다양한 저장소를 고민 중인 상황으로 가정



#### 일반적인 웹 애플리케이션 계층 구조

<img src="https://user-images.githubusercontent.com/86271759/158824005-60c34faa-8b2e-4230-82db-458a78e6f8a7.png">

- 컨트롤러 : 웹 MVC의 컨트롤러 역할
- 서비스 : 도메인 객체를 가지고 핵심 비즈니스 로직을 구현
- 리포지토리(=저장소) : DB에 접근, 도메인 객체를 DB에 저장하고 관리
- 도메인 : 비즈니스 도메인 객체
  - 예 : 회원, 주문, 쿠폰 등 주로 DB에 저장하고 관리됨



#### 클래스 의존관계

<img src="https://user-images.githubusercontent.com/86271759/158824807-1de32c27-f5b0-4493-b04b-f65151fe9b63.png">

- 아직 DB가 선정되지 않아서 우선 interface로 구현 클래스를 변경할 수 있도록 설계
- 초기 개발 단계에서는 구현체로 가벼운 메모리 기반의 데이터 저장소를 사용함







### 3-2. 회원 도메인과 리포지토리 만들기

#### 회원 객체

`Member`

```java
package hello.hellospring.domain;

public class Member {

    private Long id; // 회원이 입력하는 id가 아니라 시스템이 정해주는 id 
    private String name;

    // getter와 setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

```



#### 회원 리포지토리 인터페이스

회원 객체를 저장하는 저장소

`MemberRepository`

```java
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
```

- Optional : findById나 findByName에서 null값이 나올 수도 있는데 이때 null값을 Optional로 감싸서 반환할 수 있음



#### 회원 리포지토리 메모리 구현체

`MemoryMemberRepository`

```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L; // 키값을 생성해줌

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
}
```







### 3-3. 회원 리포지토리 테스트 케이스 작성





### 3-4. 회원 서비스 개발



### 3-5. 회원 서비스 테스트 

