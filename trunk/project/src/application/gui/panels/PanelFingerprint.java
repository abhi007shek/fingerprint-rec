/*-----------------------------------------------------------------------------+

			Filename			: PanelFingerprint.java
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

package application.gui.panels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PanelFingerprint extends JPanel
{
	//---------------------------------------------------------- CONSTANTS --//

	//---------------------------------------------------------- VARIABLES --//	
	BufferedImage buffer;
	
	//------------------------------------------------------- CONSTRUCTORS --//	

	//------------------------------------------------------------ METHODS --//	
	public void setBufferedImage (BufferedImage buffer)
	{
		this.buffer = buffer;
		repaint();
	}
	//---------------------------------------------------- PRIVATE METHODS --//
	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		Graphics2D g2d=(Graphics2D) g;
		
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						     RenderingHints.VALUE_INTERPOLATION_BICUBIC); 
		if (buffer != null)
		{
			g.drawImage(buffer,0,0,getWidth(), getHeight(), this);
		}
	}
}
