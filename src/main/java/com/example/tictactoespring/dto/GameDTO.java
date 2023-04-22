package com.example.tictactoespring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GameDTO {
    private String gameId;
    private String board;
    private String winner;
    private String type;
    private String currentTurnType;
}
