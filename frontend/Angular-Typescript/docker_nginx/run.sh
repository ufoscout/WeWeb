#!/bin/sh

envsubst '\$BACKEND_URL \$NGINX_WORKER_PROCESSES \$NGINX_WORKER_CONNECTIONS \$NGINX_LOGGER_LEVEL' <${ASSETS}/nginx.conf.template > /etc/nginx/nginx.conf

echo "Nginx configuration:    "
echo "------------------------"
cat /etc/nginx/nginx.conf
echo "------------------------"

exec nginx -c /etc/nginx/nginx.conf
