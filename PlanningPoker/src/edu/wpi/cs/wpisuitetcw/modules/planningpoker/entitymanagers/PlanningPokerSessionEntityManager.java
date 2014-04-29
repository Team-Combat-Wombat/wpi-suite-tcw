/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Combat Wombat
 ******************************************************************************/

package edu.wpi.cs.wpisuitetcw.modules.planningpoker.entitymanagers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;

import edu.wpi.cs.wpisuitetcw.modules.planningpoker.controllers.EndOnDeadlineController;
import edu.wpi.cs.wpisuitetcw.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetcw.modules.planningpoker.notifications.EmailNotifier;
import edu.wpi.cs.wpisuitetcw.modules.planningpoker.notifications.SMSNotifier;
import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;

/**
 * This is the entity manager for the PlanningPokerSession in the PlanningPoker
 * module.
 * 
 */
public class PlanningPokerSessionEntityManager implements
		EntityManager<PlanningPokerSession> {

	/** The database */
	Data db;
	HashMap<Integer,Timer> deadlineMap;

	/**
	 * Constructs the entity manager. This constructor is called by
	 * {@link edu.wpi.cs.wpisuitetng.ManagerLayer#ManagerLayer()}. To make sure
	 * this happens, be sure to place add this entity manager to the map in the
	 * ManagerLayer file.
	 * 
	 * @param db
	 *            a reference to the persistent database
	 */
	public PlanningPokerSessionEntityManager(Data db) {
		this.db = db;
		deadlineMap = new HashMap<Integer,Timer>();
	}

	/*
	 * Saves a PlanningPokerSession when it is received from a client
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#makeEntity(edu.wpi.cs.
	 * wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public PlanningPokerSession makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {

		// Parse the message from JSON
		final PlanningPokerSession newPlanningPokerSession = PlanningPokerSession
				.fromJson(content);

		int newID;
		PlanningPokerSession[] allSessions = this.getAll(s);
		if (allSessions.length == 0) {
			newID = 1;
		} else {
			PlanningPokerSession mostRecent = allSessions[allSessions.length - 1];
			newID = mostRecent.getID() + 1;
		}
		newPlanningPokerSession.setID(newID);
		
		//Now make something so we can have sessions expire later
		Timer end = new Timer();
		end.schedule(new EndOnDeadlineController(newPlanningPokerSession), newPlanningPokerSession.getDeadline());
		deadlineMap.put((Integer)newID, end);

		// Save the message in the database if possible, otherwise throw an
		// exception
		// We want the message to be associated with the project the user logged
		// in to
		if (!db.save(newPlanningPokerSession, s.getProject())) {
			throw new WPISuiteException();
		}

		// Return the newly created message (this gets passed back to the
		// client)
		return newPlanningPokerSession;
	}

	/*
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#getEntity(edu.wpi.cs.wpisuitetng
	 * .Session, java.lang.String)
	 */
	@Override
	public PlanningPokerSession[] getEntity(Session s, String id)
			throws NotFoundException, WPISuiteException {
		// Throw an exception if an ID was specified, as this module does not
		// support
		// retrieving specific PlanningPokerSessions.
		// List<Model> results = db.retrieveAll(new PlanningPokerSession(),
		// s.getProject());
		List<Model> results = db.retrieve(
				new PlanningPokerSession().getClass(), "ID",
				Integer.parseInt(id), s.getProject());
		return results.toArray(new PlanningPokerSession[0]);
	}

	/*
	 * Returns all of the messages that have been stored.
	 * 
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(edu.wpi.cs.wpisuitetng
	 * .Session)
	 */
	@Override
	public PlanningPokerSession[] getAll(Session s) throws WPISuiteException {
		List<Model> messages = db.retrieveAll(new PlanningPokerSession(),
				s.getProject());
		
		if (messages.size() == 0) {
			System.out.println("CREATED DEFAULT.");
			PlanningPokerSession defaultSession = new PlanningPokerSession();
			defaultSession.setID(1);
			defaultSession.setName("Default Session");
			defaultSession.setDescription("This session is for requirements that have not been assigned");
			
			if (db.save(defaultSession, s.getProject())) {
				PlanningPokerSession[] created = new PlanningPokerSession[1];
				created[0] = defaultSession;
				return created;
			} else {
				throw new WPISuiteException();
			}
		}

		// Return the list of messages as an array
		return messages.toArray(new PlanningPokerSession[0]);
	}

	/*
	 * Message cannot be updated. This method always throws an exception.
	 * 
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#update(edu.wpi.cs.wpisuitetng
	 * .Session, java.lang.String)
	 */
	@Override
	public PlanningPokerSession update(Session s, String content)
			throws WPISuiteException {

		
		PlanningPokerSession updatedSession = PlanningPokerSession
				.fromJson(content);
		
		/*
		 * Because of the disconnected objects problem in db4o, we can't just
		 * save PlanningPokerSessions. We have to get the original defect from
		 * db4o, copy properties from updatedPlanningPokerSession, then save the
		 * original PlanningPokerSession again.
		 */
		List<Model> oldPlanningPokerSessions = db.retrieve(
				PlanningPokerSession.class, "id", updatedSession.getID(),
				s.getProject());
		if (oldPlanningPokerSessions.size() < 1
				|| oldPlanningPokerSessions.get(0) == null) {
			throw new BadRequestException(
					"PlanningPokerSession with ID does not exist.");
		}

		PlanningPokerSession existingSession = (PlanningPokerSession) oldPlanningPokerSessions
				.get(0);

		// copy values to old PlanningPokerSession and fill in our changeset
		// appropriately
		existingSession.copyFrom(updatedSession);

		if (!db.save(existingSession, s.getProject())) {
			throw new WPISuiteException(
					"Could not save when updating existing session.");
		}
		return existingSession;
	}

	/*
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#save(edu.wpi.cs.wpisuitetng
	 * .Session, edu.wpi.cs.wpisuitetng.modules.Model)
	 */
	@Override
	public void save(Session s, PlanningPokerSession model)
			throws WPISuiteException {

		// Save the given session in the database
		db.save(model);
	}

	/*
	 * Messages cannot be deleted
	 * 
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteEntity(edu.wpi.cs.
	 * wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {

		// This module does not allow PlanningPokerSessions to be deleted, so
		// throw an exception
		throw new WPISuiteException();
	}

	/*
	 * Messages cannot be deleted
	 * 
	 * @see
	 * edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteAll(edu.wpi.cs.wpisuitetng
	 * .Session)
	 */
	@Override
	public void deleteAll(Session s) throws WPISuiteException {

		// This module does not allow PlanningPokerSessions to be deleted, so
		// throw an exception
		throw new WPISuiteException();
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#Count()
	 */
	@Override
	public int Count() throws WPISuiteException {
		// Return the number of PlanningPokerSessions currently in the database
		return db.retrieveAll(new PlanningPokerSession()).size();
	}

	/**
	 * Parses a 'command' from the first argument of the advanced GET API.
	 * Currently, the only command supported is 'sendEmail'. This method
	 * receives the arguments as parsed from the URL. However, advanced API core
	 * functionality does not URL-encode the data for you.
	 * 
	 * sendEmail: Sends an email to 'recipient' notifying of the start or end of
	 * a session. The arguments are:
	 * <ul>
	 * <li>notificationType: start or end</li>
	 * <li>recipient: email address at which to send</li>
	 * <li>deadline: the deadline for the planning poker session</li>
	 * </ul>
	 * 
	 * @param s
	 *            The user session
	 * @param args
	 *            The arguments for this get operation
	 * @return null
	 */
	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		if (args.length == 0) {
			throw new WPISuiteException("Not enough arguments.");
		}

		String command = args[2];
		if (command.equals("sendEmail")) {
			if (args.length < 6) {
				throw new WPISuiteException(
						"Usage: /sendMail/(start|end)/<recipient>/<deadline>");
			}

			try {
				String notificationType = URLDecoder.decode(args[3], "UTF-8");
				String email = URLDecoder.decode(args[4], "UTF-8");
				String deadline;
				if (args[5] == null) {
					deadline = "";
				} else {
					deadline = URLDecoder.decode(args[5], "UTF-8");
				}
				sendEmailNotification(notificationType, email, deadline);
			} catch (UnsupportedEncodingException e) {
				Logger.getLogger("PlanningPoker").log(
						Level.SEVERE,
						"Unsupported encoding when parsing deadline for "
								+ "sending notifications.", e);
			}
		} else if (command.equals("sendSMS")) {
			if (args.length < 6) {
				throw new WPISuiteException(
						"Usage: /sendSMS/(start|end)/<recipient>/<deadline>");
			}

			try {
				String notificationType = URLDecoder.decode(args[3], "UTF-8");
				String buddy = URLDecoder.decode(args[4], "UTF-8");
				String deadline;
				if (args[5] == null) {
					deadline = "";
				} else {
					deadline = URLDecoder.decode(args[5], "UTF-8");
				}
				sendSMSNotification(notificationType, buddy, deadline);
			} catch (UnsupportedEncodingException e) {
				Logger.getLogger("PlanningPoker").log(
						Level.SEVERE,
						"Unsupported encoding when parsing deadline for "
								+ "sending notifications.", e);
			}
		}

		return null;
	}

	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		throw new WPISuiteException();
	}

	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		return null;
	}

	/**
	 * Sends an email to a particular email address notifying upon start or end
	 * of a planning poker session.
	 * 
	 * @param notificationType
	 *            'start' or 'end'
	 * @param toAddress
	 *            The recipient address
	 * @param deadline
	 *            String containing the deadline.
	 */
	private void sendEmailNotification(String notificationType, String toAddress,
			String deadline) {
		EmailNotifier.sendMessage(notificationType, toAddress, deadline);
	}

	/**
	 * Sends an SMS to a particular phone number notifying upon start or end
	 * of a planning poker session.
	 * 
	 * @param notificationType
	 *            'start' or 'end'
	 * @param toAddress
	 *            The recipient phone number
	 * @param deadline
	 *            String containing the deadline.
	 */
	public void sendSMSNotification(String notificationType, String phoneNumber,
			String deadline) {
		SMSNotifier.sendMessage(notificationType, phoneNumber, deadline);
	}
}
