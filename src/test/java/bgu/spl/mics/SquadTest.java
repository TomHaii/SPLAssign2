package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {
    Squad squad;

    @BeforeEach
    public void setUp(){
        squad = new Squad();
    }

    @Test
    public void test(){
        assertNotNull(squad);
        Agent bond = new Agent();
        bond.setName("bond");
        bond.setSerialNumber("007");
        Agent alec = new Agent();
        alec.setName("alec");
        alec.setSerialNumber("006");
        squad.load(new Agent[]{bond, alec});
        List<String> tmp = new LinkedList<>();
        tmp.add("006");
        tmp.add("007");
        assertTrue(squad.getAgents(tmp));
        assertEquals(squad.getAgentsNames(tmp), Arrays.asList("bond", "alec"));
        tmp.add("000");
        assertFalse(squad.getAgents(tmp));

    }
}
