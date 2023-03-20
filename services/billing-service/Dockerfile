FROM openjdk:17-jdk-alpine AS build
WORKDIR /workspace/app/user-service
ADD build/libs build/libs/
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/user-service-1.3.jar)

FROM openjdk:17-jdk-alpine
ARG DEPENDENCY=/workspace/app/user-service/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","ru.shaplov.user.AccountServiceApplication"]
EXPOSE 8000