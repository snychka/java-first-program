FROM maven:3.6.3-jdk-11-openj9

WORKDIR /src/app/

COPY ./pom.xml .

RUN ["mkdir", "/home/projects"]

RUN groupadd projects && useradd -g projects projects && \
    chown -R projects:projects /src/app && \
    chown -R projects:projects /home/projects

USER projects

COPY --chown=projects:projects . .

RUN ["mvn", "clean"]

RUN ["mvn", "de.qaware.maven:go-offline-maven-plugin:resolve-dependencies", "test", "-Dmaven.test.failure.ignore"]

ENTRYPOINT ["sh"]
