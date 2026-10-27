package org.firstinspires.ftc.teamcode;

import com.danpeled.msftc.GlobalTelemetry;
import com.danpeled.msftc.VelocityUnit;
import com.danpeled.msftc.dashboard.TunableNumber;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Team6976TeleOp2027", group = "6976")
public class Team6976TeleOp2027 extends LinearOpMode {
    Team6976HM2027 robot = new Team6976HM2027();
    public boolean intakeToggle = false;

    private TunableNumber speedHigh = new TunableNumber(this, "speedHigh", 2000);
    private TunableNumber speedMid = new TunableNumber(this, "speedMid", 1400);
    private TunableNumber speedLow = new TunableNumber(this, "speedLow", 600);
    private TunableNumber zeroPower = new TunableNumber(this, "zeroPower", 0);

    private TunableNumber target1 = new TunableNumber(this, "target1", 250);
    private TunableNumber target2 = new TunableNumber(this, "target2", 0);

    private TunableNumber[] speed = {speedLow, speedMid, speedHigh};

    int index = 1;

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

            // This button choice was made so that it is hard to hit on accident,
            // it can be freely changed based on preference.
            // The equivalent button is start on Xbox-style controllers.
            if (gamepad1.options) {
                robot.imu.resetYaw();
            }

            double botHeading = robot.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]

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

            telemetry.addData("ShooterSpeed", robot.shooter.getVelocity(VelocityUnit.TicksPerSecond));
            telemetry.addData("ShooterSetpoint", speed[index].getValueAsDouble());
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

            if(gamepad2.x){
                index = 0;
            } else if (gamepad2.y){
                index = 1;
            } else if (gamepad2.b){
                index = 2;
            }

            if (gamepad1.a) {
                robot.shooter.setSetpoint(target1.getValueAsDouble());
            }
            robot.shooter.setSetpoint(target2.getValueAsDouble());

            if (gamepad2.right_trigger >= 0.5) {
                robot.shooterLeft.setPower(0.5);
                robot.shooterRight.setPower(-0.5);
                robot.transferMotor.setPower(0.5);
                robot.shooterGate.setPosition(1);
            } else if (gamepad2.left_trigger >= 0.5) {
                robot.intakeLeft.setPower(0.5);
                robot.intakeRight.setPower(0.5);
            }

            if (gamepad2.right_trigger >= 0.5f){
                shooting(speed[index]);
            }
            //Set motor powers to 0, rn the motors will run forever and ever cuz you never told it to go back to 0 power
            //close the gate

            if (gamepad2.left_trigger >= 0.5f){
                robot.intakeLeft.setPower(1); //Intakes will spin in the same direction because one is reversed in HW
                robot.intakeRight.setPower(1);
                robot.intakeMotor.setPower(0.5);
                robot.transferMotor.setPower(0.5);
                //Intake motor + transfer will also be spinning
            }

            if (gamepad2.left_trigger >= 0.5){
                toggle();
            }


            if (gamepad2.dpadLeftWasPressed()){
                robot.turretServo.setPosition(0.25);
            }

            if (gamepad2.dpadRightWasPressed()){
                robot.turretServo.setPosition(0.75);
            }
            //set servo powers to 0
            robot.transferMotor.setPower(0);
            robot.kicker.setPosition(0);
            robot.intakeLeft.setPower(0);
            robot.intakeRight.setPower(0);

            robot.shooter.setSetpoint(0);
        }
    }

    // class specific method that aren't in constants file go here
    public void exampleMethod (double power){
        robot.DriveLeftFront.setPower(power); robot.DriveRightFront.setPower(-power);
        robot.DriveLeftBack.setPower(-power);   robot.DriveRightBack.setPower(power);
    }

    public void toggle(){
        if (!intakeToggle) {
            intakeToggle = true;
            intaking(0.5);
        }

        if (intakeToggle) {
            intakeToggle = false;
        }
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
     public void intake(double power) {
        robot.intakeLeft.setPower(-power);
        robot.intakeRight.setPower(power);
        robot.intakeMotor.setPower(power);
     }

     public void shoot(double power){
        robot.shooterLeft.setPower(-power);
        robot.shooterRight.setPower(power);
        robot.shooterGate.setPosition(1);
     }

    public void shooting(TunableNumber radPerSecond) {

        robot.shooter.setSetpoint(radPerSecond.getValueAsDouble());

        if(Math.abs(robot.shooter.getVelocity(VelocityUnit.TicksPerSecond) - radPerSecond.getValueAsDouble()) == 300){
            robot.transferMotor.setPower(1);
            robot.intake.setPower(1);
            kick();
        }

    }

    public void kick(){
        robot.kicker.setPosition(1);
        if(Math.abs(robot.kicker.getPosition() - 1) == .1){
            robot.kicker.setPosition(0);
        }
    }

    public void intaking(double power) {
        robot.intakeLeft.setPower(1);
        robot.intakeRight.setPower(1);
        robot.intakeMotor.setPower(0.5);
        robot.transferMotor.setPower(0.5);
    }
}

