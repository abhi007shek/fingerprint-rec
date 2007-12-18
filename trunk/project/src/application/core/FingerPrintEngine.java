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
//import java.io.File;
//import java.io.IOException;
//
//import javax.imageio.ImageIO;

import application.gui.MainFrame;

public class FingerPrintEngine implements MainFrameListener
{
	//---------------------------------------------------------- CONSTANTS --//

	//---------------------------------------------------------- VARIABLES --//	
	private MainFrame mainWindow;
	private String filename;
	private BinaryMatrix binaryPicture;
	
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
		binaryPicture = new BinaryMatrix(filename);
		
		// Print original image
		mainWindow.setOriginalImage(binaryPicture.getOriginalImage());
		
		// Print binary result
		binaryPicture.setColors(Color.black, Color.green);
		mainWindow.setBinaryPicture(binaryPicture.toBufferedImage());
		
		// Remove noise
		binaryPicture.addBorders(1);
		binaryPicture.removeNoise();
		mainWindow.setSmoothedPicture(binaryPicture.toBufferedImage());
		
		// Skeletonization
		binaryPicture.skeletonize();
		mainWindow.setSkeletonPicture(binaryPicture.toBufferedImage());
		
//		try 
//		{
//			ImageIO.write(binaryPicture.toBufferedImage(), "bmp", new File("C:/temp/myImage2.bmp"));
//		} catch (IOException e) {
//			// TODO ENLEVER TOUT
//			e.printStackTrace();
//		}
		
		// TODO
	}
	
	@Override
	public void newPictureFile(String filename) 
	{
		this.filename = filename;
	}

}
