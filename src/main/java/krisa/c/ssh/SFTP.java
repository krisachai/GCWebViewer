/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package krisa.c.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Krisa.Chaijaroen
 */
public class SFTP {

    JSch jsch = new JSch();
    Session session = null;
    ChannelSftp sftpChannel;
    

    public SFTP(String host, String usr, String pass) throws JSchException {
        session = jsch.getSession(usr  , host, 22);

        session.setConfig("StrictHostKeyChecking", "no");

        session.setPassword(pass);

        session.connect();

        Channel channel = session.openChannel("sftp");
        channel.connect();
        sftpChannel = (ChannelSftp) channel;
    }

    public InputStream getFile(String src) throws SftpException {
        return sftpChannel.get(src);
    }

    public void close() {
        sftpChannel.exit();
        session.disconnect();
    }
}
