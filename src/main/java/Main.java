import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

        SshServer sshd = SshServer.setUpDefaultServer();
        int portNumber;
        try {
            portNumber = Integer.parseInt(args[0]);
            sshd.setPort(IsAvailable(portNumber));
        }catch (NumberFormatException e){
            e.printStackTrace();
            throw e;
        }catch (Exception e) {
            e.printStackTrace();
        }


        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("hostkey.ser")));

        sshd.setShellFactory(new EchoShellFactory());

        sshd.setHost("localhost");

        PasswordAuthenticator psswd = (username, password, session) -> username!=null && username.length()>0 && username.equals(password); //Password authenticator for SSH connection
        sshd.setPasswordAuthenticator(psswd);
        try {
            sshd.start();

            System.out.println(sshd.isStarted()?"SSHD is Started":"Cant start the service");
            System.out.println("Host : " + sshd.getHost());
            System.out.println("Port : " + sshd.getPort());

            Thread.sleep(10000000);// Sleep for main thread
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static int IsAvailable(int port) throws IOException {
        System.out.println("If the given port is in use, the port checker will automatically use an available port");
        System.out.println("--------------Testing port " + port);
        Socket s = null;
        try {
            s = new Socket("localhost", port);

            // If the code makes it this far without an exception it means
            // something is using the port and has responded.
            System.out.println("--------------Port " + port + " is not available");
            s.close();
            ServerSocket tempSocket = new ServerSocket(0);
            System.out.println("--------------Port " + tempSocket.getLocalPort() + " is automatically assigned");
            return tempSocket.getLocalPort();
        } catch (IOException e) {
            System.out.println("--------------Port " + port + " is available");
            return port;
        } finally {
            if( s != null && !s.isClosed()){
                    s.close();
            }
        }
    }
}
