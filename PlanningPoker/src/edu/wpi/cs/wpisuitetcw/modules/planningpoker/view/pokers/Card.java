/**
 * 
 */
package edu.wpi.cs.wpisuitetcw.modules.planningpoker.view.pokers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

/**
 * 
 */
public class Card extends JPanel {
	// constants
	private final String ERROR_MSG = "<html><font color='red'>Positive integer only</font></html>";
	private final String BUTTON_TEXT = "\u2716";
	private final Dimension CARD_DIMENSION = new Dimension(146, 194);

	private final JTextField txtboxValue;
	private final JLabel labelError;
	private final JButton closeButton;

	private Image cardPicture = null;

	public Card() {
		txtboxValue = new JTextField(3);

		labelError = new JLabel(ERROR_MSG);
		labelError.setVisible(false);

		closeButton = new JButton(BUTTON_TEXT);
		closeButton.setFont(closeButton.getFont().deriveFont((float) 8));
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.setVisible(false); // button is not visible until user
										// mouseover it

		// add listener
		this.addListenerToValueTextBox(txtboxValue, this);
		this.addListenerToCloseButton(closeButton, this);

		// load background image
		try {
			Image img = ImageIO.read(getClass().getResource("new_card.png"));
			ImageIcon icon = new ImageIcon(img);
			this.cardPicture = icon.getImage();
		} catch (IOException ex) {
		}

		// setup the card panel
		this.setLayout(new MigLayout());

		JPanel container = new JPanel();
		container.setLayout(new MigLayout());
		container.add(txtboxValue, "center, wrap");
		container.add(labelError, "center");
		container.setBackground(Color.WHITE);
		container.setOpaque(false);

		this.add(closeButton, "right, wrap");
		this.add(container, "center, wrap, span");
		this.setPreferredSize(CARD_DIMENSION);

		// set border
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		// add highlight feature to the card
		this.addMouseoverHightlight(closeButton, this);
		this.addMouseoverHightlight(txtboxValue, this);
		this.addMouseoverHightlight(labelError, this);
		this.addMouseoverHightlight(this, this);
	}

	/**
	 * set the image as the background of the panel
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (cardPicture != null) {
			g.drawImage(cardPicture, -3, 0, this);
		}
	}

	/**
	 * determine if user enters pure integer value
	 * 
	 * @return true if so, else otherwise
	 */
	public boolean validateCardValue() {
		String inputValue = this.txtboxValue.getText();

		if (inputValue.equals("")) {
			return false;
		} else {
			return this.isPositiveInteger(inputValue);
		}
	}

	/**
	 * Determine if the String contains positive integer
	 * 
	 * @param String
	 *            input
	 * @return true if so; false otherwise
	 */
	private boolean isPositiveInteger(String s) {
		try {
			int value = Integer.parseInt(s);
			if (value > 0) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * makes the card invalid by changing the color
	 */
	public void setCardInvalid() {
		this.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
		this.labelError.setVisible(true);
	}

	/**
	 * card is valid, set the border back to black
	 */
	public void setCardValid() {
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		this.labelError.setVisible(false);
	}

	/**
	 * listener for dynamically validate the textfield input
	 */
	private void addListenerToValueTextBox(JTextField textbox, final Card aCard) {
		textbox.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (aCard.validateCardValue()) {
					aCard.setCardValid();
				} else {
					aCard.setCardInvalid();
				}
			}
		});
	}

	/**
	 * Allows a card to be closed
	 * 
	 * @param closeButton
	 * @param card
	 *            object
	 */
	private void addListenerToCloseButton(JButton closeButton, final Card aCard) {
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				aCard.discardThisCard();
			}
		});

	}

	/**
	 * Discard the card by hiding it from the view
	 */
	private void discardThisCard() {
		this.setVisible(false);
	}

	/**
	 * adds mouse over feature to the card
	 * 
	 * @param Card
	 *            object
	 */
	private void addMouseoverHightlight(JComponent item, final Card aCard) {
		item.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// set the card back to normal
				System.out.println("Mouse existed");
				aCard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
				closeButton.setVisible(false);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// highlight the card
				System.out.println("Mouse entered");
				aCard.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
				// show the close button
				closeButton.setVisible(true);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * getTxtboxValue
	 * 
	 * @return the textbox for setting the value
	 */
	public JTextField getTxtboxValue() {
		return txtboxValue;
	}

	/**
	 * return the value of the card
	 * 
	 * @return
	 */
	public int getValue() {
		int value;
		try {
			value = Integer.parseInt(this.txtboxValue.getText());
		} catch (NumberFormatException e) {
			value = 0;
		}
		return value;
	}

}
