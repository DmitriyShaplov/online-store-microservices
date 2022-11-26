FROM openjdk:17-jdk-alpine AS build
WORKDIR /workspace/app
RUN apk update && apk add git
RUN git clone https://github.com/DmitriyShaplov/kuber_users_crud.git
WORKDIR kuber_users_crud
RUN git checkout master
RUN chmod +x gradlew && ./gradlew clean build
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/kuber_users_crud.jar)

FROM openjdk:17-jdk-alpine
ARG DEPENDENCY=/workspace/app/kuber_users_crud/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","ru.shaplov.userscrud.KuberUsersCrudApplication"]
EXPOSE 8000