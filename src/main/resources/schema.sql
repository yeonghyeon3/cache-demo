-- init.sql

-- 기존 테이블이 있으면 삭제 (테스트용)
DROP TABLE IF EXISTS dashboard_data;

-- 대시보드 데이터 테이블 생성 (10개 필터 컬럼 + 7개 measure 컬럼)
CREATE TABLE dashboard_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dimension1 VARCHAR(50),
    dimension2 VARCHAR(50),
    dimension3 VARCHAR(50),
    dimension4 VARCHAR(50),
    dimension5 VARCHAR(50),
    dimension6 VARCHAR(50),
    dimension7 VARCHAR(50),
    dimension8 VARCHAR(50),
    dimension9 VARCHAR(50),
    dimension10 VARCHAR(50),
    measure1 INT,
    measure2 INT,
    measure3 INT,
    measure4 INT,
    measure5 INT,
    measure6 INT,
    measure7 INT
);

--
-- 기존 테이블이 있으면 삭제 (테스트용)
DROP TABLE IF EXISTS cache_entries;
CREATE TABLE cache_entries (
    cache_name VARCHAR(64) NOT NULL,
    cache_key VARCHAR(1024) NOT NULL,
    cache_value BLOB NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (cache_name, cache_key(255))
);