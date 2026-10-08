package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Team6976TeleOp2027", group = "6976")
public class Team6976TeleOp2027 extends LinearOpMode {
    Team6976HM2027 robot = new Team6976HM2027();


    @Override
    public void runOpMode() {
        robot.Map(hardwareMap);
        telemetry.addData("Say", "TeleOp Starting");
        telemetry.update();

        robot.DriveRightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.DriveRightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.DriveLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.DriveLeftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.DriveRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.DriveRightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.DriveLeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.DriveLeftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //servos here. Ex: robot.gate.setPosition(0); When robot starts, the gate will be set to close

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

            if (gamepad2.right_trigger >= 0.5f){ //They return floats
                robot.ShooterLeft.setPower(0.5);
                robot.ShooterRight.setPower(0.5); //These shooter motors will spin in the same direction because you set one of them to reverse in the HW
                robot.Transfer.setPower(0.5);
                robot.ShooterGate.setPosition(1);
            }
            //Set motor powers to 0, rn the motors will run forever and ever cuz you never told it to go back to 0 power
            //close the gate

            if (gamepad2.left_trigger >= 0.5f){
                robot.IntakeLeft.setPower(1); //Intakes will spin in the same direction because one is reversed in HW
                robot.IntakeRight.setPower(1);
                robot.IntakeMotor.setPower(0.5);
                robot.Transfer.setPower(0.5);
                //Intake motor + transfer will also be spinning
            }
            //set servo powers to 0
            robot.ShooterLeft.setPower(0);
            robot.ShooterRight.setPower(0);
            robot.Transfer.setPower(0);
            robot.ShooterGate.setPosition(0);
            robot.IntakeLeft.setPower(0);

            if (gamepad1.a) {
                robot.shooter.setSetpoint(target1.getValueAsDouble());
            }
            robot.shooter.setSetpoint(target2.getValueAsDouble());

            //Optional Challenge, make seperate methods for intaking() and shooting()
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


    public void shooting(double power) {
        robot.ShooterLeft.setPower(0.5);
        robot.ShooterRight.setPower(0.5);
        robot.Transfer.setPower(0.5);
        robot.ShooterGate.setPosition(1);
    }

    public void intaking(double power) {
        robot.IntakeLeft.setPower(1);
        robot.IntakeRight.setPower(1);
        robot.IntakeMotor.setPower(0.5);
        robot.Transfer.setPower(0.5);
    }
}

