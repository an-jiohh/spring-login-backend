import http from 'k6/http';
import { sleep, check } from 'k6';

// export const options = {
//   vus: 50,
//   duration: '30s',
// };

export const options = {
  stages: [
  { duration: '5m', target: 2000 },
  { duration: '25m', target: 5000 },  // 30초 동안 50명의 VUs로 증가
  { duration: '10s' , target: 0 },   // 30초 동안 VUs를 0으로 감소
  ],
};

export default function() {
  const url = 'http://host.docker.internal:8080/api/memos';
  const payload = JSON.stringify({
      "content": "contentText",
    });
  const params = {
    headers: {
      'Content-Type': 'application/json',
      'Cookie': 'JSESSIONID=D02DED3195198E7908AD9F263BC2E0C4'
    },
  };
  let res = http.post(url, payload, params);
  check(res, { "status is 200": (res) => res.status === 200 });
  sleep(1);
}
