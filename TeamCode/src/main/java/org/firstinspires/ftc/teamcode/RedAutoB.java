package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "RED_leave")
public class RedAutoB extends LinearOpMode {

    Robot robot = new Robot();

    final double LAUNCHER_TARGET_VELOCITY = 2000;
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

            strafeRight(0.5,1000); // goes left

            sleep(29000);

            stop();
        }
    }

    void straight(double power, int milliseconds) {
        robot.frontLeft.setPower(-power);
        robot.backLeft.setPower(-power);
        robot.frontRight.setPower(-power);
        robot.backRight.setPower(-power);
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