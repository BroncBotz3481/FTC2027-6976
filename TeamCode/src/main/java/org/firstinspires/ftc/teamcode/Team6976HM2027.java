package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.danpeled.msftc.GearBox;
import com.danpeled.msftc.Motor;
import com.danpeled.msftc.MotorType;
import com.danpeled.msftc.mechanisms.velocity.FlywheelMechanism;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Team6976HM2027 {
    public CRServo intake = null;
    public Motor flywheelMotor1 = null;
    public Motor flywheelMotor2 = null;

    public FlywheelMechanism shooter = null;
    public DcMotor DriveRightBack = null;
    public DcMotor DriveLeftBack = null;
    public DcMotor DriveLeftFront = null;
    public DcMotor DriveRightFront = null;

    public DcMotor shooterLeft = null;
    public DcMotor shooterRight = null;
    public DcMotor intakeMotor = null;
    public DcMotor transferMotor = null;

    public CRServo intakeLeft = null;
    public CRServo intakeRight = null;

    public Servo shooterGate = null;

    public double num = 0;




    HardwareMap hwMap = null;
    public void Map(HardwareMap hardwareMap)
    {
        hwMap = hardwareMap;
        intake = hwMap.get(CRServo.class, "Intake");
        num = 67;

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

        shooterLeft = hwMap.get(DcMotor.class,"shooterLeft");
        shooterRight = hwMap.get(DcMotor.class,"shooterRight");
        intakeMotor = hwMap.get(DcMotor.class, "intakeMotor");
        transferMotor = hwMap.get(DcMotor.class, "transferMotor");

        DriveLeftFront = hwMap.get(DcMotor.class,"DriveLeftFront");
        DriveRightFront = hwMap.get(DcMotor.class,"DriveRightFront");
        DriveLeftBack = hwMap.get(DcMotor.class,"DriveLeftBack");
        DriveRightBack = hwMap.get(DcMotor.class,"DriveRightBack");
        //   Lights = hwMap.get(RevBlinkinLedDriver.class,"Lights");

        intakeLeft = hwMap.get(CRServo.class, "intakeLeft");
        intakeRight = hwMap.get(CRServo.class, "intakeRight");
        shooterGate = hwMap.get(Servo.class, "shooterGate");

        DriveLeftFront.setDirection(DcMotor.Direction.FORWARD);
        DriveLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        DriveLeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        /* If you are using encoders, set to RUN_USING_ENCODER.
         If you put RUN_USING_ENCODERS and there is no encoder value getting
         received then the motor power is set to 1.0 even if you put a different
         value */
        DriveLeftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        DriveRightFront.setDirection(DcMotor.Direction.REVERSE);
        DriveRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        DriveRightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        DriveRightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        DriveLeftBack.setDirection(DcMotor.Direction.FORWARD);
        DriveLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        DriveLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        DriveLeftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        DriveRightBack.setDirection(DcMotor.Direction.REVERSE);
        DriveRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        DriveRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        DriveRightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        shooterLeft.setDirection(DcMotor.Direction.FORWARD);
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        shooterRight.setDirection(DcMotor.Direction.REVERSE);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intakeMotor.setDirection(DcMotor.Direction.FORWARD);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        transferMotor.setDirection(DcMotor.Direction.FORWARD);
        transferMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        transferMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        transferMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intakeLeft.setDirection(CRServo.Direction.FORWARD);

        intakeRight.setDirection(CRServo.Direction.REVERSE);

    }
}
