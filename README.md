# 📑 for-work - 이력서 쇼핑몰 README
취업준비생 혹은 이직을 희망하는 사람들을 위해 모범이 되고 참고 할 수 있는 이력서를 판매하는 쇼핑몰 입니다.<br>
프로젝트 관련 회고 및 트러블 슈팅은 [여기](https://github.com/jps091/for-work/wiki)를 클릭 해주세요.

---

## 🚴 주요 기능 동작 흐름


### 1. 이력서 판매글 등록 흐름
<img width="700" height="400" src="https://github.com/user-attachments/assets/36b4324a-afff-4a29-b320-7b2fcb7fad85">


### 2. 이력서 구매 흐름
- [핵심 비지니스 로직에 대한 안정성을 높이기 위해 재시도 및 별도의 로그 테이블을 구성 하였습니다.](https://github.com/jps091/for-work/wiki/@Retryable-&-LogTable-%EC%84%A4%EA%B3%84%EB%A1%9C-%EC%84%9C%EB%B9%84%EC%8A%A4-%EC%95%88%EC%A0%95%EC%84%B1-%EB%86%92%EC%9D%B4%EA%B8%B0)
  <img width="900" height="450" alt="image" src="https://github.com/user-attachments/assets/2de832e6-20c2-48be-a09d-1c9f6e095261">

---

## 🗂 프로젝트 구조

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

## 🪃 아키텍처

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/0089951e-98b2-4970-bdde-c653626484b5">

---

## 💼 ERD

<img width="1000" height="700" alt="image" src="https://github.com/user-attachments/assets/fa804a6b-7a15-4e85-bab2-45bf468d67c4">

---
## 📝 API 명세서
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
