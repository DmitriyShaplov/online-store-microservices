FROM openjdk:17-jdk-alpine AS build
WORKDIR /workspace/app/kuber_users_crud
ADD build/libs/ build/libs/
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/kuber_users_crud-1.2.jar)

FROM openjdk:17-jdk-alpine
ARG DEPENDENCY=/workspace/app/kuber_users_crud/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","ru.shaplov.profile.KuberUsersCrudApplication"]
EXPOSE 8000