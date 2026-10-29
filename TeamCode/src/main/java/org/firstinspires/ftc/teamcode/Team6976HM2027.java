package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.danpeled.msftc.GearBox;
import com.danpeled.msftc.Motor;
import com.danpeled.msftc.MotorType;
import com.danpeled.msftc.mechanisms.velocity.FlywheelMechanism;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

public class Team6976HM2027 {

    public CRServo intake = null;
    public Servo TurretServo = null;
    public FlywheelMechanism shooter = null;
    public Motor flywheelMotor1 = null;
    public Motor flywheelMotor2 = null;

    public DcMotor DriveRightBack = null;
    public DcMotor DriveLeftBack = null;
    public DcMotor DriveLeftFront = null;
    public DcMotor DriveRightFront = null;

    public CRServo Turret = null;
    public DcMotor IntakeMotor = null;
    public CRServo TransferLeft = null;
    public CRServo TransferRight = null;
    public CRServo IntakeLeft = null;
    public CRServo IntakeRight = null;

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

        intake = hwMap.get(CRServo.class, "Intake");

        flywheelMotor2 = new Motor("FrontFlywheel")
                .ofType(MotorType.GOBILDA_6000)
                .withZeroPowerBehaviour(DcMotor.ZeroPowerBehavior.BRAKE)
                .withGearBox(GearBox.fromOutputRPM(6000, 60))
                .withHardCurrentLimit(20)
                .withSoftCurrentLimit(10)
                .withCurrentLimitEnabled()
                .enableEncoder()
                .withDirection(DcMotorSimple.Direction.FORWARD);

        flywheelMotor1 = new Motor("BackFlywheel")
                .ofType(MotorType.GOBILDA_6000)
                .withZeroPowerBehaviour(DcMotor.ZeroPowerBehavior.BRAKE)
                .withGearBox(GearBox.fromOutputRPM(6000, 60))
                .withHardCurrentLimit(20)
                .withSoftCurrentLimit(10)
                .withCurrentLimitEnabled()
                .enableEncoder()
                .withFollowers(flywheelMotor2)
                .withDirection(DcMotorSimple.Direction.REVERSE);

        shooter = new FlywheelMechanism("flywheel", flywheelMotor1)
                .withLimits(0, 100)
                .withPID(new PIDController(0.1, 0, 0))
                .withFeedforward(new SimpleMotorFeedforward(0, 0, 0));

        num = 67;
        DriveLeftFront = hwMap.get(DcMotor.class,"DriveLeftFront");
        DriveRightFront = hwMap.get(DcMotor.class,"DriveRightFront");
        DriveLeftBack = hwMap.get(DcMotor.class,"DriveLeftBack");
        DriveRightBack = hwMap.get(DcMotor.class,"DriveRightBack");

        IntakeMotor = hwMap.get(DcMotor.class,"Intake");
        TransferLeft = hwMap.get(CRServo.class,"TransferLeft");
        TransferRight = hwMap.get(CRServo.class, "TransferRIght");
        IntakeLeft = hwMap.get(CRServo.class,"IntakeLeftServo");
        IntakeRight = hwMap.get(CRServo.class,"IntakeRightServo");
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

        TransferLeft.setDirection(CRServo.Direction.FORWARD);
        TransferRight.setDirection(CRServo.Direction.REVERSE);


        IntakeLeft.setDirection(CRServo.Direction.FORWARD);

        IntakeRight.setDirection(DcMotorSimple.Direction.REVERSE);



    }
}
