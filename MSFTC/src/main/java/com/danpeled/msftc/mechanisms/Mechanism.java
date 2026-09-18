package com.danpeled.msftc.mechanisms;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

public abstract class Mechanism {
    private final String m_name;

    public String getName() {
        return m_name;
    }



    public Mechanism(String name) {
        m_name = name;
    }
}
