/*-----------------------------------------------------------------------------+

			Filename			: MainFrame.java
		
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
import java.awt.Point;
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
import application.gui.legend.PanelLegend;
import application.gui.panels.PanelPictureViewer;
import application.gui.picturechooser.PictureChooser;

public class MainFrame extends JFrame
{
	//---------------------------------------------------------- CONSTANTS --//
	private int SPACE = 3;							// Space between components

	//---------------------------------------------------------- VARIABLES --//
	
	private JPanel bkgPanel;						// Main panel
	private PanelPictureViewer [] pictureViewers;	// Picture panels
	private PanelLegend legend;						// Legend
	private JButton btBrowse;						// Buttons
	private JButton btExtract;
	private PictureChooser pictureChooser;			// Picture chooser
	
	private Collection<MainFrameListener> listeners;// Listeners
	 
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
	/**
	 * Initialize frame
	 */
	public void init()
	{
		for (int i = 0 ; i < pictureViewers.length ; ++i)
		{
			pictureViewers[i].init();
		}
	}
	
	/**
	 * Set the image of the i-est fingerprint
	 * @param i fingerprint index (1-7)
	 * @param image buffered image
	 */
	public void setImage (int i, BufferedImage image)
	{
		pictureViewers[i].setFingerprint(image);
	}
	
	/**
	 * Tell if the i-est fingerprint is computing
	 * @param i fingerprint index (1-7)
	 * @param isWorking true if is working
	 */
	public void setIsWorking (int i, boolean isWorking)
	{
		pictureViewers[i].setIsWorking(isWorking);
	}

	/**
	 * Add a main frame listener
	 * @param listener listener
	 */
	public void addMainFrameListener(MainFrameListener listener) 
	{
        listeners.add(listener);
    }
	
	/**
	 * Set the core of the i-est fingerprint
	 * @param i fingerprint index (1-7)
	 * @param core 
	 * @param coreRadius
	 */
	public void setCore(int i, Point core, int coreRadius)
	{
		pictureViewers[i].setCore(core,coreRadius);
	}	
	
	public void setEnableButtons (boolean enabled)
	{
		btBrowse.setEnabled(enabled);
		btExtract.setEnabled(enabled);
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
		setSize(1024, 600);
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
		pictureViewers = new PanelPictureViewer[8];
		
		pictureViewers[0] = new PanelPictureViewer("1.Original");
		pictureViewers[1] = new PanelPictureViewer("2.Binary (mean)");
		pictureViewers[2] = new PanelPictureViewer("3.Binary (local)");
		pictureViewers[3] = new PanelPictureViewer("4.Smoothed");
		pictureViewers[4] = new PanelPictureViewer("5.Skeleton");
		pictureViewers[5] = new PanelPictureViewer("6.Direction");
		pictureViewers[6] = new PanelPictureViewer("7.Core");
		pictureViewers[7] = new PanelPictureViewer("8.Minutiae");
		
		// Legend
		legend = new PanelLegend();
		
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
		for (int i = 0 ; i < pictureViewers.length ; ++i)
		{
			bkgPanel.add(pictureViewers[i]);
		}
		
		// Add buttons
		bkgPanel.add(btBrowse);
		bkgPanel.add(btExtract);
		
		// Add legend
		bkgPanel.add(legend);
		
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
	            10,						    // Relative horizontal space
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
	            10,						    // Relative horizontal space
	            25,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(SPACE, SPACE, SPACE, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(btExtract, gbConstBtExtract);
		
		// Panel legend
		GridBagConstraints gbConstPanelLegend = new GridBagConstraints (	
				0,							// Column number
	            2,							// Line number
	            1,							// Nb occupied lines
	            2,							// Nb occupied columns
	            10,							// Relative horizontal space
	            25,							// Relative vertical space
	            GridBagConstraints.CENTER,	// Where to place component when resizing
	            GridBagConstraints.BOTH,	// How to rescale component
	            new Insets(SPACE, SPACE, SPACE, SPACE), // Spaces (top, left, bottom, right)
	            0,							// In space X
	            0							// In space Y
	    );
		gbLayoutPicturesPanel.setConstraints(legend, gbConstPanelLegend);
		
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
		gbLayoutPicturesPanel.setConstraints(pictureViewers[0], gbConstPanel1);

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
		gbLayoutPicturesPanel.setConstraints(pictureViewers[1], gbConstPanel2);
		
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
		gbLayoutPicturesPanel.setConstraints(pictureViewers[2], gbConstPanel3);
		
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
		gbLayoutPicturesPanel.setConstraints(pictureViewers[3], gbConstPanel4);
		
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
		gbLayoutPicturesPanel.setConstraints(pictureViewers[4], gbConstPanel5);
		
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
		gbLayoutPicturesPanel.setConstraints(pictureViewers[5], gbConstPanel6);
		
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
		gbLayoutPicturesPanel.setConstraints(pictureViewers[6], gbConstPanel7);
		
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
		gbLayoutPicturesPanel.setConstraints(pictureViewers[7], gbConstPanel8);
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
			btExtract.setEnabled(true);
		}
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
