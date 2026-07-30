# --- ETAPA 1: Build do Angular com Node.js ---
FROM node:22-alpine AS build
WORKDIR /app

# Copia as dependências e instala
COPY package*.json ./
RUN npm install

# Copia todo o código fonte e gera os arquivos da build de produção
COPY . .
RUN npm run build -- --configuration production

# --- ETAPA 2: Servir os arquivos estáticos com Nginx ---
FROM nginx:alpine

# Copia os arquivos gerados no estágio 'build' acima
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