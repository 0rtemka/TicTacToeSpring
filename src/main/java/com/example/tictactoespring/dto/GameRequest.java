package com.example.tictactoespring.dto;

import lombok.Data;

@Data
public class GameRequest {
    private String gameId;
    private int x;
    private int y;
    private String type;
}
