package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    Inventory inv;

    @BeforeEach
    public void setUp(){
     //   inv = new Inventory();
    }

    @Test
    public void test(){
        assertNotNull(inv);
        inv.load(new String[]{"Knife", "Gun", "Pen", "Laser_Pen"});
        assertTrue(inv.getItem("Knife"));
        assertFalse(inv.getItem("Axe"));
    }
}
