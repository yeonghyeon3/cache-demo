-- init.sql
use dashboard;

-- 기존 테이블이 있으면 삭제 (테스트용)
# DROP TABLE IF EXISTS cache_entries;
CREATE TABLE cache_entries (
       cache_name VARCHAR(64) NOT NULL,
       cache_key VARCHAR(1024) NOT NULL,
       cache_value BLOB NOT NULL,
       last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       PRIMARY KEY (cache_name, cache_key(255))
);

-- 기존 테이블이 있으면 삭제 (테스트용)
# DROP TABLE IF EXISTS dashboard_data;

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

DELIMITER //

-- 저장 프로시저: 배치 단위로 데이터 생성
CREATE PROCEDURE generate_data()
BEGIN
    DECLARE i BIGINT DEFAULT 0;
    DECLARE batch_size INT DEFAULT 1000;
    DECLARE total_rows BIGINT DEFAULT 30000000; -- 3천만 건 (필요시 값을 조정)
    
    WHILE i < total_rows DO
        SET @s = CONCAT(
          "INSERT INTO dashboard_data (dimension1, dimension2, dimension3, dimension4, dimension5, dimension6, dimension7, dimension8, dimension9, dimension10, ",
          "measure1, measure2, measure3, measure4, measure5, measure6, measure7) ",
          "SELECT ",
              "ELT(FLOOR(1 + RAND()*10), 'AlphaDimension','BravoDimension','CharlieDimension','DeltaDimension','EchoDimension','FoxtrotDimension','GolfDimension','HotelDimension','IndiaDimension','JulietDimension'), ",
              "ELT(FLOOR(1 + RAND()*10), 'AlphaDimension','BravoDimension','CharlieDimension','DeltaDimension','EchoDimension','FoxtrotDimension','GolfDimension','HotelDimension','IndiaDimension','JulietDimension'), ",
              "ELT(FLOOR(1 + RAND()*10), 'AlphaDimension','BravoDimension','CharlieDimension','DeltaDimension','EchoDimension','FoxtrotDimension','GolfDimension','HotelDimension','IndiaDimension','JulietDimension'), ",
              "ELT(FLOOR(1 + RAND()*10), 'AlphaDimension','BravoDimension','CharlieDimension','DeltaDimension','EchoDimension','FoxtrotDimension','GolfDimension','HotelDimension','IndiaDimension','JulietDimension'), ",
              "ELT(FLOOR(1 + RAND()*10), 'AlphaDimension','BravoDimension','CharlieDimension','DeltaDimension','EchoDimension','FoxtrotDimension','GolfDimension','HotelDimension','IndiaDimension','JulietDimension'), ",
              "ELT(FLOOR(1 + RAND()*10), 'AlphaDimension','BravoDimension','CharlieDimension','DeltaDimension','EchoDimension','FoxtrotDimension','GolfDimension','HotelDimension','IndiaDimension','JulietDimension'), ",
              "ELT(FLOOR(1 + RAND()*10), 'AlphaDimension','BravoDimension','CharlieDimension','DeltaDimension','EchoDimension','FoxtrotDimension','GolfDimension','HotelDimension','IndiaDimension','JulietDimension'), ",
              "ELT(FLOOR(1 + RAND()*10), 'AlphaDimension','BravoDimension','CharlieDimension','DeltaDimension','EchoDimension','FoxtrotDimension','GolfDimension','HotelDimension','IndiaDimension','JulietDimension'), ",
              "ELT(FLOOR(1 + RAND()*10), 'AlphaDimension','BravoDimension','CharlieDimension','DeltaDimension','EchoDimension','FoxtrotDimension','GolfDimension','HotelDimension','IndiaDimension','JulietDimension'), ",
              "ELT(FLOOR(1 + RAND()*10), 'AlphaDimension','BravoDimension','CharlieDimension','DeltaDimension','EchoDimension','FoxtrotDimension','GolfDimension','HotelDimension','IndiaDimension','JulietDimension'), ",
              -- 7개의 value 컬럼: 50000~70000 사이의 랜덤 정수
              "FLOOR(50000 + RAND()*20001), ",
              "FLOOR(50000 + RAND()*20001), ",
              "FLOOR(50000 + RAND()*20001), ",
              "FLOOR(50000 + RAND()*20001), ",
              "FLOOR(50000 + RAND()*20001), ",
              "FLOOR(50000 + RAND()*20001), ",
              "FLOOR(50000 + RAND()*20001) ",
          "FROM (SELECT 1 FROM information_schema.tables LIMIT ", batch_size, ") t"
        );
        PREPARE stmt FROM @s;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        SET i = i + batch_size;
        COMMIT;
    END WHILE;
END //

DELIMITER ;

-- 데이터 생성 프로시저 호출
CALL generate_data();