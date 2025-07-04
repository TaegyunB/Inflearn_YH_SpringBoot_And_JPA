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