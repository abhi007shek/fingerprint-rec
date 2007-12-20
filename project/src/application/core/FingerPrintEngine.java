/*-----------------------------------------------------------------------------+

			Filename			: FingerPrintEngine.java
			Creation date		: 16 déc. 07
		
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
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import application.core.FingerPrint.direction;
import application.gui.MainFrame;

public class FingerPrintEngine implements MainFrameListener
{
	//---------------------------------------------------------- CONSTANTS --//
	private class computeThread extends Thread
	{
		public void run()
		{
			compute();
		}
	}
	
	//---------------------------------------------------------- VARIABLES --//	
	private MainFrame mainWindow;
	private String filename;
	private FingerPrint fingerprint;
	private computeThread thread;
	
	//------------------------------------------------------- CONSTRUCTORS --//	
	
	//------------------------------------------------------------ METHODS --//	
	
	public FingerPrintEngine(MainFrame mainWindow) 
	{
		this.mainWindow = mainWindow;
		thread = new computeThread();
	}
	
	
	//---------------------------------------------------- PRIVATE METHODS --//

	private void compute()
	{
		// Disable buttons
		mainWindow.setEnableButtons(false);
		
		// Create binaryPicture
		fingerprint = new FingerPrint(filename);
		
		// Print original image
		mainWindow.setImage(0, fingerprint.getOriginalImage());
		
		// Print binary result
		fingerprint.setColors(Color.black, Color.green);
		fingerprint.binarizeMean();
		mainWindow.setImage(1, fingerprint.toBufferedImage());
		
		// Print binary local result
		fingerprint.setColors(Color.black, Color.green);
		fingerprint.binarizeLocalMean();
		mainWindow.setImage(2,fingerprint.toBufferedImage());
		
		// Remove noise
		fingerprint.addBorders(1);
		fingerprint.removeNoise();
		fingerprint.removeNoise();
		fingerprint.removeNoise();
		mainWindow.setImage(3,fingerprint.toBufferedImage());
		
		// Skeletonization
		fingerprint.skeletonize();
		mainWindow.setImage(4,fingerprint.toBufferedImage());
		
		// Direction
		direction [][] dirMatrix = fingerprint.getDirections();
		BufferedImage buffer = fingerprint.directionToBufferedImage(dirMatrix);
		mainWindow.setImage(5,buffer);
		
		// Core
		Point core = fingerprint.getCore(dirMatrix);
		mainWindow.setImage(6,buffer);
		mainWindow.setCorePicture(6,core);
		
		try 
		{
			ImageIO.write(fingerprint.directionToBufferedImage(dirMatrix), "bmp", new File("C:/temp/myImage2.bmp"));
		} 
		catch (IOException e) 
		{
			// TODO ENLEVER TOUT
			e.printStackTrace();
		}
		
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
