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

	//---------------------------------------------------------- VARIABLES --//	
	private MainFrame mainWindow;
	private String filename;
	private FingerPrint fingerprint;
	
	//------------------------------------------------------- CONSTRUCTORS --//	

	//------------------------------------------------------------ METHODS --//	
	
	public FingerPrintEngine(MainFrame mainWindow) 
	{
		this.mainWindow = mainWindow;
	}
	
	
	//---------------------------------------------------- PRIVATE METHODS --//

	
	@Override
	public void startExtraction() 
	{		
		// Create binaryPicture
		fingerprint = new FingerPrint(filename);
		
		// Print original image
		mainWindow.setOriginalImage(fingerprint.getOriginalImage());
		
		// Print binary result
		fingerprint.setColors(Color.black, Color.green);
		fingerprint.binarizeMean();
		mainWindow.setBinaryPicture(fingerprint.toBufferedImage());
		
		// Print binary local result
		fingerprint.setColors(Color.black, Color.green);
		fingerprint.binarizeLocalMean();
		mainWindow.setBinaryLocalPicture(fingerprint.toBufferedImage());
		
		// Remove noise
		fingerprint.addBorders(1);
		fingerprint.removeNoise();
		mainWindow.setSmoothedPicture(fingerprint.toBufferedImage());
		
		// Skeletonization
		fingerprint.skeletonize();
		mainWindow.setSkeletonPicture(fingerprint.toBufferedImage());
		
		// Direction
		direction [][] dirMatrix = fingerprint.getDirections();
		BufferedImage buffer = fingerprint.directionToBufferedImage(dirMatrix);
		mainWindow.setDirectionPicture(buffer);
		
		// Core
		Point core = fingerprint.getCore(dirMatrix);
		buffer.getGraphics().fillOval(core.x, core.y, 10, 10);
		mainWindow.setCorePicture(buffer);
		
		try 
		{
			ImageIO.write(fingerprint.directionToBufferedImage(dirMatrix), "bmp", new File("C:/temp/myImage2.bmp"));
		} catch (IOException e) {
			// TODO ENLEVER TOUT
			e.printStackTrace();
		}
		
		// TODO
	}
	
	@Override
	public void newPictureFile(String filename) 
	{
		this.filename = filename;
	}

}
