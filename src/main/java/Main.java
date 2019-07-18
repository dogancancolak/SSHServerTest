import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

        SshServer sshd = SshServer.setUpDefaultServer();

        sshd.setPort(9393);

        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("hostkey.ser")));

        sshd.setShellFactory(new EchoShellFactory());

        sshd.setHost("127.0.0.1");

        PasswordAuthenticator psswd = (username, password, session) -> true;
        sshd.setPasswordAuthenticator(psswd);
        try {
            sshd.start();

            System.out.println(sshd.isStarted());
            System.out.println(sshd.getHost());
            System.out.println(sshd.getPort());

            Thread.sleep(10000000);
        }catch (Exception e){
            e.printStackTrace();
        }




    }
}
