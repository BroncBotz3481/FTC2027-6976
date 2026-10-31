package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.danpeled.msftc.GearBox;
import com.danpeled.msftc.Motor;
import com.danpeled.msftc.MotorType;
import com.danpeled.msftc.mechanisms.velocity.FlywheelMechanism;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Team6976HMap2027 {

    public CRServo intake = null;
    public Motor flywheelMotor1 = null;
    public Motor flywheelMotor2 = null;


    public FlywheelMechanism shooter = null;

    public DcMotor DriveRightBack = null;

    public DcMotor DriveLeftBack = null;

    public DcMotor DriveLeftFront = null;

    public DcMotor DriveRightFront = null;
    HardwareMap hwMap = null;
    public void Map(HardwareMap hardwareMap)
    {
        hwMap = hardwareMap;
        intake = hwMap.get(CRServo.class, "Intake");

        flywheelMotor1 = new Motor("flywheelMotor2")
                .ofType(MotorType.GOBILDA_6000)
                .withZeroPowerBehaviour(DcMotor.ZeroPowerBehavior.BRAKE)
                .withGearBox(GearBox.fromOutputRPM(6000, 60))
                .withHardCurrentLimit(20)
                .withSoftCurrentLimit(10)
                .withCurrentLimitEnabled()
                .enableEncoder()
                .withDirection(DcMotorSimple.Direction.FORWARD);

        flywheelMotor1 = new Motor("flywheelMotor1")
                .ofType(MotorType.GOBILDA_6000)
                .withZeroPowerBehaviour(DcMotor.ZeroPowerBehavior.BRAKE)
                .withGearBox(GearBox.fromOutputRPM(6000, 60))
                .withHardCurrentLimit(20)
                .withSoftCurrentLimit(10)
                .withCurrentLimitEnabled()
                .enableEncoder()
                .withFollowers(flywheelMotor2)
                .withDirection(DcMotorSimple.Direction.FORWARD);

        shooter = new FlywheelMechanism("flywheel", flywheelMotor1)
                .withLimits(0, 100)
                .withPID(new PIDController(0.1, 0, 0))
                .withFeedforward(new SimpleMotorFeedforward(0, 0, 0));

        DriveLeftFront = hwMap.get(DcMotor.class,"DriveLeftFront");
        DriveRightFront = hwMap.get(DcMotor.class,"DriveRightFront");
        DriveLeftBack = hwMap.get(DcMotor.class,"DriveLeftBack");
        DriveRightBack = hwMap.get(DcMotor.class,"DriveRightBack");

        DriveLeftFront.setDirection(DcMotor.Direction.FORWARD);
        DriveLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        DriveLeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        DriveLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        DriveRightFront.setDirection(DcMotor.Direction.REVERSE);
        DriveRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        DriveRightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        DriveRightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        DriveLeftBack.setDirection(DcMotor.Direction.FORWARD);
        DriveLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        DriveLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        DriveLeftBack.  setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        DriveRightBack.setDirection(DcMotor.Direction.REVERSE);
        DriveRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        DriveRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        DriveRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

}
