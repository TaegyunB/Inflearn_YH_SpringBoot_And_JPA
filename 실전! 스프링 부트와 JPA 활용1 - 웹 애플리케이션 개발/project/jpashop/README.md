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

## 섹션 3. 도메인 분석 설계
### 엔티티 클래스 개발 1
~~~java
@Embeddable  // 내장 타입 클래스를 정의할 때 사용

@Embedded  // 엔티티에서 내장 타입을 사용할 때 붙임

@JoinColumn  // 데이터베이스 테이블에서 외래 키(FK)를 어떤 컬럼으로 매핑할 지 지정하는 어노테이션. 연관관계의 주인에 사용되며, 실제로 DB에 FK 컬럼이 생성되는 쪽임

@OneToMany(mappedBy = "member")  // mappedBy: 연관관계의 주인이 아닌 쪽에서 사용됨. 이 필드를 기준으로 DB에 FK 컬럼이 생성되지 않음

@Enumerated(EnumType.STRING)  // 항상 String으로 지정 -> ORDINAL로 하면 숫자로 되기 때문에 순서가 바뀌면 지정된 숫자도 바뀜

/*
    상속 매핑 전략 지정
    - JPA는 객체지향 언어의 상속 구조를 RDB 테이블에 매핑할 수 있게 해줌
    - 그 중 SINGLE_TABLE 전략은 모든 자식 클래스를 하나의 테이블에 저장하는 방식임
*/
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

/*
    구분자 컬럼 지정
    - SINGLE_TABLE 전략에서는 하나의 테이블에 여러 타입(Book, Album, Movie)이 들어가기 때문에, 어떤 클래스의 데이터인지 구분하는 컬럼이 필요함
    - 이 역할을 하는 것이 구분자(discriminator) 컬럼이고, 이름을 dtype으로 지정한 것
*/
@DiscriminatorColumn(name = "dtype")

/*
    구분자 컬럼에 저장할 값 지정
    - 해당 클래스를 저장할 때, dtype 컬럼에 어떤 값을 넣을지 지정함
    - 예를 들면, Book 객체가 저장되면, dtype 컬럼에는 B가 들어감
*/
@DiscriminatorValue("B")
~~~
- 내장 타입: JPA에서 엔티티의 일부로 취급되는 값 객체

### 엔티티 클래스 개발 2
**실무에서는 @ManyToMany 절대 사용 금지**
~~~java
@ManyToMany
@JoinTable(name = "category_item",
    joinColumns = @JoinColumn(name = "category_id"),       // 현재 엔티티(Category)의 FK
    inverseJoinColumns = @JoinColumn(name = "item_id"))    // 상대 엔티티(Item)의 FK
private List<Item> items = new ArrayList<>();
~~~
- @ManyToMany는 두 테이블이 서로 다대다 관계를 가질 때 사용함
- 하지만 RDB에서는 다대다 관계를 직접 표현할 수 없기 때문에, 중간에 조인 테이블(연결 테이블)을 만들어야 함
- 이 조인 테이블을 정의하는 게 바로 @JoinTable 임



















