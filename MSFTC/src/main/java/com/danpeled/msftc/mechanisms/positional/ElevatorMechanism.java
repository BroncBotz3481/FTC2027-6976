package com.danpeled.msftc.mechanisms.positional;


import com.arcrobotics.ftclib.controller.wpilibcontroller.ElevatorFeedforward;

import com.danpeled.msftc.Motor;
import com.danpeled.msftc.dashboard.DashboardUtils;

public class ElevatorMechanism extends PositionalMechanism<ElevatorMechanism, ElevatorFeedforward> {
    public ElevatorMechanism(String name, Motor leadMotor) {
        super(name, leadMotor);
        ff = new ElevatorFeedforward(0, 0, 0);
    }

    @Override
    protected double getFeedforwardOutput(double feedback) {
        return ff.calculate(feedback, 0);
    }

    @Override
    protected void uploadFFConfig(String where) {
        DashboardUtils.uploadConfig(() -> ff.ks, (ks) -> ff = new ElevatorFeedforward(ks, ff.kg, ff.ka), where, "Ks");
        DashboardUtils.uploadConfig(() -> ff.kg, (kg) -> ff = new ElevatorFeedforward(ff.ks, kg, ff.ka), where, "Kg");
        DashboardUtils.uploadConfig(() -> ff.ka, (ka) -> ff = new ElevatorFeedforward(ff.ks, ff.kg, ka), where, "Ka");
    }
}
