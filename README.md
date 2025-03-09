##  📑 For-Work - 쇼핑몰

### 📁 프로젝트 개요
판매 요청자가 관리자에게 양식에 맞게 이력서 판매 요청을 하면 관리자가 검토 후 판매 여부를 결정하여, 검증된 이력서만 판매하는 쇼핑몰입니다. 

해당 프로젝트는 복잡한 비지니스 로직을 효과적으로 구현하고, 검증하는 것을 목표로 하였습니다. 

또한 지속 성장 가능한 소프트웨어 설계에 대해 고민하면서 진행하였습니다.

---

### 👥 기여도
프론트엔드 개발자 1명과 협업, DB 설계 부터 백엔드 개발 및 배포 100% 기여

- [서비스 배포 링크](https://main--resume-market.netlify.app/)

- [프로젝트 관련 포스팅](https://github.com/jps091/for-work/wiki)

---

### 🗂 프로젝트 구조

1. Presentation, Application, Domain, Persistence 4계층으로 프로젝트를 구성 하였습니다.
2. Persistent Object와 Domain Object를 분리 하여 핵심 비지니스 로직은 Domain Object에 구현 하였습니다.
3. Repository는 인터페이스로 설계 하여 서비스 계층과 결합도를 낮췄습니다.
- 더 자세한 설명은 [4계층 프로젝트를 도입한 이유](https://github.com/jps091/for-work/wiki/%EB%8F%84%EB%A9%94%EC%9D%B8%EC%9D%84-%EB%8F%84%EC%9E%85%ED%95%9C-%EC%9D%B4%EC%9C%A0)를 확인 해주세요.

```markdown
├── common # 공통 모듈 및 설정 파일
│ ├── annotation # 커스텀 애노테이션
│ ├── api # API 인터페이스
│ ├── config # 설정 파일
│ ├── controller # 공통 컨트롤러
│ ├── domain # 공통 도메인 모델
│ ├── error # 오류 처리 모듈
│ ├── exception # 예외 처리 모듈
│ ├── infrastructure # 인프라 관련 설정
│ └── service # 공통 서비스
├── domain # 주요 비즈니스 도메인
│ └── user # 사용자 관련 도메인
│       ├── controller
│       ├── infrastructure
│       ├── model
│       └── service
│ ├── cartresume # 장바구니 이력서 관련 도메인
│ ├── maillog # 메일 로그 도메인
│ ├── order # 주문 관련 도메인
│ ├── orderresume # 주문 이력서 관련 도메인
│ ├── resume # 이력서 관련 도메인
│ ├── resumedecision # 이력서 결정 관련 도메인
│ ├── retrylog # 재시도 로그 도메인
│ ├── salespost # 판매 게시물 도메인
│ ├── thumbnailimage # 썸네일 이미지 도메인
│ ├── token # 토큰 관련 도메인
│ ├── transaction # 트랜잭션 도메인
│ └── cart # 장바구니 관련 도메인
├── interceptor
└── resolver
```
---

### 🪃 아키텍처

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/0089951e-98b2-4970-bdde-c653626484b5">

---

### 💼 ERD

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/fa804a6b-7a15-4e85-bab2-45bf468d67c4">

---

### 📝 API 명세서
0. [스웨거 API 명세서](http://search-info.n-e.kr:8080/swagger-ui/index.html?urls.primaryName=%EC%A0%84%EC%B2%B4%20%EC%82%AC%EC%9A%A9%EC%9E%90%EB%A5%BC%20%EC%9C%84%ED%95%9C%20For-work%20Service%20%EB%8F%84%EB%A9%94%EC%9D%B8%20API)
1. [노션 /open-api/v1/users (회원가입, 로그인 관련 API)](https://cuboid-sunfish-749.notion.site/1-open-api-v1-users-API-125bb83a1adf8058b761f7aea46a368d?pvs=4)
2. [노션 /api/v1/users (회원 정보 관련 API)](https://cuboid-sunfish-749.notion.site/2-api-v1-users-API-125bb83a1adf8055b989ea3d9703127e?pvs=4)
3. [노션 /api/v1/resumes (이력서 관리 판매자 전용 API)](https://cuboid-sunfish-749.notion.site/3-api-v1-resumes-API-125bb83a1adf802db310f1c66d3fb4ef?pvs=4)
4. [노션 /admin-api/v1/resumes (관리자 전용 API)](https://cuboid-sunfish-749.notion.site/4-admin-api-v1-resumes-API-125bb83a1adf801aa9aed118927b8e93?pvs=4)
5. [노션 /open-api/v1/sales-posts (판매글 조회 관련 API)](https://cuboid-sunfish-749.notion.site/5-open-api-v1-sales-posts-API-125bb83a1adf8060a748dfc09bbd65da?pvs=4)
6. [노션 /api/v1/sales-posts (판매글 관리 판매자 전용 API)](https://cuboid-sunfish-749.notion.site/6-api-v1-sales-posts-API-125bb83a1adf8087b933c649108fa7f4?pvs=4)
7. [노션 /api/v1/cart-resumes (장바구니 상세 기능 API)](/api/v1/cart-resumes (장바구니 상세 기능 API))
8. [노션 /api/v1/checkout (주문 생성 및 결제, 환불 관련 API)](https://cuboid-sunfish-749.notion.site/8-api-v1-checkout-API-125bb83a1adf80d09b8dce430def4f92?pvs=4)
9. [노션 /api/v1/orders (구매자 주문 관리 관련 API)](https://cuboid-sunfish-749.notion.site/9-api-v1-orders-API-125bb83a1adf80bba604c203038bd6e5?pvs=4)
10. [노션 /api/v1/token/reissue (JWT 재발급 API)](https://cuboid-sunfish-749.notion.site/10-api-v1-token-reissue-JWT-API-125bb83a1adf80e2aadcc10aec7dab20?pvs=4)

---

### 🚴 주요 기능 동작 흐름

</br>

**1. 이력서 판매글 등록 흐름**

<img width="700" height="400" src="https://github.com/user-attachments/assets/36b4324a-afff-4a29-b320-7b2fcb7fad85"></br>


**2. 이력서 구매 흐름**

<img width="900" height="450" alt="image" src="https://github.com/user-attachments/assets/2de832e6-20c2-48be-a09d-1c9f6e095261">

---

### 🚀 구매자 관련 기능 구현 화면

</br>

**판매글 조회(Pagination Key 방식)**

<img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/3552dae3-ef3d-44d9-b9c8-2b6b6bdeeb9e" /></br>

<img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/ddf4d3c3-7050-41c7-b90a-d376ed8bb143" /></br>

<img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/56daa5e2-2ac8-4814-8f36-e175cc8fbf37" /></br>

<img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/149828b4-e20d-4a79-ae8e-a2bd7368bb27" /></br>

<img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/6b9225da-cb8f-4273-abbd-88f69dfc46ee" /></br>

---

### 🌈 판매자 관련 기능 구현 화면

<img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/f1b43f8d-6d5d-43be-9633-dd8b63e5aac4" /></br>

<img width="650" height="500" alt="image" src="https://github.com/user-attachments/assets/85806210-1f9e-4bc1-a6d5-995609486b8c" /></br>

---

### 🔥 어드민 관련 기능 구현 화면

</br>

**판매 요청 이력서 전체 조회**

<img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/ac7766b8-d5fe-4c16-bab2-dde0a1966900" /></br>


**판매 요청 이력서 상세 조회**

<img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/ad91b6cb-e386-4349-881f-0ae47e5e55e0" /></br>

---

### 📓 프로젝트 회고

결제, 환불, 자동 구매 확정, 주문 상태 관리, 페이징 처리(Pagination Key)와 같은 로직을 간소화하여 구현했으며, 부하 테스트와 모니터링을 통해 성능 최적화를 경험했습니다. 또한, 약 150개의 유닛 테스트를 작성하고 검증하여 코드의 신뢰성을 높일 수 있었습니다.

프로젝트 과정에서 API 응답에 엔티티를 직접 노출하거나, 서비스 계층의 비대화, ORM 양방향 관계와 같은 주요 안티 패턴을 식별하고, 4계층 구조, 일급 컬렉션, 디자인 패턴 등을 적용해 더 객체지향적이고 품질 높은 코드에 대해 생각할 수 있었습니다.

마지막으로 CI/CD 프로세스를 설계하고, 배포하며 백엔드 서버 개발 전반뿐만 아니라 인프라 설계까지 경험할 수 있었던 의미 있는 프로젝트였습니다.

---

**⚒ 사용 기술 스택**

Java 17, Spring Boot 3.3.2, JPA, Query DSL, MyBatis

MySQL 8.0.4, Redis, RabbitMQ, JWT, Swagger, Scouter

EC2, S3, CDN, RDS, ElastiCache, SES, Github Actions, Docker, ECR, CodeDeploy
