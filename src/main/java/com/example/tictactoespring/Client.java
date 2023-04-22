package com.example.tictactoespring;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
    private static String host = "http://localhost:8080/";

    public static void main(String[] args) {
        RestTemplate template = new RestTemplate();
        Scanner scanner = new Scanner(System.in);

        Map<String, String> game = template.getForObject(host, HashMap.class);
        String type = game.get("type");
        String gameId = game.get("gameId");

        while (true) {
            System.out.println(game.get("board"));

            if (game.get("winner") != null) {
                System.out.println("Winner is - " + game.get("winner"));
                break;
            }

            System.out.println("Your type is  " + type);
            System.out.println("Current turn type " + game.get("currentTurnType"));

            System.out.println();
            System.out.println();

            System.out.println("1. Update");
            System.out.println("2. Turn");
            System.out.print(">>> ");

            int menu = scanner.nextInt();

            if (menu == 1) {
                game = template.getForObject(host + "?gameId=" + gameId, HashMap.class);
            } else if (menu == 2) {
                System.out.print("x - ");
                int x = scanner.nextInt();
                System.out.print("y - ");
                int y = scanner.nextInt();

                Map<String, String> request = Map.of(
                        "gameId", gameId,
                        "x", String.valueOf(x),
                        "y", String.valueOf(y),
                        "type", type
                );

                game = template.postForObject(host, request, HashMap.class);
            }
        }
    }
}
