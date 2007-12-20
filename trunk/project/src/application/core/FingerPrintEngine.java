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
//import java.io.File;
//import java.io.IOException;
//
//import javax.imageio.ImageIO;

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
		Point [] minutiaeMatrix = fingerprint.getMinutiae(core, coreRadius);
		// TODO
		mainWindow.setIsWorking(7, false);		
//		try 
//		{
//			ImageIO.write(fingerprint.directionToBufferedImage(dirMatrix), "bmp", new File("C:/temp/myImage2.bmp"));
//		} 
//		catch (IOException e) 
//		{
//			// TODO ENLEVER TOUT
//			e.printStackTrace();
//		}
//		
//		// Enable buttons
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
