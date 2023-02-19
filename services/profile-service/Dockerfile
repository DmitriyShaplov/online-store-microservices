FROM openjdk:17-jdk-alpine AS build
WORKDIR /workspace/app/profile-service
ADD build/libs build/libs/
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/profile-service-1.3.jar)

FROM openjdk:17-jdk-alpine
ARG DEPENDENCY=/workspace/app/profile-service/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","ru.shaplov.profile.ProfileServiceApplication"]
EXPOSE 8000