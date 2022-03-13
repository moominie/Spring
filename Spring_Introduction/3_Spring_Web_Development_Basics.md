# 스프링 입문 - 코드로 배우는 스프링 부트, 웹MVC, DB 접근 기술



## 3. 스프링 웹 개발 기초

- 정적 컨텐츠 : 웹 브라우저에 html 파일을 그대로 내려주는 방식  

- MVC와 템플릿 엔진 : 템플릿 엔진을 Model, View, Controller로 쪼개서 렌더링된 HTML을 웹 브라우저에 내려주는 방식  

- API : 보통 객체를 json 파일로 변환하여 데이터를 내려주는 방식  



### 3-1. 정적 컨텐츠

- 스프링 부트의 정적 컨텐츠 기능

  https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-static-content



- 정적 컨텐츠 만들기

`resources/static/hello-static.html`

```html
<!DOCTYPE html>
<html>
<head>
    <title>static content</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
정적 컨텐츠입니다.
</body>
</html>
```



- 실행하기

브라우저 http://localhost:8080/hello-static.html 접속

웹브라우저의 요청을 받고 내장 톰캣 서버는 우선 관련 컨트롤러를 찾음  

hello-static 관련 컨트롤러가 없기 때문에 그냥 `resources/static/hello-static.html` 파일을 찾아서 그대로 반환해줌  



### 3-2. MVC와 템플릿 엔진



#### MVC : Model, View, Controller

- View : 화면을 그리는데에 집중  
- Model, Controller : 비즈니스 로직과 관련 있거나 화면 뒷단의 작업을 처리하는데에 집중  



##### Controller

- HelloController

```java
package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }
}
```



#### View

`resources/template/hello-template.html` 만들기

```html
<html xmlns:th="http://www.thymeleaf.org">
<body>
<p th:text="'hello ' + ${name}">hello! empty</p>
</body>
</html>
```



#### 실행하기

브라우저 http://localhost:8080/hello-mvc?name=spring 실행

웹 브라우저에서 `localhost:8080/hello-mvc`를 요청하면 스프링 부트의 내장 톰캣 서버가 이와 관련한 helloController를 찾음  

매핑된 helloMvc 메소드를 실행하는데 파라미터값(key값=name, value=spring)을 받아서 매개변수로 넣어주고, Model 객체를 이용해서 View에 값을 넘겨줌     

viewResolver가 `resources/template/hello-template`을 찾아서 Thymeleaf 템플릿 엔진 처리(HTML 변환) 후 웹 브라우저에 반환함  



- 소스보기 결과

```html
<html>
<body>
<p>hello spring</p>
</body>
</html>
```



###### @RequestParam

메소드의 매개변수에 값을 받아와서 넣어줄 수 있음

`@RequestParam("가져올 데이터의 이름") [Data Type] [변수 이름]`



### 3-3. API



#### @ResponseBody 문자 반환



##### HelloController

```java
package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
    }
}
```

@ResponseBody를 사용하면 viewResolver를 사용하지 않음  

대신 HTTP의 BODY에 문자 내용을 직접 반환함(주의 : HTML의 body tag 아님)  



##### 실행하기

웹 브라우저에 http://localhost:8080/hello-string?name=spring 실행하기  



- 소스보기 결과

```html
hello spring
```

템플릿 엔진과 달리 @ResponseBody는 웹 브라우저에 String 문자를 그대로 반환함  



#### @ResponseBody 객체 반환

##### HelloController

```java
package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("hello-api")
    @ResponseBody
    public String helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    static class Hello {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
```

@ResponseBody를 사용하고 객체를 반환하면 객체가 JSON으로 변환되어 반환됨  

ViewResolver 대신 HttpMessageConverter가 동작함  

- 기본 문자 처리 : StringHttpMessageConverter
- 기본 객체 처리 : MappingJackson2HttpMessageConverter



##### 실행하기

웹 브라우저에서 http://localhost:8080/hello-api?name=spring 실행하기  



- 소스보기 결과

```html
{"name":"spring"}
```



###### Java의 Getter, Setter

- 단축키 : `command + n`





