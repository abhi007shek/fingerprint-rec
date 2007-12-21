/*-----------------------------------------------------------------------------+

			Filename			: FingerPrintEngine.java
		
			Project				: fingerprint-recog
			Package				: application

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
package application.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import application.core.FingerPrint.direction;
import application.gui.MainFrame;

public class FingerPrintEngine implements MainFrameListener
{
	//------------------------------------------------------------- TYPES --//
	
	// Thread that only compute fingerprint images
	private class computeThread extends Thread
	{
		public void run()
		{
			compute();
		}
	}
	
	//---------------------------------------------------------- CONSTANTS --//
	
	//---------------------------------------------------------- VARIABLES --//	
	private MainFrame mainWindow;				// Main frame
	private String filename;					// Filename of the current fingerprint
	private FingerPrint fingerprint;			// Current fingerprint
	private computeThread thread;				// Computation thread
	
	//------------------------------------------------------- CONSTRUCTORS --//	
	
	//------------------------------------------------------------ METHODS --//	
	
	/**
	 * Construct the fingerprint engine from a given window
	 * @param the main window
	 */
	public FingerPrintEngine(MainFrame mainWindow) 
	{
		this.mainWindow = mainWindow;
		thread = new computeThread();
	}	
	
	//---------------------------------------------------- PRIVATE METHODS --//

	/**
	 * Compute all the fingerprint data and synchronize with the GUI
	 */
	private void compute()
	{
		// Disable buttons
		mainWindow.setEnableButtons(false);
	
		// Create binaryPicture
		fingerprint = new FingerPrint(filename);
		
		BufferedImage buffer;
		
		// Print original image
		mainWindow.setIsWorking(0, true);
		buffer = fingerprint.getOriginalImage();
		mainWindow.setImage(0, buffer);
		mainWindow.setIsWorking(0, false);
		
		// Print binary result
		mainWindow.setIsWorking(1, true);
		fingerprint.setColors(Color.black, Color.green);
		fingerprint.binarizeMean();
		buffer = fingerprint.toBufferedImage();
		mainWindow.setIsWorking(1, false);
		mainWindow.setImage(1, buffer);
		
		// Print binary local result
		mainWindow.setIsWorking(2, true);
		fingerprint.setColors(Color.black, Color.green);
		fingerprint.binarizeLocalMean();
		buffer = fingerprint.toBufferedImage();
		mainWindow.setIsWorking(2, false);
		mainWindow.setImage(2,buffer);
		
		// Remove noise
		mainWindow.setIsWorking(3, true);
		fingerprint.addBorders(1);
		fingerprint.removeNoise();
		fingerprint.removeNoise();
		fingerprint.removeNoise();
		buffer = fingerprint.toBufferedImage();
		mainWindow.setIsWorking(3, false);
		mainWindow.setImage(3,buffer);
		
		// Skeletonization
		mainWindow.setIsWorking(4, true);
		fingerprint.skeletonize();
		mainWindow.setIsWorking(4, false);
		mainWindow.setImage(4,fingerprint.toBufferedImage());
		
		// Direction
		mainWindow.setIsWorking(5, true);
		direction [][] dirMatrix = fingerprint.getDirections();
		buffer = fingerprint.directionToBufferedImage(dirMatrix);
		mainWindow.setIsWorking(5, false);
		mainWindow.setImage(5,buffer);
		
		// Core
		mainWindow.setIsWorking(6, true);
		buffer = fingerprint.directionToBufferedImage(dirMatrix);
		Point core = fingerprint.getCore(dirMatrix);
		mainWindow.setImage(6,buffer);
		int coreRadius = buffer.getWidth() / 3;
		mainWindow.setIsWorking(6, false);
		mainWindow.setCore(6,core, coreRadius);
		
		// Minutiaes
		mainWindow.setIsWorking(7, true);
		buffer = fingerprint.directionToBufferedImage(dirMatrix);
		mainWindow.setCore(7,core, coreRadius);
		ArrayList<Point> intersections = fingerprint.getMinutiaeIntersections(core, coreRadius);
		ArrayList<Point> endPoints = fingerprint.getMinutiaeEndpoints(core, coreRadius);
		
		// Add intersections
		Graphics g = buffer.getGraphics();
		g.setColor(Color.magenta);
		for (Point point : intersections)
		{
			g.fillOval(point.x-2, point.y-2, 5, 5);
		}
		
		g.setColor(Color.blue);
		for (Point point : endPoints)
		{
			g.fillOval(point.x-2, point.y-2, 5,5);
		}
		
		// Add to buffer
		mainWindow.setImage(7,buffer);
		mainWindow.setIsWorking(7, false);		
		
		// Enable buttons
		mainWindow.setEnableButtons(true);
		
	}
	
	@Override
	public void startExtraction() 
	{	
		thread = new computeThread();
		thread.start();
	}
	
	@Override
	public void newPictureFile(String filename) 
	{
		this.filename = filename;
		mainWindow.init();
	}

}
