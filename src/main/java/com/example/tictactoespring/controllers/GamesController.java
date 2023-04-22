package com.example.tictactoespring.controllers;

import com.example.tictactoespring.dto.GameDTO;
import com.example.tictactoespring.dto.GameRequest;
import com.example.tictactoespring.models.Game;
import com.example.tictactoespring.models.GamePlay;
import com.example.tictactoespring.services.GameService;
import com.example.tictactoespring.utils.ErrorResponse;
import com.example.tictactoespring.utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class GamesController {
    private final GameService gameService;
    private static int connections = 0;

    @GetMapping("/")
    public GameDTO getGame(@RequestParam(name = "gameId", required = false) String gameId) {
        if (gameId == null) {
            String type = connections % 2 == 0 ? "X" : "O";
            connections++;
            return gameService.convertToGameDTO(gameService.connectGame(), type);
        }

        Game game = gameService.findGameById(gameId);
        return gameService.convertToGameDTO(game);
    }

    @PostMapping("/")
    public GameDTO gameplay(@RequestBody GameRequest gameRequest) {
        Game game = gameService.findGameById(gameRequest.getGameId());
        if (game.getWinner() == null) {
            GamePlay gamePlay = gameService.convertToGamePlay(gameRequest);
            gameService.gamePlay(gamePlay, game);
        }
        return gameService.convertToGameDTO(game);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(NotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }
}
