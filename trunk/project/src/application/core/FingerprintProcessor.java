/*-----------------------------------------------------------------------------+

			Filename			: FingerprintProcessor.java
			Creation date		: 16 déc. 07
		
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

public class FingerprintProcessor 
{
	//---------------------------------------------------------- CONSTANTS --//

	//---------------------------------------------------------- VARIABLES --//	
	
	//------------------------------------------------------- CONSTRUCTORS --//	

	//------------------------------------------------------------ METHODS --//
	
	/**
	 * Binarize a picture
	 */
	public BinaryMatrix binarizePicture (String filename)
	{
		return new BinaryMatrix(filename);
	}

	//---------------------------------------------------- PRIVATE METHODS --//
}
