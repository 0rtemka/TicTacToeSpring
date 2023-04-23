package com.example.tictactoespring.services;

import com.example.tictactoespring.dto.GameDTO;
import com.example.tictactoespring.dto.GameRequest;
import com.example.tictactoespring.models.*;
import com.example.tictactoespring.utils.NotFoundException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class GameService {
    private final GamesStorage gamesStorage;

    public Game findGameById(String id) {
        Game game = gamesStorage.getGames().get(id);
        if (game == null) throw new NotFoundException("Игра с указанным id не найдена");
        return game;
    }
    public Game createGame() {
        Game game = new Game();
        Random rnd = new Random();

        game.setGameId(UUID.randomUUID().toString());
        game.setBoard(new int[3][3]);
        game.setGameStatus(GameStatus.WAITING);
        game.setCurrentTurnType(
                rnd.nextInt(0,1) == 0 ?
                        TicTacToe.X :
                        TicTacToe.O
        );

        gamesStorage.setGame(game);

        return game;
    }

    public Game connectGame() {
        Optional<Game> optGame = gamesStorage.getGames().
                values().
                stream().
                filter(game -> game.getGameStatus().equals(GameStatus.WAITING)).
                findFirst();

        Game game;

        if (optGame.isPresent()) {
            game = optGame.get();
            game.setGameStatus(GameStatus.ONGOING);
        } else {
            game = createGame();
        }

        return game;
    }

    public String drawBoard(Game game) {
        int[][] board = game.getBoard();
        StringBuilder boardString = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                String value = board[i][j] == 1 ?
                        "X" : board[i][j] == 2 ?
                        "O" : " ";

                if (j+1 != board[i].length) boardString.append(value).append(" | ");
                else boardString.append(value);

            }
            boardString.append("\n");
            if (i+1 != board.length) boardString.append("_________\n");
        }

        return boardString.toString();
    }

    public Game gamePlay(GamePlay gamePlay, Game game) {
        if (!gamePlay.getType().equals(game.getCurrentTurnType())) return game;
        if (game.getBoard()[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] != 0) return game;
        if (game.getWinner() != null) return game;

        game.getBoard()[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] = gamePlay.getType().value;
        game.setCurrentTurnType(
                game.getCurrentTurnType().equals(TicTacToe.O) ?
                        TicTacToe.X :
                        TicTacToe.O
        );

        if (checkWinner(game.getBoard(), TicTacToe.X)) {
            game.setWinner("X");
            game.setGameStatus(GameStatus.FINISHED);
        } else if (checkWinner(game.getBoard(), TicTacToe.O)) {
            game.setWinner("O");
            game.setGameStatus(GameStatus.FINISHED);
        }

        gamesStorage.setGame(game);

        return game;
    }

    public boolean checkWinner(int[][] board, TicTacToe type) {
        int[] boardArray = new int[9];
        int indx = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardArray[indx++] = board[i][j];
            }
        }

        int[][] winCombinations = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};

        for (int i = 0; i < winCombinations.length; i++) {
            int counter = 0;
            for (int j = 0; j < winCombinations[i].length; j++) {
                if (boardArray[winCombinations[i][j]] == type.value) {
                    counter++;

                    if (counter == 3) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public GameDTO convertToGameDTO(Game game, String type) {
        GameDTO gameDTO = new GameDTO();

        gameDTO.setGameId(game.getGameId());
        gameDTO.setBoard(drawBoard(game));
        gameDTO.setWinner(game.getWinner());
        gameDTO.setType(type);
        gameDTO.setCurrentTurnType(game.getCurrentTurnType().toString());

        return gameDTO;
    }

    public GameDTO convertToGameDTO(Game game) {
        GameDTO gameDTO = new GameDTO();

        gameDTO.setGameId(game.getGameId());
        gameDTO.setBoard(drawBoard(game));
        gameDTO.setWinner(game.getWinner());
        gameDTO.setCurrentTurnType(game.getCurrentTurnType().toString());

        return gameDTO;
    }

    public GamePlay convertToGamePlay(GameRequest gameRequest, TicTacToe type) {
        GamePlay gamePlay = new GamePlay(
                gameRequest.getX(),
                gameRequest.getY(),
                type
        );

        return gamePlay;
    }


}
