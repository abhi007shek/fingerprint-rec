/*-----------------------------------------------------------------------------+

			Filename			: Matrix.java
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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class BinaryMatrix
{	//---------------------------------------------------------- CONSTANTS --//
	private int BLACK_WHITE_BOUNDARY = 100;
	
	//---------------------------------------------------------- VARIABLES --//	
	boolean map [][] = null;
	int width;
	int height;
	
	//------------------------------------------------------- CONSTRUCTORS --//		
	public BinaryMatrix (String filename)
	{
		// Open and create the buffered image
		BufferedImage mapImage;
		try 
		{
			mapImage = ImageIO.read(new File(filename));
		}
		catch (IOException e) 
		{
			mapImage = null;
			e.printStackTrace();
		}
		
		// Create the binary picture
		width = mapImage.getWidth();
		height = mapImage.getHeight();
		
		// Iterate on each pixel
		int curColor;
		
		for (int i = 0 ; i < width ; ++i)
		{
			for (int j = 0 ; j < height ; ++j)
			{
				curColor = mapImage.getRGB(i,j);
				int R = (curColor >>16 ) & 0xFF;
				int G = (curColor >> 8 ) & 0xFF;
				int B = (curColor      ) & 0xFF;
				
				int greyVal = (R + G + B) / 3;
				if (greyVal < BLACK_WHITE_BOUNDARY)
				{
					map[i][j] = false;
				}
				else
				{
					map[i][j] = true;
				}
			}
		}
	}

	//------------------------------------------------------------ METHODS --//	
	boolean get(int x, int y)
	{
		return map[x][y];
	}
	
	void set(int x, int y, boolean val)
	{
		map[x][y] = val;
	}

	public int getWidth() 
	{
		return width;
	}

	public int getHeight() 
	{
		return height;
	}
	
	public BufferedImage toBufferedImage()
	{
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		for (int i = 0 ; i < width ; ++i)
		{
			for (int j = 0 ; j < height ; ++j)
			{
				if (map[i][j] == false)
				{
					bufferedImage.setRGB(i, j, Color.black.getRGB());
				}
				else
				{
					bufferedImage.setRGB(i, j, Color.white.getRGB());
				}
			}
		}
		
		return bufferedImage;
	}
	
	//---------------------------------------------------- PRIVATE METHODS --//
}
