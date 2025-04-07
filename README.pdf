# Cache 적용 데모
목차
1. 의존성 설정
   - build.gradle
   - application.yml
2. 적용 방법
3. 캐시 확인
   - case1: 캐시 미적용 시 API 호출
   - case2: 캐시 적용 시 API 호출
   - 저장된 캐시 확인
     - Caffeine
     - Redis
4. 캐시 초기화
   - CacheEvict를 적용하지 않고 업데이트 시
   - CacheEvict 적용 시
5. 캐시 업데이트
   - CacheManager 를 주입받아 프로그래밍 레벨에서 캐시 핸들링

## 의존성 설정
build.gradle
```java
/**
 build.gradle
 */
// Spring Cache
implementation 'org.springframework.boot:spring-boot-starter-cache'
// Cache type : caffeine
implementation 'com.github.ben-manes.caffeine:caffeine'
// Cache type : redis
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
```
application.yml
```yaml
spring:
  cache:
    # 캐시 구현체
    type: caffeine # local in-memory
  #    type: redis # global in-memory
  caffeine:
    spec: maximumSize=500,expireAfterWrite=600s,recordStats
  data:
    redis:  
      host: localhost  # 또는 도커/compose 환경에서는 'redis'
      port: 6379
      timeout: 6000

  # 조회할 메인 DBMS
  datasource:
    url: jdbc:mysql://localhost:3306/dashboard?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: ${MYSQL_USERNAME:root}
    password: ${MYSQL_PASSWORD:cachemysql}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## 적용 방법
1. `@EnableCaching` 어노테이션 적용
    ```java
    // main 메서드에 @EnableCaching 추가
    @SpringBootApplication
    @EnableCaching
    public class CashdeomApplication {

        public static void main(String[] args) {
            SpringApplication.run(CashdeomApplication.class, args);
        }

    }
    ```
2. 캐싱할 서비스에 `@Cacheable` 어노테이션 적용
```java
// key: 직렬화한 파라미터, value: response를 저장할 이름
@Cacheable(value = "dashboardData", key = "#request.toString()")
@Transactional(readOnly = true)
public List<Dashboard> fetchDashboardWithCache(FetchDashboardRequest request) {
    return mybatisDashboardRepository.fetchDashboard(request);
}
```

## 캐시 확인
> 최초 요청 후 동일한 요청에 대한 응답을 캐싱하여 SQL을 실행하지 않고 메모리의 값을 응답하는 것을 확인한다.
1. case1: 캐시 미적용 시 API 호출
   - 첫 번째 호출, 1분 26초
        ```http request
        GET http://localhost:8080/dashboard/fetch-without-cache?dimension1=AlphaDimension

        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        Date: Mon, 31 Mar 2025 07:01:03 GMT
        Keep-Alive: timeout=60
        Connection: keep-alive



        Response file saved.
        > 2025-03-31T160103.200.json

        Response code: 200; Time: 86296ms (1 m 26 s 296 ms); Content length: 40966 bytes (40.97 kB)
        ```
        - 두 번째 호출, 1분 28초
        ```http request
        GET http://localhost:8080/dashboard/fetch-without-cache?dimension1=AlphaDimension

        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        Date: Mon, 31 Mar 2025 07:05:11 GMT
        Keep-Alive: timeout=60
        Connection: keep-alive

        Response file saved.
        > 2025-03-31T160511.200.json

        Response code: 200; Time: 88577ms (1 m 28 s 577 ms); Content length: 40966 bytes (40.97 kB)
        ```
2. case2: 캐시 적용 시 API 호출
    - 첫 번째 호출, 1분 27초
        ```http request
        GET http://localhost:8080/dashboard/fetch-with-cache?dimension1=AlphaDimension

        HTTP/1.1 200 
        Content-Type: application/json
        Transfer-Encoding: chunked
        Date: Mon, 31 Mar 2025 07:08:37 GMT
        Keep-Alive: timeout=60
        Connection: keep-alive

        Response file saved.
        > 2025-03-31T160837.200.json

        Response code: 200; Time: 87321ms (1 m 27 s 321 ms); Content length: 40966 bytes (40.97 kB)
        ```
    - 두 번째 호출, 0.0007초
      ```http request
       GET http://localhost:8080/dashboard/fetch-with-cache?dimension1=AlphaDimension

       HTTP/1.1 200
       Content-Type: application/json
       Transfer-Encoding: chunked
       Date: Mon, 31 Mar 2025 07:10:33 GMT
       Keep-Alive: timeout=60
       Connection: keep-alive

       Response file saved.
        > 2025-03-31T161033.200.json

       Response code: 200; Time: 7ms (7 ms); Content length: 40966 bytes (40.97 kB)
       ```
3. 저장된 캐시 확인
   - Caffeine
        ```http request
        GET http://localhost:8080/cache/stats/dashboardData

        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        Date: Mon, 31 Mar 2025 07:14:58 GMT
        Keep-Alive: timeout=60
        Connection: keep-alive

        {
            "cacheType": "Caffeine",
            "cacheName": "dashboardData",
            "hitCount": 0,
            "missCount": 0,
            "loadSuccessCount": 0,
            "loadFailureCount": 0,
            "totalLoadTime": 0.0,
            "evictionCount": 0,
            "evictionWeight": 0,
            "size": 1,
            "keys": [
                "FetchDashboardRequest(dimension1=AlphaDimension, dimension2=null, dimension3=null, dimension4=null, dimension5=null, dimension6=null, dimension7=null, dimension8=null, dimension9=null, dimension10=null)"
            ]
        }
        Response file saved.
        > 2025-03-31T161458.200.json

        Response code: 200; Time: 11ms (11 ms); Content length: 401 bytes (401 B)
        ```
   - Redis
       ```http request
        GET http://localhost:8080/cache/stats/dashboardData

        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        Date: Mon, 31 Mar 2025 07:16:50 GMT
        Keep-Alive: timeout=60
        Connection: keep-alive

        {
            "cacheType": "Redis",
            "cacheName": "dashboardData",
            "pattern": "dashboardData::*",
            "keys": [
                "dashboardData::FetchDashboardRequest(dimension1=AlphaDimension, dimension2=null, dimension3=null, dimension4=null, dimension5=null, dimension6=null, dimension7=null, dimension8=null, dimension9=null, dimension10=null)",
                "dashboardData::FetchDashboardRequest(dimension1=null, dimension2=null, dimension3=null, dimension4=null, dimension5=null, dimension6=null, dimension7=null, dimension8=null, dimension9=null, dimension10=null)"
            ],
            "size": 2
        }
        Response file saved.
      >   2025-03-31T161650.200.json

        Response code: 200; Time: 13ms (13 ms); Content length: 526 bytes (526 B)
        ```
      redis cli사용 내부 직접 확인
      ```shell
      ╰─ docker exec -it cache-redis redis-cli
      127.0.0.1:6379> keys *
      1) "dashboardData::FetchDashboardRequest(dimension1=AlphaDimension, dimension2=null, dimension3=null, dimension4=null, dimension5=null, dimension6=null, dimension7=null, dimension8=null, dimension9=null, dimension10=null)"
      2) "dashboardData::FetchDashboardRequest(dimension1=null, dimension2=null, dimension3=null, dimension4=null, dimension5=null, dimension6=null, dimension7=null, dimension8=null, dimension9=null, dimension10=null)"
         127.0.0.1:6379>
      ```

## 캐시 초기화
> 동일한 요청에 대해 캐시가 저장된 상태에서 내부 데이터 변경 시 캐시 초기화 할 수 있어야 한다.
### CacheEvict 적용
```java
java
@Transactional
@CacheEvict(value = "dashboardData", allEntries = true)
public void updateDashboardWithCacheEvict() {
        mybatisDashboardRepository.updateDashboard();
        }
```
1. CacheEvict를 적용하지 않고 업데이트 시
    - 첫번째 호출
        ```http request
        GET http://localhost:8080/dashboard/fetch-with-cache

        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        Date: Mon, 31 Mar 2025 08:26:08 GMT
        Keep-Alive: timeout=60
        Connection: keep-alive

        [
        {
        "dimension1": "CharlieDimension",
        "dimension2": "GolfDimension",
        "dimension3": "EchoDimension",
        "dimension4": "DeltaDimension",
        "dimension5": "FoxtrotDimension",
        "dimension6": "EchoDimension",
        "dimension7": null,
        "dimension8": null,
        "dimension9": null,
        "dimension10": null,
        "measure1": 70405.0065,
        "measure2": 70405.0065,
        "measure3": 70405.0065,
        "measure4": 70405.0065,
        "measure5": 70401.7948,
        "measure6": 60413.1629,
        "measure7": 60306.658
        }, ...
        ]
        Response file saved.
        > 2025-03-31T172608.200.json

        Response code: 200; Time: 11ms (11 ms); Content length: 40989 bytes (40.99 kB)
        
        ```
   - CacheEvict 적용하지 않고 업데이트 후 두 번째 호출
        > 값이 변하지 않음 기존 캐시를 그대로 응답하기 때문에 0.0007초 소요
       ```http request
       GET http://localhost:8080/dashboard/fetch-with-cache

       HTTP/1.1 200
       Content-Type: application/json
       Transfer-Encoding: chunked
       Date: Mon, 31 Mar 2025 08:31:21 GMT
       Keep-Alive: timeout=60
       Connection: keep-alive

       [
       {
       "dimension1": "CharlieDimension",
       "dimension2": "GolfDimension",
       "dimension3": "EchoDimension",
       "dimension4": "DeltaDimension",
       "dimension5": "FoxtrotDimension",
       "dimension6": "EchoDimension",
       "dimension7": null,
       "dimension8": null,
       "dimension9": null,
       "dimension10": null,
       "measure1": 70405.0065,
       "measure2": 70405.0065,
       "measure3": 70405.0065,
       "measure4": 70405.0065,
       "measure5": 70401.7948,
       "measure6": 60413.1629,
       "measure7": 60306.658
       }, ...
       ]
       Response file saved.
        > 2025-03-31T173121.200.json

       Response code: 200; Time: 7ms (7 ms); Content length: 40989 bytes (40.99 kB)

       ```
     
2.CacheEvict 적용 시
> 캐시 초기화 후 조회 시 업데이트된 데이터로 응답, 조회 시간 1분 27초
```http request

GET http://localhost:8080/dashboard/fetch-with-cache

    HTTP/1.1 200 
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Mon, 31 Mar 2025 08:39:13 GMT
    Keep-Alive: timeout=60
    Connection: keep-alive

    [
      {
        "dimension1": "CharlieDimension",
        "dimension2": "GolfDimension",
        "dimension3": "EchoDimension",
        "dimension4": "DeltaDimension",
        "dimension5": "FoxtrotDimension",
        "dimension6": "EchoDimension",
        "dimension7": null,
        "dimension8": null,
        "dimension9": null,
        "dimension10": null,
        "measure1": 90405.0065,
        "measure2": 90405.0065,
        "measure3": 90405.0065,
        "measure4": 90405.0065,
        "measure5": 90401.7948,
        "measure6": 60413.1629,
        "measure7": 60306.658
      }, ...
    ]
    Response file saved.
    > 2025-03-31T173913.200.json

    Response code: 200; Time: 87425ms (1 m 27 s 425 ms); Content length: 40989 bytes (40.99 kB)
```
    
## 캐시 업데이트
> 특정 행동 후 지정한 요청에 대해 저장된 캐시가 업데이트 될 수 있어야 한다.
1. CacheManager 를 주입받아 프로그래밍 레벨에서 캐시 핸들링
    ```java
    @Transactional
    public void updateDashboardWithCacheUpdate() {
        // 내부 데이터 변경
        mybatisDashboardRepository.updateDashboard();
        
        // 캐싱할 요청 초기화
        FetchDashboardRequest request = new FetchDashboardRequest();

        Cache cache = cacheManager.getCache("dashboardData");
        if (cache != null) {
            cache.evict(request.toString());
        }

        // 최신 데이터 조회 후 캐시에 직접 저장
        List<Dashboard> newData = mybatisDashboardRepository.fetchDashboard(request);
        if (cache != null) {
            cache.put(request.toString(), newData);
        }
    }
    ```
    내부에서 초기화 후 최신 데이터로 캐시 초기화
    > 조회 시간 0.0008초, 업데이트된 데이터 조회
    ```http request
    GET http://localhost:8080/dashboard/fetch-with-cache

    HTTP/1.1 200 
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Mon, 31 Mar 2025 08:44:28 GMT
    Keep-Alive: timeout=60
    Connection: keep-alive

    [
      {
        "dimension1": "CharlieDimension",
        "dimension2": "GolfDimension",
        "dimension3": "EchoDimension",
        "dimension4": "DeltaDimension",
        "dimension5": "FoxtrotDimension",
        "dimension6": "EchoDimension",
        "dimension7": null,
        "dimension8": null,
        "dimension9": null,
        "dimension10": null,
        "measure1": 100405.0065,
        "measure2": 100405.0065,
        "measure3": 100405.0065,
        "measure4": 100405.0065,
        "measure5": 100401.7948,
        "measure6": 60413.1629,
        "measure7": 60306.658
      }, ...
    ]
    Response file saved.
    > 2025-03-31T174428.200.json

    Response code: 200; Time: 8ms (8 ms); Content length: 41262 bytes (41.26 kB)
    ```