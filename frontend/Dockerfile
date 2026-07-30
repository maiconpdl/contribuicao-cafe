# Etapa 2: Servir os arquivos estáticos com Nginx
FROM nginx:alpine

# Copia os arquivos gerados pelo Angular
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

# Configuração para suportar o roteamento do Angular (SPA)
RUN echo 'server { \
    listen 80; \
    location / { \
        root /usr/share/nginx/html; \
        index index.html; \
        try_files $uri $uri/ /index.html; \
    } \
}' > /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]