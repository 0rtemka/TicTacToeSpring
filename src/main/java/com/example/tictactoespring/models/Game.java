package com.example.tictactoespring.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Game implements Serializable {
    private String gameId;
    private int[][] board;
    private GameStatus gameStatus;
    private String winner;
    private TicTacToe currentTurnType;
}
