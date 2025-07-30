import http from 'k6/http';
import { sleep, check } from 'k6';

// export const options = {
//   vus: 50,
//   duration: '30s',
// };

export const options = {
  stages: [
  { duration: '3m', target: 2500 },
  { duration: '10m', target: 2500 },  // 30초 동안 50명의 VUs로 증가
    { duration: '10s' , target: 0 },  
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
      'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcklkIjoiMTIzNCIsIm5hbWUiOiLslYjrqZTrqqgiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc1MjgyMDk5NywiZXhwIjoxNzUyODIyNzk3fQ.lKek6DRTCnkVATit3I1eiHmlb3FiUrm3_KF8k4tKcYs'
    },
  };
  let res = http.post(url, payload, params);
  check(res, { "status is 200": (res) => res.status === 200 });
  sleep(1);
}
