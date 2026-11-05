package org.firstinspires.ftc.teamcode;


import com.danpeled.msftc.VelocityUnit;

public class RobotSystems {
    private Team6976HM2027 robot;

    public RobotSystems (Team6976HM2027 robot){
        this.robot = robot;
    }

    public void shoot(double TPS){
//        robot.backShooter.setVelocity(TPS);
//        robot.frontShooter.setVelocity(TPS);
        robot.shooter.setSetpoint(TPS);

        if(Math.abs(robot.shooter.getVelocity(VelocityUnit.TicksPerSecond)- TPS) < 300){
            robot.TransferRight.setPower(-1);
            robot.TransferLeft.setPower(1);
            robot.TransferMotor.setPower(1);
            kick();
        }
    }

    public void intake(double power){
        robot.IntakeMotor.setPower(power);
        robot.TransferRight.setPower(-power);
        robot.TransferLeft.setPower(power);
        robot.TransferMotor.setPower(power);
    }

    public void kick(){
        robot.kicker.setPosition(1);
        if(Math.abs(robot.kicker.getPosition() - 1) < .1){
            robot.kicker.setPosition(0);
        }
    }

    public void transfer(){
        robot.TransferRight.setPower(1);
        robot.TransferLeft.setPower(-1);
        robot.TransferMotor.setPower(1);
    }

    public void outtake(double power){
        robot.IntakeMotor.setPower(-power);
        robot.TransferRight.setPower(-power);
        robot.TransferLeft.setPower(power);
        robot.TransferMotor.setPower(-power);
    }
}
