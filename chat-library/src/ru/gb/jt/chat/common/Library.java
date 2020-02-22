package ru.gb.jt.chat.common;

/**
 * Высокоуровневый прортокол чата.
 * взаимодействие клиента и сервера
 *
 */
public class Library {
    /*
    /auth_request±login±password       запрс авторизации
    /auth_accept±nickname        подтверждение авторизации
    /auth_error         ошибка авторизации
    /broadcast±msg          общие сообщения
    /msg_format_error±msg   аналог Exceptions
    /user_list±user1±user2.....
    * */
    public static final String DELIMITER = "±";
    public static final String AUTH_REQUEST = "/auth_request";
    public static final String AUTH_ACCEPT = "/auth_accept";
    public static final String AUTH_DENIED = "/auth_denied";
    // если мы вдруг не поняли, что за сообщение и не смогли разобрать
    public static final String MSG_FORMAT_ERROR = "/msg_format_error";
    // то есть сообщение, которое будет посылаться всем
    public static final String TYPE_BROADCAST = "/bcast";
    public static final String TYPE_BCAST_CLIENT = "/client_msg";
    public static final String USER_LIST = "/user_list";

    public static final String AUTH_NEW_CLIENT_REQUEST = "/auth_new_client_request";

    public static String getTypeBcastClient(String msg) {
        return TYPE_BCAST_CLIENT + DELIMITER + msg;
    }

    public static String getUserList(String users) {
        return USER_LIST + DELIMITER + users;
    }

    public static String getAuthRequest(String login, String password) {
        return AUTH_REQUEST + DELIMITER + login + DELIMITER + password;
    }

    public static String getAuthAccept(String nickname) {
        return AUTH_ACCEPT + DELIMITER + nickname;
    }

    public static String getAuthDenied() {
        return AUTH_DENIED;
    }

    public static String getMsgFormatError(String message) {
        return MSG_FORMAT_ERROR + DELIMITER + message;
    }

    public static String getTypeBroadcast(String src, String message) {
        return TYPE_BROADCAST + DELIMITER + System.currentTimeMillis() +
                DELIMITER + src + DELIMITER + message;
    }

    public static String getAuthNewClientRequest(String login, String password, String nickName) {
        return AUTH_NEW_CLIENT_REQUEST + DELIMITER + login + DELIMITER + password+ DELIMITER + nickName;
    }

}
