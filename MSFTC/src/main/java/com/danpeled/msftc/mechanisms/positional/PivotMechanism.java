package com.danpeled.msftc.mechanisms.positional;

import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;

import com.danpeled.msftc.Motor;
import com.danpeled.msftc.dashboard.DashboardUtils;

public class PivotMechanism extends PositionalMechanism<PivotMechanism, SimpleMotorFeedforward> {
    public PivotMechanism(String name, Motor leadMotor) {
        super(name, leadMotor);
        ff = new SimpleMotorFeedforward(0, 0, 0);
    }

    @Override
    protected double getFeedforwardOutput(double feedback) {
        return ff.calculate(feedback);
    }

    @Override
    protected void uploadFFConfig(String where) {
        DashboardUtils.uploadConfig(() -> ff.ks, (ks) -> ff = new SimpleMotorFeedforward(ks, ff.kv, ff.ka), where, "Ks");
        DashboardUtils.uploadConfig(() -> ff.kv, (kv) -> ff = new SimpleMotorFeedforward(ff.ks, kv, ff.ka), where, "Kv");
        DashboardUtils.uploadConfig(() -> ff.ka, (ka) -> ff = new SimpleMotorFeedforward(ff.ks, ff.kv, ka), where, "Ka");
    }
}
