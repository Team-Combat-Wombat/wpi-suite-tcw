/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Combat Wombat
 ******************************************************************************/

package edu.wpi.cs.wpisuitetcw.modules.planningpoker.view.UIComponent;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

/**
 * A panel that has a name and description text boxes
 * 
 * This panel should be used when clients needs a name
 * and a description box as an integrated unit
 */
public class NameDescriptionPanel extends JPanel {
	
	private static final int TEXTBOX_HEIGHT = 26;
	
	/** A text field for name */
	private final JLabel nameLabel;
	private final JTextArea nameTextField;
	private final JScrollPane nameFrame;
	
	/** A text field for description */
	private final JLabel descriptionLabel;
	private final JTextArea descriptionTextField;
	final JScrollPane descriptionFrame;
	
	/** A list of Components that lie to the right of name text box */
	private List<Component> nextToNameTextboxComponents;
	
	/** A list of Components that lie below the name text box */
	private List<Component> belowNameTextboxComponents;
	
	/**
	 * Construct the NameDescriptionPanel by
	 * creating and adding the name and description
	 * text boxes with their labels
	 */
	public NameDescriptionPanel() {
		// Create text box and label for name
		nameLabel =  new JLabel("Name *");
		nameTextField	 = new JTextArea();
		nameTextField.setLineWrap(true);
		nameTextField.setWrapStyleWord(true);
		nameFrame = new JScrollPane(nameTextField);

		// Create text box and label for desription
		descriptionLabel = new JLabel("Description *");
		descriptionTextField 	= new JTextArea();
		descriptionTextField.setLineWrap(true);
		descriptionTextField.setWrapStyleWord(true);
		descriptionFrame = new JScrollPane(descriptionTextField);
		
		// Initialize the list of extra components
		nextToNameTextboxComponents = new ArrayList<>();
		belowNameTextboxComponents  = new ArrayList<>();


		setLayout(new MigLayout("fill, inset 0", "", "0[][][][grow]0"));
		add(nameLabel, "left, growx, wrap");
		add(nameFrame, "growx, wrap, height " + TEXTBOX_HEIGHT + "px!");
		add(descriptionLabel, "left, growx, span");
		add(descriptionFrame, "grow");
	}
	
	/**
	 * Construct the NameDescriptionPanel by
	 * creating and adding the name and description
	 * text boxes with the given Strings as their corresponding labels
	 */
	public NameDescriptionPanel(String name, String description) {
		this();
		
		// Assign the given strings to the JLabel
		nameLabel.setText(name);
		descriptionLabel.setText(description);
	}
	
	/**
	 * Construct the NameDescriptionPanel by
	 * creating and adding the name and description
	 * text boxes with the given Strings as their corresponding labels
	 * The text boxes can only be editable when if isEditable is true
	 */
	public NameDescriptionPanel(String name, String description, boolean isEditable) {
		this(name, description);
		
		// Disable the text boxes if isEditable is false
		if (!isEditable) {
			nameTextField.setEditable(false);
			descriptionTextField.setEditable(false);
		}
	}
	
	// VVVVVVVVVVVVVVVVVVVVVVVV CONTENT METHODS VVVVVVVVVVVVVVVVVVVVVVVV
	/**
	 * Put the given name in the name text box
	 * @param A string that would be put 
	 * in the name text box
	 */
	public void setName(String name) {
		nameTextField.setText(name);
	}
	
	/**
	 * Remove the text inside the name text box
	 */
	public void clearName() {
		nameTextField.setText("");
	}
	
	/**
	 * Put the given text in the description text box
	 * @param text A string that would be put
	 * in the description text box
	 */
	public void setDescription(String text) {
		descriptionTextField.setText(text);
	}
	
	/**
	 * Remove the text inside the description text box
	 */
	public void clearDescription() {
		descriptionTextField.setText("");
	}
	
	// VVVVVVVVVVVVVVVVVVVVVV LAYOUT METHODS VVVVVVVVVVVVVVVVVVVVVVVV
	/**
	 * Remove the name text box and its label
	 */
	public void removeNameTextbox() {
		// Remove the name text box
		remove(nameLabel);
		remove(nameFrame);
		
		// Modify the layout so the description fills up
		if (nextToNameTextboxComponents.isEmpty() &&
				belowNameTextboxComponents.isEmpty()) {
			// Only description box remains
			setLayout(new MigLayout("fill, inset 0", "", "0[][grow]0"));
			add(descriptionLabel, "left, growx, span");
			add(descriptionFrame, "grow");
		} else if (!nextToNameTextboxComponents.isEmpty() && 
				belowNameTextboxComponents.isEmpty()) {
			// Description box and the right elements remain
			setLayout(new MigLayout("fill, inset 0", "", "0[][][grow]0"));
			
			// TODO Add right elements to the first row
			
			add(descriptionLabel, "left, growx, span");
			add(descriptionFrame, "grow");
		} else if (nextToNameTextboxComponents.isEmpty() &&
				!belowNameTextboxComponents.isEmpty()) {
			// Description box and the above elements left
			setLayout(new MigLayout("fill, inset 0", "", "0[][][grow]0"));
			
			// TODO add the above element to the first row
			add(descriptionLabel, "left, growx, span");
			add(descriptionFrame, "grow");
		} else {
			// Description box, the right elements, and
			// the above elements remain
			
		}
	}
	
	/**
	 * Add new component to the right side of the name text box
	 * @param otherComponent The component that would be added to
	 * the right side of the name text box
	 */
	public void addToRightSideNameTextbox(Component otherComponent) {
		
	}
	
	/**
	 * Add a list of components to the right side of the name text box
	 * @param otherComponents The list of components that would be added to
	 * the right side of the name text box
	 */
	public void addToRightSideNameTextbox(List<Component> otherComponents) {
		
	}
	
	/**
	 * Add a component below the name text box
	 * @param otherComponent The component that would be added 
	 * below name text box
	 */
	public void addBelowNameTextbox(Component otherComponent) {
		
	}
	
	/**
	 * Add a list of components below the name text box
	 * @param otherComponents The list of component that would be added
	 * below the name text box
	 */
	public void addBelowNameTextbox(List<Component> otherComponents) {
		
	}
	
}
