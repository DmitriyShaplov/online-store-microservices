FROM openjdk:17-jdk-alpine AS build
WORKDIR /workspace/app/userauth
ADD build/libs build/libs/
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/auth-service-1.7.jar)

FROM openjdk:17-jdk-alpine
ARG DEPENDENCY=/workspace/app/userauth/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","ru.shaplov.authservice.AuthServiceApplication"]
EXPOSE 8001