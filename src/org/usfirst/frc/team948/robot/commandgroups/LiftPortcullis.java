package org.usfirst.frc.team948.robot.commandgroups;

import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.robot.commands.DriveStraightDistance;
import org.usfirst.frc.team948.robot.commands.RaiseAcquirerTo;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LiftPortcullis extends CommandGroup{
	
	public LiftPortcullis() {
		addSequential(new RaiseAcquirerTo(Robot.Level.PORTCULLIS_LOW));
		
		addSequential(new RaiseAcquirerTo(Robot.Level.PORTCULLIS_HIGH));
		
		addSequential(new DriveStraightDistance(1,10)); //CHECK VALUE
	}
}
