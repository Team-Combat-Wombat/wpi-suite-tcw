/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetcw.modules.planningpoker.controllers.req;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import edu.wpi.cs.wpisuitetcw.modules.planningpoker.controllers.GenericPUTRequestObserver;
import edu.wpi.cs.wpisuitetcw.modules.planningpoker.models.PlanningPokerRequirement;
import edu.wpi.cs.wpisuitetcw.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetcw.modules.planningpoker.models.PlanningPokerVote;
import edu.wpi.cs.wpisuitetcw.modules.planningpoker.view.overviews.SessionInProgressPanel;
import edu.wpi.cs.wpisuitetcw.modules.planningpoker.view.overviews.viewSessionComp.ViewSessionReqPanel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * This controller adds all the requirements to the specified session
 * 
 * @author Josh Hebert
 * 
 */
public class AddRequirementController implements ActionListener {

	private PlanningPokerSession session = null;
	private ViewSessionReqPanel view;

	public AddRequirementController(PlanningPokerSession s, ViewSessionReqPanel v) {
		this.session = s;
		this.view = v;
	}

	/*
	 * This method is called when the user clicks the vote button
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		
		ArrayList<PlanningPokerRequirement> r = new ArrayList<PlanningPokerRequirement>();
		/*for(PlanningPokerRequirement i: this.view.getSelectedLeft()){
			r.add(i);
		}
		
		this.session.addRequirements(r);
		
		
		this.view.addToRight(this.view.getSelectedLeft());
		*/
		// Update the session remotely
		final Request request = Network.getInstance()
				.makeRequest(
						"planningpoker/session/".concat(String.valueOf(this.session
								.getID())), HttpMethod.POST);
		// Set the data to be the session to save (converted to JSON)
		request.setBody(this.session.toJSON());
		// Listen for the server's response
		request.addObserver(new GenericPUTRequestObserver(this));
		// Send the request on its way
		request.send();

	}
}
