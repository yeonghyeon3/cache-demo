# docker/nginx/Dockerfile

# 1) 경량 Alpine 기반 Nginx 이미지 사용
FROM nginx:alpine

# 2) 불필요한 기본 설정 파일 제거 (선택)
RUN rm /etc/nginx/conf.d/default.conf

# 3) 호스트의 nginx.conf를 이미지에 복사
#    프로젝트 구조가 docker/nginx/nginx.conf 라면,
#    빌드 컨텍스트는 프로젝트 루트에서 `docker/nginx` 폴더로 지정하세요.
COPY nginx.conf /etc/nginx/nginx.conf

# 4) Healthcheck (선택)
HEALTHCHECK --interval=10s --timeout=3s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1

# 5) 컨테이너 포트 노출
EXPOSE 80

# 6) 포그라운드 실행
CMD ["nginx", "-g", "daemon off;"]
