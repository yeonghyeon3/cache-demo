# 베이스 이미지로 공식 Redis Alpine 버전 사용
FROM redis:alpine

# 커스텀 설정 파일이 있다면 컨테이너로 복사
# 예: redis.conf 파일을 커스터마이징해서 사용할 경우
# COPY redis.conf /usr/local/etc/redis/redis.conf

# 기본 명령어를 오버라이드해서 커스텀 설정 파일을 사용하도록 지정
CMD ["redis-server", "/usr/local/etc/redis/redis.conf"]