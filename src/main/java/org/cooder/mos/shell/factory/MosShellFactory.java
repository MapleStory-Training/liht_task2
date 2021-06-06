package org.cooder.mos.shell.factory;

import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.InteractiveProcessShellFactory;
import org.cooder.mos.shell.Shell;

/**
 * <mos的shell工厂>
 *
 * @author lihaitao on 2021/6/5
 */
public class MosShellFactory extends InteractiveProcessShellFactory {

    @Override
    public Command createShell(ChannelSession channel) {
        return new Shell();
    }
}
