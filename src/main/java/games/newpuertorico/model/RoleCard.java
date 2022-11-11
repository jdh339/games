package games.newpuertorico.model;

/**
 * A RoleCard represents one of the Role cards in "Puerto Rico".
 * <p>
 * A game model should keep 6-8 of these, depending on the number of players.
 */
public class RoleCard {
    private final RoleCardType type;
    private boolean isClaimed = false;
    private int doubloons = 0;

    /**
     * Creates a RoleCard with the given type.
     *
     * @param roleCardType A role type (Settler, Mayor, etc.) that is permanently assigned.
     */
    public RoleCard(RoleCardType roleCardType) {
        this.type = roleCardType;
    }

    /**
     * Returns whether this card is of the given type.
     *
     * @param roleCardType A role type (Settler, Mayor, etc.)
     * @return A boolean, true if the role matches.
     */
    public boolean isType(RoleCardType roleCardType) {
        return type.equals(roleCardType);
    }

    /**
     * Claims this role card and resets its doubloons to zero.
     */
    public void claim() {
        isClaimed = true;
        doubloons = 0;
    }

    /**
     * Returns current number of doubloons on the card.
     *
     * @return An int, current number of doubloons.
     */
    public int getDoubloons() {
        return doubloons;
    }

    /**
     * Adds a doubloon to the card if it hasn't been claimed; otherwise, does nothing.
     */
    public void addDoubloonIfUnclaimed() {
        if (!isClaimed) {
            doubloons += 1;
        }
    }

    /**
     * Returns whether the role card is claimed (i.e. not available).
     *
     * @return A boolean, true if a Player has already taken this card this round.
     */
    public boolean isClaimed() {
        return isClaimed;
    }

    /**
     * Resets the role card so it may be claimed again next round.
     */
    public void reset() {
        isClaimed = false;
    }
}
