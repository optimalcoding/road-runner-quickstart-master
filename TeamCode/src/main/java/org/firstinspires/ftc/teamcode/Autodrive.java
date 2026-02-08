package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "16557_RR1_Coordinate_Master")
public class Autodrive extends LinearOpMode {

    Robot robot = new Robot();

    // The "Launch Station" coordinate from your Pedro snippet
    Vector2d launchStation = new Vector2d(48.139, 95.494);

    // --- Subsystem Action Helpers ---
    public Action setLauncher(double vel) { return packet -> { robot.launcher.setVelocity(vel); return false; }; }
    public Action setFeeder(double vel) { return packet -> { robot.feeder.setVelocity(vel); return false; }; }
    public Action setIntake(double vel) { return packet -> { robot.intake.setVelocity(vel); return false; }; }

    // --- The Automated Launch Routine ---
    public Action launchRoutine() {
        return new SequentialAction(
                setLauncher(1250),
                new com.acmerobotics.roadrunner.SleepAction(1.5),
                setFeeder(-5000),
                new com.acmerobotics.roadrunner.SleepAction(0.05),
                setFeeder(0),
                new com.acmerobotics.roadrunner.SleepAction(0.5),
                setIntake(-1250),
                new com.acmerobotics.roadrunner.SleepAction(0.2),
                setFeeder(-5000),
                new com.acmerobotics.roadrunner.SleepAction(2.0),
                setLauncher(0), setIntake(0), setFeeder(0)
        );
    }

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        // Start Pose (Path1 start)
        Pose2d startPose = new Pose2d(21.161, 122.700, Math.toRadians(-45));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        waitForStart();

        // Execution of the full path sequence with launches at the specific coordinate
        Actions.runBlocking(
                new SequentialAction(
                        // 1. Move to Launch Station (Path 1)
                        drive.actionBuilder(startPose)
                                .strafeToLinearHeading(launchStation, Math.toRadians(-45))
                                .build(),

                        // 2. LAUNCH at (48.139, 95.494)
                        launchRoutine(),

                        // 3. Move to Pickup 1 (Path 2 & 3)
                        drive.actionBuilder(new Pose2d(launchStation, Math.toRadians(-45)))
                                .strafeTo(new Vector2d(48.062, 82.437))
                                .strafeTo(new Vector2d(18.057, 82.246))
                                .build(),

                        // 4. Return to Launch Station (Path 4)
                        drive.actionBuilder(new Pose2d(18.057, 82.246, Math.toRadians(-45)))
                                .strafeTo(launchStation)
                                .build(),

                        // 5. LAUNCH AGAIN
                        launchRoutine(),

                        // 6. Move to Pickup 2 (Path 5 & 6)
                        drive.actionBuilder(new Pose2d(launchStation, Math.toRadians(-45)))
                                .strafeTo(new Vector2d(48.238, 58.918))
                                .strafeTo(new Vector2d(17.739, 59.263))
                                .build(),

                        // 7. Return to Launch Station (Path 7)
                        drive.actionBuilder(new Pose2d(17.739, 59.263, Math.toRadians(-45)))
                                .strafeTo(launchStation)
                                .build(),

                        // 8. FINAL LAUNCH
                        launchRoutine(),

                        // 9. Park (Path 8)
                        drive.actionBuilder(new Pose2d(launchStation, Math.toRadians(-45)))
                                .strafeTo(new Vector2d(42.980, 78.759))
                                .build()
                )
        );
    }
}