# Spring 로그인 시스템 구현 리포지토리

해당 디렉토리는 session 으로 구현한 로그인을 spring-security기반으로 마이그레이션을 진행한 디렉토리입니다.


## ⚙️ 마이그레이션 과정

1. SecurityConfig 설정
   - SecurityConfig.class 바탕으로 Bean 생성
     - SecurityFilterChain
     - CorsConfigurationSource
     - AuthenticationManager
     - PasswordEncoder
2. UserDetails 구현
   - Spring Security가 사용할 사용자 인증 객체 정의
3. CustomUserDetailsService 구현
   - 사용자 정보(UserDetails)를 DB에서 조회해주는 구현체
4. 회원가입 로직에서 Password 암호화 적용
   - SecurityConfig에서 생성한 PasswordEncoder를 이용하여 암호화 진행
5. 로그인 처리 변경
   - 세션 + SecurityContext 등록  
     SecurityContext 등록 및 세션에는 수동으로 등록해줘야함
6. 로그아웃 처리 (Spring Security에서 관리)
   - 기존 로그아웃 로직 삭제
7. 현재 로그인된 사용자 정보 조회
   - @AuthenticationPrincipal로 정보 접근

## ✅ 전체 흐름 요약
```
[사용자 로그인 요청]
     ↓
UsernamePasswordAuthenticationToken 생성 (id + pw)
     ↓
AuthenticationManager.authenticate(token)
→ 내부적으로 customUserDtailsService 호출
     ↓
CustomUserDetailsService.loadUserByUsername()
     ↓
→ DB에서 사용자 조회 후 CustomUserDetails 반환
     ↓
PasswordEncoder.matches(raw, encoded)
     ↓
성공 → Authentication 객체 생성
-> AuthenticationManager.authenticate(token)에서 반환됨
     ↓
SecurityContext에 Authentication 저장
     ↓
SecurityContextHolder에 SecurityContext 저장
     ↓
세션에도 SPRING_SECURITY_CONTEXT로 저장됨
```
