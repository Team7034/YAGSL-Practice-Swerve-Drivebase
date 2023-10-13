package frc.robot.commands.photonvision;

import org.photonvision.PhotonCamera;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.SwerveSubsystem;

public class DriveToAprilTag extends CommandBase {
    // Change this to match the name of the camera
    private final PhotonCamera camera = new PhotonCamera("photonvision");

    SwerveSubsystem swerveDrive;

    public DriveToAprilTag(SwerveSubsystem swerveSubsystem) {
        this.swerveDrive = swerveSubsystem;
    }

    @Override
    public void execute() {
        var result = camera.getLatestResult();

        if (result.hasTargets()) {
            var pose = result.getBestTarget().getBestCameraToTarget();

            var translation = new Translation2d(pose.getTranslation().getX(), pose.getTranslation().getY());

            double currentDistance = translation.getNorm();
            double desiredDistance = 2.0;
            double distanceError = desiredDistance - currentDistance;

            double speed = 0.1 * distanceError;

            double yaw = result.getBestTarget().getYaw();

            swerveDrive.drive(translation.rotateBy(new Rotation2d(speed)), yaw, false, false);
        }
    }
}
