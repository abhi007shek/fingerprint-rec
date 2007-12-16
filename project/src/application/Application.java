package application;

import javax.swing.UIManager;

import application.gui.MainFrame;


/*-----------------------------------------------------------------------------+

			Filename			: Application.java
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

public class Application 
{

	
	//---------------------------------------------------------- CONSTANTS --//

	//---------------------------------------------------------- VARIABLES --//	
	private static MainFrame mainWindow;
	
	//------------------------------------------------------- CONSTRUCTORS --//	

	//------------------------------------------------------------ METHODS --//	
	public static void main(String[] args) 
	{
		// Set the style
		try
		{
			UIManager.setLookAndFeel( "de.javasoft.plaf.synthetica.SyntheticaBlueMoonLookAndFeel"  );
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		// Create the main frame
		mainWindow = new MainFrame();
		
		
		
		
		// Show the window
		mainWindow.setVisible(true);
	}
	//---------------------------------------------------- PRIVATE METHODS --//
}
