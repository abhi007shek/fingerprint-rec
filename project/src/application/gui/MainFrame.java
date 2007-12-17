/*-----------------------------------------------------------------------------+

			Filename			: MainFrame.java
			Creation date		: 16 déc. 07
		
			Project				: fingerprint-recog
			Package				: application.gui

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

package application.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;

import application.gui.panels.PanelPictureViewer;

public class MainFrame extends JFrame
{
	//---------------------------------------------------------- CONSTANTS --//
	private int SPACE = 3;

	//---------------------------------------------------------- VARIABLES --//
	
	// Picture panels
	private PanelPictureViewer pan1Original;
	private PanelPictureViewer pan2BinaryPicture;
	private PanelPictureViewer pan3NoiseRemoval;
	private PanelPictureViewer pan4Skeleton;
	private PanelPictureViewer pan5CoreDetection;
	private PanelPictureViewer pan6Minutiae;
	
	// Buttons
	private JButton btBrowse;
	private JButton btExtract;

	//------------------------------------------------------- CONSTRUCTORS --//
	public MainFrame()
	{
		// Initialize frame
		initFrame();
		
		// Create panels
		createObjects();
	
		// Set layouts
		setLayouts();
	}

	//------------------------------------------------------------ METHODS --//	

	//---------------------------------------------------- PRIVATE METHODS --//
	private void initFrame()
	{
		setSize(1000, 200);
		setTitle("Fingerprint patern extractor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void createObjects()
	{
		// Picture panels
		pan1Original = new PanelPictureViewer("Original");
		pan2BinaryPicture = new PanelPictureViewer("Binary");
		pan3NoiseRemoval = new PanelPictureViewer("Noise removal");
		pan4Skeleton = new PanelPictureViewer("Skeleton");
		pan5CoreDetection = new PanelPictureViewer("Core");
		pan6Minutiae = new PanelPictureViewer("Minutiae");
		
		// Buttons
		btBrowse = new JButton("...");
		btExtract = new JButton("Extract !");
	}
	
	private void setLayouts()
	{
		// Add panels to the main panel		
		add(pan1Original);
		add(pan2BinaryPicture);
		add(pan3NoiseRemoval);
		add(pan4Skeleton);
		add(pan5CoreDetection);
		add(pan6Minutiae);
		
		// Add buttons
		add(btBrowse);
		add(btExtract);
		
		GridBagLayout gbLayoutPicturesPanel = new GridBagLayout();
		setLayout(gbLayoutPicturesPanel);
		
		// Button browse constraints
		GridBagConstraints gbConstBtBrowse = new GridBagConstraints (	
				0,							// Column number
	            0,							// Line number
	            1,							// Nb occupied lines
	            1,							// Nb occupied columns
	            10,						    // Relative horizontal space
	            50,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(0, SPACE, SPACE, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(btBrowse, gbConstBtBrowse);
		
		// Button extract constraints
		GridBagConstraints gbConstBtExtract = new GridBagConstraints (	
				0,							// Column number
	            1,							// Line number
	            1,							// Nb occupied lines
	            1,							// Nb occupied columns
	            10,						    // Relative horizontal space
	            50,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(0, SPACE, SPACE, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(btExtract, gbConstBtExtract);
		
		// Panel 1 constraints
		GridBagConstraints gbConstPanel1 = new GridBagConstraints (	
				1,							// Column number
	            0,							// Line number
	            1,							// Nb occupied lines
	            2,							// Nb occupied columns
	            20,							// Relative horizontal space
	            50,						// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(0, SPACE, 0, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(pan1Original, gbConstPanel1);

		// Panel 2 constraints
		GridBagConstraints gbConstPanel2 = new GridBagConstraints (	
				2,							// Column number
	            0,							// Line number
	            1,							// Nb occupied lines
	            2,							// Nb occupied columns
	            20,							// Relative horizontal space
	            100,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(0, SPACE, 0, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(pan2BinaryPicture, gbConstPanel2);
		
		// Panel 3 constraints
		GridBagConstraints gbConstPanel3 = new GridBagConstraints (	
				3,							// Column number
	            0,							// Line number
	            1,							// Nb occupied lines
	            2,							// Nb occupied columns
	            20,							// Relative horizontal space
	            100,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(0, SPACE, 0, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(pan3NoiseRemoval, gbConstPanel3);
		
		// Panel 4 constraints
		GridBagConstraints gbConstPanel4 = new GridBagConstraints (	
				4,							// Column number
	            0,							// Line number
	            1,							// Nb occupied lines
	            2,							// Nb occupied columns
	            20,							// Relative horizontal space
	            100,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(0, SPACE, 0, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(pan4Skeleton, gbConstPanel4);
		
		// Panel 5 constraints
		GridBagConstraints gbConstPanel5 = new GridBagConstraints (	
				5,							// Column number
	            0,							// Line number
	            1,							// Nb occupied lines
	            2,							// Nb occupied columns
	            20,							// Relative horizontal space
	            100,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(0, SPACE, 0, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(pan5CoreDetection, gbConstPanel5);
		
		// Panel 6 constraints
		GridBagConstraints gbConstPanel6 = new GridBagConstraints (	
				6,							// Column number
	            0,							// Line number
	            1,							// Nb occupied lines
	            2,							// Nb occupied columns
	            20,							// Relative horizontal space
	            100,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(0, SPACE, 0, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(pan6Minutiae, gbConstPanel6);
	}
}
