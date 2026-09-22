package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.ArmFeedforward;
import com.danpeled.msftc.GearBox;
import com.danpeled.msftc.GlobalTelemetry;
import com.danpeled.msftc.Motor;
import com.danpeled.msftc.MotorType;
import com.danpeled.msftc.dashboard.TunableNumber;
import com.danpeled.msftc.mechanisms.positional.ArmMechanism;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name = "yya")
public class ExampleOpMode extends LinearOpMode {
    private TunableNumber target1 = new TunableNumber(this, "target1", 90);
    private TunableNumber target2 = new TunableNumber(this, "target2", 0);

    private final Motor armMotor = new Motor("armMotor")
            .ofType(MotorType.GOBILDA_6000)
            .withZeroPowerBehaviour(DcMotor.ZeroPowerBehavior.BRAKE)
            .withGearBox(GearBox.fromOutputRPM(6000, 60))
            .withHardCurrentLimit(20)
            .withSoftCurrentLimit(10)
            .withCurrentLimitEnabled()
            .enableEncoder()
            .withDirection(DcMotorSimple.Direction.FORWARD);

    private final ArmMechanism arm = new ArmMechanism("arm", armMotor)
            .withLimits(0, 100)
            .withPID(new PIDController(0.1, 0, 0))
            .withFeedforward(new ArmFeedforward(0, 0, 0));

    @Override
    public void runOpMode() {
        GlobalTelemetry.init(telemetry);

        armMotor.queryMotor(hardwareMap);

        arm.initConfig();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) arm.setSetpoint(target1.getValueAsDouble());
            if (gamepad1.b) arm.setSetpoint(target2.getValueAsDouble());

            arm.update();
        }
    }
}
