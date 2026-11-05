package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;

import com.danpeled.msftc.GlobalTelemetry;
import com.danpeled.msftc.VelocityUnit;
import com.danpeled.msftc.dashboard.TunableNumber;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

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

    TunableNumber defaultShooterSpeed = new TunableNumber(this,"Default Speed", 0);

    TunableNumber turretPose = new TunableNumber(this,"TurretPose", 0);;
    double p = 5;
    double ks = 7;
    @Override
    public void runOpMode() {




        robot.Map(hardwareMap);
        telemetry.addData("Say", "TeleOp Starting");
        telemetry.update();

        GlobalTelemetry.init(telemetry);

//        robot.flywheelMotor1.queryMotor(hardwareMap);
//        robot.flywheelMotor2.queryMotor(hardwareMap);
//
//        robot.shooter.initConfig();


        robot.DriveRightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.DriveRightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.DriveLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.DriveLeftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.DriveRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.DriveRightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.DriveLeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.DriveLeftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.frontShooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(p,0,0,ks));
        robot.backShooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(p,0,0,ks));

        robot.TurretServo.setPosition(0.5);

        //servos here. Ex: robot.gate.setPosition(0); When robot starts, the gate will be set to close
        boolean fieldCentric = false;

        waitForStart();

        while (opModeIsActive()) {

            boolean speedslow = gamepad1.right_bumper;
            double mag = speedslow ? 0.5 : 1.0;

            if (gamepad1.left_stick_button && gamepad1.right_stick_button) {
                fieldCentric = !fieldCentric;
            }

            drivetrain.defaultCommand(
                    fieldCentric,
                    gamepad1.left_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_stick_x,
                    mag
            ); //if true, field centric, if false, robot centric

            if (gamepad1.options) {
                robot.imu.resetYaw();
            }

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
                if (gamepad2.left_bumper) {
                    commands.outtake(1);
                }

                //set servo powers to 0
                robot.IntakeMotor.setPower(0);
                robot.TransferRight.setPower(0);
                robot.TransferLeft.setPower(0);
                robot.kicker.setPosition(0);
                robot.TransferMotor.setPower(0);
//                robot.shooter.setSetpoint(defaultShooterSpeed.getValueAsDouble());
//                robot.shooter.update();
            }
        }


    public void telemetry(){
        telemetry.addData("RightFront", robot.DriveRightFront.getCurrentPosition());
        telemetry.addData("RightBack", robot.DriveRightBack.getCurrentPosition());
        telemetry.addData("LeftFront", robot.DriveLeftFront.getCurrentPosition());
        telemetry.addData("LeftBack", robot.DriveLeftBack.getCurrentPosition());

        telemetry.addData("ShooterSpeed", robot.frontShooter.getVelocity());
//        telemetry.addData("ShooterSpeed", robot.shooter.getVelocity(VelocityUnit.TicksPerSecond));
        telemetry.addData("ShooterSetpoint", speed[index]);
        telemetry.addData("Turret Pose", robot.TurretServo.getPosition());
//        telemetry.addData("flywheelfront", robot.flywheelMotor1.getVelocity(VelocityUnit.TicksPerSecond));
        telemetry.update();
    }


}

