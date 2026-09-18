package com.danpeled.msftc;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.danpeled.msftc.dashboard.TunableNumber;

public class GlobalTelemetry {
    private static Telemetry m_telemetry;

    public static void init(Telemetry t) {
        m_telemetry = t;
        TunableNumber.initTunables();
    }

    public static void warn(String warning) {
        if (m_telemetry != null) {
            m_telemetry.addLine("<font color='#FFFF00'>⚠ WARNING: " + warning + "</font>");
            m_telemetry.update();
        }
    }

    public static void error(String warning) {
        if (m_telemetry != null) {
            m_telemetry.addLine("<font color='#FF0000'>✖ ERROR: " + warning + "</font>");
            m_telemetry.update();
        }
    }
}
