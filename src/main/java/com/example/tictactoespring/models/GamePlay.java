package com.example.tictactoespring.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamePlay implements Serializable {
    private int coordinateX;
    private int coordinateY;
    private TicTacToe type;
}
