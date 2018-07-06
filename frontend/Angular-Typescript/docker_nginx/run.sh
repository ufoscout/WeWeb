#!/usr/bin/dumb-init /bin/sh

envsubst '\$BACKEND_URL' <${ASSETS}/nginx.conf.template > /etc/nginx/nginx.conf

cat /etc/nginx/nginx.conf
exec nginx -c /etc/nginx/nginx.conf
