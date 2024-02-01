FROM gradle:8.5.0-jdk21-alpine as builder
WORKDIR /source
COPY --chown=gradle:gradle build.gradle ./
COPY --chown=gradle:gradle settings.gradle ./
COPY --chown=gradle:gradle src ./src/
RUN gradle --no-daemon --console=plain bootJar

FROM openjdk:21-slim-bookworm
ENV APP_ROOT /rock-paper-scissors
RUN groupadd --gid 1111 --system rock-paper-scissors \
    && useradd --uid 1111 --system --gid rock-paper-scissors rock-paper-scissors \
    && mkdir --parents ${APP_ROOT} \
    && chown --recursive rock-paper-scissors:rock-paper-scissors ${APP_ROOT}
USER rock-paper-scissors
WORKDIR ${APP_ROOT}
COPY --chown=rock-paper-scissors:rock-paper-scissors --from=builder /source/build/libs/rock-paper-scissors.jar ./
EXPOSE 8080
EXPOSE 8081
CMD ["java" , "-server", "-Xmx512m", "-Xms128m", "-XX:+UseParallelGC", "-jar", "rock-paper-scissors.jar"]
