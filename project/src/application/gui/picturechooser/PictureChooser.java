/*-----------------------------------------------------------------------------+

			Filename			: PictureChooser.java
		
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
		setDialogTitle("Select a fingerprint raster image");
		
		// Initial position
		setCurrentDirectory(new java.io.File("./data"));
		
		// Set filters
		setFileSelectionMode(JFileChooser.FILES_ONLY);
		setAcceptAllFileFilterUsed(false);
		
		addChoosableFileFilter(new SimpleFilter("PNG (*.png)",".png"));
		addChoosableFileFilter(new SimpleFilter("BMP (*.bmp)",".bmp"));
		addChoosableFileFilter(new SimpleFilter("GIF (*.gif)",".gif"));
		addChoosableFileFilter(new SimpleFilter("JPEG (*.jpg)",".jpg"));
		
		// Add picture previewer
		PicturePreviewPanel preview = new PicturePreviewPanel();
		setAccessory(preview);
		addPropertyChangeListener(preview);
	}

	//------------------------------------------------------------ METHODS --//	

	//---------------------------------------------------- PRIVATE METHODS --//
}
