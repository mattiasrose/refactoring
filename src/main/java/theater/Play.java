package theater;

/**
 * Class representing a play that can be performed.
 */
public class Play {

    private final String name;
    private final String type;

    /**
     * Construct a play with the given name and type.
     *
     * @param name play title
     * @param type type of play
     */
    public Play(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Return name of play.
     *
     * @return the play name
     */
    public String getName() {
        return name;
    }

    /**
     * Return the type of play.
     *
     * @return the play type
     */
    public String getType() {
        return type;
    }
}

