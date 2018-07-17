FROM nginx:1-alpine

# Environment variables required for this build
# ---------------------------------------------
ENV ASSETS=/docker

ENV NGINX_WORKER_PROCESSES=auto
ENV NGINX_WORKER_CONNECTIONS=16384
ENV NGINX_LOGGER_LEVEL=info

# Update and upgrade
# ------------------------------------
RUN apk update -f \
  && apk upgrade -f \
  && apk --no-cache add ca-certificates \
  && update-ca-certificates \
  && rm -rf /var/cache/apk/* \
  && rm -rf /var/log/nginx/* \
  && rm -rf /usr/share/nginx/html/* \
  && rm -f /etc/nginx/nginx.conf


# Deploying frontend
# ------------------
ADD ./dist/Angular-Typescript /usr/share/nginx/html/
ADD ./docker_nginx/mime.types /etc/nginx/mime.types
ADD ./docker_nginx $ASSETS


# Expose nginx port
# -----------------
EXPOSE 80


# By default, execute run.sh script
# ---------------------------------
CMD ["/docker/run.sh"]
