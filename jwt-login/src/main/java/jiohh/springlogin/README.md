# API 명세서

## 공통 사항

### 1. 응답 형식
#### 성공 응답
```json
{
  "status": "success",
  "data": { ... }  // API별 응답 데이터
}
```

#### 에러 응답
```json
{
  "status": "error",
  "code": "ERROR_CODE",
  "message": "에러 메시지"
}
```

### 2. 주요 에러 코드
- `INVALID_CREDENTIALS`: 로그인 실패
- `INVALID_TOKEN`: 유효하지 않은 토큰
- `SESSION_EXPIRED`: 세션 만료
- `UNAUTHORIZED`: 인증 필요
- `FORBIDDEN`: 권한 없음
- `NOT_FOUND`: 리소스 없음
- `VALIDATION_ERROR`: 유효성 검사 실패
- `INTERNAL_ERROR`: 서버 내부 오류

## API 목록

### 1. 인증 관련 API

#### 1.1. 로그인
- **엔드포인트**: `/login`
- **메서드**: POST
- **요청 데이터**:
  ```json
  {
    "userId": "string",
    "password": "string"
  }
  ```
- **성공 응답** (200 OK):
  ```json
  {
    "status": "success",
    "data": {
      "userId": "string",
      "email": "string",
      "role": "string",
      "accessToken": "eyJhbGciOiJIUzI1NiIsInR..."
    }
  }
  ```
- **실패 응답** (401 Unauthorized):
  ```json
  {
    "status": "error",
    "code": "INVALID_CREDENTIALS",
    "message": "이메일 또는 비밀번호가 올바르지 않습니다"
  }
  ```
- **특이사항**:
    - Access Token은 응답 본문에 포함되며, Authorization 헤더(`Bearer {token}`)로 매 요청마다 전송
    - Refresh Token은 HttpOnly + Secure 쿠키로 저장됨
    - 클라이언트는 Access Token이 만료되었을 경우 Refresh Token을 이용하여 토큰 재발급 요청 수행

#### 1.2. 로그아웃
- **엔드포인트**: `/logout`
- **메서드**: POST
- **성공 응답** (200 OK):
  ```json
  {
    "status": "success"
  }
  ```
- **실패 응답** (401 Unauthorized):
  ```json
  {
    "status": "error",
    "code": "UNAUTHORIZED",
    "message": "인증이 필요합니다"
  }
  ```
- **특이사항**: 
    - 서버에서 저장된 Refresh Token 제거
    - 클라이언트 쿠키의 Refresh Token 삭제


#### 1.3. 토큰 재발급
- **엔드포인트**: `/reissue`
- **메서드**: POST
- **요청 헤더**:
  - `Cookie`: HttpOnly + Secure Refresh Token 포함
- **성공 응답** (200 OK):
  ```json
  {
    "status": "success",
    "data": {
      "accessToken": "eyJhbGciOiJIUzI1NiIsInR..."
    }
  }
  ```
- **실패 응답** (401 Unauthorized):
  ```json
  {
    "status": "error",
    "code": "INVALID_TOKEN",
    "message": "Refresh Token이 유효하지 않거나 만료되었습니다"
  }
  ```
- **특이사항**:
  - 클라이언트는 Access Token이 만료된 경우 `/reissue` 요청을 통해 새로운 Access Token을 발급받을 수 있음
  - Refresh Token은 쿠키에 저장되어 전송되며, 서버는 DB 내 저장된 Refresh Token과 비교하여 유효성 검증 후 Access Token 재발급

#### 1.4. 인증 상태 확인
- **엔드포인트**: `/me`
- **메서드**: GET
- **성공 응답** (200 OK):
  ```json
  {
    "status": "success",
    "data": {
      "userId": "number",
      "name": "string",
      "role": "string"
    }
  }
  ```
- **실패 응답** (401 Unauthorized):
  ```json
  {
    "status": "error",
    "code": "SESSION_EXPIRED",
    "message": "세션이 만료되었습니다"
  }
  ```
- **특이사항**:
    - Authorization 헤더에 Access Token 필요
    - Access Token이 만료된 경우, Refresh Token으로 재발급 후 재시도 가능

#### 1.4. 회원가입
- **엔드포인트**: `/signup`
- **메서드**: POST
- **요청 데이터**:
  ```json
  {
    "userId": "string",
    "password": "string",
    "name": "string"
  }
  ```
- **성공 응답** (201 Created):
  ```json
  {
    "status": "success"
  }
  ```
- **실패 응답** (400 Bad Request):
  ```json
  {
    "status": "error",
    "code": "VALIDATION_ERROR",
    "message": "이미 사용 중인 아이디입니다"
  }
  ```

### 2. 메모 관련 API

#### 2.1. 메모 목록 조회
- **엔드포인트**: `/api/memos`
- **메서드**: GET
- **성공 응답** (200 OK):
  ```json
  {
    "status": "success",
    "data": {
      "memos": [
        {
          "id": "number",
          "content": "string",
          "date": "string"  // "YYYY.MM.DD" 형식
        }
      ]
    }
  }
  ```
- **실패 응답** (401 Unauthorized):
  ```json
  {
    "status": "error",
    "code": "UNAUTHORIZED",
    "message": "인증이 필요합니다"
  }
  ```
- **인증 필요**: Yes

#### 2.2. 메모 작성
- **엔드포인트**: `/api/memos`
- **메서드**: POST
- **요청 데이터**:
  ```json
  {
    "content": "string"
  }
  ```
- **성공 응답** (201 Created):
  ```json
  {
    "status": "success",
    "data": {
      "id": "number",
      "content": "string",
      "date": "string"
    }
  }
  ```
- **실패 응답** (400 Bad Request):
  ```json
  {
    "status": "error",
    "code": "VALIDATION_ERROR",
    "message": "메모 내용은 필수입니다"
  }
  ```
- **인증 필요**: Yes

## 공통 사항
1. 인증 관련
   - 모든 인증이 필요한 요청에 Access Token 필요 (Authorization: Bearer {token})
   - Access Token이 만료된 경우 Refresh Token을 이용하여 재발급 요청
   - Refresh Token은 HttpOnly + Secure 쿠키로 저장됨
   - 401 응답 시 프론트엔드에서 자동으로 로그인 페이지로 리다이렉트 또는 토큰 재발급 로직 수행
2. **HTTP 상태 코드**
   - 400 Bad Request: 잘못된 요청 데이터
   - 401 Unauthorized: 인증 실패 또는 세션 만료
   - 403 Forbidden: 권한 없음
   - 404 Not Found: 리소스를 찾을 수 없음
   - 500 Internal Server Error: 서버 오류
