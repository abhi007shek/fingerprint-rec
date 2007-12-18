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
					map[i][j] = !(greymap[i][j] > (greymean)); 
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
		
		boolean [][] newmap = new boolean [width][height];
		copyMatrix(map, newmap);
		
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
				newmap[i][j] = (val > 0.5);
			}
		}
		
		copyMatrix(newmap, map);
	}
	
	public void skeletonize()
	{
		int fstLin = 1;
		int lstLin = width - 1;
		int fstCol = 1;
		int lstCol = height - 1;
		
		boolean [][] prevM = new boolean [width][height];;
		boolean [][] newM = new boolean [width][height];;
		
		copyMatrix(map, prevM);
		
		int A, B;
		
		boolean [] neighbors;
		
		int nbTurns = 0;
		
		// We skeletonize until there are no changes between two iterations
		while (true)
		{
			nbTurns++;
			
			copyMatrix(prevM, newM);			
			
			// First subiteration, for NW and SE neigbors
			for (int i = fstLin ; i < lstLin ; ++i)
			{
				for (int j = fstCol ; j < lstCol ; ++j)
				{
					neighbors = getNeigbors(newM, i,j);
					
					// Get the decision values
					B = getSum(neighbors);
					A = getTransitions(neighbors);
					
					if ( ( B >= 2 ) && ( B <= 6 ) )
					{
						if ( A == 1 )
						{
							if ((neighbors[0] && neighbors[2] && neighbors[4]) == false )
		                    {
		                    	if ((neighbors[2] && neighbors[4] && neighbors[5]) == false )
		                        {
		                        	newM [i][j] = false;
		                        }
		                    }
						}
					}
				}
			}
			
			// Second subiteration, for NE and SW neigbors
			for (int i = fstLin ; i < lstLin ; ++i)
			{
				for (int j = fstCol ; j < lstCol ; ++j)
				{
					neighbors = getNeigbors(newM,i,j);
					
					// Get the decision values
					B = getSum(neighbors);
					A = getTransitions(neighbors);
					
					if ( ( B >= 2 ) && ( B <= 6 ) )
					{
						if ( A == 1 )
						{
							if ((neighbors[0] && neighbors[2] && neighbors[6]) == false )
		                    {
		                    	if ((neighbors[0] && neighbors[4] && neighbors[6]) == false )
		                        {
		                        	newM [i][j] = false;
		                        }
		                    }
						}
					}
				}
			}
			
			// Stop conditions		
			if (equal(newM, prevM, width, height))
			{
				break;
			}
			
			copyMatrix(newM, prevM);
		}
		
		// Return matrix
		copyMatrix(newM, map);
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
	
	private void copyMatrix (boolean [][] src, boolean [][] dst)
	{
		for (int i = 0 ; i < width ; ++i)
		{
			for (int j = 0 ; j < height ; ++j)
			{
				dst[i][j] = src[i][j];
			}
		}
	}
	
	private boolean[] getNeigbors(boolean [][] mat, int i, int j)
	{
		boolean[] neigbors = new boolean[8];
		
		neigbors[0] = mat[i-1][j+0];
		neigbors[1] = mat[i-1][j+1];
		neigbors[2] = mat[i+0][j+1];
		neigbors[3] = mat[i+1][j+1];
		neigbors[4] = mat[i+1][j+0];
		neigbors[5] = mat[i+1][j-1];
		neigbors[6] = mat[i+0][j-1];
		neigbors[7] = mat[i-1][j-1];
		
		return neigbors;
	}
	
	private int getTransitions (boolean [] neighbors)
	{
		int nbTransitions = 0;
		
		for (int k = 0 ; k < 7 ; ++k )
		{
			if ((neighbors[k] == false) && ((neighbors[k+1] == true)))
				++nbTransitions;
		}
		
		if ((neighbors[7] == false) && ((neighbors[0] == true)))
			++nbTransitions;
		
		return nbTransitions;
	}
	
	private int getSum (boolean [] vals)
	{
		int max = vals.length;
		int sum = 0;
		
		for (int k = 0 ; k < max ; ++k )
		{
			sum += booleanToInt(vals[k]);
		}
		
		return sum;
	}
	
	private boolean equal(boolean [][] A, boolean [][] B, int w, int h)
	{
		for (int i = 0 ; i < w ; ++i)
		{
			for (int j = 0 ; j < h ; ++j)
			{
				if (A[i][j] != B[i][j])
					return false;
			}
		}
		
		return true;
	}
}
