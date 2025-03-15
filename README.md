##  📑 For-Work - 쇼핑몰

### 📖 목 차
1. [프로젝트 개요](https://github.com/jps091/for-work/blob/dev/README.md#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%9C%EC%9A%94)
2. [프로젝트 기여도](https://github.com/jps091/for-work/blob/dev/README.md#-%EA%B8%B0%EC%97%AC%EB%8F%84)
3. [프로젝트 구조](https://github.com/jps091/for-work/blob/dev/README.md#-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B5%AC%EC%A1%B0)
4. [시스템 아키텍쳐](https://github.com/jps091/for-work/blob/dev/README.md#-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98)
5. [ERD](https://github.com/jps091/for-work/blob/dev/README.md#-erd)
6. [API 명세서](https://github.com/jps091/for-work/blob/dev/README.md#-api-%EB%AA%85%EC%84%B8%EC%84%9C)
7. [주요 기능 동작 흐름 및 검증](https://github.com/jps091/for-work/blob/dev/README.md#-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EB%8F%99%EC%9E%91-%ED%9D%90%EB%A6%84-%EB%B0%8F-%EA%B2%80%EC%A6%9D)
8. [기능 구현 화면](https://github.com/jps091/for-work/blob/dev/README.md#-%EA%B5%AC%EB%A7%A4%EC%9E%90-%EA%B4%80%EB%A0%A8-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84-%ED%99%94%EB%A9%B4)
9. [프로젝트 회고](https://github.com/jps091/for-work/blob/dev/README.md#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0)
10. [기술 스택](https://github.com/jps091/for-work/blob/dev/README.md#%EC%82%AC%EC%9A%A9-%EA%B8%B0%EC%88%A0-%EC%8A%A4%ED%83%9D)

---

### 📁 프로젝트 개요
판매 요청자가 관리자에게 양식에 맞게 이력서 판매 요청을 하면 관리자가 검토 후 판매 여부를 결정하여, 검증된 이력서만 판매하는 쇼핑몰입니다. 

해당 프로젝트는 복잡한 비지니스 로직을 효과적으로 구현하고, 검증하는 것을 목표로 하였습니다. 

또한 지속 성장 가능한 소프트웨어 설계에 대해 고민하면서 아래와 같은 방식을 적용하면서 개발하였습니다.

1. 4 계층 아키텍쳐
2. OOP
3. 디자인 패턴
4. 일급 컬렉션
5. 엔티티 양방향 관계 사용X

---

### 👥 기여도
프론트엔드 개발자 1명과 협업, DB 설계 부터 백엔드 개발 및 배포 100% 기여

- [서비스 배포 링크](https://main--resume-market.netlify.app/)

- [프로젝트 관련 포스팅](https://github.com/jps091/for-work/wiki)

- [Github 칸반보드](https://github.com/users/jps091/projects/2/views/1)

---

### 🗂 프로젝트 구조

1. Presentation, Application, Domain, Persistence 4계층으로 프로젝트를 구성 하였습니다.
2. Persistent Object와 Domain Object를 분리 하여 핵심 비지니스 로직은 Domain Object에 구현 하였습니다.
3. Repository는 인터페이스로 설계 하여 서비스 계층과 결합도를 낮췄습니다.
- 더 자세한 설명은 [4계층 프로젝트를 도입한 이유](https://github.com/jps091/for-work/wiki/%EB%8F%84%EB%A9%94%EC%9D%B8%EC%9D%84-%EB%8F%84%EC%9E%85%ED%95%9C-%EC%9D%B4%EC%9C%A0)를 확인 해주세요.

<img width="850" height="600" alt="image" src="https://github.com/user-attachments/assets/15e4d5cf-1cf3-4c78-ae9b-ee64c46159b0" />


---

### 🪃 아키텍처

<img width="850" height="600" alt="image" src="https://github.com/user-attachments/assets/0089951e-98b2-4970-bdde-c653626484b5">

---

### 💼 ERD

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/fa804a6b-7a15-4e85-bab2-45bf468d67c4">

---

### 📝 API 명세서
0. [스웨거 API 명세서 ver](http://search-info.n-e.kr:8080/swagger-ui/index.html?urls.primaryName=%EC%A0%84%EC%B2%B4%20%EC%82%AC%EC%9A%A9%EC%9E%90%EB%A5%BC%20%EC%9C%84%ED%95%9C%20For-work%20Service%20%EB%8F%84%EB%A9%94%EC%9D%B8%20API)
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

### 🚴 주요 기능 동작 흐름 및 검증

</br>

**1. 판매자 이력서 판매글 등록 흐름**

<img width="800" height="400" alt="image" src="https://github.com/user-attachments/assets/f86cfc3c-2a59-4125-99c1-6174077f1fce" /></br>


</br>

**2. 이력서 주문 및 결제 흐름**

<img width="1000" height="450" alt="image" src="https://github.com/user-attachments/assets/c20432b7-7f43-4246-b722-44b738544829" /></br>



**3. 구매자 메일 전송 흐름**

<img width="700" height="350" alt="image" src="https://github.com/user-attachments/assets/6fb5d193-c927-4717-9a1a-2b8888cbabc7" /></br>


**4. 메일 전송 (RabbitMQ 비동기 큐 & Dead Letter 처리)**

<img width="1000" height="450" src="https://github.com/user-attachments/assets/e454b6d0-0356-45fa-9e89-5bbb3a227216"></br>


**✅ 약 150개의 단위 테스트로 검증**

<img width="1000" height="450" alt="image" src="https://github.com/user-attachments/assets/d5a4786b-6c63-4134-bf7a-140fc53b8f3a" />


---

### 🚀 구매자 관련 기능 구현 화면

</br>

**1. 판매글 조회(Pagination Key 방식), 회원가입, 로그인, 관리자 문의**

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/478da93c-71fe-4b15-b937-f7d2f8ad7b8d" /></br>

**2. 결제, 주문 전체, 상세 내역**

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/4b7a51e1-465d-460c-b88d-3c56eb5fb5da" /></br>

---

### 🌈 판매자 관련 기능 구현 화면

**1. 이력서 판매 요청 작성, 나의 이력서 상태**

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/2833d15a-bd28-4f36-8a09-bf041a077b58" />

---

### 🔥 어드민 관련 기능 구현 화면


**1. 요청 온 이력서 전체 조회, 상세 조회**

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/c55bf273-59f2-4a33-956f-a19918e2c73f" />

---

### 📓 프로젝트 회고

결제, 환불, 자동 구매 확정, 주문 상태 관리, 페이징 처리(Pagination Key)와 같은 복잡한 비즈니스 로직을 효율적 구현했으며, 약 150개의 유닛 테스트를 작성하고 검증하여 코드의 신뢰성을 높일 수 있었습니다. 또한 부하 테스트와 모니터링을 통해 성능 최적화를 경험했습니다.

프로젝트 과정에서 API 응답에 엔티티를 직접 노출하거나, 서비스 계층의 비대화, ORM 양방향 관계와 같은 주요 안티 패턴을 식별하고, 4계층 구조, 일급 컬렉션, 디자인 패턴 등을 적용해 더 객체지향적이고 품질 높은 코드에 대해 생각할 수 있었습니다.

마지막으로 CI/CD 프로세스를 설계하고, AWS를 활용해 배포하며 백엔드 서버 개발 전반뿐만 아니라 인프라 설계까지 경험할 수 있었던 의미 있는 프로젝트였습니다.

---

### ⚒ 사용 기술 스택

- Backend: Java 17, Spring Boot 3.3.2, JPA, Query DSL, MyBatis, RabbitMQ
- Database: MySQL 8.0.4, Redis
- Build Tool: Gradle
- Version Control: Git, GitHub
- Infra: EC2, S3, CDN, RDS, ElastiCache, SES
- Containerization: Docker
- CI/CD: Github Actions, ECR, CodeDeploy, S3
