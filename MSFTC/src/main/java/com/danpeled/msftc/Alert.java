package com.danpeled.msftc;

public class Alert {
    public static enum AlertLevel {
        WARNING, ERROR
    }

    public String text;
    public AlertLevel level;

    public Alert(String text, AlertLevel level) {
        this.text = text;
        this.level = level;
    }

    public void show() {
        switch (level) {
            case WARNING:
                GlobalTelemetry.warn(text);
                break;
            case ERROR:
                GlobalTelemetry.error(text);
        }
    }
}
