# Учбовий проект з використанням Spring Webflux Security

## Локальный запуск
- Встановити PostgreSQL
- Встановити актуальні змінні середовища:
DB_URL=jdbc:postgresql://localhost:5432/webflux_security;
  DB_URL_R2DBC=r2dbc:pool:postgres://localhost:5432/webflux_security;
  DB_USER=postgres;
  DB_PASSWORD=postgres;
  SECRET=jwt_secret;