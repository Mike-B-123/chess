package ui;

public class REPL {
    //private final JoinClient joinClient ;
    private final RegisterClient registerClient ;
    //private final BrowsingClient browsingClient ;
    public REPL(String serverURL){
        //joinClient = new JoinClient(serverURL, this) ;
        registerClient = new RegisterClient(serverURL) ;
        //browsingClient = new BrowsingClient(serverURL, this) ;
    }
    public void run(String serverURL){
        System.out.println("\uD83D\uDC36 Ready to play some Chess? First sign in.");
        System.out.print(registerClient) ;
    }
}
