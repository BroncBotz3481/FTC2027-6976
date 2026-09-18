package com.danpeled.msftc;

import androidx.annotation.NonNull;
import androidx.core.math.MathUtils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnegative;

public class Motor {
    // Maybe add background loop to update motor states that arent modified by this accessors
    private boolean m_enabled = true;
    private boolean m_encoderEnabled = false;
    private DcMotorSimple.Direction m_direction;
    private DcMotor.ZeroPowerBehavior m_zeroPowerBehaviour = DcMotor.ZeroPowerBehavior.FLOAT;
    private double m_minOutput = -1.0, m_maxOutput = 1.0;
    private Optional<Double> m_softCurrentLimit = Optional.empty();
    private Optional<Double> m_hardCurrentLimit = Optional.empty();

    private boolean m_currentLimitEnabled = false;

    private final Debouncer m_softCurrentDebouncer =
            new Debouncer(0.1); // 100ms

    private final Debouncer m_hardCurrentDebouncer =
            new Debouncer(0.02); // 20ms
    private Set<Motor> m_followers = new HashSet<>();
    private GearBox m_gearbox;
    private DcMotorEx m_internalMotor;
    private final String m_motorName;
    private double m_positionOffset = 0.0;
    private double m_ticksPerRevolution = MotorType.GOBILDA_6000.getTicksPerRevolution();

    private Alert m_overCurrentAlert = new Alert("Motor is over current!", Alert.AlertLevel.WARNING); // Modifies text on runtime
    private Alert m_invalidOutputAlert = new Alert("Motor output is outside of valid range!", Alert.AlertLevel.WARNING); // Modifies text on runtime

    public Motor(@NonNull String configName) {
        m_motorName = configName;
    }

    public void queryMotor(@NonNull HardwareMap hm) {
        m_internalMotor = hm.get(DcMotorEx.class, m_motorName);

        setDirection(m_direction);
        setZeroPowerBehaviour(m_zeroPowerBehaviour);
        setEncoderMode(m_encoderEnabled);

        if (m_internalMotor == null) {
            throw new IllegalStateException("Motor not found in hardware map: " + m_motorName);
        }
    }

    public Motor(@NonNull DcMotorEx m) {
        m_internalMotor = m;
        m_motorName = m.getDeviceName();
    }

    public Motor(@NonNull HardwareMap hm, @NonNull String configName) {
        this(hm.get(DcMotorEx.class, configName));
    }

    public Motor ofType(MotorType type) {
        m_ticksPerRevolution = type.getTicksPerRevolution();
        return this;
    }

    public Motor withCurrentLimitEnabled() {
        enableCurrentLimit();
        return this;
    }

    public Motor withSoftCurrentLimit(@Nonnegative double amps) {
        if (amps <= 0)
            throw new IllegalArgumentException("Current limit must be > 0");

        m_softCurrentLimit = Optional.of(amps);
        return this;
    }

    public Motor withHardCurrentLimit(@Nonnegative double amps) {
        if (amps <= 0)
            throw new IllegalArgumentException("Current limit must be > 0");

        m_hardCurrentLimit = Optional.of(amps);
        return this;
    }

    public boolean isOverSoftCurrent() {
        return m_softCurrentLimit.isPresent() &&
                getStatorCurrent() > m_softCurrentLimit.get();
    }

    public boolean isOverHardCurrent() {
        return m_hardCurrentLimit.isPresent() &&
                getStatorCurrent() > m_hardCurrentLimit.get();
    }

    public double getStatorCurrent() {
        return m_internalMotor.getCurrent(CurrentUnit.AMPS);
    }

    public void set(double output) {
        double clampedOutput = MathUtils.clamp(output, m_minOutput, m_maxOutput);
        double current = getStatorCurrent();

        if (m_currentLimitEnabled) {
            // Hard limit
            if (m_hardCurrentLimit.isPresent() &&
                    m_hardCurrentDebouncer.calculate(
                            current > m_hardCurrentLimit.get())) {

                m_overCurrentAlert.text = String.format(Locale.US,
                        "Motor (%s) hard current limit exceeded: %.2fA > %.2fA",
                        m_motorName,
                        current,
                        m_hardCurrentLimit.get());

                m_overCurrentAlert.show();

                clampedOutput = 0;
            }
            // Soft limit
            else if (m_softCurrentLimit.isPresent() &&
                    m_softCurrentDebouncer.calculate(
                            current > m_softCurrentLimit.get())) {

                double limit = m_softCurrentLimit.get();

                clampedOutput *= limit / current;
            }
        }

        if (output != clampedOutput) {
            m_invalidOutputAlert.text = String.format(Locale.US,
                    "Motor (%s) output %.2f limited to %.2f",
                    m_motorName,
                    output,
                    clampedOutput);

            m_invalidOutputAlert.show();
        }

        final double finalOutput = clampedOutput;

        doActionToMotorAndFollowers(
                motor -> motor.setPower(finalOutput)
        );
    }


    public double getPower() {
        return m_internalMotor.getPower();
    }

    public double getInternalEncoderPosition() {
        return m_internalMotor.getCurrentPosition();
    }

    public double getPosition() {
        return convertTicksToPosition(getInternalEncoderPosition()) + m_positionOffset;
    }

    protected double convertTicksToPosition(double ticks) {
        return (ticks *
                (360 / m_ticksPerRevolution) / m_gearbox.getRatio());
    }

    public void setPosition(double position) {
        m_positionOffset = getPosition() - position;
    }

    public void setRawPosition(double ticks) {
        m_positionOffset = getPosition() - convertTicksToPosition(ticks);
    }

    public Motor withZeroPowerBehaviour(DcMotor.ZeroPowerBehavior behaviour) {
        setZeroPowerBehaviour(behaviour);
        return this;
    }

    public Motor withMaxOutput(double maxOutput) {
        this.m_maxOutput = MathUtils.clamp(maxOutput, -1, 1);
        return this;
    }

    public void setEncoderMode(boolean activated) {
        m_encoderEnabled = activated;
        if (m_internalMotor != null)
            m_internalMotor.setMode(activated ? DcMotor.RunMode.RUN_USING_ENCODER : DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public Motor enableEncoder() {
        setEncoderMode(true);
        return this;
    }


    public Motor disableEncoder() {
        setEncoderMode(false);
        return this;
    }

    public Motor withMinOutput(double minOutput) {
        this.m_minOutput = MathUtils.clamp(minOutput, -1, 1);
        return this;
    }

    public Motor withFollowers(Motor... followers) {
        m_followers.addAll(Arrays.asList(followers));
        return this;
    }

    public Motor withDirection(DcMotorSimple.Direction direction) {
        setDirection(direction);
        return this;
    }

    public Motor disabled() {
        disable();
        return this;
    }

    public Motor enabled() {
        enable();
        return this;
    }

    public Motor withGearBox(GearBox gearBox) {
        m_gearbox = gearBox;
        return this;
    }

    public DcMotorSimple.Direction getDirection() {
        return m_direction;
    }

    public void setDirection(DcMotorSimple.Direction direction) {
        m_direction = direction;
        if (m_internalMotor != null)
            m_internalMotor.setDirection(direction);
    }


    private void doActionToMotorAndFollowers(MotorAction action) {
        action.execute(m_internalMotor);
        for (Motor m : m_followers) {
            action.execute(m.m_internalMotor);
        }
    }


    public void disable() {
        m_enabled = false;
        if (m_internalMotor != null)
            m_internalMotor.setMotorDisable();
    }

    public void enable() {
        m_enabled = true;
        if (m_internalMotor != null)
            m_internalMotor.setMotorEnable();
    }

    public boolean isMotorEnabled() {
        return m_enabled;
    }

    public DcMotorEx getInternalMotor() {
        return m_internalMotor;
    }

    public double getVelocity() { // ticks per second, need to check how that works
        return m_internalMotor.getVelocity();
    }

    public void setZeroPowerBehaviour(DcMotor.ZeroPowerBehavior behaviour) {
        m_zeroPowerBehaviour = behaviour;
        if (m_internalMotor != null)
            m_internalMotor.setZeroPowerBehavior(behaviour);
    }

    public void enableCurrentLimit() {
        m_currentLimitEnabled = true;
    }

    public void disableCurrentLimit() {
        m_currentLimitEnabled = false;
    }

    @FunctionalInterface
    private interface MotorAction {
        void execute(DcMotorEx m);
    }
}
