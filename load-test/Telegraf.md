
# Telegraf 설정 (macOS 기준)

## 설치 - homebrew 이용

```sh
brew update
brew install telegraf
# 설치 후 확인
telegraf --version
```

## 기본 설정 파일 생성

```
telegraf config > telegraf.conf
```
현재 디렉토리에 telegraf.conf 파일을 생성해줍니다.  
이 파일을 편집해서 설정합니다.

---

## InfluxDB 출력 및 시스템 입력 플러그인 설정 (outputs.influxdb, inputs)

telegraf.conf에서 다음 부분을 찾아 설정:

```
[[outputs.influxdb]]
  urls = ["http://localhost:8086"]
  database = "k6"          # k6와 같은 DB 사용 가능
  skip_database_creation = false

[[inputs.cpu]]
  percpu = true
  totalcpu = true
  report_active = true

[[inputs.mem]]
[[inputs.disk]]
[[inputs.system]]
[[inputs.processes]]
[[inputs.net]]
```
InfluxDB가 Docker로 실행 중이라면 localhost → host.docker.internal 사용

기본적인 시스템 지표 (CPU, 메모리, 디스크, 네트워크, 시스템 정보 등)를 수집 설정

---

## Telegraf 실행

```
telegraf --config telegraf.conf
```

---

## Grafana 데이터 소스 추가

### InfluxDB 데이터소스 추가
	•	Type: InfluxDB
	•	URL: http://localhost:8086
	•	Database: k6

### 대시보드 예시 쿼리 (InfluxQL)

CPU 사용률:
```sql
SELECT mean("usage_user") FROM "cpu" WHERE $timeFilter GROUP BY time($__interval) fill(null)
```
메모리 사용률:
```sql
SELECT mean("used_percent") FROM "mem" WHERE $timeFilter GROUP BY time($__interval) fill(null)
```
디스크 사용률:
```sql
SELECT mean("used_percent") FROM "disk" WHERE $timeFilter GROUP BY time($__interval), "path" fill(null)
```

+ 참고 예시 대시보드 템플릿 : 5955