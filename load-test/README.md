# Load Testing

## 프로젝트 개요
이 프로젝트는 Spring 기반의 로그인 백엔드 시스템에 대한 부하 테스트를 수행한 README입니다.

### 구성요소
- k6
- InfluxDB
- Grafana
- Telegraf


## 파일 및 폴더 구조

```
load-test/
├── docker-compose.yml          # Grafana 및 관련 서비스를 설정하고 실행하기 위한 Docker Compose 파일
├── grafana/                    # Grafana 관련 파일 및 플러그인 디렉토리
│   ├── csv/                    # CSV 파일 저장 디렉토리
│   ├── grafana.db              # Grafana 데이터베이스 파일
│   ├── pdf/                    # PDF 파일 저장 디렉토리
│   └── plugins/                # Grafana 플러그인 디렉토리
├── jwt.md                      # JWT 관련 정보 및 설정 방법 문서
├── k6/                         # 부하 테스트 스크립트 디렉토리
│   ├── login_script.js         # 로그인 기능 부하 테스트 스크립트
│   ├── memo_jwt_script.js      # JWT를 사용한 메모 기능 테스트 스크립트
│   └── memo_session_script.js  # 세션을 사용한 메모 기능 테스트 스크립트
├── README.md                   # 프로젝트 설명 및 가이드 문서
├── session.md                  # 세션 관리 관련 정보 및 설정 방법 문서
├── telegraf.conf               # Telegraf 설정 파일
└── Telegraf.md                 # Telegraf 설정 및 사용법 문서
```

## 테스트 실행 방법
1. Grafana, InfluxDB, K6 서비스를 시작(Docker Compose)
   ```bash
   docker-compose up -d
   ```
2. Telegraf 서비스 설치 및 시작  
   **상세내용은 Telegraf.md 파일 참고**  
   ```bash
   telegraf --config telegraf.conf
   ```
3. k6 스크립트를 실행
   ```bash
   docker-compose run k6 run /scripts/memo_jwt_script.js
   ```

## 테스트 스크립트
`k6` 디렉토리에는 다음과 같은 테스트 스크립트가 포함되어 있습니다:
- `login_script.js`: 로그인 기능 테스트 스크립트
- `memo_jwt_script.js`: JWT를 사용한 메모 기능 테스트 스크립트
- `memo_session_script.js`: 세션을 사용한 메모 기능 테스트 스크립트

## 결과 분석
- [JWT와 Session의 성능 차이는 어떻게 될까? – 부하테스트 1](http://localhost:3001/blog/Backendload_test1)
- [JWT와 Session의 성능 차이는 어떻게 될까? – 부하테스트 2](http://localhost:3001/blog/Backendload_test2)
- [JWT와 Session의 성능 차이는 어떻게 될까? – 부하테스트 3](http://localhost:3001/blog/Backendload_test3)

## 참고 자료
- `Telegraf.md`: Telegraf 설정 및 사용법
- `jwt.md`: JWT 부하테스트 K6 결과
- `session.md`: 세션 부하테스트 K6 결과