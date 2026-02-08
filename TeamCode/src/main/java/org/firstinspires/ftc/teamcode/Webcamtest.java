package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;


@Autonomous
public class Webcamtest extends OpMode {

    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();

    @Override
    public void init() {
        aprilTagWebcam.init(hardwareMap, telemetry);

    }

    @Override
    public void loop() {

        aprilTagWebcam.update();
        AprilTagDetection id20 = aprilTagWebcam.tagID(20);
        aprilTagWebcam.displayDetectionTelemetry(id20);
        telemetry.addData("id20", id20);


    }
}