package com.danpeled.msftc;

public enum MotorType {
    GOBILDA_6000(28),
    GOBILDA_435(383.6),
    GOBILDA_312(537.7),
    GOBILDA_223(751.8),
    GOBILDA_1620(2786.2),
    REV_HD_HEX_20(560),
    REV_HD_HEX_40(1120),
    REV_CORE_HEX(288);

    private final double ticksPerRevolution;

    MotorType(double ticksPerRevolution) {
        this.ticksPerRevolution = ticksPerRevolution;
    }

    public double getTicksPerRevolution() {
        return ticksPerRevolution;
    }
}
