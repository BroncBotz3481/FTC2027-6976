package org.firstinspires.ftc.teamcode;

import android.health.connect.datatypes.units.Velocity;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp
public class TestTransfer extends OpMode {
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
    double isNeg = 1;


    @Override
    public void init() {
        robot.Map(hardwareMap);
        telemetry.addLine("Init Complete");
    }

    @Override
    public void loop() {
        if (gamepad1.rightBumperWasPressed()){
            isNeg = isNeg * -1;
        }

        if (gamepad1.b){
            robot.TransferMotor.setPower(1 * isNeg);
        } else{
            robot.TransferMotor.setPower(0 * isNeg);
        }
        if (gamepad1.a){
            robot.TransferLeft.setPower(1 * isNeg);
            robot.TransferRight.setPower(-1 * isNeg);
        }else {
            robot.TransferLeft.setPower(0);
            robot.TransferRight.setPower(0);
        }

        if (gamepad1.x) {
            robot.IntakeMotor.setPower(1 * isNeg);
        } else{
            robot.IntakeMotor.setPower(0);
        }
        



    }
}