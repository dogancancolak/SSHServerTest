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
            out.write((">").getBytes());
            out.flush();
        }

        public void destroy() {
            thread.interrupt();
        }

        public void run() {
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            try {
                    String s = r.readLine();
                    System.out.println(s);
                    if (s.equals("")) {
                        out.write(("Command Not Found>").getBytes());
                        out.flush();
                    }
                    else{
                        out.write(("Reply=create{MainPhone=97143717341;Done=1;Profile=FIX_VMB_ELIFE;Reference=1;Group=1;Agency=MDS;}>").getBytes());
                        out.flush();
                    }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                callback.onExit(0);
            }
        }
    }
}