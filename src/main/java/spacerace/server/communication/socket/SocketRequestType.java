package spacerace.server.communication.socket;

public enum SocketRequestType {
    REGISTER_PLAYER,
    GET_GAME_STATE,
    GET_GAME_STATE_FOR_VIEWING,
    POST_ACTION,
    SEND_START_COMMAND,
    GET_GAME_RESULT,
    TEST
}
