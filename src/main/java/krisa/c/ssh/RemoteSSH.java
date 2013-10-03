/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package krisa.c.ssh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import org.apache.sshd.ClientChannel;
import org.apache.sshd.ClientSession;
import org.apache.sshd.SshClient;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 *@author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 * Modify by Krisa.Chaijaroen
 */
public class RemoteSSH {

    /**
     * @param args the command line arguments
     */
    SshClient client;
    ClientSession session;
    ClientChannel channel;

    public RemoteSSH(String host, String user, String password) throws InterruptedException, IOException {
        client = SshClient.setUpDefaultClient();
        client.start();
        session = client.connect(host, 22).await().getSession();
        session.authPassword(user, password).await().isSuccess();
        channel = session.createChannel(ClientChannel.CHANNEL_SHELL);
    }

    public String sendCMD(String cmd, long timeout) throws IOException {
        ByteArrayOutputStream sent = new ByteArrayOutputStream();
        PipedOutputStream pipedIn = new TeePipedOutputStream(sent);
        channel.setIn(new PipedInputStream(pipedIn));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        channel.setOut(out);
        channel.setErr(err);
        channel.open();
        pipedIn.write(cmd.getBytes());
        pipedIn.flush();
        channel.waitFor(ClientChannel.STDOUT_DATA, timeout);
        //System.out.println(out.toString());
        return out.toString();
    }

    public void disconnect() {
        channel.close(true);
        client.stop();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
     
    }
}

class TeePipedOutputStream extends PipedOutputStream {

    private OutputStream tee;

    public TeePipedOutputStream(OutputStream tee) {
        this.tee = tee;
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        tee.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        tee.write(b, off, len);
    }
}