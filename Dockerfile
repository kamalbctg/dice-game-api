FROM eclipse-temurin:11.0.20_8-jdk-alpine

RUN uname -r
RUN java -version

MAINTAINER kamalctg@gmail.com

## Create non root user
ARG USER=app
RUN apk add --update sudo
RUN adduser -D $USER \
        && echo "$USER ALL=(ALL) NOPASSWD: ALL" > /etc/sudoers.d/$USER \
        && chmod 0440 /etc/sudoers.d/$USER


RUN mkdir -p /usr/local/app
WORKDIR /usr/local/apps/moderator

## Copy resources
COPY etc/applaunch.sh /usr/local/app/applaunch.sh
COPY target/*.jar  /usr/local/app/
RUN ["chmod", "+x", "/usr/local/app/applaunch.sh"]

# allowing app user /usr/local dir permission
RUN chown -R app:app /usr/local
# switching user from root to app
USER app

CMD sudo rm /etc/sudoers.d/app && \
    /usr/local/app/applaunch.sh