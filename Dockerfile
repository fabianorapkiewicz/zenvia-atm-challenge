FROM openjdk:11

LABEL maintainer="Fabiano Nogueira Rapkiewicz"

WORKDIR /
ADD build/libs/zenvia-atm-1.0.0.jar zenvia-atm.jar

CMD ["java", "-jar", "zenvia-atm.jar"]
