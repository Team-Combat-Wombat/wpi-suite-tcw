/**
 * 
 */
package edu.wpi.cs.wpisuitetcw.modules.planningpoker.models;

import org.w3c.dom.views.AbstractView;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * @author Nicholas Kalamvokis and Matt Suarez
 * @date 3/24/2014
 */



public class PlanningPokerRequirement extends Requirement{
	private int sessionId; 
	
	public int getSessionId() {
		return sessionId;
	}


	public void setSessionId(int id) {
		this.sessionId = id;
	}



	/**
	 * 
	 */
	public PlanningPokerRequirement() {
		setId(-1);
		System.out.println("Session ID:" + this.sessionId);

	}
	
	
}
