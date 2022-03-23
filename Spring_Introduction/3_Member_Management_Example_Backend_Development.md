# 스프링 입문 - 코드로 배우는 스프링 부트, 웹MVC, DB 접근 기술



## 3. 회원 관리 예제 - 백엔드 개발



### 3-1. 비즈니스 요구사항 정리

- 데이터 : 회원ID, 이름
- 기능 : 회원 등록, 조회
- 아직 DB는 선정되지 않은 것으로 가정
  - RDB, NoSQL 등 다양한 저장소를 고민 중인 상황으로 가정



#### 일반적인 웹 애플리케이션 계층 구조

<img src="https://user-images.githubusercontent.com/86271759/158824005-60c34faa-8b2e-4230-82db-458a78e6f8a7.png" width=600 height=200>

- 컨트롤러 : 웹 MVC의 컨트롤러 역할
- 서비스 : 도메인 객체를 가지고 핵심 비즈니스 로직을 구현
- 리포지토리(=저장소) : DB에 접근, 도메인 객체를 DB에 저장하고 관리
- 도메인 : 비즈니스 도메인 객체
  - 예 : 회원, 주문, 쿠폰 등 주로 DB에 저장하고 관리됨



#### 클래스 의존관계

<img src="https://user-images.githubusercontent.com/86271759/158824807-1de32c27-f5b0-4493-b04b-f65151fe9b63.png" width=600 height=400>

- 아직 데이터 저장소가 선정되지 않아서 우선 interface로 구현 클래스를 변경할 수 있도록 설계
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

- Optional : findById()나 findByName() 결과로 null값이 나올 수도 있는데 이때 null값을 Optional로 감싸서 반환할 수 있음



#### 회원 리포지토리 메모리 구현체

`MemoryMemberRepository`

```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

// implements interface에서 단축키 option + Enter 쓰면 메서드를 implements할 수 있음
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
```







### 3-3. 회원 리포지토리 테스트 케이스 작성

- 자바는 JUnit이라는 프레임워크로 테스트를 실행
  - 반복 실행이 가능
  - 여러 테스트를 한번에 실행 가능



#### 회원 리포지토리 메모리 구현체 테스트

`src/test/java/hello/hellospring/repository/MemoryMemberRepositoryTest.java`

```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemoryMemberRepositoryTest {
    MemoryMemberRepository repository = new MemoryMemberRepository();

    /* Test끼리 서로 의존관계 없이 설계되어야 함
    Test가 끝날 때마다 repository를 깔끔하게 지워주는 코드 */
    @AfterEach
    public void afterEach() {
        repository.storeClear();
    }


    @Test
    public void save() {
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();
        /* 방식1 org.junit.jupiter... import 하기
        Assertions.assertEquals(member, result);
        */
        /* 방식2 org.assertj.core... import하기
        Assertions.assertThat(member).isEqualTo(result);
         Assertions에 option + Enter 쳐서 static import하기*/
        assertThat(result).isEqualTo(member);
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member(); // Shift + F6 : rename
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();
        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();
        assertThat(result.size()).isEqualTo(2);
    }
}
```

- @AfterEach : 각 테스트가 종료될 때 마다 이 기능을 실행함  
- 테스트는 각각 독립적으로 실행되어야 함   
  - 테스트 순서에 의존관계가 있는 것은 좋은 테스트가 아님
  - 여기서는 @AfterEach로 각 테스트가 끝날 때마다 메모리DB에 저장된 데이터를 삭제하여 테스트 간 의존관계를 없앰  







### 3-4. 회원 서비스 개발



### 3-5. 회원 서비스 테스트 

