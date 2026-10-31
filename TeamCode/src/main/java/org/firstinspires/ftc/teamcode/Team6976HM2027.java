package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.danpeled.msftc.GearBox;
import com.danpeled.msftc.Motor;
import com.danpeled.msftc.MotorType;

import com.danpeled.msftc.dashboard.TunableNumber;
import com.danpeled.msftc.mechanisms.velocity.FlywheelMechanism;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

public class Team6976HM2027 {

    public Servo TurretServo = null;
    public FlywheelMechanism shooter = null;
//    public Motor flywheelMotor1 = null;
//    public Motor flywheelMotor2 = null;

    public DcMotorEx frontShooter = null;
    public DcMotorEx backShooter = null;


    public DcMotor DriveRightBack = null;
    public DcMotor DriveLeftBack = null;
    public DcMotor DriveLeftFront = null;
    public DcMotor DriveRightFront = null;

    public DcMotor IntakeMotor = null;
    public CRServo TransferLeft = null;
    public CRServo TransferRight = null;
    public DcMotor TransferMotor = null;

    public Servo kicker = null;

    public double num = 0;

    public IMU imu = null;


    HardwareMap hwMap = null;
    public void Map(HardwareMap hardwareMap)
    {


        hwMap = hardwareMap;

        // Retrieve the IMU from the hardware map
        imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        frontShooter = hwMap.get(DcMotorEx.class, "FrontFlywheel");
        backShooter = hwMap.get(DcMotorEx.class, "BackFlywheel");

//        flywheelMotor2 = new Motor("FrontFlywheel")
//                .ofType(MotorType.GOBILDA_6000)
//                .withZeroPowerBehaviour(DcMotor.ZeroPowerBehavior.BRAKE)
//                .withGearBox(GearBox.fromOutputRPM(6000, 6000))
//                .withHardCurrentLimit(20)
//                .withSoftCurrentLimit(10)
//                .withCurrentLimitEnabled()
//                .enableEncoder()
//                .withDirection(DcMotorSimple.Direction.FORWARD);
//        flywheelMotor1 = new Motor("BackFlywheel")
//                .ofType(MotorType.GOBILDA_6000)
//                .withZeroPowerBehaviour(DcMotor.ZeroPowerBehavior.FLOAT)
//                .withGearBox(GearBox.fromOutputRPM(6000, 6000))
//                .withHardCurrentLimit(20)
//                .withSoftCurrentLimit(10)
//                .withCurrentLimitEnabled()
//                .enableEncoder()
//                .withFollowers(flywheelMotor2)
//                .withDirection(DcMotorSimple.Direction.REVERSE);
//
//        shooter = new FlywheelMechanism("flywheel", flywheelMotor1)
//
//                .withPID(new PIDController(0.1, 0, 0))
//                .withFeedforward(new SimpleMotorFeedforward(0, 0, 0));

        num = 67;
        DriveLeftFront = hwMap.get(DcMotor.class,"DriveLeftFront");
        DriveRightFront = hwMap.get(DcMotor.class,"DriveRightFront");
        DriveLeftBack = hwMap.get(DcMotor.class,"DriveLeftBack");
        DriveRightBack = hwMap.get(DcMotor.class,"DriveRightBack");

        IntakeMotor = hwMap.get(DcMotor.class,"Intake");
        TransferMotor = hwMap.get(DcMotor.class, "Transfer");
        TransferLeft = hwMap.get(CRServo.class,"TransferLeft");
        TransferRight = hwMap.get(CRServo.class, "TransferRight");

        kicker = hwMap.get(Servo.class,"kicker");
        TurretServo = hwMap.get(Servo.class, "TurretServo");
        //   Lights = hwMap.get(RevBlinkinLedDriver.class,"Lights");

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

        IntakeMotor.setDirection(DcMotor.Direction.FORWARD);
        IntakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        IntakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        IntakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        TransferMotor.setDirection(DcMotor.Direction.FORWARD);
        TransferMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        TransferMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TransferMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontShooter.setDirection(DcMotorEx.Direction.FORWARD);
        frontShooter.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontShooter.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontShooter.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        frontShooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(1.8246, 0, 0, 0.01));

        backShooter.setDirection(DcMotorEx.Direction.REVERSE);
        backShooter.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backShooter.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backShooter.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backShooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(1.8246, 0, 0, 0.01));


    }
}
