# Spring Jwt 로그인 - 마이그레이션

## 📌 개요

JWT (JSON Web Token) 기반 로그인으로 구현한 프로젝트를 Spring Security 과 JJWT를 사용하여 마이그레이션을 진행한 프로젝트입니다.


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

## 마이그레이션 과정

1. Spring Security 설정 구성
   - SecurityFilterChain을 직접 정의하여 필요한 보안 기능 선택적으로 활성화  
   - CSRF, formLogin, httpBasic 등 불필요한 설정 제거  
   - JWT 인증 필터를 Spring Security 필터 체인에 삽입

2. UserDetails / UserDetailsService 구현
   - 사용자 도메인 모델을 기반으로 커스텀 CustomUserDetails 생성 
   - CustomUserDetailsService에서 DB 조회 후 인증 정보 반환

3. 비밀번호 암호화
   - PasswordEncoder 등록 및 회원가입 시 암호화 저장
   - 기존 PasswordEncoder 없이 수동으로 Salt처리

4. JWT 유틸리티 마이그레이션
   - 기존 Base64, Hash함수를 이용하여 수동으로 JWT생성을 마이그레이션
   - JJWT 기반으로 AccessToken / RefreshToken 생성
   - 서명 검증, 만료시간 체크, 사용자 정보 파싱 기능 포함

5. 로그인 처리
   - AuthenticationManager를 통해 인증 로직 실행
   - 인증 성공 시 AccessToken 응답 + RefreshToken은 HttpOnly 쿠키로 저장

6. JWT 인증 필터 구현
   - 요청마다 Authorization 헤더의 Bearer 토큰 추출
   - 토큰 유효성 검사 후 SecurityContextHolder에 인증 객체 저장

7. 사용자 정보 접근 방식 변경 
   - @AuthenticationPrincipal 또는 SecurityContextHolder를 통해 현재 사용자 정보 접근

8. 토큰 재발급
   - RefreshToken 유효성 검증 후 AccessToken 재발급 API 제공
   - RefreshToken은 DB에 저장하여 클라이언트와 서버에서 함께 관리
   - 재발급시 토큰 로테이션
9. 로그아웃 처리
   - RefreshToken 삭제 및 HttpOnly 쿠키 만료 설정
   - 클라이언트 단에서 AccessToken도 제거

10. 인증/인가 예외 처리 설정
    - AuthenticationEntryPoint → 401 (비로그인 요청)
    - AccessDeniedHandler → 403 (권한 없는 요청)
