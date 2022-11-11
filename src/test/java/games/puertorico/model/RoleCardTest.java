package games.puertorico.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleCardTest {

    @Test
    void testClaimingRemovesDoubloons() {
        RoleCard roleCard = new RoleCard(RoleCardType.SETTLER);
        roleCard.addDoubloonIfUnclaimed();
        roleCard.claim();
        assertEquals(0, roleCard.getDoubloons());
    }

    @Test
    void testAddDoubloonsIfUnclaimedWhenClaimed() {
        RoleCard roleCard = new RoleCard(RoleCardType.SETTLER);
        roleCard.claim();
        roleCard.addDoubloonIfUnclaimed();
        assertEquals(0, roleCard.getDoubloons());
    }

    @Test
    void testAddDoubloonsIfUnclaimedWhenUnclaimed() {
        RoleCard roleCard = new RoleCard(RoleCardType.SETTLER);
        roleCard.addDoubloonIfUnclaimed();
        assertEquals(1, roleCard.getDoubloons());
    }

    @Test
    void testClaimAndReset() {
        RoleCard roleCard = new RoleCard(RoleCardType.SETTLER);
        assertFalse(roleCard.isClaimed());
        roleCard.claim();
        assertTrue(roleCard.isClaimed());
        roleCard.reset();
        assertFalse(roleCard.isClaimed());
    }
}
