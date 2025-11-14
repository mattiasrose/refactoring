package theater;

/**
 * Class representing a performance of a play..
 */
public class Performance {

    private final String playID;
    private final int audience;

    /**
     * Construct a performance for the play with the given id and audience size.
     *
     * @param playID   play identifier
     * @param audience audience size
     */
    public Performance(String playID, int audience) {
        this.playID = playID;
        this.audience = audience;
    }

    /**
     * Return the id of the play being performed.
     *
     * @return play id
     */
    public String getPlayID() {
        return playID;
    }

    /**
     * Return the audience size for this performance.
     *
     * @return the number of audience members
     */
    public int getAudience() {
        return audience;
    }
}

