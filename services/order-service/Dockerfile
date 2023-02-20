FROM openjdk:17-jdk-alpine AS build
WORKDIR /workspace/app/order-service
ADD build/libs build/libs/
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/order-service-1.6.jar)

FROM openjdk:17-jdk-alpine
ARG DEPENDENCY=/workspace/app/order-service/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","ru.shaplov.orderservice.OrderServiceApplication"]
EXPOSE 8002