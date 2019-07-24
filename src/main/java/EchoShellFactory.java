import org.apache.sshd.common.Factory;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.command.Command;

import java.io.*;

public class EchoShellFactory implements Factory<Command> {

    public Command create() {
        return new EchoShell();
    }

    public static class EchoShell implements Command, Runnable {

        private InputStream in;
        private OutputStream out;
        private OutputStream err;
        private ExitCallback callback;
        private Environment environment;
        private Thread thread;

        public InputStream getIn() {
            return in;
        }

        public OutputStream getOut() {
            return out;
        }

        public OutputStream getErr() {
            return err;
        }

        public Environment getEnvironment() {
            return environment;
        }

        public void setInputStream(InputStream in) {
            this.in = in;
        }

        public void setOutputStream(OutputStream out) {
            this.out = out;
        }

        public void setErrorStream(OutputStream err) {
            this.err = err;
        }

        public void setExitCallback(ExitCallback callback) {
            this.callback = callback;
        }

        public void start(Environment env) throws IOException {
            environment = env;
            thread = new Thread(this, "EchoShell");
            thread.start();
            out.write(("Connection Initialized\n\r").getBytes()); //When connection established write this to clients shell
            out.flush();
        }

        public void destroy() {
            thread.interrupt();
        }

        public void run() {
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            try {
                //Connection can be continous if while is on
                //while(true) {

                String s = r.readLine(); // Read clients shell then generate response for it below
                if (s.equals("")) {
                    out.write(("Command Not Found\n\r").getBytes());
                    out.flush();
                } else {
                    out.write(("Command '" + s + "' Successfully taken!!\n\r").getBytes());
                    out.flush();
                }

                //}

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                callback.onExit(0);
            }
        }
    }
}