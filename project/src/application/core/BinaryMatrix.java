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
	
	//---------------------------------------------------------- VARIABLES --//	
	boolean map [][] = null;
	int width;
	int height;
	
	BufferedImage originalImage;
	
	//------------------------------------------------------- CONSTRUCTORS --//		
	public BinaryMatrix (String filename)
	{
		// Open and create the buffered image
		try 
		{
			originalImage = ImageIO.read(new File(filename));
			
			// Create the binary picture
			width = originalImage.getWidth();
			height = originalImage.getHeight();
			
			
			int [][] greymap = new int [width][height];
			
			// Generate greymap
			int curColor;
			
			for (int i = 0 ; i < width ; ++i)
			{
				for (int j = 0 ; j < height ; ++j)
				{
					curColor = originalImage.getRGB(i,j);
					int R = (curColor >>16 ) & 0xFF;
					int G = (curColor >> 8 ) & 0xFF;
					int B = (curColor      ) & 0xFF;
					
					int greyVal = (R + G + B) / 3;
					greymap[i][j] = greyVal;
				}
			}
			
			// Get the grey mean
			int greymean = getGreylevelMean(greymap, width, height);
			
			// Generate the boolean value
			map = new boolean [width][height];
			for (int i = 0 ; i < width ; ++i)
			{
				for (int j = 0 ; j < height ; ++j)
				{
					map[i][j] = !(greymap[i][j] > greymean); 
				}
			}
		}
		catch (IOException e) 
		{
			originalImage = null;
			e.printStackTrace();
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
			System.out.println("");
		}
		
		return bufferedImage;
	}
	
	public BufferedImage getOriginalImage() 
	{
		return originalImage;
	}

	//---------------------------------------------------- PRIVATE METHODS --//
	private int getGreylevelMean( int [][] greymap, int w, int h)
	{
		int total = 0;
		for (int i = 0 ; i < w ; ++i)
		{
			for (int j = 0 ; j < h ; ++j)
			{
				total += greymap[i][j]; 
			}
		}
		
		return total/(w*h);
	}
}
