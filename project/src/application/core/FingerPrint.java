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
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class FingerPrint

{
	public enum direction { NONE, HORIZONTAL, VERTICAL, POSITIVE, NEGATIVE};
	//---------------------------------------------------------- CONSTANTS --//
	Color DEFAULT_ZERO_COLOR = Color.black;
	Color DEFAULT_ONE_COLOR = Color.pink;
	//---------------------------------------------------------- VARIABLES --//	
	boolean binMap [][];
	int [][] greyMap;
	int greymean;
	
	int width;
	int height;
	Color zeroColor;
	Color oneColor;
	
	BufferedImage originalImage;
	
	//------------------------------------------------------- CONSTRUCTORS --//		
	public FingerPrint (String filename)
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
			
			
			greyMap = new int [width][height];
			binMap = new boolean [width][height];
			
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
					greyMap[i][j] = greyVal;
				}
			}
			
			// Get the grey mean
			greymean = getGreylevelMean(greyMap, width, height);
		}
		catch (IOException e) 
		{
			originalImage = null;
			e.printStackTrace();
		}
	}

	//------------------------------------------------------------ METHODS --//	
	public int getWidth() 
	{
		return width;
	}

	public int getHeight() 
	{
		return height;
	}
	
	public ArrayList<Point> getMinutiaeIntersections (Point core, int coreRadius )
	{
		ArrayList<Point> minutiae = new ArrayList<Point>();
		
		int minI = core.x - coreRadius;
		int maxI = core.x + coreRadius;
		int minJ = core.y - coreRadius;
		int maxJ = core.y + coreRadius;
		
		if (minI < 1)
			minI = 1;
		
		if (maxI > width - 2)
			maxI = width - 2;
		
		if (minJ < 1)
			minJ = 1;
		
		if (maxJ > height - 2)
			maxJ = height - 2;
		
		int nbOnNeighbors;
		Point currentPoint;
		
		for (int i = minI ; i < maxI ; ++i)
		{
			for (int j = minJ ; j < maxJ ; ++j)
			{	
				currentPoint = new Point(i,j);
				
				if (getDistance(currentPoint, core) < coreRadius)
				{
					// Determine if it is a intersection point
					nbOnNeighbors = getSum(getFourNeigbors(i,j));
					
					if ( (binMap[i][j] == true) && ((nbOnNeighbors == 3) || (nbOnNeighbors == 4)))
					{
						minutiae.add(currentPoint);
					}
				}
			}
		}
		
		return minutiae;
	}
	
	public ArrayList<Point> getMinutiaeEndpoints (Point core, int coreRadius )
	{
		ArrayList<Point> minutiae = new ArrayList<Point>();
		
		int minI = core.x - coreRadius;
		int maxI = core.x + coreRadius;
		int minJ = core.y - coreRadius;
		int maxJ = core.y + coreRadius;
		
		if (minI < 1)
			minI = 1;
		
		if (maxI > width - 2)
			maxI = width - 2;
		
		if (minJ < 1)
			minJ = 1;
		
		if (maxJ > height - 2)
			maxJ = height - 2;
		
		int nbOnNeighbors;
		boolean [] neighbors;
		Point currentPoint;
		for (int i = minI ; i < maxI ; ++i)
		{
			for (int j = minJ ; j < maxJ ; ++j)
			{		
				currentPoint = new Point(i,j);
				
				if (getDistance(currentPoint, core) < coreRadius)
				{
					// Determine if it is a intersection point
					neighbors = getNeigbors(binMap, i, j);
					nbOnNeighbors = getSum(neighbors);
					
					if ( 	(binMap[i][j] == true) && 
							(nbOnNeighbors == 1) && 
							((neighbors[0] == true) || (neighbors[2] == true) || (neighbors[4] == true) || (neighbors[6] == true)))
					{
						minutiae.add(currentPoint);
					}
				}
			}
		}
		
		return minutiae;
	}
	
	public BufferedImage toBufferedImage()
	{
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0 ; i < width ; ++i)
		{
			for (int j = 0 ; j < height ; ++j)
			{
				if (binMap[i][j] == false)
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
	
	public void binarizeMean()
	{
		// Generate the boolean value
		for (int i = 0 ; i < width ; ++i)
		{
			for (int j = 0 ; j < height ; ++j)
			{
				binMap[i][j] = !(greyMap[i][j] > (greymean)); 
			}
		}
	}
	
	public void binarizeLocalMean()
	{
		int windowSize = 20;
		int step = 20;
		
		float localGreyMean;
		for (int i = 0 ; i < width ; i += step)
		{
			for (int j = 0 ; j < height ; j += step)
			{					
				// Get greymean
				localGreyMean = 0;
				int ik, jk = 0;
				for (ik = i ; ik < (i + windowSize); ++ik)
				{
					if (ik >= width)
						break;
					
					// Get greymean
					for (jk = j ; jk < (j + windowSize); ++jk)
					{
						if (jk >= height)
							break;
						
						localGreyMean += greyMap[ik][jk];
					}
				}
				
				if (jk*ik != 0)
				{
					localGreyMean = localGreyMean / ((ik-i)*(jk-j));
				}
				
				
				// If the local grey mean is too high (too permissive)
				// we take the global greymean
				if (localGreyMean > greymean)
				{
					localGreyMean = 0.75f*greymean + 0.25f*localGreyMean;
				}
				
				// Binarize window
				for (ik = i ; ik < (i + windowSize); ++ik)
				{
					if (ik >= width)
						break;
					
					// Get greymean
					for (jk = j ; jk < (j + windowSize); ++jk)
					{
						if (jk >= height)
							break;
						
						binMap[ik][jk] = !(greyMap[ik][jk] > (localGreyMean)); 
					}
				}
				
			}
		}
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
					newmap[i][j] = binMap [i-size][j-size];
				}
			}
		}
		
		binMap = newmap;
	}
	
	public void removeNoise()
	{
		float [][] filter = {	{0.0625f,0.1250f,0.0625f},
								{0.1250f,0.2500f,0.1250f},
								{0.0625f,0.1250f,0.0625f}};
		
		int limWidth  = width -1;
		int limHeight = height-1;
		
		boolean [][] newmap = new boolean [width][height];
		copyMatrix(binMap, newmap);
		
		for (int i = 1 ; i < limWidth ; ++i)
		{
			for (int j = 1 ; j < limHeight ; ++j)
			{
				float val = 0;
				for (int ik = -1 ; ik <= 1 ; ++ik)
		        {
			        for (int jk = -1 ; jk <= 1 ; ++jk)
			        {
			        	val += booleanToInt(binMap[i+ik][j+jk])*filter[1+ik][1+jk];
			        }
		        }
				newmap[i][j] = (val > 0.5);
			}
		}
		
		copyMatrix(newmap, binMap);
	}
	
	public void skeletonize()
	{
		int fstLin = 1;
		int lstLin = width - 1;
		int fstCol = 1;
		int lstCol = height - 1;
		
		boolean [][] prevM = new boolean [width][height];;
		boolean [][] newM = new boolean [width][height];;
		
		copyMatrix(binMap, prevM);
		
		int A, B;
		
		boolean [] neighbors;
		
		// We skeletonize until there are no changes between two iterations
		while (true)
		{
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
			else
			{
				copyMatrix(newM, prevM);
			}			
		}
		
		// Return matrix
		copyMatrix(newM, binMap);
	}

	public BufferedImage directionToBufferedImage(direction [][] dirMatrix)
	{
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for (int i = 0 ; i < width ; ++i)
		{
			for (int j = 0 ; j < height ; ++j)
			{
				bufferedImage.setRGB(i, j, (dirToColor (dirMatrix[i][j])).getRGB());
			}
		}
		
		return bufferedImage;
	}
	
	public direction [][] getDirections()
	{
		// Direction patterns
		direction [][] dirMatrix = new direction [width][height];
		
		int minI = 1;
		int maxI = width - 2;
		int minJ = 1;
		int maxJ = height - 2;
		
		for (int i = 0 ; i < width ; ++i)
		{
			for (int j = 0 ; j < height ; ++j)
			{
				if ((binMap[i][j] == false) || (i < minI) || (i > maxI) || (j < minJ) || (j > maxJ) )
					dirMatrix[i][j] = direction.NONE;
				else if ((binMap[i-1][j+1] == true) && (binMap[i+1][j-1] == true))
					dirMatrix[i][j] = direction.POSITIVE;
				else if ((binMap[i-1][j-1] == true) && (binMap[i+1][j+1] == true))
					dirMatrix[i][j] = direction.NEGATIVE;
				else if ((binMap[i][j-1] == true) && (binMap[i][j+1] == true))
					dirMatrix[i][j] = direction.VERTICAL;
				else if ((binMap[i-1][j] == true) && (binMap[i+1][j] == true))
					dirMatrix[i][j] = direction.HORIZONTAL;
				else
					dirMatrix[i][j] = direction.NONE;
			}
		}
		
		return dirMatrix;
	}
	
	public Point getCore (direction [][] dirMatrix)
	{
		// Private class to store partial results
		class coreInfos
		{
			private int nbVer, nbHor, nbPos, nbNeg;
			
			public float getIndex()
			{				
				float perVer, perHor, perPos, perNeg;
				float total = nbVer + nbHor + nbPos + nbNeg;
				float res;
				
				if (total == 0)
					return 1;
				
				perVer = nbVer / total;
				perHor = nbHor / total;
				perPos = nbPos / total;
				perNeg = nbNeg / total;
				
				res = 	  Math.abs(perVer - .25f)
						+ Math.abs(perHor - .25f)
						+ Math.abs(perPos - .25f)
						+ Math.abs(perNeg - .25f);
				
				return res;
			}
			
			public void reset()
			{
				nbVer = 0;
				nbHor = 0;
				nbPos = 0;
				nbNeg = 0;				
			}
			
			public void copyFrom(coreInfos r)
			{
				nbVer = r.nbVer;
				nbHor = r.nbHor;
				nbPos = r.nbPos;
				nbNeg = r.nbNeg;					
			}
			
			public void incVertical  (){++nbVer;}
			public void incHorizontal(){++nbHor;}
			public void incPositive  (){++nbPos;}
			public void incNegative  (){++nbNeg;}
		}
		
		// Variables
		Point core = new Point();
		int windowSize = width / 8;
		
		int minI = windowSize;
		int maxI = width - windowSize;
		int minJ = windowSize;
		int maxJ = height - windowSize;
		
		int minIK, maxIK, minJK, maxJK;
		
		coreInfos bestCandidate = new coreInfos();
		coreInfos currentCandidate = new coreInfos();
		
		bestCandidate.reset();
		
		for (int i = minI ; i < maxI ; ++i)
		{
			for (int j = minJ ; j < maxJ ; ++j)
			{	
				// Reset current infos
				currentCandidate.reset();
				
				minIK = i - windowSize;
				maxIK = i + windowSize;
				minJK = j - windowSize;
				maxJK = j + windowSize;
				
				// Calculate direction proportions
				for (int ik = minIK ; ik < maxIK ; ++ik)
				{					
					for (int jk = minJK ; jk < maxJK; ++jk)
					{
						// Increment the good value 
						switch (dirMatrix[ik][jk]) 
						{								
							case HORIZONTAL:
								currentCandidate.incHorizontal();
								break;
								
							case POSITIVE:
								currentCandidate.incPositive();
								break;
								
							case NEGATIVE:
								currentCandidate.incNegative();
								break;
								
							case VERTICAL:
								currentCandidate.incVertical();
								break;
						}
					}
				}
				
				// Check if we keep the core
				if (currentCandidate.getIndex() <= bestCandidate.getIndex())
				{
					bestCandidate.copyFrom(currentCandidate);
					core.x = i;
					core.y = j;
				}
			}
		}
		return core;
	}
	
	public Point getCore2 (direction [][] dirMatrix)
	{
		Point core = new Point();
		
		int windowSize = 5;
		
		int minI = windowSize;
		int maxI = width - windowSize;
		int minJ = windowSize;
		int maxJ = height - windowSize;
		
		class tempResult
		{
			public int nbHorizontal;
			public int nbVertical;
			
			public void reset()
			{
				nbHorizontal = 0;
				nbVertical = 0;
			}
			
			public void copy(tempResult r)
			{
				this.nbHorizontal = r.nbHorizontal;
				this.nbVertical = r.nbVertical;
			}
			
			public boolean isBetter(tempResult r)
			{
				if ((r.nbHorizontal + r.nbVertical) >= (nbHorizontal + nbVertical))
				{
					if ((nbHorizontal == 0) && (r.nbHorizontal != 0))
						return true;
					
					if ((nbVertical == 0) && (r.nbVertical != 0))
						return true;
					
//					if (java.lang.Math.abs(r.nbHorizontal - r.nbVertical) <= java.lang.Math.abs(nbHorizontal - nbVertical))
//						return true;
				}
				
				return false;
			}
			
			public void incH()
			{
				nbHorizontal++;
			}
			
			public void incV()
			{
				nbVertical++;
			}
		};
		
		tempResult bestResult = new tempResult();
		tempResult currentResult = new tempResult();
		

		for (int i = minI ; i < maxI ; ++i)
		{
			for (int j = minJ ; j < maxJ ; ++j)
			{				
				currentResult.reset();
				
				// Check the number of pixel in each direction
				for (int ik = i-windowSize ; ik < i+windowSize ; ++ik)
				{
					for (int jk = j-windowSize ; jk < j+windowSize ; ++jk)
					{
						if (dirMatrix[ik][jk] == direction.HORIZONTAL)
							currentResult.incH();
						if (dirMatrix[ik][jk] == direction.VERTICAL)
							currentResult.incV();
					}
				}
				
				// Check if the new point is better
				if (bestResult.isBetter(currentResult))
				{
					bestResult.copy(currentResult);
					core.x = i;
					core.y = j;
				}
			}
		}
		
		return core;
	}

	//---------------------------------------------------- PRIVATE METHODS --//
	
	private boolean [] getFourNeigbors(int i, int j)
	{
		boolean []neighbors = new boolean [4];
		neighbors[0] = binMap[i+0][j-1];
		neighbors[1] = binMap[i+1][j+0];
		neighbors[2] = binMap[i+0][j+1];
		neighbors[3] = binMap[i-1][j+0];
			
		return neighbors;
	}
	
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
	
	private float getDistance(Point a, Point b)
	{
		int deltaX = b.x - a.x;
		int deltaY = b.y - a.y;
		
		return (float)Math.sqrt(Math.abs(deltaX*deltaX)+Math.abs(deltaY*deltaY));
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
	
	private Color dirToColor(direction dir)
	{
		switch (dir) 
		{
			case NONE:
				return Color.black;
				
			case HORIZONTAL:
				return Color.red;
				
			case POSITIVE:
				return Color.green;
				
			case NEGATIVE:
				return Color.yellow;
				
			case VERTICAL:
				return Color.cyan;
							
			default:
				return Color.black;
		}
	}
}
