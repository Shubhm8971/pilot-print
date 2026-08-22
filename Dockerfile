FROM node:22-alpine AS build

WORKDIR /app/print-pilot
COPY print-pilot/package*.json ./
RUN npm ci
COPY print-pilot/ ./
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/print-pilot/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
