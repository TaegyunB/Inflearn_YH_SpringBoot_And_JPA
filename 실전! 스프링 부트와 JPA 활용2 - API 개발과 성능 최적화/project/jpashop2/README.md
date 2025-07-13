# 실전! 스프링 부트와 JPA 활용 2 - API 개발과 성능 최적화
## 섹션 2. API 개발 기본
### 회원 등록 API
**API랑 Controller랑 다른 패키지로 구분하는 것을 선호**
- Controller
    - 보통 서버 내부 View 렌더링이나 내부 용도의 웹 페이지 처리 담당
    - 예: /order/form 같은 URL로 HTML 페이지를 반환
- API
    - 주로 JSON 데이터를 반환해서 프런트엔드나 외부 시스템에서 호출하게끔 제공
    - 주로 데이터를 처리
    - 예: /api/orders 로 JSON 데이터를 반환
- 역할과 사용자가 다르고, 관리, 보안, 응답, 형식, 버전 관리 관점이 다르기 때문에 다른 패키지로 나누는 것을 선호

**Entity를 절대 API에서 사용하지 말 것**<br>
**API를 만들 때에는 항상 Entity를 파라미터로 받지 말고 DTO를 생성하고 활용할 것**
- Entity는 애플리케이션 내부 도메인 모델
- API에 그대로 노출하면 내부 설계가 외부로 유출됨
- 이후 Entity 필드나 관계를 바꾸면, API 계약도 깨져서 클라이언트가 전부 오류남
<br>

- @RequestBody
    - HTTP 요청의 Body 내용을 자바 객체로 변환해주는 어노테이션
    - 주로 JSON 데이터를 자바 객체로 바꿔서 컨트롤러 메서드의 파라미터에 주입할 떄 사용함
    - 즉, 클라이언트가 JSON을 보내면, 스프링이 그걸 자동으로 객체에 매핑해 줌
    - 주의사항
        - @ModelAttribute는 주로 폼 데이터(application/x-www-form-urlencoded)처리
        - @RequestBody는 JSON 같은 raw body를 처리

### 회원 수정 API
- PUT은 리소스 전체 업데이트를 할 때 주로 사용
- PATCH는 리소스 일부만 부분 업데이트를 할 때 주로 사용

### 회원 조회 API
**엔티티를 외부로 노출하지 말고 항상 DTO로 변환하기**
~~~java
    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }
~~~
- Stream: 컬렉션 데이터를 함수형으로 처리할 수 있게 해주는 API
- map(): 각 요소를 다른 형태로 변환하는 중간 연산
- m -> new MemberDto(m.getName()): 람다 표현식
    - m: 각각의 Member 객체(매개변수)
    - ->: 람다 연산자
    - new MemberDto(m.getName()): Member를 MemberDto로 변환
- .collect(Collectors.toList())
    - collect(): Stream의 최종 연산, 결과를 수집
    - Collectors.toList(): Stream 요소들을 List로 수집하는 Collector

### 퀴즈
1. API 개발에서 JPA 엔티티 객체를 요청 파라미터나 응답 값으로 직접 사용하는 것을 지양해야 하는 주된 이유는 무엇일까요?<br>
    A: API의 스펙이 엔티티의 변화에 직접적으로 영향을 받기 때문<br>
    => API 스펙이 엔티티 변화에 묶여버리면, 엔티티 수정 시 클라이언트 API가 깨지는 문제가 발생함. 이는 유닛 전반에서 강조된 주요 문제점임

2. API 요청/응답에서 DTO(Data Transfer Object)를 사용하면 어떤 이점을 얻을 수 있나요?<br>
    A: 엔티티와 API 스펙을 분리하고 필요한 데이터만 선별적으로 노출할 수 있음<br>
    => DTO를 사용하면 엔티티 내부 구현이 외부에 노출되지 않아 안전하며, API별로 필요한 데이터만 정확히 전달하고 스펙을 독립적으로 관리할 수 있음

3. 강의에서 'API 개발의 표준적인 접근 방식'으로 가장 강조된 내용은 무엇인가요?<br>
    A: API 입/출력 시 엔티티를 직접 사용하지 않고 항상 DTO를 활용하여 분리해야 함<br>
    => 강의에서 가장 중요하게 강조된 부분. 엔티티는 내부 로직에 집중하고, 외부와의 통신(API)에는 항상 DTO를 사용하여 분리하는 것이 바람직한 설계임

4. 회원 정보 수정을 위한 API를 RESTful 방식으로 설계할 때, 일반적으로 어떤 HTTP 메서드를 사용하는 것이 권장될까요?<br>
    A: PUT<br>
    => RESTful API 설계에서 리소스를 생성할 때는 POST, 기존 리소스 전체를 갱신할 때는 PUT 메서드를 사용하는 것이 일반적인 규약임

5. API로 목록 데이터를 조회할 때, 응답을 단순 JSON 배열로 반환하는 대신 별도의 'Wrapper' 객체(예: Result 클래스) 안에 배열을 담아 반환하는 방식의 장점은 무엇인가요?<br>
    A: 응답 데이터에 목록 외의 부가 정보(예: 총 개수, 상태 코드)를 유연하게 추가할 수 있음<br>
    => 단순 배열로는 메타데이터(총 개수 등)를 함께 보내기 어렵지만, 객체로 감싸면 데이터 목록 외에 필요한 추가 정보를 유연하게 담을 수 있어 편리함. 조회 API 유닛에서 설명됨

## 섹션 4. API 개발 고급 - 지연 로딩과 조회 성능 최적화
### 간단한 주문 조회 V1: 엔티티를 직접 노출
- 엔티티를 직접 노출할 때는 양방향 연관관계가 걸린 곳은 꼭! 한 곳을 @JsonIgnore 처리 해야함.
    - 안그러면 양쪽을 서로 호출하면서 무한 루프가 걸림

- 지연 로딩(Lazy)을 피하기 위해 즉시 로딩(EAGER)으로 설정하면 안됨
    - 즉시 로딩 때문에 연관관계가 필요 없는 경우에도 데이터를 항상 조회해서 성능 문제가 발생할 수 있음
    - 즉시 로딩으로 설정하면 성능 튜닝이 매우 어려워 짐

- 항상 지연 로딩을 기본으로 하고, 성능 최적화가 필요한 경우에는 페치 조인(fetch join)을 사용

### 간단한 주문 조회 V2: 엔티티를 DTO로 변환
- 쿼리가 총 1 + N + N번 실행됨
    - order 조회 1번
    - order -> member 지연 로딩 조회 N 번
    - order -> delivery 지연 로딩 조회 N 번
    - 예) order의 결과가 4개면 최악의 경우 1 + 4 + 4번 실행됨(최악의 경우)
        - 지연로딩은 영속성 컨텍스트에서 조회하므로, 이미 조회된 경우 쿼리를 생략함

### 간단한 주문 조회 V3: 엔티티를 DTO로 변환 - 페치 조인 최적화
- 엔티티를 페치 조인(fetch join)을 사용해서 쿼리 1번에 조회
- 페치 조인으로 order -> member, order -> delivery 는 이미 조회된 상태이므로 지연로딩 X
~~~java
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +  // Order와 연관된 Member를 함께 조회
                        " join fetch o.delivery d", Order.class  // Order와 연관된 Delivery를 함께 조회
        ).getResultList();
    }
~~~
- join fetch
  - JPA에서 연관된 엔티티를 한 번의 쿼리로 함께 조회하는 기능
  - N+1 문제를 해결하는 핵심적인 방법

### 간단한 주문 조회 V4: JPA에서 DTO 바로 조회
- repository -> order -> simplequery 폴더 생성
  - OrderSimpleQueryRepositroy 파일 생성 후 쿼리를 따로 관리

~~~java
public List<OrderSimpleQueryDto> findOrderDtos() {
    return em.createQuery(
            "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                    " from Order o" +
                    " join o.member m" +
                    " join o.delivery d", OrderSimpleQueryDto.class)
            .getResultList();
}
~~~
- 일반적인 SQL을 사용할 때 처럼 원하는 값을 선택해서 조회
- new 명령어를 사용해서 JPQL의 결과를 DTO로 즉시 변환
- SELECT 절에서 원하는 데이터를 직접 선택하므로 DB -> 애플리케이션 네트워크 용량 최적화(생각보다 마비)
- 리포지토리 재사용성 떨어짐, API 스펙에 맞춘 코드가 리포지토리에 들어가는 단점

**쿼리 방식 선택 권장 순서**
1. 우선 엔티티를 DTO로 변환하는 방법을 선택한다.
2. 필요하면 페치 조인으로 성능을 최적화 한다 -> 대부분의 성능 이슈가 해결된다.
3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.
4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용한다.