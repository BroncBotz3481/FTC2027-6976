package com.danpeled.msftc.mechanisms.positional;

import androidx.core.math.MathUtils;

import com.arcrobotics.ftclib.controller.PIDController;

import com.danpeled.msftc.Alert;
import com.danpeled.msftc.Motor;
import com.danpeled.msftc.dashboard.DashboardUtils;
import com.danpeled.msftc.dashboard.TunableNumber;
import com.danpeled.msftc.mechanisms.Mechanism;

import java.util.Locale;
import java.util.Optional;

public abstract class PositionalMechanism<T extends PositionalMechanism<T, FFType>, FFType> extends Mechanism {
    protected final Motor leadMotor;
    protected PIDController positionPID;
    protected TunableNumber lowerLimit, upperLimit;
    protected Optional<Double> setpoint = Optional.empty();
    protected FFType ff;

    private Alert m_limitsNotValidAlert = new Alert("Lower limit not valid!", Alert.AlertLevel.WARNING); // modifies text when active

    public PositionalMechanism(String name, Motor leadMotor) {
        super(name);
        this.leadMotor = leadMotor;

        lowerLimit = new TunableNumber(getName(), "lowerLimit", Double.MIN_VALUE);
        upperLimit = new TunableNumber(getName(), "upperLimit", Double.MAX_VALUE);
    }

    @SuppressWarnings("unchecked")
    public T withLimits(double lowerLimit, double upperLimit) {
        setUpperLimit(upperLimit);
        setLowerLimit(lowerLimit);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withUpperLimit(double UpperLimit) {
        setUpperLimit(UpperLimit);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withLowerLimit(double lowerLimit) {
        setLowerLimit(lowerLimit);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withFeedforward(FFType ff) {
        this.ff = ff;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withPID(PIDController pid) {
        positionPID = pid;
        return (T) this;
    }

    protected void setLowerLimit(double lower) {
        lowerLimit.setValue(lower);
    }

    protected void setUpperLimit(double upper) {
        upperLimit.setValue(upper);
    }

    protected double calculateClosedLoopControl(double setpoint) {
        return positionPID.calculate(getPosition(), setpoint);
    }

    protected abstract double getFeedforwardOutput(double feedback);

    public void runToPosition(double setpoint) {
        setSetpoint(setpoint);
        update();
    }

    public void update() {
        if (lowerLimit.getValueAsDouble() >= upperLimit.getValueAsDouble()) {
            m_limitsNotValidAlert.text = (String.format(
                    Locale.US,
                    "%s Lower limit (%.2f) must be less than upper limit (%.2f)",
                    getName(),
                    lowerLimit.getValueAsDouble(),
                    upperLimit.getValueAsDouble()
            ));
            m_limitsNotValidAlert.show();
        }
        if (!setpoint.isPresent()) return;

        double actualSetpoint = MathUtils.clamp(setpoint.orElse(lowerLimit.getValueAsDouble()), lowerLimit.getValueAsDouble(), upperLimit.getValueAsDouble());

        double feedback = calculateClosedLoopControl(actualSetpoint);
        double u = feedback + getFeedforwardOutput(feedback);
        if (u < 0 && getPosition() <= lowerLimit.getValueAsDouble()) leadMotor.set(0);
        else if (u > 0 && getPosition() >= upperLimit.getValueAsDouble()) leadMotor.set(0);
        else leadMotor.set(u);
    }

    public void setSetpoint(double setpoint) {
        this.setpoint = Optional.of(setpoint);
    }


    public double getPosition() {
        return leadMotor.getPosition();
    }

    public void initConfig() {
        DashboardUtils.uploadConfig(this, getName());
    }

    public FFType getFeedForward() {
        return ff;
    }

    public PIDController getPIDController() {
        return positionPID;
    }

    protected abstract void uploadFFConfig(String where);
}
