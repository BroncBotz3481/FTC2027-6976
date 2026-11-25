
package org.firstinspires.ftc.teamcode;

import com.danpeled.msftc.dashboard.TunableNumber;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Team6976AutoShooterTimeBased", group = "6976")
public class Team6976AutoShooterTimeBased extends LinearOpMode {

    Team6976HM2027 robot = new Team6976HM2027();
    ElapsedTime Time = new ElapsedTime();

    private double addedPower = 400;
    int index = 0;
    double shooting = 330;
    private double[] speed = {shooting + addedPower};
    TunableNumber kPB = new TunableNumber(this,"PB", 15); //P value in PIDF

    TunableNumber kSB = new TunableNumber(this,"SB", 20); //F value in PIDF

    // Robot measurements
    static final double WHEEL_DIAMETER = 1.9;
    static final double TICKS_PER_REV = 537.7;
    static final double TRACK_WIDTH = 14.0;

    @Override
    public void runOpMode() {
        robot.Map(hardwareMap);



        resetEncoders();
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        robot.TurretServo.setPosition(0);
        robot.frontShooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(kPB.getValueAsDouble(), 0, 0, kSB.getValueAsDouble()));

        telemetry.addLine("Ready to start");
        telemetry.update();
        telemetry();

        waitForStart();

        shoot();
        sleep(3000);
        transfer();
        sleep(5000);
        stopTransfer();
        stopShoot();
        robot.DriveRightFront.setPower(-1);
        robot.DriveRightBack.setPower(1);
        robot.DriveLeftFront.setPower(1);
        robot.DriveLeftBack.setPower(-1);
        sleep(700);
        robot.DriveLeftFront.setPower(-1);
        robot.DriveLeftBack.setPower(-1);
        robot.DriveRightBack.setPower(-1);
        robot.DriveRightFront.setPower(-1);
        sleep(1500);
        robot.DriveRightFront.setPower(-0.6);
        robot.DriveRightBack.setPower(0.6);
        robot.DriveLeftFront.setPower(0.6);
        robot.DriveLeftBack.setPower(-0.6);
        sleep(150);
        robot.DriveLeftFront.setPower(0);
        robot.DriveLeftBack.setPower(0);
        robot.DriveRightBack.setPower(0);
        robot.DriveRightFront.setPower(0);





//        if (opModeIsActive()) {
//
//            // 1. Drive forward 24 inches
//            driveForward(24, 0.5, 4000);
//
//            // 2. Strafe left 10 inches
//            strafeLeft(10, 0.5, 3000);
//
//            // 3. Turn 180 degrees
//
//        }
//
//        stopDrive();
    }

    public void telemetry(){
        telemetry.addData("Front Velocity",
                "%.1f TPS", robot.frontShooter.getVelocity());
    }
    private void shoot() {
        robot.frontShooter.setVelocity(speed[index] + addedPower);
    }

    private void transfer() {
        robot.TransferMotor.setPower(-1);
    }

    private void stopShoot() {
        robot.frontShooter.setVelocity(0);
    }

    private void stopTransfer() {
        robot.TransferMotor.setPower(0);
    }

    // Convert inches to encoder ticks
    public int inchesToTicks(double inches) {
        return (int) Math.round(
                inches * TICKS_PER_REV
                        / (Math.PI * WHEEL_DIAMETER)
        );
    }

    // Drive forward
    public void driveForward(double inches, double power, int timeout) {
        moveRobot(
                inches, inches,
                inches, inches,
                power, timeout
        );
    }

    // Strafe left using mecanum wheels
    public void strafeLeft(double inches, double power, int timeout) {
        moveRobot(
                -inches, inches,
                inches, -inches,
                power, timeout
        );
    }

    // Turn 180 degrees
    public void turn180(double power, int timeout) {
        double turnDistance = Math.PI * TRACK_WIDTH / 2;

        moveRobot(
                -turnDistance, turnDistance,
                -turnDistance, turnDistance,
                power, timeout
        );
    }

    // Move all four wheels to their encoder targets
    public void moveRobot(
            double leftFrontInches,
            double rightFrontInches,
            double leftBackInches,
            double rightBackInches,
            double power,
            int timeout) {

        DcMotor[] motors = {
                robot.DriveLeftFront,
                robot.DriveRightFront,
                robot.DriveLeftBack,
                robot.DriveRightBack
        };

        double[] distances = {
                leftFrontInches,
                rightFrontInches,
                leftBackInches,
                rightBackInches
        };

        // Set target positions
        for (int i = 0; i < motors.length; i++) {
            int target = motors[i].getCurrentPosition()
                    + inchesToTicks(distances[i]);

            motors[i].setTargetPosition(target);
            motors[i].setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motors[i].setPower(Math.abs(power));
        }

        // Wait until movement finishes or times out
        Time.reset();

        while (opModeIsActive()
                && Time.milliseconds() < timeout
                && (robot.DriveLeftFront.isBusy()
                || robot.DriveRightFront.isBusy()
                || robot.DriveLeftBack.isBusy()
                || robot.DriveRightBack.isBusy())) {

            telemetry.addData("Left Front", robot.DriveLeftFront.getCurrentPosition());
            telemetry.addData("Right Front", robot.DriveRightFront.getCurrentPosition());
            telemetry.addData("Left Back", robot.DriveLeftBack.getCurrentPosition());
            telemetry.addData("Right Back", robot.DriveRightBack.getCurrentPosition());
            telemetry.update();
        }

        stopDrive();
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // Reset all four wheel encoders
    public void resetEncoders() {
        setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    // Set the mode for all four drive motors
    public void setDriveMode(DcMotor.RunMode mode) {
        robot.DriveLeftFront.setMode(mode);
        robot.DriveRightFront.setMode(mode);
        robot.DriveLeftBack.setMode(mode);
        robot.DriveRightBack.setMode(mode);
    }

    // Stop all four drive motors
    public void stopDrive() {
        robot.DriveLeftFront.setPower(0);
        robot.DriveRightFront.setPower(0);
        robot.DriveLeftBack.setPower(0);
        robot.DriveRightBack.setPower(0);
    }
}
