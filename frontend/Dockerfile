# Build do Angular com Node.js
FROM node:22-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
# Gera os arquivos na pasta dist/
RUN npm run build -- --configuration production

# Etapa 2: Servir os arquivos
FROM nginx:alpine

# Copia os arquivos gerados pelo Angular para a pasta do Nginx
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]