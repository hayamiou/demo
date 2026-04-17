import http from 'k6/http';
import { Rate } from 'k6/metrics';

const failureRate = new Rate('failed_requests');
export function test_api_endpoints_config() {
    const baseUrl = 'http://localhost:8080';

    // Test GET /books
    let res = http.get(`${baseUrl}/books`);
    failureRate.add(res.status !== 200);

    // Test POST /books avec données aléatoires
    const payload = JSON.stringify({
        title: `Book ${Math.random()}`,
        author: `Author ${Math.random()}`
    });

    res = http.post(`${baseUrl}/books`, payload, {
        headers: { 'Content-Type': 'application/json' }
    });
    failureRate.add(res.status !== 201);
}