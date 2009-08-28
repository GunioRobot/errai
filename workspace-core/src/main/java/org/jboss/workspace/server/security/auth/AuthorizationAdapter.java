package org.jboss.workspace.server.security.auth;

import org.jboss.workspace.client.rpc.CommandMessage;

public interface AuthorizationAdapter {
    public void challenge(CommandMessage message);
    public boolean isAuthenticated(CommandMessage message);
}