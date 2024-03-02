FROM amazoncorretto:17-alpine3.14

# Cria grupo de sistema e usuario para Isolamento e Padroes de Segurança
# Add um novo grupo de sistema chamado appuser com GID: 1001
# Add um novo usuario de sistema e adiciona ao grupo appuser com UID: 1001
RUN addgroup -g 1001 -S appuser \
  && adduser -u 1001 -S appuser -G appuser

# Configura fuso horário do container para São Paulo
ENV TZ America/Sao_Paulo

# Configurando e Instalando dependencias para o container
RUN apk --no-cache add tzdata curl msttcorefonts-installer fontconfig \
    && update-ms-fonts \
    && echo $TZ > /etc/timezone \
    && cp /usr/share/zoneinfo/Etc/GMT+3 /etc/localtime \
    && date

USER appuser:appuser

HEALTHCHECK \
    --interval=30s \
    --timeout=30s \
    --start-period=15s \
    --retries=120 \
    CMD nc -z 127.0.0.1 8080 || \
        exit 1

EXPOSE 8080
ADD target/taskify-1.0.0.jar app.jar

ENV JAVA_TOOL_OPTIONS \
    -XX:+UseG1GC -XX:MaxRAMPercentage=85.0 -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -Duser.language=pt -Duser.country=BR

ENTRYPOINT ["java", "-Djava.security.egd=storedFile:/dev/./urandom", "-jar", "/app.jar"]