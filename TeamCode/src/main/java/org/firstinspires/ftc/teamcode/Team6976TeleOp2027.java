package org.firstinspires.ftc.teamcode;

import com.danpeled.msftc.GlobalTelemetry;
import com.danpeled.msftc.dashboard.TunableNumber;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp
public class Team6976TeleOp2027 extends OpMode {
    Team6976HM2027 robot = new Team6976HM2027();
    TunableNumber turretPose = new TunableNumber(this,"TurretPose", 0);;
    private double addedPower = 450;
    int index = 1;
    double low = 600;
    double mid = 670;
    double high = 750;
    DriveTrainSubsystem drivetrain = new DriveTrainSubsystem(robot);
    private double[] speed = {low + addedPower, mid + addedPower, high + addedPower};



    double F = 0;TunableNumber kP = new TunableNumber(this,"PF", 19);

    TunableNumber kS = new TunableNumber(this,"SF", 10);;

    TunableNumber kPB = new TunableNumber(this,"PB", 19);

    TunableNumber kSB = new TunableNumber(this,"SB", 14);;
    double P = 0;
    double[] stepSizes = {10, 1, 0.1, 0.01, 0.001, 0.0001};
    double[] VelocitystepSizes = {1000, 100, 100, 10, 0.001, 0.0001};
    int stepIndex = 1;
    int VelocitystepIndexstepIndex = 1;
    double isNeg = 1;

    boolean fieldCentric = false;


    @Override
    public void init() {
        robot.Map(hardwareMap);
        telemetry.addLine("Init Complete");

        double p = 20;
        double ks = 18.90;

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

        robot.TurretServo.setPosition(0.5);
    }

    @Override
    public void loop() {

        robot.backShooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(kP.getValueAsDouble(), 0, 0, kS.getValueAsDouble()));
        robot.frontShooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(kPB.getValueAsDouble(), 0, 0, kSB.getValueAsDouble()));
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

        telemetry(fieldCentric, mag); //pushes telementry to Driver Station

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

        if(gamepad1.y){
            addedPower += 50;
        }
        if (gamepad1.a){
            addedPower -= 50;
        }

        if (gamepad2.rightBumperWasPressed()){
            isNeg = isNeg * -1;
        }



        if (gamepad2.b){
            robot.TransferMotor.setPower(1 * isNeg);
        } else{
            robot.TransferMotor.setPower(0 * isNeg);
        }
        if (gamepad2.a){
            robot.TransferLeft.setPower(1 * isNeg);
            robot.TransferRight.setPower(-1 * isNeg);
        }else {
            robot.TransferLeft.setPower(0);
            robot.TransferRight.setPower(0);
        }

        if (gamepad2.x) {
            robot.IntakeMotor.setPower(1 * isNeg);
        } else{
            robot.IntakeMotor.setPower(0);
        }

        if (gamepad2.right_trigger_pressed){
            robot.frontShooter.setVelocity(speed[index] + addedPower);
            robot.backShooter.setVelocity(-speed[index] + addedPower);
        }
        robot.frontShooter.setPower(0);
        robot.backShooter.setPower(-0);


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





    }

    public void telemetry(boolean fieldCentric, double driveSpeed) {

        double targetSpeed = speed[index] + addedPower;

        // ==========================================
        // TEAM HEADER
        // ==========================================

        telemetry.addLine("================================");
        telemetry.addLine("       TEAM 6976 | TELEOP");
        telemetry.addLine("================================");

        // ==========================================
        // GAMEPAD 1 - DRIVER CONTROLS
        // ==========================================

        telemetry.addLine("");
        telemetry.addLine("========== GAMEPAD 1 ==========");
        telemetry.addLine("            DRIVER");
        telemetry.addLine("--------------------------------");

        telemetry.addLine("[LEFT STICK]  Drive / Strafe");
        telemetry.addLine("[RIGHT STICK] Rotate");

        telemetry.addLine("[L3 + R3]     Toggle Field Centric");
        telemetry.addLine("[RB]          Slow Mode (50%)");
        telemetry.addLine("[OPTIONS]     Reset IMU Heading");

        telemetry.addLine("[DPAD UP]     Drive Forward");
        telemetry.addLine("[DPAD DOWN]   Drive Backward");
        telemetry.addLine("[DPAD LEFT]   Strafe Left");
        telemetry.addLine("[DPAD RIGHT]  Strafe Right");

        telemetry.addLine("[Y]           Shooter Offset +50");
        telemetry.addLine("[A]           Shooter Offset -50");

        telemetry.addLine("--------------------------------");

        telemetry.addData("Drive Mode",
                fieldCentric ? "FIELD CENTRIC" : "ROBOT CENTRIC");

        telemetry.addData("Drive Speed",
                "%.0f%%", driveSpeed * 100);

        telemetry.addData("Shooter Offset",
                "%.0f TPS", addedPower);

        // ==========================================
        // GAMEPAD 2 - OPERATOR CONTROLS
        // ==========================================

        telemetry.addLine("");
        telemetry.addLine("========== GAMEPAD 2 ==========");
        telemetry.addLine("           OPERATOR");
        telemetry.addLine("--------------------------------");

        telemetry.addLine("[X]           Low Shooter Preset");
        telemetry.addLine("[Y]           Mid Shooter Preset");
        telemetry.addLine("[B]           High Shooter Preset");

        telemetry.addLine("[RT]          Shoot");
//        telemetry.addLine("[RB]          Kick");

        telemetry.addLine("[LT]          Intake");
        telemetry.addLine("[LB]          Outtake");

//        telemetry.addLine("[DPAD UP]     Turret Tunable Position");
        telemetry.addLine("[DPAD DOWN]   Turret Position 0");
        telemetry.addLine("[DPAD LEFT]   Turret Position 0.5");
        telemetry.addLine("[DPAD RIGHT]  Turret Position 1");

        telemetry.addLine("--------------------------------");

        telemetry.addData("Selected Preset",
                index == 0 ? "LOW" :
                        index == 1 ? "MID" : "HIGH");

        telemetry.addData("Shoot",
                gamepad2.right_trigger >= 0.5 ? "ACTIVE" : "OFF");

//        telemetry.addData("Kick",
//                gamepad2.right_bumper ? "ACTIVE" : "OFF");

        telemetry.addData("Intake",
                gamepad2.left_trigger >= 0.5 ? "ACTIVE" : "OFF");

        telemetry.addData("Outtake",
                gamepad2.left_bumper ? "ACTIVE" : "OFF");

        // ==========================================
        // SHOOTER STATUS
        // ==========================================

        telemetry.addLine("");
        telemetry.addLine("========== SHOOTER STATUS ==========");

        telemetry.addData("Target Velocity",
                "%.1f TPS", targetSpeed);

        telemetry.addData("Front Velocity",
                "%.1f TPS", robot.frontShooter.getVelocity());

        telemetry.addData("Back Velocity",
                "%.1f TPS", robot.backShooter.getVelocity());

        telemetry.addData("Front Velocity Error",
                "%.1f TPS",
                targetSpeed - robot.frontShooter.getVelocity());

        telemetry.addData("Back Velocity Error",
                "%.1f TPS",
                targetSpeed - robot.backShooter.getVelocity());

        // ==========================================
        // TURRET STATUS
        // ==========================================

        telemetry.addLine("");
        telemetry.addLine("========== TURRET STATUS ==========");

        telemetry.addData("Current Position",
                "%.3f", robot.TurretServo.getPosition());

        telemetry.addData("Tunable Position",
                "%.3f", turretPose.getValueAsDouble());

        // ==========================================
        // DRIVETRAIN STATUS
        // ==========================================

        telemetry.addLine("");
        telemetry.addLine("========== DRIVETRAIN ==========");

        telemetry.addData("Front Left",
                robot.DriveLeftFront.getCurrentPosition());

        telemetry.addData("Front Right",
                robot.DriveRightFront.getCurrentPosition());

        telemetry.addData("Back Left",
                robot.DriveLeftBack.getCurrentPosition());

        telemetry.addData("Back Right",
                robot.DriveRightBack.getCurrentPosition());

        telemetry.addLine("================================");

        telemetry.update();
    }
}