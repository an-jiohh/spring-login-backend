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
      "role": "string"
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
    - `withCredentials: true` 설정 필요
    - 응답에 세션 쿠키 포함

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
- **특이사항**: 세션 쿠키 제거됨

#### 1.3. 인증 상태 확인
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
    - 앱 시작 시 자동 호출
    - 세션 유효성 검증에 사용

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
1. **인증 관련**
   - 모든 API 요청에 `withCredentials: true` 설정 필요
   - 인증이 필요한 요청에서 세션이 없으면 401 응답
   - 401 응답 시 프론트엔드에서 자동으로 로그인 페이지로 리다이렉트
2. **HTTP 상태 코드**
   - 400 Bad Request: 잘못된 요청 데이터
   - 401 Unauthorized: 인증 실패 또는 세션 만료
   - 403 Forbidden: 권한 없음
   - 404 Not Found: 리소스를 찾을 수 없음
   - 500 Internal Server Error: 서버 오류
