# (주)클랩 백엔드 서버

### HTTP RULE

| HTTP Method | 사용상황                           | 반환(상태코드) |
|-------------|--------------------------------|----------|
| GET         | 리소스의 조회                        | 200      | 
| POST        | 새로운 리소스 생성                     | 201      |
| PUT         | 리소스의 전체 업데이트 또는 ID를 통한 리소스 생성  | 204      |
| PATCH       | 리소스의 일부분(일부 필드) 업데이트           | 204      |
| DELETE      | 리소스의 삭제                        | 204      |

### 계층 RULE

| 계층         | 역할                                                          |
|------------|-------------------------------------------------------------|
| Controller | HTTP 요청을 받아 적절한 Service 호출, 입력 검증, 유효성 검사, HTTP 응답 생성 및 반환  |
| Service    | 비즈니스 로직 수행, DTO 와 엔티티 변환, 다수 Repository 를 통한 하나의 트랜잭션 처리 작업 |
| Model      | Entity, DTO 가 속하며 데이터구조, 데이터베이스와의 연동되는 객체                   |
| Repository | DB 관련 CRUD 작업, DB 의 결과를 Entity 로 변환하는 작업                    |

### 개행 RULE

- 지역변수는 사이에 개행을 두지 않는다. 하지만 첫 지역변수 전줄, 마지막 지역변수 다음줄에 개행을 추가한다.
- 생성자 전후에 개행을 추가한다.
- 추상체, 구현체 모두 메서드 전후에 개행을 추가한다. 단 마지막 메서드 후에는 추가하지 않는다.
- 클래스의 마지막 줄에는 개행을 추가한다.

### 연관관계 매핑 RULE

- M:1 관계에서는 M 에서 1 에 대한 정보까지 추가한다. ex) setProduct() 는 Product 가 아닌 WishProduct 에서 수행을 하는것 처럼
- save 를 하는 과정에서 우선 객체를 생성하고 연관관계를 맺어준 후에 repository.save() 를 호출한다.

### 계층간 의존 RULE

- M:1 관계에서 M 에서는 1에 대한 조회만을 수행하기에 서비스 계층에서는 레포지토리 계층을 의존한다.(R)
- M:1 관계에서 1 에서는 M에 대한 로직을 수행할 수 있기에(삭제 등) 서비스 계층을 의존한다.(CUD)
