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
	Color DEFAULT_ZERO_COLOR = Color.black;
	Color DEFAULT_ONE_COLOR = Color.pink;
	//---------------------------------------------------------- VARIABLES --//	
	boolean map [][] = null;
	int width;
	int height;
	Color zeroColor;
	Color oneColor;
	
	BufferedImage originalImage;
	
	//------------------------------------------------------- CONSTRUCTORS --//		
	public BinaryMatrix (String filename)
	{
		// Initialize colors
		zeroColor = DEFAULT_ZERO_COLOR;
		oneColor = DEFAULT_ONE_COLOR;
		
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
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0 ; i < width ; ++i)
		{
			for (int j = 0 ; j < height ; ++j)
			{
				if (map[i][j] == false)
				{
					bufferedImage.setRGB(i, j, zeroColor.getRGB());
				}
				else
				{
					bufferedImage.setRGB(i, j, oneColor.getRGB());
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
	
	public void setColors(Color zeroColor, Color oneColor)
	{
		this.zeroColor = zeroColor;
		this.oneColor = oneColor;
	}
	
	public void addBorders(int size)
	{
		boolean [][] newmap = new boolean [width + 2*size][height + 2*size];
		
		int limitTop = size;
		int limitLeft = size;
		int limitRight = width;
		int limitBottom = height;
		
		width += 2*size;
		height += 2*size;
		
		for (int i = 0 ; i < width ; ++i)
		{
			for (int j = 0 ; j < height ; ++j)
			{
				if ( i < limitLeft || i > limitRight || j < limitTop || j > limitBottom)
				{
					newmap[i][j] = false;
				}
				else
				{
					newmap[i][j] = map [i-size][j-size];
				}
			}
		}
		
		map = newmap;
	}
	
	public void removeNoise()
	{
		float [][] filter = {	{0.0625f,0.1250f,0.0625f},
								{0.1250f,0.2500f,0.1250f},
								{0.0625f,0.1250f,0.0625f}};
		
		int limWidth  = width -1;
		int limHeight = height-1;
		
		for (int i = 1 ; i < limWidth ; ++i)
		{
			for (int j = 1 ; j < limHeight ; ++j)
			{
				float val = 0;
				for (int ik = -1 ; ik <= 1 ; ++ik)
		        {
			        for (int jk = -1 ; jk <= 1 ; ++jk)
			        {
			        	val += booleanToInt(map[i+ik][j+jk])*filter[1+ik][1+jk];
			        }
		        }
				map[i][j] = (val > 0.5);
			}
		}
	}
	
	public void skeletonize()
	{
		
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
	
	private int booleanToInt(boolean b)
	{
		return (b==true)?1:0;
	}	
}
