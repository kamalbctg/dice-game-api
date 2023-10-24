## Pre Requisites
* Open JDK 11 
* Maven 3.3.9

## Configurations
Update resources/application.properties
### available option
* app.board.score.start=6
* app.board.score.penalty=4
* app.board.score.winning=25
* roll.dice.api=http://developer-test.hishab.io/api/v1/roll-dice

## build & run

### maven build
```
  mvn spring-boot:run
```

### docker build
```
  docker build -t hishab/board-game-app .
  docker run -it -p 8080:8000 hishab/board-game-app
```