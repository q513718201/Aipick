package com.hazz.aipick.events;

public class ChangeEvent {
    public String isDemo;

    public ChangeEvent(int isDemo) {
        this.isDemo = isDemo + "";
    }

    @Override
    public String toString() {
        return "ChangeEvent{" +
                "isDemo='" + isDemo + '\'' +
                '}';
    }
}
