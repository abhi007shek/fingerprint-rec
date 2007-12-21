/*-----------------------------------------------------------------------------+

			Filename			: PicturePreviewPanel.java
		
			Project				: fingerprint-recog
			Package				: application.gui.picturechooser

			Developed by		: Thomas DEVAUX & Estelle SENAY
			                      (2007) Concordia University
			                      
			                      Adapted from R.J. Lorimer
			                      ("Use an Accessory to Spice Up JFileChooser", 
			                      http://www.javalobby.org/forums/thread.jspa?messageID=91844740)

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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PicturePreviewPanel extends JPanel implements PropertyChangeListener
{
	//---------------------------------------------------------- CONSTANTS --//

	//---------------------------------------------------------- VARIABLES --//	
	private JLabel label;
	private int maxImgWidth;
	
	//------------------------------------------------------- CONSTRUCTORS --//	

	//------------------------------------------------------------ METHODS --//	

	//---------------------------------------------------- PRIVATE METHODS --//
	

	public PicturePreviewPanel() 
	{
		setLayout(new BorderLayout(5,5));
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		add(new JLabel("Preview:"), BorderLayout.NORTH);
		label = new JLabel();
		label.setOpaque(false);
		label.setPreferredSize(new Dimension(200, 200));
		maxImgWidth = 195;
		label.setBorder(BorderFactory.createEtchedBorder());
		add(label, BorderLayout.CENTER);
	}

	public void propertyChange(PropertyChangeEvent evt) 
	{
		Icon icon = null;
		if(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt.getPropertyName())) 
		{
			File newFile = (File) evt.getNewValue();
			if(newFile != null) 
			{
				try 
				{
					BufferedImage img = ImageIO.read(newFile);
					float width = img.getWidth();
					float height = img.getHeight();
					float scale = height / width;
					width = maxImgWidth;
					height = (width * scale); // height should be scaled from new width							
					icon = new ImageIcon(img.getScaledInstance(Math.max(1, (int)width), Math.max(1, (int)height), Image.SCALE_SMOOTH));
				}
				catch(IOException e) 
				{
					// couldn't read image.
				}
			}	
			label.setIcon(icon);
			this.repaint();	
		}
	}
	
}
