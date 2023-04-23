# TicTacToeSpring
TicTacToe game in Java with Spring Boot

http://localhost:8080 [GET] - connect game

http://localhost:8080?gameId={gameID} [GET] - check game status by id

http://localhost:8080 [POST] - make turn with body:
{
  gameId: {gameId}
  x: {coordinate X}
  y: {coordinate Y}
}
