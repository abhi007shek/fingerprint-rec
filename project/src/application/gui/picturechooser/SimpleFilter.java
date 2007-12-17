/*-----------------------------------------------------------------------------+

			Filename			: PictureFilter.java
			Creation date		: 17 déc. 07
		
			Project				: fingerprint-recog
			Package				: application.gui.picturechooser

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

import javax.swing.filechooser.FileFilter;

public class SimpleFilter extends FileFilter
{
	//---------------------------------------------------------- CONSTANTS --//

	//---------------------------------------------------------- VARIABLES --//	
	private String description;
	private String extension;
	
	//------------------------------------------------------- CONSTRUCTORS --//	
	public SimpleFilter(String description, String extension)
	{
		this.description = description;
		this.extension = extension;
	}
	//------------------------------------------------------------ METHODS --//	
	public boolean accept(File file)
	{
		if(file.isDirectory()) 
		{ 
			return true; 
		} 
		String myFile = file.getName().toLowerCase(); 
		return myFile.endsWith(extension);
	}
	
	public String getDescription()
	{
		return description;
	}
	
	//---------------------------------------------------- PRIVATE METHODS --//
}
