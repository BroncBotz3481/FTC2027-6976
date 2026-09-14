package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous (name="Team4008AutoEncoderBased", group="4008")
public class Team4008AutoTimeBased extends LinearOpMode {

    Team4008HM2025 robot = new Team4008HM2025();
    ElapsedTime Time = new ElapsedTime();

    @Override
    public void runOpMode() {
        robot.Map(hardwareMap);
        sleep(3000);
        waitForStart();

        moveForward(0.5,1000); // This powers all the motors to move the robot forward for 1000 milseconds
    }

    public void moveForward (double power, int time){
        robot.DriveRightFront.setPower(power);
        robot.DriveLeftFront.setPower(power);
        robot.DriveRightBack.setPower(power);
        robot.DriveLeftBack.setPower(power);
        sleep(time);
        robot.DriveRightFront.setPower(0);
        robot.DriveLeftFront.setPower(0);
        robot.DriveRightBack.setPower(0);
        robot.DriveLeftBack.setPower(0);
    }
}