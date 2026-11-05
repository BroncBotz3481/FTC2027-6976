package org.firstinspires.ftc.teamcode;

import android.health.connect.datatypes.units.Velocity;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp
public class FlywheelMechansim extends OpMode {
    Team6976HM2027 robot = new Team6976HM2027();
    public double highVelocity = 1500;
    public double lowVelocity = 600;

    public double curTargetVelocity = highVelocity;

    double F = 0;
    double P = 0;
    double[] stepSizes = {10, 1, 0.1, 0.01, 0.001, 0.0001};
    double[] VelocitystepSizes = {1000, 100, 100, 10, 0.001, 0.0001};
    int stepIndex = 1;
    int VelocitystepIndexstepIndex = 1;


    @Override
    public void init() {
        robot.Map(hardwareMap);
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        robot.frontShooter.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        robot.backShooter.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        telemetry.addLine("Init Complete");
    }

    @Override
    public void loop() {
        // WasPressed executes the code once and not again until you release and press again
        if (gamepad1.yWasPressed()) {
            if (curTargetVelocity == highVelocity) {
                curTargetVelocity = lowVelocity;
            } else {
                curTargetVelocity = highVelocity;
            }
        }

        // Gamepad 2: Adjust target velocity
        if (gamepad2.dpadUpWasPressed()) {
            curTargetVelocity += VelocitystepSizes[VelocitystepIndexstepIndex];
        }

        if (gamepad2.dpadDownWasPressed()) {
            curTargetVelocity -= VelocitystepSizes[VelocitystepIndexstepIndex];
        }

// Gamepad 2: Change velocity step size
        if (gamepad2.bWasPressed()) {
            VelocitystepIndexstepIndex =
                    (VelocitystepIndexstepIndex + 1) % VelocitystepSizes.length;
        }

        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }
        if (gamepad1.dpadLeftWasPressed()) {
            F -= stepSizes[stepIndex];
        }
        if (gamepad1.dpadRightWasPressed()) {
            F += stepSizes[stepIndex];
        }
        if (gamepad1.dpadDownWasPressed()) {
            P -= stepSizes[stepIndex];
        }
        if (gamepad1.dpadUpWasPressed()) {
            P += stepSizes[stepIndex];
        }
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        PIDFCoefficients backpidfCoefficients = new PIDFCoefficients(P, 0, 0, F + 2.5f);
        robot.frontShooter.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        robot.backShooter.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, backpidfCoefficients);
        robot.frontShooter.setVelocity(curTargetVelocity);
        robot.backShooter.setVelocity(curTargetVelocity);

        double curVelocity = robot.frontShooter.getVelocity();
        double error = curTargetVelocity - curVelocity;


        telemetry.addData("Target Velocity", curTargetVelocity);
        telemetry.addData("Current Velocity", "%.2f", curVelocity);
        telemetry.addData("Error", "%.2f", error);
        telemetry.addLine("--------------------------");
        telemetry.addData("Tuning P", "%.4f (Dpad U/D)",P);
        telemetry.addData("Tuning F", "%.4f (Dpad L/R)",F);
        telemetry.addData("Step Size", "%.4f (B button) ", stepSizes[stepIndex]);
        telemetry.addLine("--------------------------");
        telemetry.addData("Target Velocity Step", "%.4f",
                VelocitystepSizes[VelocitystepIndexstepIndex]);
        telemetry.addData("Target Velocity Controls", "Gamepad 2 Dpad U/D");
        telemetry.addData("Velocity Step Controls", "Gamepad 2 B");


    }
}