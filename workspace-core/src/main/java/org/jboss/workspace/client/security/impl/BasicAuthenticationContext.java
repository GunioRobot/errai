package org.jboss.workspace.client.security.impl;

import org.jboss.workspace.client.security.AuthenticationContext;
import org.jboss.workspace.client.security.Role;

import java.util.Set;

public class BasicAuthenticationContext implements AuthenticationContext {
    private Set<Role> roles;
    private String name;
    private boolean valid;

    public BasicAuthenticationContext(Set<Role> roles, String name) {
        this.roles = roles;
        this.name = name;
        valid = true;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void logout() {
        valid = false;
    }

    public boolean isValid() {
        return valid;
    }
}