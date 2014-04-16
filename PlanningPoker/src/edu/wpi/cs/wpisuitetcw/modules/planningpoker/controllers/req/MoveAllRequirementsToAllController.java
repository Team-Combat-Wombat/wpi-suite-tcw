/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Combat Wombat
 ******************************************************************************/

package edu.wpi.cs.wpisuitetcw.modules.planningpoker.controllers.req;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import edu.wpi.cs.wpisuitetcw.modules.planningpoker.controllers.GenericPUTRequestObserver;
import edu.wpi.cs.wpisuitetcw.modules.planningpoker.entitymanagers.ViewSessionTableManager;
import edu.wpi.cs.wpisuitetcw.modules.planningpoker.models.PlanningPokerRequirement;
import edu.wpi.cs.wpisuitetcw.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetcw.modules.planningpoker.view.overviews.viewSessionComp.ViewSessionReqPanel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * This controller adds all the requirements to the "All" pool
 * 
 */
public class MoveAllRequirementsToAllController implements ActionListener {

	private PlanningPokerSession session = null;
	private ViewSessionReqPanel view;

	public MoveAllRequirementsToAllController(PlanningPokerSession s, ViewSessionReqPanel v) {
		this.session = s;
		this.view = v;
	}

	/**
	 * Processes the received session
	 * @param s The pooll session that holds all unassigned reqs
	 */
	public void receivedData(PlanningPokerSession s){
		PlanningPokerRequirement r;
		
		//Move the requested reqs from session to all
		for(String a : this.view.getAllRightRequirements()){
				r = session.getReqByName(a);
				ArrayList<PlanningPokerRequirement> d = new ArrayList<PlanningPokerRequirement>();
				d.add(r);
				session.deleteRequirements(d);
				s.addRequirement(r);
				ViewSessionTableManager a1 = new ViewSessionTableManager();
				a1.refreshRequirements(1, s.getRequirements());
				

				ViewSessionTableManager a2 = new ViewSessionTableManager();
				a2.refreshRequirements(session.getID(), session.getRequirements());
		}
		
		
		//Updates both
		final Request request = Network.getInstance().makeRequest("planningpoker/session/".concat(String.valueOf(s.getID())), HttpMethod.POST);
		request.setBody(session.toJSON());
		request.addObserver(new GenericPUTRequestObserver(this));
		request.send();
		
		final Request request2 = Network.getInstance().makeRequest("planningpoker/session/1", HttpMethod.POST);
		request2.setBody(s.toJSON());
		request2.addObserver(new GenericPUTRequestObserver(this));
		request2.send();
		
		//Updates the view
		this.view.allReqTable.repaint();
		this.view.sessionReqTable.repaint();
	}
	
	/*
	 * This method is called when the user clicks the << button
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		final Request request = Network.getInstance().makeRequest("planningpoker/session/1", HttpMethod.GET);
		request.addObserver(new MoveAllRequirementsToAllRequestObserver(this));
		request.send();
		
		

	}
}
