## SaltUtil

#### SecureRandom을 사용하는 이유

일반적인 Random 클래스는 내부 시드(seed)로 인해 예측 가능한 값이 생성될 수 있어 보안에 부적합
SecureRandom은 예측이 거의 불가능한 난수를 생성합니다.

#### 16바이트 길이로 salt를 설정한 이유

- 충분한 무작위성 보장
  16바이트 = 128비트 = 128비트에 해당하는 각 값을 무작위로 생성하였을때 2 ** 128개의 조합
- 바이트 배열은 문자 집합 제한 없이 모든 조합 가능
- 바이트 배열은 Base64 등으로 직렬화 가능 = 안전한 저장/전송


#### Base64로 인코딩하는 이유
- 바이트 배열을 문자열로 저장하기 위해
- URL, 파일, DB 저장에 안전한 문자 형식
- **인코딩 후에는?**
  - Base64는 3바이트를 4문자로 인코딩 = 4/3으로 증가
  - 16 // 3 = 5.33
  - 3바이트씩 5개 그룹 = 15바이트 처리
  - 남은 1바이트 처리 시 패딩(==)
  - (5+1) * 4 = 24문자로 변환
  - SaltUtil 코드에서는 byte[16] 고정이므로, Base64 문자열은 항상 24자

---

## HashUtil

#### MessageDigest
- MessageDigest는 Java 표준 라이브러리 (java.security)에서 제공하는 해시 함수 처리 클래스
- 알고리즘(예: SHA-256, MD5, SHA-512 등)을 사용해 단방향 암호화 해시값을 계산

아래는 HashUtil 클래스에서 사용된 MessageDigest, byte[], Hex 변환, StringBuilder의 사용 목적을 상세히 설명한 내용입니다.


**주요 메서드**

| 메서드 | 설명 |
|--------|------|
| `getInstance(String algorithm)` | 지정한 알고리즘의 해시 인스턴스를 생성 (예: `"SHA-256"`) |
| `update(byte[] input)` | 해시할 데이터를 누적 공급 |
| `digest()` | 지금까지 공급된 데이터에 대한 해시 결과 반환 (`byte[]`) |
| `digest(byte[] input)` | 입력을 한 번에 해시하고 결과 반환 |
| `reset()` | 내부 상태 초기화 |


- 해시 함수는 바이트 단위로 계산합니다.
- 예를 들어 SHA-256의 출력은 항상 256비트 = 32바이트 고정이며, 이는 숫자나 문자열이 아닌 raw byte 데이터입니다.
- 문자열 형태가 아닌, 2진수 형태의 결과값이기 때문에 byte[]로 반환됩니다.


#### 왜 Hex 문자열로 바꾸는가?

•	byte[]는 사람이 읽기 어렵고, DB 저장/로깅/출력 등에도 적합하지 않습니다.
•	그래서 보통 16진수(hex)로 바꿔서 처리합니다.

장점
•	사람이 식별 가능 (예: SHA-256 해시: a94a8fe5ccb19ba61c4c0873d391e987982fbbd3)
•	고정된 길이 (SHA-256 = 32바이트 = 64자리 16진수 문자열)
•	URL/DB 저장에 안전하고 정형화된 문자열 형태로 변환 가능

⸻ 

#### StringBuilder를 사용하는 이유


이유
•	반복문을 통해 1바이트씩 문자열로 누적하는 경우, StringBuilder를 쓰는 것이 성능상 매우 효율적
•	String은 immutable(불변)이라서 문자열 덧붙이기를 할 때마다 새로운 객체 생성 → 성능 저하
•	StringBuilder는 내부적으로 하나의 버퍼를 사용해 문자열을 이어붙임

String.format("%02x", b)의 의미
•	각 바이트를 2자리의 16진수 문자열로 변환
•	0a, 1f, ae 처럼 항상 2자리 유지되도록 %02x 포맷 사용


**생성자**

| 생성자 | 설명 |
|--------|------|
| `StringBuilder()` | 기본 용량(16)으로 초기화 |
| `StringBuilder(int capacity)` | 초기 용량을 지정 |
| `StringBuilder(String str)` | 주어진 문자열로 초기화 |

**메소드**

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `append(String s)` | 문자열 추가 | `sb.append("abc")` |
| `insert(int offset, String s)` | 지정 위치에 문자열 삽입 | `sb.insert(3, "X")` |
| `delete(int start, int end)` | 지정 범위 삭제 (`start <= i < end`) | `sb.delete(2, 5)` |
| `deleteCharAt(int index)` | 특정 인덱스 문자 삭제 | `sb.deleteCharAt(3)` |
| `replace(int start, int end, String str)` | 범위 대체 | `sb.replace(0, 4, "Hi")` |
| `reverse()` | 문자열 뒤집기 | `sb.reverse()` |
| `charAt(int index)` | 인덱스에 있는 문자 반환 | `char c = sb.charAt(2)` |
| `length()` | 현재 문자열 길이 반환 | `int len = sb.length()` |
| `toString()` | `String` 객체로 변환 | `String s = sb.toString()` |
| `setLength(int newLength)` | 문자열 길이 조절 | `sb.setLength(5)` |
| `capacity()` | 내부 버퍼 용량 확인 | `int cap = sb.capacity()` |
| `ensureCapacity(int min)` | 버퍼 용량을 최소값 이상으로 확장 | `sb.ensureCapacity(100)` |
