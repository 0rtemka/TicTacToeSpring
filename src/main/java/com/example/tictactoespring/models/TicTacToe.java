package com.example.tictactoespring.models;

import java.io.Serializable;

public enum TicTacToe implements Serializable {
    X(1), O(2);

    public int value;

    TicTacToe(int value) {
        this.value = value;
    }
}
