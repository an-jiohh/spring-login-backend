# Spring Jwt 로그인

## 📌 개요

JWT (JSON Web Token) 기반 로그인을 구현하는 프로젝트입니다.

## ⚙️ 기술 스택

- **Backend**: Spring Boot
- **Frontend**: React
- **Database**: H2 (인메모리 DB)

---

## 🚀 구현 방식별 설명

### 공통
- Spring Data JPA의 추상화 대신, EntityManager를 명시적으로 사용하여 데이터베이스 접근 로직을 구현

### JWT 기반 로그인
- 세션 방식으로 구현 완료, 세션 프로젝트를 기반으로 JWT으로 마이그레이션 진행
- 로그인 성공 시 Access Token과 Refresh Token을 생성하여 클라이언트에 전달
    - Access Token: 만료 시간이 짧고, Authorization 헤더를 통해 매 요청마다 포함됨
    - Refresh Token: 만료 시간이 길며, HttpOnly 쿠키로 저장됨
- 서버는 Refresh Token을 Redis 등에 저장하여 유효성 관리 및 탈취 시 폐기 가능하도록 구성
- 클라이언트는 Access Token이 만료되었을 경우, Refresh Token을 이용해 Access Token 재발급 요청 수행
- 로그아웃 시 서버에서 Refresh Token을 제거하여 재사용 방지
- 보안 요소
    - HTTPS 사용을 전제로 전송 중 탈취 방지
    - Refresh Token은 HttpOnly + Secure 쿠키에 저장
    - Access Token은 노출되어도 제한된 시간 동안만 사용 가능
    - 서버 측 토큰 무효화 전략 적용 (토큰 로테이션 등)

---

## ⚙️ 마이그레이션 과정

### 설계(Access + Refresh Token 기반)
1. 로그인 시 서버에서 2개의 토큰 발급
   - Access Token (예: 30분)
   - Refresh Token (예: 7일)

2. Access Token은 Authorization: Bearer <token>으로 API 요청 시 사용

3. 만료되면 Refresh Token을 이용해 Access Token 재발급

### 단계별 마이그레이션 진행

1. JwtUtil 구현 (Access + Refresh 생성/검증)
- 두 토큰의 만료 시간 다르게 설정
- 비밀키 동일하게 사용 가능
- 
2. 로그인 성공 시 2개의 토큰 발급
   (refreshToken은 HttpOnly 쿠키로 전달하는 것으로 고려)

3. 클라이언트는 Authorization 헤더에 accessToken 실어서 API 호출

4. 인증 필터(JwtAuthFilter)에서 Access Token 검증

5. Access Token 만료 시 Refresh Token을 통해 재발급
   - 요청: POST /auth/refresh  
   - 검증: Refresh Token 유효성 + 사용자 정보 확인  
   - 응답: 새로운 Access Token   

---


✅ TODO
- [X] API 명세서 개선
- [X] JWT 기반 로그인 구현
  - [X] JwtUtil 구현 (Access + Refresh 생성/검증)
  - [X] 로그인 성공 시 2개의 토큰 발급
  - [X] 클라이언트는 Authorization 헤더에 accessToken 실어서 API 호출
  - [X] 인증 필터(JwtAuthFilter) 구현
  - [X] Access Token 재발급 구현


