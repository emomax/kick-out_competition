package fourinarowbot.server.response;

public class PlaceMarkerResponse {

    private String message;

    public static PlaceMarkerResponse responseWithMessage(final String message) {
        final PlaceMarkerResponse serverResponse = new PlaceMarkerResponse();
        serverResponse.setMessage(message);
        return serverResponse;
    }

    public static PlaceMarkerResponse emptyResponse() {
        return new PlaceMarkerResponse();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
