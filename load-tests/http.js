import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend } from 'k6/metrics';

const responseBodySize = new Trend('response_body_size');

const baseUrl = (__ENV.BASE_URL || 'http://localhost:8080').replace(/\/$/, '');
const targetPath = normalizePath(__ENV.TARGET_PATH || '/');
const method = (__ENV.HTTP_METHOD || 'GET').toUpperCase();
const requestBody = __ENV.REQUEST_BODY || null;
const expectedStatus = readNumber('EXPECTED_STATUS', 200);
const expectedContentType = __ENV.EXPECTED_CONTENT_TYPE ?? 'application/json';
const maxP95Ms = readNumber('MAX_P95_MS', 500);
const maxErrorRate = readNumber('MAX_ERROR_RATE', 0.01);
const maxResponseBytes = readNumber('MAX_RESPONSE_BYTES', 0);

const thresholds = {
  checks: ['rate==1'],
  http_req_duration: [`p(95)<${maxP95Ms}`],
  http_req_failed: [`rate<${maxErrorRate}`],
};

// 0이면 응답 크기는 판정하지 않는다. 필요한 시나리오에서만 활성화한다.
if (maxResponseBytes > 0) {
  thresholds.response_body_size = [`p(95)<${maxResponseBytes}`];
}

export const options = {
  stages: [
    { duration: __ENV.RAMP_UP_DURATION || '10s', target: readNumber('TARGET_VUS', 10) },
    { duration: __ENV.HOLD_DURATION || '30s', target: readNumber('TARGET_VUS', 10) },
    { duration: __ENV.RAMP_DOWN_DURATION || '10s', target: 0 },
  ],
  thresholds,
  userAgent: 'efub6-k6-load-test/1.0',
};

export default function () {
  const headers = {};
  if (__ENV.CONTENT_TYPE) {
    headers['Content-Type'] = __ENV.CONTENT_TYPE;
  }
  if (__ENV.AUTH_TOKEN) {
    headers.Authorization = `Bearer ${__ENV.AUTH_TOKEN}`;
  }

  const response = http.request(method, `${baseUrl}${targetPath}`, requestBody, {
    headers,
    responseCallback: http.expectedStatuses(expectedStatus),
    tags: { endpoint: `${method} ${targetPath}` },
    timeout: __ENV.REQUEST_TIMEOUT || '5s',
  });

  responseBodySize.add(response.body ? response.body.length : 0);

  const checks = {
    [`status is ${expectedStatus}`]: (res) => res.status === expectedStatus,
  };
  if (expectedContentType) {
    checks[`content type includes ${expectedContentType}`] = (res) =>
        (res.headers['Content-Type'] || '').includes(expectedContentType);
  }
  check(response, checks);

  sleep(readNumber('SLEEP_SECONDS', 1));
}

function normalizePath(path) {
  return path.startsWith('/') ? path : `/${path}`;
}

function readNumber(name, fallback) {
  const rawValue = __ENV[name];
  if (rawValue === undefined || rawValue === '') {
    return fallback;
  }

  const value = Number(rawValue);
  if (!Number.isFinite(value) || value < 0) {
    throw new Error(`${name} must be a non-negative number: ${rawValue}`);
  }
  return value;
}