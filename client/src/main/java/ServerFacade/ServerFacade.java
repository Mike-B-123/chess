package ServerFacade;

import com.google.gson.Gson;
import model.*;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverURL ;

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL ;
    }

    public Object clearCall() throws Exception {
        var path = "/db";
        return this.makeRequest("DELETE", path, null, Object.class);
    }
    public Object registerCall(User user) throws Exception {
        var path = "/user";
        AuthData response = this.makeRequest("POST", path, user, AuthData.class);
        // how should I properly make this into a authdata?
        return response ;
    }
    public Object loginCall(User user) throws Exception {
        var path = "/session";
        return this.makeRequest("POST", path, user, Object.class);
    }
    public Object logoutCall(String authToken) throws Exception {
        var path = "/session";
        return this.makeRequest("DELETE", path, authToken, Object.class);
    }
    public Object listCall(String authToken) throws Exception {
        var path = "/game";
        return this.makeRequest("GET", path, authToken, Object.class);
    }
    public Object createGameCall(CreateGameName gameName) throws Exception {
        var path = "/game";
        return this.makeRequest("POST", path, gameName, Object.class);
    }
    public Object joinGameCall(JoinData joinData) throws Exception {
        var path = "/game";
        return this.makeRequest("PUT", path, joinData, Object.class);
    }
    public <T> T makeRequest(String method, String path, Object requestObject, Class<T> responseClass) throws Exception{
       try {
           URL url = (new URI(serverURL + path)).toURL(); // what does URI do?
           HttpURLConnection http = (HttpURLConnection) url.openConnection(); // allows for creation of http request (kinda a request and response object in one)
           http.setRequestMethod(method); // adds the GET/POST/DELETE part
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
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json"); // letting server know we are sending Json data in reqBody
            String reqInfo = new Gson().toJson(request) ; // serializes
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

}
