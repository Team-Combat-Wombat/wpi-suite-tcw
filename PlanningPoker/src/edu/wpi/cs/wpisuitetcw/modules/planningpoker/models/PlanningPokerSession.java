package edu.wpi.cs.wpisuitetcw.modules.planningpoker.models;

import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;


/**
 * Contains a Planning Poker Session
 * 
 * @author Josh Hebert
 * 
 */
public class PlanningPokerSession extends AbstractModel {

	private int id = -1;
	private boolean isCancelled = false;
	private Date startTime = null;
	private Date endTime = null;
	private String name = "";

	// Getters and Setters
	// ///////////////////////////////////////////////////////////////
	public void cancel() {
		this.isCancelled = true;
		this.endTime = new Date();
	}

	public boolean isCancelled() {
		return this.isCancelled;
	}

	/**
	 * If this model is eligible for activation, it will be activated. Criteria
	 * for activation are: -> It cannot already be active -> It cannot have been
	 * canceled -> It must have at least one requirement
	 */
	public void activate() {
		// If this hasn't been canceled

		if (!this.isCancelled) {
			// And has a valid amount of requirements
			if (this.getRequirements().size() >= 0) {
				// Activate
				this.startTime = new Date();
			}
		}
	}
	
	/**
	 * Gets a list of requirements from the PlanningPokerSession Entity 
	 * Manager
	 * @return List of requirements owned by this session
	 */
	public ArrayList<PlanningPokerRequirement> getRequirements(){
		return null;
		
	}
	/**
	 * Returns the status of this session, i.e. whether or not it is open to
	 * voting
	 * 
	 * @return The status of the session
	 */
	public boolean isActive() {

			return !(this.startTime == null);

	}

	public boolean isDone() {
		if (endTime == null) {
			return false;
		} else
			return true;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public String getName() {
		return this.name;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return this.id;
	}


	
	
	
	// Functions for requirements
	
	
	
	
	// public void createRequirements
	
	
	/* Adds a requirement to the list of requirements
	 * @param newReq -> new Requirements to be added
	 */
	/*public void addRequirements(ArrayList<PlanningPokerRequirement> newReqs){
		this.reqsList.append(newReqs);
	}
	*/
	
	/* Deletes a requirement by session ID
	 * @param requirementId -> ID of requirement to be deleted
	 */
	/*public void deleteRequirement(int requirementId){
		for(int i = 0; i < reqsList.size(); i++){
			if(reqsList.get(i).getId() == requirementId){
				reqsList.remove(i);
				break;
			}
		}
	}
	
	*/
	
	
	// ///////////////////////////////////////////////////////////////
	/**
	 * Constructs a PlanningPokerSession for the given string message
	 * 
	 * @param message
	 */
	public PlanningPokerSession() {

	}

	/**
	 * Converts the model to a JSON String
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, PlanningPokerSession.class);
	}

	/**
	 * Convert from JSON back to a Planning Poker Session
	 * 
	 * @param Serialized
	 *            JSON String that encodes to a Session Model
	 * @return the PlanningPokerSession contained in the given JSON
	 */
	public static PlanningPokerSession fromJson(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, PlanningPokerSession.class);
	}

	/**
	 * Returns an array of PlanningPokerSession parsed from the given
	 * JSON-encoded string.
	 * 
	 * @param json
	 *            a string containing a JSON-encoded array of
	 *            PlanningPokerSession
	 * @return an array of PlanningPokerSession deserialized from the given json
	 *         string
	 */
	public static PlanningPokerSession[] fromJsonArray(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, PlanningPokerSession[].class);
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Session";
	}

	/*
	 * The methods below are required by the model interface, however they do
	 * not need to be implemented for a basic model like PlanningPokerSession.
	 */

	@Override
	public void save() {
	}

	@Override
	public void delete() {
	}

	@Override
	public Boolean identify(Object o) {
		return null;
	}

}