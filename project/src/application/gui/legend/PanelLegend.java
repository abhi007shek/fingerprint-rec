/*-----------------------------------------------------------------------------+

			Filename			: PanelLegend.java
		
			Project				: fingerprint-recog
			Package				: application.gui.legend

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

package application.gui.legend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelLegend extends JPanel
{
	//---------------------------------------------------------- CONSTANTS --//
	private int SPACE = 4; 						// Space between objects
	
	//---------------------------------------------------------- VARIABLES --//	
	private JLabel textDir;						// Directions title
	private JLabel textMinutiae;				// Minutiae title
	private JLabel textCopyright;				// Copyright
	
	private LabelledColor colDirPos;			// Positive direction line
	private LabelledColor colDirNeg;			// Negative direction line
	private LabelledColor colDirVer;			// Vertical direction line
	private LabelledColor colDirHor;			// Horizontal direction line

	private LabelledColor colMinEnd;			// End point Minutiae line 
	private LabelledColor colMinInt;			// Intersections Minutiae line
	
	

	//------------------------------------------------------- CONSTRUCTORS --//
	
	/**
	 * Construct a panel legend
	 */
	public PanelLegend() 
	{
		// Create objects
		textDir = new JLabel("Direction legend");
		textMinutiae = new JLabel("Minutiae legend");
		textCopyright = new JLabel("design: T. Devaux & E. Senay");
		
		Font titleFont = new Font("Arial",Font.BOLD,15);
		Font copyrightFont = new Font("Arial",0,9);
		
		colDirPos = new LabelledColor (Color.green,  " Pos. (/)");
		colDirNeg = new LabelledColor (Color.yellow, " Neg. (\\)");
		colDirVer = new LabelledColor (Color.cyan,   " Ver. (|)");
		colDirHor = new LabelledColor (Color.red,    " Hor. (-)");

		colMinEnd = new LabelledColor (Color.blue, 	 " Endpoints");
		colMinInt = new LabelledColor (Color.magenta," Intersect.");
		
		// Set options
		setBackground(Color.black);
		textDir.setBackground(Color.black);
		textMinutiae.setBackground(Color.black);
		textDir.setVerticalAlignment(SwingConstants.CENTER);
		textMinutiae.setVerticalAlignment(SwingConstants.CENTER);
		textDir.setFont(titleFont);
		textMinutiae.setFont(titleFont);
		textDir.setForeground(Color.gray);
		textMinutiae.setForeground(Color.gray);
		textDir.setPreferredSize(new Dimension(1,1));	
		textMinutiae.setPreferredSize(new Dimension(1,1));
		
		setPreferredSize(new Dimension(1,1));
		
		// Copyright
		textCopyright.setVerticalAlignment(SwingConstants.CENTER);
		textCopyright.setHorizontalAlignment(SwingConstants.CENTER);
		textCopyright.setFont(copyrightFont);
		textCopyright.setForeground(Color.darkGray);
		textCopyright.setPreferredSize(new Dimension(1,1));	
		
		// Set layout
		setLayout(new GridLayout(9,1,SPACE,SPACE));
		
		// Add components
		add(textDir);
		add(colDirPos);
		add(colDirNeg);
		add(colDirVer);
		add(colDirHor);
		add(textMinutiae);
		add(colMinEnd);
		add(colMinInt);
		add(textCopyright);
	}

	//------------------------------------------------------------ METHODS --//	

	//---------------------------------------------------- PRIVATE METHODS --//
}
