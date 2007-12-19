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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import application.core.MainFrameListener;
import application.gui.panels.PanelPictureViewer;
import application.gui.picturechooser.PictureChooser;

public class MainFrame extends JFrame
{
	//---------------------------------------------------------- CONSTANTS --//
	private int SPACE = 3;

	//---------------------------------------------------------- VARIABLES --//
	
	// Main panel
	private JPanel bkgPanel;
	
	// Picture panels
	private PanelPictureViewer panOriginal;
	private PanelPictureViewer panBinaryPicture;
	private PanelPictureViewer panBinaryLocalPicture;
	private PanelPictureViewer panNoiseRemoval;
	private PanelPictureViewer panSkeleton;
	private PanelPictureViewer panDirection;
	private PanelPictureViewer panCore;
	private PanelPictureViewer panMinutiae;
	
	// Buttons
	private JButton btBrowse;
	private JButton btExtract;
	
	// Picture chooser
	private PictureChooser pictureChooser;
	
	// Listeners
	private Collection<MainFrameListener> listeners;
	 
	//------------------------------------------------------- CONSTRUCTORS --//
	public MainFrame()
	{
		// Initialize frame
		initFrame();
		
		// Create panels
		createObjects();
	
		// Set layouts
		setLayouts();
		
		// Define button behaviour
		initButtons();
	}

	//------------------------------------------------------------ METHODS --//	
	public void addMainFrameListener(MainFrameListener listener) 
	{
        listeners.add(listener);
    }
	
	public void setOriginalImage(BufferedImage image)
	{
		panOriginal.setFingerprint(image);
	}
	
	public void setBinaryLocalPicture(BufferedImage image)
	{
		panBinaryLocalPicture.setFingerprint(image);
	}

	public void setBinaryPicture(BufferedImage image)
	{
		panBinaryPicture.setFingerprint(image);
	}

	public void setSmoothedPicture(BufferedImage image)
	{
		panNoiseRemoval.setFingerprint(image);
	}

	public void setSkeletonPicture(BufferedImage image)
	{
		panSkeleton.setFingerprint(image);
	}
	
	public void setDirectionPicture(BufferedImage image)
	{
		panDirection.setFingerprint(image);
	}	
	
	public void setCorePicture(BufferedImage image)
	{
		panCore.setFingerprint(image);
	}	

	//---------------------------------------------------- PRIVATE METHODS --//
	private void initButtons()
	{
		// Initial state
		btExtract.setEnabled(false);
		
		// Set actions
		btExtract.addActionListener(	new ActionListener() {
			        					public void actionPerformed(ActionEvent e) 
			        					{onBtExtractPressed();}});
		
		btBrowse.addActionListener(	new ActionListener() {
									public void actionPerformed(ActionEvent e) 
									{onBtBrowsePressed();}});
	}
	
	private void initFrame()
	{
		setSize(800, 600);
		setTitle("Fingerprint pattern extractor");

		// Icon
		Image icon = Toolkit.getDefaultToolkit().getImage("./ressources/fp_icon.png");
		setIconImage(icon);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBackground(Color.black);
	}
	
	private void createObjects()
	{
		// Main panel
		bkgPanel = new JPanel();
		
		// Picture panels
		panOriginal = new PanelPictureViewer("1.Original");
		panBinaryPicture = new PanelPictureViewer("2.Binary (mean)");
		panBinaryLocalPicture = new PanelPictureViewer("3.Binary (local)");
		panNoiseRemoval = new PanelPictureViewer("4.Smoothed");
		panSkeleton = new PanelPictureViewer("5.Skeleton");
		panDirection = new PanelPictureViewer("6.Direction");
		panCore = new PanelPictureViewer("7.Core");
		panMinutiae = new PanelPictureViewer("8.Minutiae");
		
		// Buttons
		btBrowse = new JButton("...");
		btExtract = new JButton("Extract !");
		
		// Picture chooser
		pictureChooser = new PictureChooser();
		
		// Listeners
		listeners = new ArrayList<MainFrameListener>();
	}
	
	private void setLayouts()
	{
		// Add panels to the main panel		
		bkgPanel.add(panOriginal);
		bkgPanel.add(panBinaryPicture);
		bkgPanel.add(panBinaryLocalPicture);
		bkgPanel.add(panNoiseRemoval);
		bkgPanel.add(panSkeleton);
		bkgPanel.add(panDirection);
		bkgPanel.add(panCore);
		bkgPanel.add(panMinutiae);
		
		// Add buttons
		bkgPanel.add(btBrowse);
		bkgPanel.add(btExtract);
		
		bkgPanel.setLayout(new BorderLayout());
		bkgPanel.setBackground(Color.black);
		
		add (bkgPanel);
		
		GridBagLayout gbLayoutPicturesPanel = new GridBagLayout();
		bkgPanel.setLayout(gbLayoutPicturesPanel);
		
		// Button browse constraints
		GridBagConstraints gbConstBtBrowse = new GridBagConstraints (	
				0,							// Column number
	            0,							// Line number
	            1,							// Nb occupied lines
	            1,							// Nb occupied columns
	            4,						    // Relative horizontal space
	            25,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(SPACE, SPACE, SPACE, SPACE), // Spaces (top, left, bottom, right)
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
	            4,						    // Relative horizontal space
	            25,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(SPACE, SPACE, SPACE, SPACE), // Spaces (top, left, bottom, right)
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
	            25,						// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(SPACE, SPACE, SPACE, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(panOriginal, gbConstPanel1);

		// Panel 2 constraints
		GridBagConstraints gbConstPanel2 = new GridBagConstraints (	
				2,							// Column number
	            0,							// Line number
	            1,							// Nb occupied lines
	            2,							// Nb occupied columns
	            20,							// Relative horizontal space
	            25,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(SPACE, SPACE, SPACE, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(panBinaryPicture, gbConstPanel2);
		
		// Panel 3 constraints
		GridBagConstraints gbConstPanel3 = new GridBagConstraints (	
				3,							// Column number
	            0,							// Line number
	            1,							// Nb occupied lines
	            2,							// Nb occupied columns
	            20,							// Relative horizontal space
	            50,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(SPACE, SPACE, SPACE, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(panBinaryLocalPicture, gbConstPanel3);
		
		// Panel 4 constraints
		GridBagConstraints gbConstPanel4 = new GridBagConstraints (	
				4,							// Column number
	            0,							// Line number
	            2,							// Nb occupied lines
	            2,							// Nb occupied columns
	            20,							// Relative horizontal space
	            50,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(SPACE, SPACE, SPACE, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(panNoiseRemoval, gbConstPanel4);
		
		// Panel 5 constraints
		GridBagConstraints gbConstPanel5 = new GridBagConstraints (	
				1,							// Column number
	            2,							// Line number
	            1,							// Nb occupied lines
	            2,							// Nb occupied columns
	            20,							// Relative horizontal space
	            50,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(SPACE, SPACE, SPACE, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(panSkeleton, gbConstPanel5);
		
		// Panel 6 constraints
		GridBagConstraints gbConstPanel6 = new GridBagConstraints (	
				2,							// Column number
	            2,							// Line number
	            1,							// Nb occupied lines
	            2,							// Nb occupied columns
	            20,							// Relative horizontal space
	            50,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(SPACE, SPACE, SPACE, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(panDirection, gbConstPanel6);
		
		// Panel 7 constraints
		GridBagConstraints gbConstPanel7 = new GridBagConstraints (	
				3,							// Column number
	            2,							// Line number
	            1,							// Nb occupied lines
	            2,							// Nb occupied columns
	            20,							// Relative horizontal space
	            50,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(SPACE, SPACE, SPACE, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(panCore, gbConstPanel7);
		
		// Panel 8 constraints
		GridBagConstraints gbConstPanel8 = new GridBagConstraints (	
				4,							// Column number
	            2,							// Line number
	            1,							// Nb occupied lines
	            2,							// Nb occupied columns
	            20,							// Relative horizontal space
	            50,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(SPACE, SPACE, SPACE, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(panMinutiae, gbConstPanel8);
	}
	
	private void onBtBrowsePressed()
	{
		// Open the picture chooser dialog
		int choice;
		choice = pictureChooser.showOpenDialog(this);
		
		if(choice == JFileChooser.APPROVE_OPTION) 
		{
			File picFile = pictureChooser.getSelectedFile();
			fireNewPictureFile(picFile.getAbsolutePath());
		}
		
		btExtract.setEnabled(true);
	}
	
	private void onBtExtractPressed()
	{
		fireStartExtraction();
	}
	
	private void fireNewPictureFile(String filename)
	{
		for(MainFrameListener listener : listeners) 
		{
			listener.newPictureFile(filename);
        }
	}
	
	private void fireStartExtraction()
	{
		for(MainFrameListener listener : listeners) 
		{
			listener.startExtraction();
        }
	}
	
	//-------------------------------------------------------- PRIVATE TYPES --//
}
