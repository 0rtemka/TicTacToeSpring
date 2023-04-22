package com.example.tictactoespring;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
    private static String host = "http://localhost:8080/";

    public static void main(String[] args) {
        RestTemplate template = new RestTemplate();
        Scanner scanner = new Scanner(System.in);

        ResponseEntity<HashMap> response = template.getForEntity(host, HashMap.class);

        String cookies = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookies);

        HashMap<String, String> game = response.getBody();
        String gameId = game.get("gameId");
        String type = game.get("type");

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
                        "y", String.valueOf(y)
                );

                HttpEntity entity = new HttpEntity(request, headers);
                ResponseEntity<HashMap> response1 = template.exchange(host, HttpMethod.POST, entity, HashMap.class);

                game = response1.getBody();
            }
        }
    }
}
