package com.project.mazegame.Testing;

import com.project.mazegame.objects.Player;

import org.junit.Test;


public class TestMultiPlayer {

    Player player1 = new Player();
    Player player2 = new Player();

    @Test
    public void initialHealth() {

    }

    @Test
    public void testAttack() {
        player1.attack();
    }
}
