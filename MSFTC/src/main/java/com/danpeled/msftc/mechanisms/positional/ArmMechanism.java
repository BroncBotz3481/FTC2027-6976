package com.danpeled.msftc.mechanisms.positional;

import com.arcrobotics.ftclib.controller.wpilibcontroller.ArmFeedforward;

import com.danpeled.msftc.Motor;
import com.danpeled.msftc.dashboard.DashboardUtils;

public class ArmMechanism extends PositionalMechanism<ArmMechanism, ArmFeedforward> {
    public ArmMechanism(String name, Motor leadMotor) {
        super(name, leadMotor);
        ff = new ArmFeedforward(0, 0, 0, 0);
    }

    @Override
    protected double getFeedforwardOutput(double feedback) {
        return ff.calculate(getPosition(), feedback);
    }

    @Override
    protected void uploadFFConfig(String where) {
        DashboardUtils.uploadConfig(() -> ff.ks, (ks) -> ff = new ArmFeedforward(ks, ff.kcos, ff.kv, ff.ka), where, "Ks");
        DashboardUtils.uploadConfig(() -> ff.kcos, (cos) -> ff = new ArmFeedforward(ff.ks, cos, ff.kv, ff.ka), where, "KCos");
        DashboardUtils.uploadConfig(() -> ff.kv, (kv) -> ff = new ArmFeedforward(ff.ks, ff.kcos, kv, ff.ka), where, "Kv");
        DashboardUtils.uploadConfig(() -> ff.ka, (ka) -> ff = new ArmFeedforward(ff.ks, ff.kcos, ff.kv, ka), where, "Ka");
    }
}
