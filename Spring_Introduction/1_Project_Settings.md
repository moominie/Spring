# 스프링 입문 - 코드로 배우는 스프링 부트, 웹MVC, DB 접근 기술



## 1. 프로젝트 환경설정



### 1-1. 프로젝트 생성



#### 사전 준비

- ★ Java 11

- IDE : IntelliJ



#### 스프링 부트 스타터 사이트에서 스프링 프로젝트 생성

https://start.spring.io



##### spring initializer

- Project : Gradle Project

  - 요즘 실무에서 대부분 Gradle 사용

- Language : Java

- Spring Boot : 2.6.4 (2022/03/13 기준)
  - SNAPSHOT, M1 같은 미정식 버전을 제외하고 정식 & 최신 버전을 사용하기

- Project Metadata

  - Group (회사명) : hello
  - Artifact (프로젝트명) : hello-spring
  - Name : hello-spring
  - 나머지 설정 그대로

- Dependencies

  - 어떤 라이브러리를 땡겨서 쓸 것인가?

  - 다음 2가지 추가하기

    1. Spring Web

    2. Thymeleaf

- GENERATE

  - 원하는 디렉토리에 zip 파일 압축 풀기




#### IntelliJ에서 프로젝트 실행

- 열기 - 디렉토리의 hello-spring/build.gradle 선택 - 프로젝트로 열기  



##### 프로젝트 구조 확인

- `src`

  - src/main/java 아래 실제 패키지와 소스파일이 존재

  - src/main/resources 아래 java를 제외한 나머지 설정파일들이 존재
    - xml, properties, html 등

  - src/test에는 테스트코드와 관련된 소스가 존재
    - 요즘 개발 트렌드에서 테스트코드가 매우 중요!

- `build.gradle`

```java
plugins {
	id 'org.springframework.boot' version '2.6.4'
	id 'io.spring.dependency-management' version '1.0.11.RELEASE'
	id 'java'
}

group = 'hello'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	testImplementation('org.springframework.boot:spring-boot-starter-test') {
		exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
	}
}
test {
	useJUnitPlatform()
}
```

~~(오류) test의 useJUnitPlatform() 인식 안 됨~~

~~(해결) 강의자료의 dependencies와 test 코드로 대체 후 reload(코끼리 버튼 클릭)~~



##### 프로젝트 실행

- src/main/java/hello.hellospring/HelloSpringApplication 실행
- 기본 메인 클래스 실행
  - 콘솔에 `Tomcat started on port(s): 8080 (http) ...` 이 뜸
- 브라우저 http://localhost:8080 접속
  - Whitelabel Error Page 뜨면 성공



#### IntelliJ Gradle 대신 자바 직접 실행하기 설정 (성능 문제)

최근 IntelliJ 버전은 Gradle을 통해 실행하는 것이 기본 설정인데 이럴 경우 실행속도가 느리다는 단점이 있음

- 자바 직접 실행하도록 변경하는 법

  - Preferences - Gradle 검색

  - 빌드, 실행, 배포 - 빌드 도구 - Gradle 선택

  - Gradle 프로젝트 - 빌드 및 실행에서 두 옵션 모두 `IntelliJ IDEA` 선택







### 1-2. 라이브러리 살펴보기

Gradle은 의존관계가 있는 라이브러리를 함께 다운로드함(다 땡겨옴)  

IntelliJ 오른쪽 상단 Gradle의 Dependencies가 라이브러리 간 의존관계임



#### Spring Boot 라이브러리

##### spring-boot-starter-web

- spring-boot-starter-tomcat
- spring-webmvc
  - spring-aop, spring-beans, ...



##### spring-boot-starter-thymeleaf

타임리프 템플릿 엔진(View)



##### spring-boot-starter (공통)

- spring-boot
  - spring-core
- spring-boot-starter-logging
  - logback, slf4j, ...



#### Test 라이브러리

##### spring-boot-starter-test

- junit : 테스트 프레임워크 (핵심)
- mockito
- assertj : 테스트코드를 더 편하게 작성하도록 도와주는 라이브러리
- spring-test : 스프링 통합 테스트 지원 라이브러리







### 1-3. View 환경설정



#### Welcome Page 만들기

- 스프링 부트의 Welcome Page 기능
  - `resources/static/index.html` 파일이 Welcome Page가 됨(정적페이지)

```html
<!DOCTYPE html>
<html>
<head>
    <title>Hello</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
Hello
<a href="/hello">hello</a>
</body>
</html>
```

(참고) https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-welcome-page



#### thymeleaf 템플릿 엔진

##### thymelear 템플릿 엔진 동적 페이지 만들기

- 컨트롤러 만들기

  1. 패키지 만들기

     - 위치 : `java/hello.hellospring/`

     - 이름 : controller

  2. 자바 클래스 만들기

     - 위치 : `java/hello.hellospring/controller`

     - 이름 : HelloController

```java
package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello") //url 매핑
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        return "hello"; //화면 리턴
    }
}
```

컨트롤러에서 리턴 값으로 문자를 반환하면 viewResolver가 화면을 찾아서 처리함

- 스프링 부트 템플릿 엔진 기본 viewName 매핑 기능 : `resources/templates/` + {viewName} + `.html`

(예) `return "hello";`는 `resources/templates/hello.html`을 반환



- 템플릿 만들기
  - 위치 : `resources/templates/`
  - 이름 : hello.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Hello</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<p th:text="'안녕하세요. ' + ${data}" >안녕하세요. 손님</p>
</body>
</html>
```



##### thyme leaf 템플릿엔진 동작 확인하기

- 서버 중지 후 다시 실행
- 브라우저 http://localhost:8080/hello 접속

```http
안녕하세요. hello!!
```



(참고) `spring-boot-devtools` 라이브러리를 추가하면 html 파일을 컴파일만 해주면 서버 재시작없이 View 파일 변경이 가능함

- IntelliJ 컴파일 방법 : Build - Recompile







### 1-4. 빌드하고 실행하기

#### terminal에서 실행 (mac)

(주의) IntelliJ에서 실행 중인 서버 중지해야 함

1. hello-spring 디렉토리로 이동

   `% cd /users/중간경로/hello-spring`

2. `% ./gralew build`

3. `% cd build/libs`
4. `% java -jar hello-spring-0.0.1-SNAPSHOT.jar`
5. 브라우저 http://localhost:8080 에서 실행 확인하기





