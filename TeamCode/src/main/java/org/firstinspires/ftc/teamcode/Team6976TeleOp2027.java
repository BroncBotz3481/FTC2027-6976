package org.firstinspires.ftc.teamcode;

import com.danpeled.msftc.GlobalTelemetry;
import com.danpeled.msftc.dashboard.TunableNumber;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Team4008TeleOp2025", group = "4008")
public class Team6976TeleOp2027 extends LinearOpMode {
    Team6976HMap2027 robot = new Team6976HMap2027();

    //shooting speed, raidans per second probably
    private TunableNumber target1 = new TunableNumber(this, "target1", 90);
    private TunableNumber target2 = new TunableNumber(this, "target2", 0);

    @Override
    public void runOpMode() {
        robot.Map(hardwareMap);
        telemetry.addData("Say", "TeleOp Starting");
        telemetry.update();

        GlobalTelemetry.init(telemetry);

        robot.flywheelMotor1.queryMotor(hardwareMap);
        robot.flywheelMotor2.queryMotor(hardwareMap);

        robot.shooter.initConfig();

        robot.DriveRightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.DriveRightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.DriveLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.DriveLeftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.DriveRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.DriveRightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.DriveLeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.DriveLeftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();



        while (opModeIsActive()) {

            boolean speedslow = gamepad1.right_bumper;
            double mag = speedslow ? 0.5 : 1.0;

            double y = gamepad1.left_stick_y; // Remember, this is reversed!
            double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = -gamepad1.right_stick_x;

            // Optional Deadzones
//            double y = (Math.abs(gamepad1.left_stick_y) > 0.1 ? gamepad1.left_stick_y : 0); // Remember, this is reversed!
//            double x = -(Math.abs(gamepad1.left_stick_x) > 0.1 ? gamepad1.left_stick_x : 0) * 1.1; // Counteract imperfect strafing
//            double rx = -gamepad1.right_stick_x;


            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1.5);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            robot.DriveLeftFront.setPower(frontLeftPower * mag);
            robot.DriveLeftBack.setPower(backLeftPower * mag);
            robot.DriveRightFront.setPower(frontRightPower * mag);
            robot.DriveRightBack.setPower(backRightPower * mag);

            telemetry.addData("RightFront", robot.DriveRightFront.getCurrentPosition());
            telemetry.addData("RightBack", robot.DriveRightBack.getCurrentPosition());
            telemetry.addData("LeftFront", robot.DriveLeftFront.getCurrentPosition());
            telemetry.addData("LeftBack", robot.DriveLeftBack.getCurrentPosition());
            telemetry.update();

            if (gamepad1.dpad_up){
                moveForward(mag);
            } else if (gamepad1.dpad_down){
                moveBackward(mag);
            } else if (gamepad1.dpad_left){
                moveLeft(mag);
            } else if (gamepad1.dpad_right){
                moveRight(mag);
            }

            if (gamepad1.a) {
                robot.shooter.setSetpoint(target1.getValueAsDouble());
            }
                robot.shooter.setSetpoint(target2.getValueAsDouble());


        }
    }



    // class specific method that aren't in constants file go here
    public void exampleMethod (double power){
        robot.DriveLeftFront.setPower(power); robot.DriveRightFront.setPower(-power);
        robot.DriveLeftBack.setPower(-power);   robot.DriveRightBack.setPower(power);
    }

    public void moveLeft(double power) {
        robot.DriveLeftFront.setPower(power);
        robot.DriveRightFront.setPower(-power);
        robot.DriveLeftBack.setPower(-power);
        robot.DriveRightBack.setPower(power);
    }
    //
    public void moveRight(double power) {
        // Left Wheels                         //Right Wheels
        robot.DriveLeftFront.setPower(-power);
        robot.DriveRightFront.setPower(power);
        robot.DriveLeftBack.setPower(power);
        robot.DriveRightBack.setPower(-power);
    }

    public void moveForward(double power) {
        // Left Wheels                         //Right Wheels
        robot.DriveLeftFront.setPower(power);
        robot.DriveRightFront.setPower(power);
        robot.DriveLeftBack.setPower(power);
        robot.DriveRightBack.setPower(power);
    }

    public void moveBackward(double power) {
        // Left Wheels                         //Right Wheels
        robot.DriveLeftFront.setPower(-power);
        robot.DriveRightFront.setPower(-power);
        robot.DriveLeftBack.setPower(-power);
        robot.DriveRightBack.setPower(-power);
    }

    public void stopDriveTrainMotors() {
        // Left Wheels                         //Right Wheels
        robot.DriveLeftFront.setPower(0);
        robot.DriveRightFront.setPower(0);
        robot.DriveRightBack.setPower(0);
        robot.DriveLeftBack.setPower(0);
    }
}

