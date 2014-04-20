/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Combat Wombat
 ******************************************************************************/

package edu.wpi.cs.wpisuitetcw.modules.planningpoker.view.overviews.viewSessionComp;

import javax.swing.JButton;
import javax.swing.JPanel;
import edu.wpi.cs.wpisuitetcw.modules.planningpoker.view.actionlisteners.ViewSessionBtnPanelListeners;
import edu.wpi.cs.wpisuitetcw.modules.planningpoker.view.overviews.ViewSessionPanel;

public class ViewSessionBtnPanel extends JPanel{
	private final JButton activateBtn;
	private final JButton addBtn;
	public final ViewSessionPanel parentPanel;
	
	
	public ViewSessionBtnPanel(ViewSessionPanel parentPanel) {
		this.parentPanel = parentPanel;
		
		// set up buttons
		this.addBtn = new JButton("Add Requirements");
		this.activateBtn = new JButton("Activate Session");
		
		activateBtn.addActionListener(new ViewSessionBtnPanelListeners.ActivateSession(this));
		
		// add buttons
		this.add(this.addBtn);
		this.add(this.activateBtn);
	}
	

}
