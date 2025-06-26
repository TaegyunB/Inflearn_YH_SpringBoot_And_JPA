# 실전! 스프링 부트와 JPA 활용 1 - 웹 애플리케이션 개발
## 섹션 2. 프로젝트 환경설정
### View 환경 설정
~~~
build.gradle

dependencies {
    implementation 'org.springframework.boot:spring-boot-devtools'
}
~~~
- `spring-boot-devtools` 라이브러리를 추가하면, 'html' 파일을 컴파일만 해주면 서버 재시작 없이 View 파일 변경이 가능
- 인텔리J 컴파일 방버비: 메뉴 build -> Recompile

### JPA와 DB 설정, 동작확인
- application.properties 파일 삭제 후 application.yml 파일 생성
~~~
application.yml

spring:
  datasource:
    url: jdbc:h2:tcp://localhost/~/jpashop
    username: sa
    password:
    driver-class-name: org.h2.Driver

  jpa:
    hibernate:
      ddl-auto: create  # 실행시키는 시점에 가지고 있는 테이블을 다 지우고 다시 생성함
    properties:
      hibernate:
#        show_sql: true  # System.out 으로 출력됨
        format_sql: true

logging:
  level:
    org.hibernate.SQL: debug  # jpa나 hibernate가 생성하는 sql이 다 보임 -> log로 출력됨
~~~

**쿼리 파라미터 로그 남기기**
- 외부 라이브러리 사용
    - https://github.com/gavlyukovskiy/spring-boot-data-source-decorator
- 스프링 부트를 사용하면 이 라이브러리만 추가하면 됨
~~~
implementation "com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6"
~~~
- 참고
    - 쿼리 파라미터를 로그로 남기는 외부 라이브러리는 시스템 자원을 사용하므로, 개발 단계에서는 편하게 사용해도 됨
    - 하지만 운영시스템에 적용하려면 꼭 성능테스트를 하고 사용하는 것이 좋음

### 퀴즈
1. 스프링 부트 Starter 라이브러리의 주된 목적은 무엇일까요?<br>
    A: 자주 사용하는 라이브러리 자동 포함 및 버전 관리<br>
    => 스프링 부트 스타터는 웹, JPA 등 특정 기능 구현에 필요한 여러 라이브러리를 모아두고 버전 호환성을 자동으로 관리해줌

2. 개발 중 View 템플릿(예: Thymeleaf) 수정 시 빠른 변경 확인을 위해 활용하면 좋은 Spring Boot 기능은 무엇일까요?<br>
    A: DevTools<br>
    => Spring Boot DevTools는 개발 중 코드나 템플릿 변경이 발생했을 때 애플리케이션을 자동으로 재시작하거나 변경 사항을 즉시 반영하여 개발 생산성을 높여주는 기능

3. 강의에서 개발 및 테스트 환경용으로 H2 데이터베이스를 추천한 주요 이유는 무엇일까요?<br>
    A: 설치와 설정이 간단하며 임베디드 모드 지원으로 편리해서<br>
    => H2는 설정과 사용이 간편하고 애플리케이션에 내장(임베디드)하여 사용할 수 있어 개발 및 테스트 단계에서 매우 유용함

4. 스프링 부트와 JPA 설정 시 'application.yml' 파일에서 'hibernate.ddl-auto' 속성을 'create'로 설정했을 때 애플리케이션 실행 시점의 동작은 무엇일까요?<br>
    A: 기존 테이블을 모두 삭제하고 엔티티에 맞춰 재생성<br>
    => 'create' 설정은 애플리케이션 시작 시 데이터베이스 스키마를 초기화(기존 테이블 삭제 후 재생성)함. 'update'는 변경분만 반영하고, 'none'은 자동 변경을 하지 않음

5. JPA의 EntityManager를 통해 데이터를 저장하거나 수정하는 등 변경 작업을 수행할 때 반드시 필요한 것은 무엇일까요?<br>
    A: @Transactional 어노테이션 또는 트랜잭션 경계 설정<br>
    => JPA를 사용한 데이터 변경(쓰기) 작업은 반드시 트랜잭션 안에서 이루어져야 함. Spring에서는 '@Transactional' 어노테이션으로 편리하게 트랜잭션을 관리할 수 있음





















