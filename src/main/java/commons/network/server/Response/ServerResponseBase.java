package commons.network.server.Response;

public abstract class ServerResponseBase {

    private String      message;
    private String      redPlayerName;
    private String      yellowPlayerName;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getRedPlayerName() {
        return redPlayerName;
    }

    public void setRedPlayerName(final String redPlayerName) {
        this.redPlayerName = redPlayerName;
    }

    public String getYellowPlayerName() {
        return yellowPlayerName;
    }

    public void setYellowPlayerName(final String yellowPlayerName) {
        this.yellowPlayerName = yellowPlayerName;
    }
}
