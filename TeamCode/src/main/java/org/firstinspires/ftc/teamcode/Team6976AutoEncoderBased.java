package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous (name="Team6976AutoEncoderBased", group="6976")
public class Team4008AutoEncoderBased extends LinearOpMode {

    Team6976HM2027 robot = new Team6976HM2027();
    ElapsedTime Time = new ElapsedTime();


    @Override
    public void runOpMode() {
        robot.Map(hardwareMap);

        telemetry.update();
        waitForStart();

        Time.reset();
        driveForward(6, 0.5, 2500); //setting a time allows for the command to time out if it takes too long
    }

    //Converts ticks to inches
    //https://firstroboticsbc.org/ftc/ftc-team-resources/autonomous-encoder-based-movement/
    public double inchesToTicks (double inches){
        return (inches * 537.7)/(2 * Math.PI * 1.9);
    }

    public void driveForward(double inches, double power, int time) {

        robot.DriveRightFront.setPower(power);
        robot.DriveLeftFront.setPower(power);
        robot.DriveRightBack.setPower(power);
        robot.DriveLeftBack.setPower(power);

        while (opModeIsActive() && Time.milliseconds() < time
                && robot.DriveLeftFront.getCurrentPosition() < inchesToTicks(inches)
                && robot.DriveRightBack.getCurrentPosition() < inchesToTicks(inches))
        {
            telemetry.addData("Encoder Val", robot.DriveLeftFront.getCurrentPosition());
        }
        robot.DriveRightFront.setPower(0);
        robot.DriveLeftFront.setPower(0);
        robot.DriveRightBack.setPower(0);
        robot.DriveLeftBack.setPower(0);

        robot.DriveLeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //Stops and resets the encoder to the 0 value for next use
        robot.DriveLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Time.reset();

    }
//if power is negative in the argument, it will turn the other way
    public void turn(int ticks, double power, int sleep) {
        robot.DriveLeftBack.setTargetPosition(-ticks);
        robot.DriveLeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.DriveLeftBack.setPower(-power);

        robot.DriveRightBack.setTargetPosition(ticks);
        robot.DriveRightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.DriveRightBack.setPower(power);

        robot.DriveLeftFront.setTargetPosition(-ticks);
        robot.DriveLeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.DriveLeftFront.setPower(-power);

        robot.DriveRightFront.setTargetPosition(ticks);
        robot.DriveRightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.DriveRightFront.setPower(power);
        sleep(sleep);
    }

}