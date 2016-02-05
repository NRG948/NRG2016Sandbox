package org.usfirst.frc.team948.robot;

import org.usfirst.frc.team948.robot.utilities.AHRSAccelerometer;
import org.usfirst.frc.team948.robot.utilities.AHRSGyro;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	public static Victor shooterBallPusher=new Victor(9);
	public static Victor rightShooterWheel = new Victor(4);
	public static Victor leftShooterWheel = new Victor(5);
	public static Victor shooterLifterMotor = new Victor(6);
	public static Victor drawbridgeArm = new Victor(12); //NEW
	public static Victor climberTapeMeasure = new Victor(13); //NEW
	public static Victor climberWinch = new Victor(14); //NEW
	public static Encoder rightShooterWheelEncoder = new Encoder(0, 1);
	public static Encoder leftShooterWheelEncoder = new Encoder(2, 3);
	public static AnalogInput shooterLifterEncoder = new AnalogInput(4);
	public static Victor motorFrontLeft = new Victor(2);
	public static Victor motorFrontRight = new Victor(0);
	public static Victor motorBackLeft = new Victor(3);
	public static Victor motorBackRight = new Victor(1);
	public static AnalogInput armAngleEncoder = new AnalogInput(5);
	public static Victor acquireArmVictor = new Victor(7);
	public static Victor acquireWheelVictor = new Victor(8);
	public static Encoder leftMotorEncoder = new Encoder(7, 8);
	public static Encoder rightMotorEncoder  = new Encoder(9, 10);
	public static AHRS ahrs = new AHRS(SPI.Port.kMXP);
	public static Gyro driveGyro = new AHRSGyro();
	public static Accelerometer accelerometer = new AHRSAccelerometer();
	public static DigitalInput ballBeamBreaker = new DigitalInput(11);
	

	public static void init() {
//The parameters typed in for the encoder objects are random.
		LiveWindow.addActuator("Drive Subsystem", "Speed Controller Front Left Victor",(Victor) motorFrontLeft);
		
		LiveWindow.addActuator("Drive Subsystem", "Speed Controller Front Right Victor",(Victor) motorFrontRight);
		
		LiveWindow.addActuator("Drive Subsystem", "Speed Controller Back Left Victor",(Victor) motorBackLeft);
		
		LiveWindow.addActuator("Drive Subsystem", "Speed Controller Back Right Victor",(Victor) motorBackRight);
		
		LiveWindow.addActuator("Drawbridge Subsystem", "Speed Controller Drawbridge Victor",(Victor) drawbridgeArm);
		
		LiveWindow.addActuator("Climber Subsystem", "Speed Controller Climber Tapemeasure Victor",(Victor) climberTapeMeasure);
		
		LiveWindow.addActuator("Climber Subsystem", "Speed Controller Climber Winch Victor",(Victor) climberWinch);

		LiveWindow.addSensor("Drive Subsystem", "Drive Gyro", (LiveWindowSendable) driveGyro);							// unknown, 6 now taken

		LiveWindow.addSensor("Drive Subsystem", "Angle Arm Encoder", armAngleEncoder);
		
		LiveWindow.addSensor("Drive Subsystem", "Left Motor Encoder", leftMotorEncoder);
		
		LiveWindow.addSensor("Drive Subsystem", "Right Motor Encoder", rightMotorEncoder);
		
		LiveWindow.addSensor("Drive Subsystem", "Accelerometer", (LiveWindowSendable) accelerometer);
		
		LiveWindow.addActuator("Acquirer Subsystem", "Acquire Arm Victor", acquireArmVictor);
		
		
		//place holder need a real DistancePerPusle
		RobotMap.leftShooterWheelEncoder.setDistancePerPulse(1);
		RobotMap.rightShooterWheelEncoder.setDistancePerPulse(1);
	}
}