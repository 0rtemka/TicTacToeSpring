package com.example.tictactoespring.controllers;

import com.example.tictactoespring.dto.GameDTO;
import com.example.tictactoespring.dto.GameRequest;
import com.example.tictactoespring.models.Game;
import com.example.tictactoespring.models.GamePlay;
import com.example.tictactoespring.models.TicTacToe;
import com.example.tictactoespring.services.GameService;
import com.example.tictactoespring.utils.ErrorResponse;
import com.example.tictactoespring.utils.NotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GamesController {
    private final GameService gameService;
    private static int connections = 0;
    private static Map<String, TicTacToe> usersType = new HashMap<>();

    @GetMapping("/")
    public GameDTO getGame(@RequestParam(name = "gameId", required = false) String gameId,
                           HttpServletResponse response) {
        if (gameId == null) {
            TicTacToe type = connections % 2 == 0 ? TicTacToe.X : TicTacToe.O;
            connections++;
            Cookie cookie = new Cookie("userId", UUID.randomUUID().toString());
            response.addCookie(cookie);
            usersType.put(cookie.getValue(), type);

            return gameService.convertToGameDTO(gameService.connectGame());
        }

        Game game = gameService.findGameById(gameId);
        return gameService.convertToGameDTO(game);
    }

    @PostMapping("/")
    public GameDTO gameplay(@RequestBody GameRequest gameRequest,
                            @CookieValue(name = "userId", required = false) String userId) {
        Game game = gameService.findGameById(gameRequest.getGameId());
        if (game.getWinner() == null) {
            TicTacToe type = usersType.get(userId);
            GamePlay gamePlay = gameService.convertToGamePlay(gameRequest, type);
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
