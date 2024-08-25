FROM gradle:8-jdk22 AS dependencies

WORKDIR /workspace
COPY build.gradle gradle.properties* settings.gradle /workspace/
RUN gradle dependencies


FROM gradle:8-jdk22 AS build

COPY --from=dependencies /root/.gradle /root/.gradle
WORKDIR /workspace
COPY . /workspace
COPY --from=dependencies /workspace/.gradle /workspace/.gradle

RUN gradle build bootJar
RUN gradle properties -q | grep -E 'version|name' | sed 's/: /=/' > properties


FROM amazoncorretto:22

RUN yum upgrade -y && \
    yum install -y jq shadow-utils && \
    groupadd app && \
    useradd -m app -g app && \
    yum remove shadow-utils -y && \
    yum clean all

WORKDIR /home/app

# Load Maven vars into env vars
COPY --chown=app:app --from=build /workspace/properties ./
COPY --chown=app:app --from=build /workspace/build/libs/*[^plain].jar ./
COPY --chown=app:app src/docker/run.sh ./run.sh

RUN chmod 500 ./run.sh

USER app:app
EXPOSE 8080

CMD ["/home/app/run.sh"]

HEALTHCHECK CMD "[[ $(curl --silent --connect-timeout 5 --max-time 30 http://localhost:8080/actuator/health | jq -r '.status') == 'UP' ]]"
