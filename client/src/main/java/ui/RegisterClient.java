package ui;
import client.ServerFacade ;

public class RegisterClient {
    private final ServerFacade server;
    public RegisterClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }
}
