
package org.usfirst.frc.team948.robot;

import org.usfirst.frc.team948.robot.commandgroups.ShootSequence;
import org.usfirst.frc.team948.robot.commandgroups.TraverseDefenseShootRoutine;
import org.usfirst.frc.team948.robot.commands.CommandBase;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.RaiseAcquirerTo;
import org.usfirst.frc.team948.robot.commands.RaiseShooterArmTo;
import org.usfirst.frc.team948.robot.commands.TurnAngle;
import org.usfirst.frc.team948.robot.commands.TurnToVisionTarget;
import org.usfirst.frc.team948.robot.commands.TurnToVisionTargetContinuous;
import org.usfirst.frc.team948.robot.commands.WaitForRPM;
import org.usfirst.frc.team948.robot.subsystems.AcquirerArm;
import org.usfirst.frc.team948.robot.subsystems.AcquirerWheel;
import org.usfirst.frc.team948.robot.subsystems.Climber;
import org.usfirst.frc.team948.robot.subsystems.Drawbridge;
import org.usfirst.frc.team948.robot.subsystems.Drive;
import org.usfirst.frc.team948.robot.subsystems.ShooterArm;
import org.usfirst.frc.team948.robot.subsystems.ShooterBar;
import org.usfirst.frc.team948.robot.subsystems.ShooterWheel;
import org.usfirst.frc.team948.robot.subsystems.VisionProcessing;
import org.usfirst.frc.team948.robot.utilities.AHRSGyro;
import org.usfirst.frc.team948.robot.utilities.NavXTester;
import org.usfirst.frc.team948.robot.utilities.PreferenceKeys;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public enum Level {
		DEFAULT(0), ACQUIRE(32), CHIVAL(63.75), SALLY_PORT_HIGH(110), FULL_BACK(140); // VALUE
																						// NEEDS
																						// TO
																						// BE
																						// CHECKED

		private double value;

		private Level(double value) {
			this.value = value;
		}

		public double getValue() {
			return value;
		}

	}

	public enum AutoPosition {

		// Angles at which to turn when performing autonomous routine
		// Positions 1 and 2 go into the right goal
		// Positions 3, 4, and 5 go into the middle goal

		LOWBAR_ONE(+58.69), POSITION_TWO(-46.14), POSITION_THREE(-13.54), POSITION_FOUR(9.63), POSITION_FIVE(10);

		private double angle;
		private AutoPosition(double angle) {
			this.angle = angle;
		}

		public double getAngle() {
			return angle;
		}
	}
	
	public enum Defense{
		RAMPARTS(90),
		LOW_BAR(10);
		private double acquirerAngle;
		private Defense(double acquirerAngle){
			this.acquirerAngle = acquirerAngle;
		}
		public double getAcquirerAngle(){
			return acquirerAngle;
		}
	}

	public static Drive drive = new Drive();
	public static ShooterWheel shooterWheel = new ShooterWheel();
	public static ShooterBar shooterBar = new ShooterBar();
	public static ShooterArm shooterArm = new ShooterArm();
	public static AcquirerArm acquirerArm = new AcquirerArm();
	public static AcquirerWheel acquirerWheel = new AcquirerWheel();
	public static VisionProcessing visionProcessing = new VisionProcessing();
	public static Climber climber = new Climber();
	public static Drawbridge drawbridge = new Drawbridge();
	public static PowerDistributionPanel pdp = new PowerDistributionPanel();

	private int screenUpdateCounter;
	public static boolean competitionRobot = false;

	Command autonomousCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		RobotMap.init();
		DS2016.buttonInit();
		visionProcessing.cameraInit();
		
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	public void disabledInit() {
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		periodicAll();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	public void autonomousInit() {

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new RawTankDrive(); break; }
		 */
		autonomousCommand = new TraverseDefenseShootRoutine(AutoPosition.LOWBAR_ONE, Defense.LOW_BAR);
		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		RobotMap.positionTracker.update();
		periodicAll();
	}

	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.

		// CommandBase.drive.initDefaultCommand();
		if (autonomousCommand != null)
			autonomousCommand.cancel();

		SmartDashboard.putData("Raise Shooter Arm to X degrees",
				new RaiseShooterArmTo(CommandBase.preferences.getDouble(PreferenceKeys.SHOOTER_ANGLE, 45)));

		SmartDashboard.putData("Raise Acquirer to X degrees",
				new RaiseAcquirerTo(CommandBase.preferences.getDouble(PreferenceKeys.ACQUIRER_ANGLE, 90)));

		SmartDashboard.putData("Turn Angle to Target", new TurnToVisionTarget(0.6));

		
		SmartDashboard.putData("Move 3 feet forward", new DriveStraightDistance(1, 3));

		SmartDashboard.putData("Turn to target", new TurnToVisionTargetContinuous());

		SmartDashboard.putData("Shoot sequence", new ShootSequence(true));

		SmartDashboard.putData("Wait for RPM", new WaitForRPM(2000, 20));
		// SmartDashboard.putData("Turn set angle to target", new
		// TurnAngle(visionProcessing.getTurningAngle(), 0.7));

	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("left encoder", RobotMap.leftMotorEncoder.get());
		SmartDashboard.putNumber("right encoder", RobotMap.rightMotorEncoder.get());
		SmartDashboard.putBoolean("Has Ball", shooterWheel.isBallLoaded());
		RobotMap.positionTracker.update();
		periodicAll();
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
		periodicAll();
	}

	public void periodicAll() {
		//SmartDashboard.putNumber("Periodic all in nanos", shooterWheel.currentTimeNanos());
		//NavXTester.parameterDisplay();
		
		// PositionTracker.updatePosition();
		// PositionTracker3D.computePosition();
		if (true) {
			SmartDashboard.putNumber("Left RPM", shooterWheel.currentLeftRPM);
			SmartDashboard.putNumber("Right RPM", shooterWheel.currentRightRPM);
			SmartDashboard.putNumber("Arm Angle", (RobotMap.armAngleEncoder.getVoltage()-CommandBase.acquirerArm.VOLTS_0)/CommandBase.acquirerArm.SLOPE_VOLTS_FROM_DEGREES);
			SmartDashboard.putNumber("Shooter Angle Value", ShooterArm.degreesFromVolts(RobotMap.shooterLifterEncoder.getVoltage()));
			SmartDashboard.putNumber("Left Shooter Encoder", RobotMap.leftShooterWheelEncoder.get());
			SmartDashboard.putNumber("Right Shooter Encoder", RobotMap.rightShooterWheelEncoder.get());

			SmartDashboard.putNumber("Distance", visionProcessing.calcDistance());
			SmartDashboard.putNumber("Shooting Angle", visionProcessing.getShootingAngle());

			SmartDashboard.putNumber("Turning Angle", visionProcessing.getTurningAngle());


			SmartDashboard.putNumber("Turning Angle Arcsin", visionProcessing.getTurningAngle());
			SmartDashboard.putNumber("Turning Angle Proportion", visionProcessing.getTurningAngleProportion());
			
			SmartDashboard.putNumber("Calculated Angle", RobotMap.ahrs.getYaw());
			SmartDashboard.putNumber("Robot X", RobotMap.positionTracker.getX());
			SmartDashboard.putNumber("Robot Y", RobotMap.positionTracker.getY());

			try {
				SmartDashboard.putData("PDP", pdp);
			} catch (Exception e) {
				//Silently ignore the exception
			}
			// for (int i = 0; i <= 15; i++) {
			// 	   SmartDashboard.putNumber("PDP current " + i, pdp.getCurrent(i));
			// }
			// SmartDashboard.putNumber("PDP Total Voltage", pdp.getVoltage());
			// SmartDashboard.putData("ShooterRampUp", new ShooterRampUp(true));
		}
		screenUpdateCounter++;
	}
}
