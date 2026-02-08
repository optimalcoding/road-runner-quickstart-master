package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "RED_2_artifact_launch")
public class RedAutoA extends LinearOpMode {

    Robot robot = new Robot();

    final double LAUNCHER_TARGET_VELOCITY = 1250;
    final double FEEDER_TARGET_VELOCITY = 5000;
    final double LAUNCHER_MIN_VELOCITY = 1075;
    final double STOP_SPEED = 0.0;
    final double FULL_SPEED = 1.0;



    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);

        robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.

        double position = 0;

        waitForStart();

        while (opModeIsActive()) {

            straight(0.5, 1500);

            sleep(500);

            robot.launcher.setVelocity(LAUNCHER_TARGET_VELOCITY);

            sleep(1500);

            robot.feeder.setVelocity(-FEEDER_TARGET_VELOCITY);

            sleep(50);

            robot.feeder.setVelocity(STOP_SPEED);

            sleep(500);

            robot.intake.setVelocity(-LAUNCHER_TARGET_VELOCITY);

            sleep(200);

            robot.feeder.setVelocity(-FEEDER_TARGET_VELOCITY);

            sleep(2000);

            robot.launcher.setVelocity(STOP_SPEED);

            sleep(100);

            robot.intake.setVelocity(STOP_SPEED);

            sleep(500);

            robot.feeder.setVelocity(STOP_SPEED);

            sleep(500);

            rotate(-0.4, 800);

            sleep(100);

            straight(0.5,1000);

            sleep(24000);

            stop();
        }
    }

    void straight(double power, int milliseconds) {
        robot.frontLeft.setPower(power);
        robot.backLeft.setPower(power);
        robot.frontRight.setPower(power);
        robot.backRight.setPower(power);
        sleep(milliseconds);
        robot.frontLeft.setPower(0);
        robot.backLeft.setPower(0);
        robot.frontRight.setPower(0);
        robot.backRight.setPower(0);
    }

    void strafeLeft(double power, int milliseconds) {
        robot.frontRight.setPower(power);
        robot.frontLeft.setPower(-power);
        robot.backRight.setPower(-power);
        robot.backLeft.setPower(power);
        sleep(milliseconds);
        robot.frontLeft.setPower(0);
        robot.backLeft.setPower(0);
        robot.frontRight.setPower(0);
        robot.backRight.setPower(0);
    }

    void strafeRight(double power, int milliseconds) {
        robot.frontRight.setPower(-power);
        robot.frontLeft.setPower(power);
        robot.backRight.setPower(power);
        robot.backLeft.setPower(-power);
        sleep(milliseconds);
        robot.frontLeft.setPower(0);
        robot.backLeft.setPower(0);
        robot.frontRight.setPower(0);
        robot.backRight.setPower(0);
    }

    void rotate(double power, int milliseconds) { //note: default rotate counter clockwise
        robot.frontRight.setPower(-power);
        robot.frontLeft.setPower(power);
        robot.backRight.setPower(-power);
        robot.backLeft.setPower(power);
        sleep(milliseconds);
        robot.frontLeft.setPower(0);
        robot.backLeft.setPower(0);
        robot.frontRight.setPower(0);
        robot.backRight.setPower(0);
    }

}