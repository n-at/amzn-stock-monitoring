package ru.doublebyte.amznsm.services;

public class SessionStorage {

    private boolean authorized = false;

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

}
