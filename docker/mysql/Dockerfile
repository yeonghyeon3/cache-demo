# Dockerfile
FROM mysql:latest

# 환경변수 설정 (필요에 따라 수정)
ENV MYSQL_ROOT_PASSWORD=rootpassword
ENV MYSQL_DATABASE=dashboard
ENV MYSQL_USER=user
ENV MYSQL_PASSWORD=password

# 초기화 스크립트 복사 (이미지 빌드 시 포함됨)
COPY init.sql /docker-entrypoint-initdb.d/