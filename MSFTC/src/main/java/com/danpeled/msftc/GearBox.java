package com.danpeled.msftc;

public class GearBox {
    private double m_inputRpm = 6000;
    private double m_ratio = 1.0;

    public GearBox() {
    }

    public GearBox(double motorRpm) {
        this.m_inputRpm = motorRpm;
    }

    public GearBox withInputRPM(double rpm) {
        this.m_inputRpm = rpm;
        return this;
    }

    public static GearBox fromOutputRPM(double inputRpm, double outputRpm) {
        return new GearBox(inputRpm).withRatio(inputRpm / outputRpm);
    }

    public GearBox withRatio(double ratio) {
        this.m_ratio *= ratio;
        return this;
    }

    public GearBox withStages(double... stages) {
        for (double stage : stages) {
            this.m_ratio *= stage;
        }
        return this;
    }

    public GearBox reset() {
        this.m_ratio = 1.0;
        return this;
    }

    public double getRatio() {
        return m_ratio;
    }

    public double getOutputRPM() {
        return m_inputRpm / m_ratio;
    }

    public double getInputRPM() {
        return m_inputRpm;
    }
}
