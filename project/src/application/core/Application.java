/*-----------------------------------------------------------------------------+

			Filename			: Application.java
		
			Project				: fingerprint-recog
			Package				: application.core

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

import javax.swing.JFrame;
import javax.swing.UIManager;

import application.gui.MainFrame;

public class Application 
{
	//---------------------------------------------------------- CONSTANTS --//

	//---------------------------------------------------------- VARIABLES --//	
	private static MainFrame mainWindow;
	private static FingerPrintEngine fingerPrintEngine;
	
	//------------------------------------------------------- CONSTRUCTORS --//	

	//------------------------------------------------------------ METHODS --//	
	public static void main(String[] args)
	{
		// Set style
		setStyle();
	    
		// Create the main frame
		mainWindow = new MainFrame();
		
		// Create objects
		fingerPrintEngine = new FingerPrintEngine(mainWindow);
		mainWindow.addMainFrameListener(fingerPrintEngine);	
		
		// Set full screen
		mainWindow.setExtendedState(mainWindow.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		
		// Show the window
		mainWindow.setVisible(true);
	}
	
	//---------------------------------------------------- PRIVATE METHODS --//
	private static void setStyle()
	{	
	    try 
	    {	    	
	    	UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel");
	    } 
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	}
}
