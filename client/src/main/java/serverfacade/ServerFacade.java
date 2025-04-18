package serverfacade;

import chess.ChessBoard;
import com.google.gson.Gson;
import model.*;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ServerFacade {
    private final String serverURL ;
    private static ServerFacade instance ;
    private String username ;
    private String authToken ;
    private HashMap<Integer, Integer> gameNumList = new HashMap<>();; // gameNum, gameID
    private HashMap<Integer, Game> gameList = new HashMap<>();;

    private ChessBoard currentChessBoard = new ChessBoard() ;

    public ServerFacade(int port) {
        this.serverURL ="http://localhost:" + port ;
        this.currentChessBoard.resetBoard();
    }
    public String getUsername() {
        return username;
    }
    public String getAuthToken() {
        return authToken;
    }
    public HashMap<Integer, Integer> getGameNumList() {
        return gameNumList;
    }
    public ChessBoard getCurrentChessBoard() {
        return currentChessBoard ;
    }

    public Object clearCall() throws Exception {
        var path = "/db";
        return this.makeRequest("DELETE", path, null, Object.class);
    }
    public AuthData registerCall(User user) throws Exception {
        var path = "/user";
        AuthData response = this.makeRequest("POST", path, user, AuthData.class);
        authToken = response.authToken();
        username = response.username() ;
        return response ;
    }
    public AuthData loginCall(User user) throws Exception {
        var path = "/session";
        AuthData response = this.makeRequest("POST", path, user, AuthData.class); // will changing the response class to AuthData mess things up?
        authToken = response.authToken();
        username = response.username() ;
        return response ;
    }
    public Object logoutCall(String reqAuthToken) throws Exception {
        var path = "/session";
        authToken = reqAuthToken ;
        return this.makeRequest("DELETE", path, reqAuthToken, Object.class);
    }
    public GamesList listCall(String authToken) throws Exception {
        var path = "/game";
        GamesList response = this.makeRequest("GET", path, null, GamesList.class);
        int counter = 1 ;
        for(Game game: response.games()){
            gameNumList.put(counter, game.gameID()) ;
            gameList.put(game.gameID(), game) ;
            counter ++ ;
        }
        return response ;
    }
    public GameID createGameCall(CreateGameName gameName) throws Exception {
        var path = "/game";
        return this.makeRequest("POST", path, gameName, GameID.class);
    }
    public Object joinGameCall(JoinData joinData) throws Exception {
        var path = "/game";
        currentChessBoard = gameList.get(joinData.gameID()).game().getBoard(); ;
        return this.makeRequest("PUT", path, joinData, Object.class);
    }


    private <T> T makeRequest(String method, String path, Object requestObject, Class<T> responseClass) throws Exception{
       try {
           URL url = (new URI(serverURL + path)).toURL(); // what does URI do?
           HttpURLConnection http = (HttpURLConnection) url.openConnection();
           http.setRequestMethod(method);// adds the GET/POST/DELETE part
           http.setDoOutput(true); // ask what this does, but it's important for request bodies!

           writeBody(requestObject, http);
           http.connect(); // this is where the request gets sent to the server. How does it know where the server is?
           throwIfNotSuccessful(http); // checking for 200 status code
           return readBody(http, responseClass); // deserilize response body
       }
       catch(Exception ex){
           throw new Exception(ex.getMessage()) ;
    }
}
    private void writeBody(Object request, HttpURLConnection http) throws IOException {
        if(authToken != null){
            http.addRequestProperty("Authorization", authToken);
        }
        http.addRequestProperty("Content-Type", "application/json") ;
        if (request != null) {
            // letting server know we are sending Json data in reqBody
            String reqInfo = new Gson().toJson(request) ;
            // serializes
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqInfo.getBytes()); // puts Json data into req body
            }
        }
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception {
        if (http.getResponseCode() != HttpURLConnection.HTTP_OK) {
            try (InputStream respErr = http.getErrorStream()) {
                InputStreamReader reader = new InputStreamReader(respErr);
                if (reader != null) {
                    Message errorResponse = new Gson().fromJson(reader, Message.class);
                    throw new Exception(errorResponse.message()) ;
                }
            }

            throw new Exception("other failure"); // is this ok?
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass); // turns into GSON from Json
                }
            }
        }
        return response;
    }
    public static ServerFacade getInstance(int port){
        if(instance == null){
            return instance = new ServerFacade(port) ;
        }
        return instance ;
    }
}
