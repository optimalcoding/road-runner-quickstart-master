package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.PathBuilder;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.MecanumDrive;


@Config
@Autonomous(name = "RedClose",group = "Autonomous")
public class RedClose extends LinearOpMode {

    /*  public class launcher {
          private DcMotorEx launcher;
          public launcher(HardwareMap hardwareMap) {
              launcher = hardwareMap.get(DcMotorEx.class, "launcher");
              launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
          }
      }

      public class feeder {
          private DcMotorEx feeder;
          public feeder(HardwareMap hardwareMap){
              feeder = hardwareMap.get(DcMotorEx.class,"feeder");
              feeder.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));
          }
      }

      public class intake {
          private DcMotorEx intake;
          public intake(HardwareMap hardwareMap){
              intake = hardwareMap.get(DcMotorEx.class,"intake");
              intake.setZeroPowerBehavior((DcMotor.ZeroPowerBehavior.BRAKE));
          }
      }
  */
    // Initialize Subsystems
    Robot robot = new Robot();

    // Constants from your BlueAutoA
    final double LAUNCHER_VEL = 1250;
    final double FEEDER_VEL = -5000;
    final double INTAKE_VEL = -1250;

    // --- Subsystem Actions (Encapsulating BlueAutoA logic) ---
    public Action setLauncher(double vel) { return packet -> { robot.launcher.setVelocity(vel); return false; }; }
    public Action setFeeder(double vel) { return packet -> { robot.feeder.setVelocity(vel); return false; }; }
    public Action setIntake(double vel) { return packet -> { robot.intake.setVelocity(vel); return false; }; }

    // Optimized Launch Routine
    public Action launchRoutine() {
        return new SequentialAction(
                setLauncher(LAUNCHER_VEL),
                new SleepAction(1),
                setIntake(INTAKE_VEL),
                new SleepAction(0.2),
                setFeeder(FEEDER_VEL),
                new SleepAction(1.8),
                new ParallelAction(setLauncher(0), setIntake(0), setFeeder(0))
        );
    }

    public Action captureBalls() {
        return new SequentialAction(
                new ParallelAction(setLauncher(-LAUNCHER_VEL), setFeeder(-FEEDER_VEL)), // Reverse briefly
                new SleepAction(0.2),
                new ParallelAction(setLauncher(0), setFeeder(0))      // Then stop
        );
    }

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        // Start Pose based on your MeepMeep (-52, -53, 45 degrees)
        Pose2d startPose = new Pose2d(-52, 53, Math.toRadians(-45));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

       /* TrajectoryActionBuilder move1 = drive.actionBuilder(startPose)
                        .strafeToLinearHeading(new Vector2d(-33,-33),Math.toRadians(45));

                Action trajend = move1.endTrajectory().build();
          */



        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                        /*move1
                        .stopAndAdd(launchRoutine())
                        .build()
                        */
                drive.actionBuilder(startPose)
                        // --- CYCLE 1: PRELOADS ---

                        .strafeTo(new Vector2d(-33, 33))

                        .stopAndAdd(launchRoutine())

                        // --- CYCLE 2: PICKUP 1 (With Stopper) ---
                        // 1. Move quickly to the "Stopper" (2 inches away from ball)
                        .strafeToLinearHeading(new Vector2d(-13, 20), Math.toRadians(90))

                        // 2. Start intake and crawl the last 2 inches to the ball
                        .afterTime(0, new ParallelAction(setFeeder(FEEDER_VEL), setIntake(INTAKE_VEL)))
                        .strafeToLinearHeading(new Vector2d(-13, 55), Math.toRadians(90))
                        .stopAndAdd(new ParallelAction(setFeeder(0),setIntake(0)))
                        .stopAndAdd(captureBalls())

                        // 3. Return to Launch Spot

                        .strafeToLinearHeading(new Vector2d(-30, 30), Math.toRadians(-40))

                        .stopAndAdd(new SleepAction(0.1))

                        .stopAndAdd(launchRoutine())

                        // --- CYCLE 3: PICKUP 2 (With Stopper) ---
                        // 1. Approach the diagonal artifacts (The "Stopper" point)
                        .strafeToLinearHeading(new Vector2d(16, 25), Math.toRadians(90))

                        // 2. Slow slide into the artifacts
                        .afterTime(0, new ParallelAction(setFeeder(FEEDER_VEL), setIntake(INTAKE_VEL)))
                        .strafeToLinearHeading(new Vector2d(14, 55), Math.toRadians(90))
                        .stopAndAdd(new ParallelAction(setFeeder(0),setIntake(0)))
                        .stopAndAdd(captureBalls())

                        // 3. Final Launch alignment
                        .lineToYConstantHeading(33)
                        .strafeToLinearHeading(new Vector2d(-30, 30), Math.toRadians(-45))

                        .stopAndAdd(new SleepAction(0.1))

                        .stopAndAdd(launchRoutine())

                        .strafeToLinearHeading(new Vector2d(34,20),Math.toRadians(-90))
                        .afterTime(0, new ParallelAction(setFeeder(FEEDER_VEL), setIntake(INTAKE_VEL)))
                        .strafeToLinearHeading(new Vector2d(34,55),Math.toRadians(-90))
                        .stopAndAdd(new ParallelAction(setFeeder(0), setIntake(0)))
                        .stopAndAdd(captureBalls())


                        .strafeToLinearHeading(new Vector2d(-30, 30), Math.toRadians(-40))

                        .stopAndAdd(new SleepAction(0.1))
                        .stopAndAdd(launchRoutine())



                        // Park / Final Move
                        .strafeToLinearHeading(new Vector2d(-16, 37), Math.toRadians(-45))
                        .build()


        );
    }
}
