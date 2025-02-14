public class BracketException extends Exception {

    private final int position;

    public BracketException(String message, int position) {
        super(message);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
