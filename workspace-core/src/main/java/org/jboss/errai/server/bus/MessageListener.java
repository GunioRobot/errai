package org.jboss.errai.server.bus;

import org.jboss.errai.client.rpc.CommandMessage;

public interface MessageListener {
    public boolean handleMessage(CommandMessage message);
}