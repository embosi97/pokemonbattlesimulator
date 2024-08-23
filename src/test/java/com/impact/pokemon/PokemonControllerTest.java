package com.impact.pokemon;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.util.Map;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PokemonControllerTest {

    private final TestRestTemplate rest;

    PokemonControllerTest(@LocalServerPort int port) {
        rest = new TestRestTemplate(new RestTemplateBuilder().rootUri(format("http://localhost:%d", port)));
    }

    @Test
    void testAttackPicksWinnerWithHitPoints() {
        Map<String, Object> response = rest.getForObject("/attack", Map.class);
        assertEquals(2, response.size());
        assertEquals("Bulbasaur", response.get("winner"));
        assertEquals(120, response.get("hitPoints"));
    }
}
