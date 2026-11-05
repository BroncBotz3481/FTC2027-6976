package org.firstinspires.ftc.teamcode;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class DriveTrainSubsystem {

    private Team6976HM2027 robot;

    public DriveTrainSubsystem(Team6976HM2027 robot){
        this.robot = robot;
    }

    public void driveFieldCentric(float y, float x, float rx, double mag){
         y = -y; // Remember, Y stick value is reversed
         x = x;
         rx = rx;
        // This button choice was made so that it is hard to hit on accident,
        // it can be freely changed based on preference.
        // The equivalent button is start on Xbox-style controllers.


        double botHeading = robot.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Rotate the movement direction counter to the bot's rotation
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX = rotX * 1.1;  // Counteract imperfect strafing

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        robot.DriveLeftFront.setPower(frontLeftPower * mag);
        robot.DriveLeftBack.setPower(backLeftPower * mag);
        robot.DriveRightFront.setPower(frontRightPower * mag);
        robot.DriveRightBack.setPower(backRightPower * mag);
    }

    public void driveRobotCentric(float y, float x, float rx, double mag){
        y = y; // Remember, this is reversed!
        x = -x * 1.1f; // Counteract imperfect strafing
        rx = -rx;

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
    }

    public void defaultCommand(boolean isFieldCentricTrue, float y, float x, float rx, double isSpeedSlow){
        if (!isFieldCentricTrue){
            driveRobotCentric(y, x, rx, isSpeedSlow);
        } else {
            driveFieldCentric(y, x, rx, isSpeedSlow);
        }
    }

    public void driveForward(double power) {
        robot.DriveLeftFront.setPower(power);
        robot.DriveRightFront.setPower(-power);
        robot.DriveLeftBack.setPower(-power);
        robot.DriveRightBack.setPower(power);
    }
    //
    public void driveRight(double power) {
        // Left Wheels                         //Right Wheels
        robot.DriveLeftFront.setPower(-power);
        robot.DriveRightFront.setPower(power);
        robot.DriveLeftBack.setPower(power);
        robot.DriveRightBack.setPower(-power);
    }
    public void driveLeft(double power) {
        // Left Wheels                         //Right Wheels
        robot.DriveLeftFront.setPower(power);
        robot.DriveRightFront.setPower(-power);
        robot.DriveLeftBack.setPower(-power);
        robot.DriveRightBack.setPower(power);
    }

    public void driveBackward(double power) {
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
