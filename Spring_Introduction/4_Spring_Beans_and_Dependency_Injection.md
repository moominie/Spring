# 스프링 입문 - 코드로 배우는 스프링 부트, 웹MVC, DB 접근 기술



## 4. 스프링 빈과 의존관계

- 회원 컨트롤러가 회원 서비스와 회원 리포지토리를 사용할 수 있게 의존관계를 준비하기
- 여러 컨트롤러에서 회원 서비스를 가져다 쓸 때 매번 new 생성자()로 인스턴스를 새로 만들어서 쓰지 않고, 스프링 컨테이너에 한 번 등록해놓으면 하나를 가지고 여러 곳에서 공유해서 쓸 수 있음  



- 정형화된 MVC 패턴
  - Controller에서 외부 요청을 받고, Service에서 비즈니스 로직을 만들고, Repository에서 데이터를 저장함  



- 스프링 빈을 등록하는 2가지 방법
  1. 컴포넌트 스캔과 자동 의존관계 설정
  2. 자바 코드로 직접 스프링 빈 등록하기  



### 4-1. 컴포넌트 스캔과 자동 의존관계 설정

- 컴포넌트 스캔 원리
  - <u>`@Component` 애노테이션</u>이 있으면 스프링 컨테이너에 <u>스프링 빈으로 자동 등록</u>됨
    - `@Controller` 
    - `@Service`
    - `@Repository`
  - <u>생성자에 `@Autowired` 를 사용</u>하면 객체 생성 시점에 스프링 컨테이너에서 해당 스프링 빈을 찾아서 주입함(생성자가 1개만 있으면 @Autowired 는 생략할 수 있음)
    - <u>스프링 빈 사이의 연관관계</u>
  - 단, `helloSpringApplication` 이 실행되므로 같은 패키지 하위에 등록된 컴포넌트 스캔만 가능 = `hello.hellospring` 하위 패키지에서만 가능!



#### 회원 컨트롤러에 의존관계 추가

`MemberController`

```java
package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
```



###### DI(Dependency Injection) : 의존성 주입  

생성자에 @Autowired 가 있으면 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어줌  이렇게 객체 의존관계를 외부에서 넣어주는 것을 DI(Dependency Injection), 의존성 주입이라 함  



#### 회원 서비스 스프링 빈 등록  

`MemberService`

```java
package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
  
아래 생략  
```



#### 회원 리포지토리 스프링 빈 등록

```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryMemberRepository implements MemberRepository {
  
  아래 생략
```



#### 스프링 빈 등록 이미지  

<img src="https://user-images.githubusercontent.com/86271759/161078792-54317418-1cc2-4e4a-b9a7-5b73d65fac4d.png" width=450 height=300>

(참고) 스프링은 스프링 컨테이너에 스프링 빈을 등록할 때, 기본으로 싱글톤으로 등록함(유일하게 하나만 등록해서 공유)  

따라서 같은 스프링 빈이면 모두 같은 인스턴스임   

설정으로 싱글톤이 아니게 설정할 수 있지만, 특별한 경우를 제외하면 대부분 싱글톤을 사용함 







### 4-2. 자바 코드로 직접 스프링 빈 등록하기

