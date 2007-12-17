/*-----------------------------------------------------------------------------+

			Filename			: PictureChooser.java
			Creation date		: 17 déc. 07
		
			Project				: fingerprint-recog
			Package				: application.gui.panels

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

package application.gui.picturechooser;

import java.io.File;

import javax.swing.JFileChooser;

public class PictureChooser extends JFileChooser
{
	//---------------------------------------------------------- CONSTANTS --//

	//---------------------------------------------------------- VARIABLES --//	

	//------------------------------------------------------- CONSTRUCTORS --//	
	public PictureChooser()
	{
		// Initialize properties
		setMultiSelectionEnabled(false);
		
		// Initial position
		setCurrentDirectory(new File("/"));
		changeToParentDirectory();
		
		// Set filters
		addChoosableFileFilter(new SimpleFilter("BMP (*.bmp)",".bmp"));
		addChoosableFileFilter(new SimpleFilter("GIF (*.gif)",".gif"));
		addChoosableFileFilter(new SimpleFilter("JPEG (*.jpg)",".jpg"));
		addChoosableFileFilter(new SimpleFilter("PNG (*.png)",".png"));
		addChoosableFileFilter(new SimpleFilter("TIFF (*.tif)",".tif"));
		
		// Add picture previewer
		PicturePreviewPanel preview = new PicturePreviewPanel();
		setAccessory(preview);
		addPropertyChangeListener(preview);
	}

	//------------------------------------------------------------ METHODS --//	

	//---------------------------------------------------- PRIVATE METHODS --//
}
