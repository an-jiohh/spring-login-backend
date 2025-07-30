import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
  vus: 10,
  duration: '30s',
};

// export const options = {
//   stages: [
//     { duration: '1m', target: 50 },  // 1분 동안 50명의 VUs로 증가
//     { duration: '3m', target: 100 }, // 3분 동안 100명의 VUs 유지
//     { duration: '1m', target: 0 },   // 1분 동안 VUs를 0으로 감소
//   ],
// };

export default function() {
  const url = 'http://host.docker.internal:8080/login';
  const payload = JSON.stringify({
      "userId": "1234",
      "password": "12341234"
    });
  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };
  let res = http.post(url, payload, params);
  
  check(res, { "status is 200": (res) => res.status === 200 });
  sleep(1);
}
