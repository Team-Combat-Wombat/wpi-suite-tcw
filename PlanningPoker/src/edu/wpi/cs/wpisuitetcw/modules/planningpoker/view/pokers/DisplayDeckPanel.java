/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Combat Wombat
 ******************************************************************************/

package edu.wpi.cs.wpisuitetcw.modules.planningpoker.view.pokers;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetcw.modules.planningpoker.models.PlanningPokerDeck;
import edu.wpi.cs.wpisuitetcw.modules.planningpoker.models.characteristics.CardDisplayMode;

public class DisplayDeckPanel extends JPanel {
	// constants
	private static final int CENTER_PANEL_WIDTH = 350;
	private static final int CENTER_PANEL_HEIGHT = 250;

	// panels for setting up the view
	private PlanningPokerDeck deck;
	private final JPanel cardPanel;
	private final JScrollPane centerPanel;
	private final JPanel container;

	/**
	 * Constructor - creating a panel for displaying a deck of card
	 */
	public DisplayDeckPanel(PlanningPokerDeck deck) {
		// setup panel
		container = new JPanel();
		cardPanel = new JPanel();

		container.setLayout(new GridBagLayout());
		container.add(cardPanel);

		centerPanel = new JScrollPane(container);
		centerPanel.setMinimumSize(new Dimension(CENTER_PANEL_WIDTH,
				CENTER_PANEL_HEIGHT));

		// initialize the deck
		this.deck = deck;

		displayDeck();

		// setup the entire panel
		this.add(centerPanel);

	}

	/**
	 * Display a deck of cards for voting.
	 */
	private void displayDeck() {
		for (int value : deck.getDeck()) {
			Card aCard = new Card(CardDisplayMode.DISPLAY, value, this);
			cardPanel.add(aCard);
		}
	}

	/**
	 * Determine if the deck allows multiple selection
	 * 
	 * @return true if deck allows multiple selection
	 */
	private boolean isMultipleSelection() {
		return deck.getMaxSelection() != 1;
	}
}