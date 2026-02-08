package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import java.util.Arrays;

@TeleOp(name = "Teleop")
public class Teleop extends LinearOpMode {




    final double FEED_TIME_SECONDS = 0.20;

    Robot robot = new Robot();
    int robotCycle = 0;



    final double LAUNCHER_TARGET_VELOCITY = 1400;
    final double FEEDER_TARGET_VELOCITY = 5000;
    final double FEEDER_MIN_VELOCITY = 750;
    final double LAUNCHER_REVERSE = -1000;
    final double FEEDER_REVERSE = 1000;
    final double STOP_SPEED = 0.0;
    final double FULL_SPEED = 1.0;



    @Override
    public void runOpMode() throws InterruptedException {


        //initialization variables, notifying robot is initialized and shows how long robot ran for
        telemetry.addData("Status", "Initialized");
        telemetry.addData("Status", "Runtime " + robot.runtime.toString());
        telemetry.update();


        robot.init(hardwareMap);



        robot.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.feeder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.frontLeft.setZeroPowerBehavior(BRAKE);
        robot.frontRight.setZeroPowerBehavior(BRAKE);
        robot.backLeft.setZeroPowerBehavior(BRAKE);
        robot.backRight.setZeroPowerBehavior(BRAKE);


        robot.launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.launcher.setZeroPowerBehavior(BRAKE);
        robot.launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(300, 0, 0, 10));


        // robot.lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        while (opModeIsActive()) {


            telemetry.addData("Robot Cycle", robotCycle);
            //  telemetry.addData("Arm Encoder Value", robot.lift.getCurrentPosition());
            telemetry.addData("Match Time (s)", getRuntime());
            telemetry.addData("FL Count", robot.frontLeft.getCurrentPosition());
            telemetry.addData("FR Count", robot.frontRight.getCurrentPosition());
            telemetry.addData("BL Count", robot.backLeft.getCurrentPosition());
            telemetry.addData("BR Count", robot.backRight.getCurrentPosition());

            telemetry.addData("Status", "Resetting Values");
            telemetry.update();



            //Powerplay controller configs for reference to centerstage indirect drive
            double FrontLeftVal = -gamepad1.left_stick_y + gamepad1.left_stick_x + (gamepad1.right_stick_x);
            double FrontRightVal = -gamepad1.left_stick_y - (gamepad1.left_stick_x) - (gamepad1.right_stick_x);
            double BackLeftVal = -gamepad1.left_stick_y - (gamepad1.left_stick_x) + (gamepad1.right_stick_x);
            double BackRightVal = -gamepad1.left_stick_y + (gamepad1.left_stick_x) - (gamepad1.right_stick_x);


            // change orientation bc going forward is backwards
            //Move range to between 0 and +1, if not already
            double[] wheelPowers = {FrontRightVal, FrontLeftVal, BackLeftVal, BackRightVal};
            Arrays.sort(wheelPowers);
            if (wheelPowers[3] > 1) {
                FrontLeftVal /= wheelPowers[3];
                FrontRightVal /= wheelPowers[3];
                BackRightVal /= wheelPowers[3];
                BackLeftVal /= wheelPowers[3];

            }

            robot.frontLeft.setPower(FrontLeftVal * 0.6);
            robot.frontRight.setPower(FrontRightVal * 0.6);
            robot.backLeft.setPower(BackLeftVal * 0.6);
            robot.backRight.setPower(BackRightVal * 0.6);
            robot.intake.setPower(gamepad2.left_stick_y);
            // robot.launcher.setPower(gamepad2.right_stick_y);




            // telemetry.addData("frontLeft", FrontLeftVal); // Note: driver hub shows this and the count version but idk why nor what it is
            // telemetry.addData("frontRight", FrontRightVal);
            // telemetry.addData("backLeft", BackLeftVal);
            // telemetry.addData("backRight", BackRightVal);

            telemetry.update();
            // set power to wheel motors
            //robot.frontLeft.setPower(FrontLeftVal);
            //robot.frontRight.setPower(FrontRightVal);
            //robot.backLeft.setPower(BackLeftVal);
            //robot.backRight.setPower(BackRightVal);




            if (gamepad2.y) {
                robot.launcher.setVelocity(LAUNCHER_TARGET_VELOCITY);


            } else if (gamepad2.b) { // stop flywheel
                robot.launcher.setVelocity(STOP_SPEED);

            }

            if (gamepad2.x) {
                robot.feeder.setVelocity(-FEEDER_TARGET_VELOCITY);
            }
            else if (gamepad2.a) {
                robot.feeder.setVelocity(STOP_SPEED);
            }

            if (gamepad2.dpad_up) {
                Reverse(450);
            }

            if (gamepad1.dpad_up) {
                robot.turret.setPosition(0.6);//raises hood

            }
            else if (gamepad1.dpad_down) {
                robot.turret.setPosition(0.2);//lowers hood

            }


            telemetry.addData("feedmotor_speed", robot.feeder.getCurrentPosition());
            telemetry.addData("launcher_speed", robot.launcher.getVelocity());

        }

        idle();

    }
    void Reverse(int milliseconds) {
        robot.launcher.setVelocity(LAUNCHER_REVERSE);
        robot.feeder.setVelocity(FEEDER_REVERSE);
        sleep(milliseconds);
        robot.launcher.setVelocity(STOP_SPEED);
        robot.feeder.setVelocity(STOP_SPEED);
    }

}