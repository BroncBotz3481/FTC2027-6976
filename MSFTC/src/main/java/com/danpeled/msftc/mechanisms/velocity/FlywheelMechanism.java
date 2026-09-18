package com.danpeled.msftc.mechanisms.velocity;

import androidx.core.math.MathUtils;

import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;

import com.danpeled.msftc.Alert;
import com.danpeled.msftc.Motor;
import com.danpeled.msftc.dashboard.DashboardUtils;
import com.danpeled.msftc.dashboard.TunableNumber;
import com.danpeled.msftc.mechanisms.Mechanism;

import java.util.Locale;
import java.util.Optional;

public class FlywheelMechanism extends Mechanism {
    private final Motor m_leadMotor;
    private PIDController m_pid;
    private SimpleMotorFeedforward m_ff;
    private TunableNumber lowerLimit, upperLimit;
    private Optional<Double> m_setpoint = Optional.empty();

    private Alert m_limitsNotValidAlert = new Alert("Lower limit not valid!", Alert.AlertLevel.WARNING); // modifies text when active

    public FlywheelMechanism(String name, Motor leadMotor) {
        super(name);
        m_ff = new SimpleMotorFeedforward(0, 0, 0);
        m_leadMotor = leadMotor;
    }

    public SimpleMotorFeedforward getFeedForward() {
        return m_ff;
    }

    public PIDController getPIDController() {
        return m_pid;
    }


    public FlywheelMechanism withLimits(double lowerLimit, double upperLimit) {
        setUpperLimit(upperLimit);
        setLowerLimit(lowerLimit);
        return this;
    }


    public FlywheelMechanism withUpperLimit(double UpperLimit) {
        setUpperLimit(UpperLimit);
        return this;
    }


    public FlywheelMechanism withLowerLimit(double lowerLimit) {
        setLowerLimit(lowerLimit);
        return this;
    }


    public FlywheelMechanism withFeedforward(SimpleMotorFeedforward m_ff) {
        m_ff = m_ff;
        return this;
    }


    public FlywheelMechanism withPID(PIDController pid) {
        m_pid = pid;
        return this;
    }

    private void setLowerLimit(double lower) {
        lowerLimit.setValue(lower);
    }

    private void setUpperLimit(double upper) {
        upperLimit.setValue(upper);
    }

    public void runVelocity(double setpoint) {
        setSetpoint(setpoint);
        update();
    }

    public void update() {
        if (lowerLimit.getValueAsDouble() >= upperLimit.getValueAsDouble()) {
            m_limitsNotValidAlert.text = (String.format(Locale.US, "%s Lower limit (%.2f) must be less than upper limit (%.2f)", getName(), lowerLimit.getValueAsDouble(), upperLimit.getValueAsDouble()));
            m_limitsNotValidAlert.show();
        }
        if (!m_setpoint.isPresent()) return;

        double actualSetpoint = MathUtils.clamp(m_setpoint.orElse(lowerLimit.getValueAsDouble()), lowerLimit.getValueAsDouble(), upperLimit.getValueAsDouble());
        double currentVelocity = getVelocity();

        double feedback = m_pid.calculate();
        double u = feedback + m_ff.calculate(actualSetpoint);

        if (u > 0 && currentVelocity >= upperLimit.getValueAsDouble()) {
            u = 0;
        } else if (u < 0 && currentVelocity <= lowerLimit.getValueAsDouble()) {
            u = 0;
        }

        m_leadMotor.set(u);
    }

    public void setSetpoint(double setpoint) {
        m_setpoint = Optional.of(setpoint);
    }


    public double getVelocity() {
        return m_leadMotor.getVelocity();
    }

    public void initConfig() {
        DashboardUtils.uploadConfig(m_pid, getName());


        DashboardUtils.uploadConfig(() -> m_ff.ks, (ks) -> m_ff = new SimpleMotorFeedforward(ks, m_ff.kv, m_ff.ka), getName(), "Ks");
        DashboardUtils.uploadConfig(() -> m_ff.kv, (kv) -> m_ff = new SimpleMotorFeedforward(m_ff.ks, kv, m_ff.ka), getName(), "Kv");
        DashboardUtils.uploadConfig(() -> m_ff.ka, (ka) -> m_ff = new SimpleMotorFeedforward(m_ff.ks, m_ff.kv, ka), getName(), "Ka");
    }
}
