/*-----------------------------------------------------------------------------+

			Filename			: PanelPictureViewer.java
			Creation date		: 16 déc. 07
		
			Project				: fingerprint-recog
			Package				: application.gui.panels

			Developed by		: Thomas DEVAUX & Estelle SENAY
			                      (2007) Concordia University

							-------------------------

	This program is free software. You can redistribute it and/or modify it 
 	under the terms of the GNU Lesser General Public License as published by 
	the Free Software Foundation. Either version 2.1 of the License, or (at your 
    option) any later version.

	This program is distributed in the hope that it will be useful, but WITHOUT 
	ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
	FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for 
    more details.

+-----------------------------------------------------------------------------*/

package application.gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelPictureViewer extends JPanel
{
	//---------------------------------------------------------- CONSTANTS --//
	private int SPACE = 4;
	
	//---------------------------------------------------------- VARIABLES --//
	JTextField titlePanel;
	PanelFingerprint picturePanel;
	

	//------------------------------------------------------- CONSTRUCTORS --//
	public PanelPictureViewer ( String pictureName)
	{
		// Initialize values
		setBackground(new Color (0, 0, 0));
		
		// Create objects
		titlePanel = new JTextField(pictureName,10);
		picturePanel = new PanelFingerprint();
		
		titlePanel.setBackground(new Color(100,100,100));
		titlePanel.setAlignmentX(JTextField.CENTER_ALIGNMENT);
		titlePanel.setAlignmentY(JTextField.CENTER_ALIGNMENT);
		titlePanel.setFont(new Font("Arial",0,15));
		
		picturePanel.setBackground(new Color(100,100,100));
		
		// Set layouts
		GridBagLayout gbLayout = new GridBagLayout();
		setLayout(gbLayout);
		
		// Title constraints
		GridBagConstraints gbConstTitlePanel = new GridBagConstraints (	
				0,							// Column number
	            0,							// Line number
	            1,							// Nb occupied lines
	            1,							// Nb occupied columns
	            100,						// Relative horizontal space
	            10,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(0, 0, SPACE, 0), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayout.setConstraints(titlePanel, gbConstTitlePanel);
		
		// Picture constraints
		GridBagConstraints gbConstTitlePicture = new GridBagConstraints (	
				0,							// Column number
	            1,							// Line number
	            1,							// Nb occupied lines
	            1,							// Nb occupied columns
	            100,						// Relative horizontal space
	            90,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(SPACE, 0, 0, 0), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayout.setConstraints(picturePanel, gbConstTitlePicture);
		
		add(picturePanel);
		add(titlePanel);
		
		titlePanel.setLayout(new GridLayout());
		picturePanel.setLayout(new GridLayout());
	}

	//------------------------------------------------------------ METHODS --//	
	public void init()
	{
		picturePanel.init();
	}
	
	public void setFingerprint(BufferedImage buffer)
	{
		picturePanel.setBufferedImage(buffer);
	}
	
	public void setCore(Point core)
	{
		picturePanel.setCore(core);
	}

	//---------------------------------------------------- PRIVATE METHODS --//
}
