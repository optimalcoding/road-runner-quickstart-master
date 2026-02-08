package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        try {
            // Corrected Version:
            meepMeep.setBackground(ImageIO.read(new File("C:/Users/ai/Pictures/decodedark.png")));
        } catch (IOException e) { e.printStackTrace(); }

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-52, -53, Math.toRadians(45)))
                        .forward(30)
                        .lineToLinearHeading(new Pose2d(-10, -10, Math.toRadians(-90)))
                        .forward(40)
                        .back(40)
                        .lineToLinearHeading(new Pose2d(-30,-30,Math.toRadians(45)))
                        .lineToLinearHeading(new Pose2d(12,-25,Math.toRadians(-90)))
                        .forward(25)
                        .back(25)
                        .lineToLinearHeading(new Pose2d(-30,-30,Math.toRadians(45)))
                        .lineToLinearHeading(new Pose2d(-10,-30,Math.toRadians(45)))
                       /* .turn(Math.toRadians(135))
                        .forward(-15)
                        .turn(Math.toRadians(-45))
                        .forward(34)
                        .turn(Math.toRadians(-90))
                        .forward(30)
                        .forward(-30)
                        .turn(Math.toRadians(90))
                        .forward(-34)
                        .turn(Math.toRadians(45))
                        */

                        .build());


        meepMeep
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}