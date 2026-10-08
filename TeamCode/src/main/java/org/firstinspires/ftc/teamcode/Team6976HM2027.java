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
import com.qualcomm.robotcore.hardware.Servo;

public class Team6976HM2027 {

    public CRServo intake = null;
    public Motor flywheelMotor1 = null;
    public Motor flywheelMotor2 = null;

    public DcMotor DriveRightBack = null;
    public DcMotor DriveLeftBack = null;
    public DcMotor DriveLeftFront = null;
    public DcMotor DriveRightFront = null;

    //mild nitpick but camel casing for the rest of the robot is preferred. Optional
    public DcMotor ShooterLeft = null;
    public DcMotor ShooterRight = null;
    public DcMotor IntakeMotor = null;
    public DcMotor Transfer = null;
    public CRServo IntakeLeft = null;
    public CRServo IntakeRight = null;

    public Servo ShooterGate = null;

    public double num = 0;




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

        num = 67;
        DriveLeftFront = hwMap.get(DcMotor.class,"DriveLeftFront");
        DriveRightFront = hwMap.get(DcMotor.class,"DriveRightFront");
        DriveLeftBack = hwMap.get(DcMotor.class,"DriveLeftBack");
        DriveRightBack = hwMap.get(DcMotor.class,"DriveRightBack");
        ShooterLeft = hwMap.get(DcMotor.class,"ShooterLeft");
        ShooterRight = hwMap.get(DcMotor.class,"ShooterRight");
        IntakeMotor = hwMap.get(DcMotor.class,"Intake");
        Transfer = hwMap.get(DcMotor.class,"Transfer");
        IntakeLeft = hwMap.get(CRServo.class,"IntakeLeftServo");
        IntakeRight = hwMap.get(CRServo.class,"IntakeRightServo");
        ShooterGate = hwMap.get(Servo.class,"ShooterGateServo");
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

        ShooterLeft.setDirection(DcMotor.Direction.REVERSE);
        ShooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ShooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ShooterLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ShooterRight.setDirection(DcMotor.Direction.FORWARD);
        ShooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ShooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ShooterRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        IntakeMotor.setDirection(DcMotor.Direction.FORWARD);
        IntakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        IntakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        IntakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Transfer.setDirection(DcMotor.Direction.FORWARD);
        Transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Transfer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        IntakeLeft.setDirection(CRServo.Direction.FORWARD);

        IntakeRight.setDirection(DcMotorSimple.Direction.REVERSE);



    }
}
