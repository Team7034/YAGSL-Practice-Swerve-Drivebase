// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.PS4Controller;
import frc.robot.commands.drive.AbsoluteFieldDrive;
import frc.robot.commands.drive.ControllerDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.drive.TeleopDrive;
import frc.robot.subsystems.SwerveSubsystem;

import java.awt.*;
import java.io.IOException;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  public static final PS4Controller driverController = new PS4Controller(0);
  public static final SwerveSubsystem drivebase = SwerveSubsystem.getInstance();

  public static final TeleopDrive teleopDrive = new TeleopDrive(drivebase,
          driverController::getLeftX, driverController::getLeftY,
          () -> driverController.getRawAxis(2),
          () -> true, false, true
          );

  AbsoluteFieldDrive closedAbsoluteDrive = new AbsoluteFieldDrive(drivebase,

          () -> MathUtil.applyDeadband(driverController.getLeftY(),
                  0.1),
          () -> MathUtil.applyDeadband(driverController.getLeftX(),
                  0.1),
          () -> -driverController.getRawAxis(2),
          false);

  private static final ControllerDrive controlDrive = new ControllerDrive(drivebase, () -> MathUtil.applyDeadband(driverController.getLeftX(), 0.1), () -> MathUtil.applyDeadband(driverController.getLeftY(), 0.1), () -> MathUtil.applyDeadband(driverController.getRawAxis(2), 0.1), true);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() throws IOException {
    // Configure the trigger bindings
    drivebase.setDefaultCommand(controlDrive);
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {

    new Trigger(driverController::getShareButton).onTrue(drivebase.runOnce(drivebase::zeroGyro));
    new Trigger(driverController::getCircleButton).whileTrue(drivebase.run(drivebase::goToZero));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }
}
