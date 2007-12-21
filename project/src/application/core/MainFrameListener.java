/*-----------------------------------------------------------------------------+

			Filename			: MainFrameListener.java
		
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

public interface MainFrameListener 
{
	/**
	 * Executed when a new file is selected
	 * @param filename filename (full path)
	 */
	public void newPictureFile(String filename);
	
	/**
	 * Called when extraction starts
	 */
	public void startExtraction();
}
