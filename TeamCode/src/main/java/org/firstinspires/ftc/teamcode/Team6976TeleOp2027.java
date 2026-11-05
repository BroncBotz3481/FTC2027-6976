package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;

import com.danpeled.msftc.GlobalTelemetry;
import com.danpeled.msftc.VelocityUnit;
import com.danpeled.msftc.dashboard.TunableNumber;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Team6976TeleOp2027", group = "6976")
public class Team6976TeleOp2027 extends LinearOpMode {


    Team6976HM2027 robot = new Team6976HM2027();
    RobotSystems commands = new RobotSystems(robot);
    DriveTrainSubsystem drivetrain = new DriveTrainSubsystem(robot);

    //ticks per second, max 6000 RPM motor is 2,800 TPS.


    private double[] speed = {2000, 1400, 60};
    int index = 1;


    @Override
    public void runOpMode() {
        TunableNumber defaultSpeed = new TunableNumber(this,"Default Speed", 0);
        TunableNumber turretPose = new TunableNumber(this,"TurretPose", 0);

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

        //servos here. Ex: robot.gate.setPosition(0); When robot starts, the gate will be set to close
        boolean fieldCentric = false;

        waitForStart();

        while (opModeIsActive()) {

            boolean speedslow = gamepad1.right_bumper;
            double mag = speedslow ? 0.5 : 1.0;

            if (gamepad1.left_stick_button && gamepad1.right_stick_button) {
                fieldCentric = !fieldCentric;
            }

            defaultCommand(fieldCentric); //if true, field centric, if false, robot centric
            telemetry(); //pushes telementry to Driver Station


            if (gamepad1.dpad_up) {
                drivetrain.driveForward(mag);
            }
            else if (gamepad1.dpad_down) {
                drivetrain.driveBackward(mag);
            }
            else if (gamepad1.dpad_left) {
                drivetrain.driveLeft(mag);
            }
            else if (gamepad1.dpad_right) {
                drivetrain.driveRight(mag);
            }

            //Testing
                boolean test = true;
                if (test) {
                    if (gamepad1.x) {
                        commands.shoot(1000);
                    }
                    if (gamepad1.y) {
                        robot.IntakeMotor.setPower(1);
                    }
                    if (gamepad1.b) {
                        commands.transfer();
                    }
                }

            //Turret Code
                if (gamepad2.dpad_up) {
                    robot.TurretServo.setPosition(turretPose.getValueAsDouble());
                }
                if (gamepad2.dpad_down) {
                    robot.TurretServo.setPosition(0);
                }
                if (gamepad2.dpad_left) {
                    robot.TurretServo.setPosition(0.5);
                }
                if (gamepad2.dpad_right) {
                    robot.TurretServo.setPosition(1);
                }


                if (gamepad2.x) {
                    index = 0;
                } else if (gamepad2.y) {
                    index = 1;
                } else if (gamepad2.b) {
                    index = 2;
                }

                if (gamepad2.right_trigger >= 0.5f) {
                    commands.shoot(speed[index]);
                }
                if (gamepad2.right_bumper) {
                    commands.kick();
                }
                //Set motor powers to 0, rn the motors will run forever and ever cuz you never told it to go back to 0 power
                //close the gate

                if (gamepad2.left_trigger >= 0.5f) {
                    commands.intake(1);
                }

                //set servo powers to 0
                robot.IntakeMotor.setPower(0);
                robot.TransferRight.setPower(0);
                robot.TransferLeft.setPower(0);
                robot.kicker.setPosition(0);
                robot.TransferMotor.setPower(0);
                robot.shooter.setSetpoint(defaultSpeed.getValueAsDouble());
                //Optional Challenge, make seperate methods for intaking() and shooting()
            }
        }


    public void telemetry(){
        telemetry.addData("RightFront", robot.DriveRightFront.getCurrentPosition());
        telemetry.addData("RightBack", robot.DriveRightBack.getCurrentPosition());
        telemetry.addData("LeftFront", robot.DriveLeftFront.getCurrentPosition());
        telemetry.addData("LeftBack", robot.DriveLeftBack.getCurrentPosition());

        //telemetry.addData("ShooterSpeed", robot.frontShooter.getVelocity());
        telemetry.addData("ShooterSpeed", robot.shooter.getVelocity(VelocityUnit.TicksPerSecond));
        telemetry.addData("ShooterSetpoint", speed[index]);
        telemetry.addData("Turret Pose", robot.TurretServo.getPosition());
        telemetry.update();
    }
    public void driveFieldCentric(){
        double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;
        boolean speedslow = gamepad1.right_bumper;
        double mag = speedslow ? 0.5 : 1.0;
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

    public void driveRobotCentric(){
        double y = gamepad1.left_stick_y; // Remember, this is reversed!
        double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = -gamepad1.right_stick_x;
        boolean speedslow = gamepad1.right_bumper;
        double mag = speedslow ? 0.5 : 1.0;
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

    public void defaultCommand(boolean isFieldCentricTrue){
        if (!isFieldCentricTrue){
            driveRobotCentric();
        } else {
            driveFieldCentric();
        }
    }


}

