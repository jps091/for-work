##  📑 For-Work - 쇼핑몰

### 📖 목 차
1. [프로젝트 개요](https://github.com/jps091/for-work/blob/dev/README.md#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%9C%EC%9A%94)
2. [프로젝트 기여도](https://github.com/jps091/for-work/blob/dev/README.md#-%EA%B8%B0%EC%97%AC%EB%8F%84)
3. [프로젝트 구조](https://github.com/jps091/for-work/blob/dev/README.md#-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B5%AC%EC%A1%B0)
4. [API 명세서](https://github.com/jps091/for-work/blob/dev/README.md#-api-%EB%AA%85%EC%84%B8%EC%84%9C)
5. [ERD](https://github.com/jps091/for-work/blob/dev/README.md#-erd)
6. [기술 스택](https://github.com/jps091/for-work/blob/dev/README.md#%EC%82%AC%EC%9A%A9-%EA%B8%B0%EC%88%A0-%EC%8A%A4%ED%83%9D)
7. [시스템 아키텍쳐](https://github.com/jps091/for-work/blob/dev/README.md#-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98)
8. [주요 기능 동작 흐름 및 검증](https://github.com/jps091/for-work/blob/dev/README.md#-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EB%8F%99%EC%9E%91-%ED%9D%90%EB%A6%84-%EB%B0%8F-%EA%B2%80%EC%A6%9D)
9. [기능 구현 화면](https://github.com/jps091/for-work/blob/dev/README.md#-%EA%B5%AC%EB%A7%A4%EC%9E%90-%EA%B4%80%EB%A0%A8-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84-%ED%99%94%EB%A9%B4)
10. [프로젝트 회고](https://github.com/jps091/for-work/blob/dev/README.md#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0)

---

### 📁 프로젝트 개요
이 프로젝트는 단순한 CRUD 구현을 넘어 복잡한 비즈니스 로직을 설계, 구현, 검증하는 경험을 쌓기 위해 기획되었습니다. 

특히, 결제, 환불, 자동 구매 확정, 주문 상태 관리 등 쇼핑몰의 핵심 기능을 효과적으로 설계하고, 성능 최적화 및 가용성을 고려한 개발을 목표로 삼았습니다.

또한, 요구사항을 분석하여 명사 테이블과 동사(비즈니스) 테이블을 분리하는 ERD 설계 방식을 적용하며, 비즈니스 로직이 데이터 구조에 적절히 반영되도록 테이블 모델링 경험을 쌓는데 목표로 하였습니다.

---

### 👥 기여도
**프론트엔드 개발자 1명과 협업, DB 설계 부터 백엔드 개발 및 배포 100% 기여**

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

### 📝 API 명세서

**[1. 스웨거 API 명세서 ver](http://search-info.n-e.kr:8080/swagger-ui/index.html?urls.primaryName=%EC%A0%84%EC%B2%B4%20%EC%82%AC%EC%9A%A9%EC%9E%90%EB%A5%BC%20%EC%9C%84%ED%95%9C%20For-work%20Service%20%EB%8F%84%EB%A9%94%EC%9D%B8%20API)**

<img width="700" height="400" alt="image" src="https://github.com/user-attachments/assets/22dc952f-6b34-4a74-8e73-34368cdf4fa3" /></br>


**[2. 노션 API 명세서 ver](https://cuboid-sunfish-749.notion.site/API-Notion-125bb83a1adf800ebb04e75115a0e3ac?pvs=4)**

<img width="800" height="400" alt="image" src="https://github.com/user-attachments/assets/90f20877-dd53-4750-b76f-a83180318456" />

---

### 💼 ERD

**명사 테이블(주요 엔터티)과 동사(비즈니스) 테이블(행위 및 관계를 나타내는 테이블)을 분리하는 ERD 설계 방식을 적용하였습니다.**

**여기서 동사 테이블은 단순히 비즈니스 로직을 반영하는 역할뿐만 아니라, 서로 직접적인 연관이 없는 명사 테이블 간의 관계를 맺어주는 역할도 수행합니다.**

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/fa804a6b-7a15-4e85-bab2-45bf468d67c4">

---

### ⚒ 사용 기술 스택

- Backend: Java 17, Spring Boot 3.3.2, JPA, Query DSL, MyBatis, RabbitMQ
- Database: MySQL 8.0.4, Redis
- Build Tool: Gradle
- Version Control: Git, GitHub
- Infra: EC2, S3, CDN, RDS, ElastiCache, SES
- Containerization: Docker
- CI/CD: Github Actions, ECR, CodeDeploy, S3

---

### 🪃 아키텍처

<img width="850" height="600" alt="image" src="https://github.com/user-attachments/assets/0089951e-98b2-4970-bdde-c653626484b5">

---

### 🚴 주요 기능 동작 흐름 및 검증

</br>

**1. 판매자 이력서 판매글 등록 흐름**

<img width="800" height="400" alt="image" src="https://github.com/user-attachments/assets/f86cfc3c-2a59-4125-99c1-6174077f1fce" /></br>


</br>

**2. 이력서 주문 및 결제 흐름**

<img width="1000" height="500" alt="image" src="https://github.com/user-attachments/assets/598d9f25-bd3b-4bf2-934b-7950783da707" /></br>

**3. 구매자 메일 전송 흐름**

<img width="600" height="350" alt="image" src="https://github.com/user-attachments/assets/6fb5d193-c927-4717-9a1a-2b8888cbabc7" /></br>


**4. 메일 전송 (RabbitMQ 비동기 큐 & Dead Letter 처리)**

<img width="1000" height="450" src="https://github.com/user-attachments/assets/e454b6d0-0356-45fa-9e89-5bbb3a227216"></br>


**✅ 약 150개의 단위 테스트로 검증**

<img width="1000" height="450" alt="image" src="https://github.com/user-attachments/assets/d5a4786b-6c63-4134-bf7a-140fc53b8f3a" />


---

### 🚀 구매자 관련 기능 구현 화면

</br>

**1. 판매글 조회(Pagination Key 방식), 회원가입, 로그인, 관리자 문의**

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/478da93c-71fe-4b15-b937-f7d2f8ad7b8d" /></br>

**2. 결제, 주문 전체 내역, 주문 상세 내역**

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/4b7a51e1-465d-460c-b88d-3c56eb5fb5da" /></br>

---

### 🌈 판매자 관련 기능 구현 화면

**이력서 판매 요청 작성, 나의 이력서 상태**

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/2833d15a-bd28-4f36-8a09-bf041a077b58" />

---

### 🔥 어드민 관련 기능 구현 화면


**요청 온 이력서 전체 조회, 상세 조회**

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/c55bf273-59f2-4a33-956f-a19918e2c73f" />

---

### 📓 프로젝트 회고

이번 프로젝트에서는 결제, 환불, 자동 구매 확정, 주문 상태 관리, 페이징 처리(Pagination Key)와 같은 로직을 간소화하여 구현했으며, 부하 테스트와 모니터링을 통해 성능 최적화를 경험했습니다. 또한, 약 150개의 유닛 테스트를 작성하고 검증하여 코드의 신뢰성을 높일 수 있었습니다.

프로젝트 과정에서 API 응답에 엔티티를 직접 노출하거나, 서비스 계층의 비대화, ORM 양방향 관계와 같은 주요 안티 패턴을 식별하고, 4계층 구조, 일급 컬렉션, 디자인 패턴 등을 적용해 더 객체지향적이고 품질 높은 코드에 대해 생각할 수 있었습니다.

또한 이미지 업로드 방식도 Presigned-URL 적용 및 CDN 도입으로 최적화했으며, 추후 AWS Lambda 기반의 이미지 리사이징 및 WebP 변환을 고려 중입니다. 

마지막으로 프론트엔드 개발자와 협업하면서 커뮤니케이션 방식을 익힐 수 있었고, 백엔드 서버 개발 전반뿐만 아니라 CI/CD 프로세스를 구축 및 인프라 설계까지 경험할 수 있었던 의미 있는 프로젝트였습니다.
