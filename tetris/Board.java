// Board.java
package tetris;

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	private boolean committed;
	private boolean [][]pastStateGrid;
	private int[] pastStateWidths;
	private int[] pastStateHeights;
	private int pastStateMaxHeight;
	private int[] widths;
	private int[] heights;
	private int maxHeight;
	
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;

		pastStateGrid = new boolean[width][height];
		grid = new boolean[width][height];

		commit();
		pastStateHeights = new int[width];
		heights = new int[width];

		pastStateWidths = new int[height];
		widths = new int[height];

		maxHeight = 0;
		pastStateMaxHeight = 0;
		
		// YOUR CODE HERE
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			int checkHeight = calculateHeight();
			if(checkHeight!=maxHeight)
			{
				throw  new RuntimeException("max Height not same" + checkHeight + " "+maxHeight);
			}
			int[] wid = new int[height];
			int[] heit = new int[width];
			for(int i = 0 ;i<width ; i++){
				for(int j = 0  ; j<height ; j++){
					if(grid[i][j]){
						heit[i] = Math.max(j+1 , heit[i]);
						wid[j]++;
					}
				}
			}
			if(!Arrays.equals(wid  , widths))throw new RuntimeException("widths not equal");
			if(!Arrays.equals(heit  , heights))throw new RuntimeException("heights not equal");

		}
	}
	private int calculateHeight(){
		int result = heights[0];
		for(int i = 1 ;i<width ; i++)
			result = Math.max(result , heights[i]);
		return result;
	}
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		assert (piece.getWidth() + x) <= this.grid.length;
		int[] ar = piece.getSkirt();
		int result = heights[x]-ar[0];
		for(int i = 1  ; i<piece.getWidth() ; i++)
			result = Math.max(result , heights[x+1]-ar[i]);
		return result;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if(!inRange(x,y))return true;
		return grid[x][y];

	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		committed = false;
		saveState();
		TPoint[] points = piece.getBody();
		int result  = PLACE_OK;

		for(int i = 0 ; i<points.length ; i++){
			int gridX = points[i].x + x;
			int gridY = points[i].y + y;
			if (!inRange(gridX, gridY)) {
				return PLACE_OUT_BOUNDS;
			}
			if(grid[gridX][gridY])return PLACE_BAD;
			grid[gridX][gridY]=true;
			widths[gridY]++;
			if(widths[gridY]==width) result = PLACE_ROW_FILLED;
			heights[gridX] = Math.max(heights[gridX] ,gridY+1);
			maxHeight = Math.max(maxHeight , heights[gridX]);
		}
		sanityCheck();
		return result;
	}
	private boolean inRange(int x , int y ){
		if(x<0 || x>=width || y<0 || y>=height)return false;
		return true;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;

		for(int i = 0 ; i<maxHeight ; i++){
			if(widths[i]==width) {
				rowsCleared++;
			}else{
				widths[i-rowsCleared] = widths[i];
				for(int j = 0 ; j<width ; j++){
					grid[j][i-rowsCleared] = grid[j][i];
				}

			}
		}
		for(int i = maxHeight - rowsCleared ; i<maxHeight ; i++){
			clearRow(i);
		}
		for(int i = 0; i < width; i++) {
			int found = 0;
			int j = heights[i]-1;
			while(j>=0){
				if(grid[i][j]){
					found=j+1;
					break;
				}
				j--;
			}
			heights[i]= found;
		}
		maxHeight -=rowsCleared;
		sanityCheck();
		return rowsCleared;
	}
	private void clearRow(int x){
		for(int i = 0 ; i<width ; i++)
			grid[i][x] = false;
		widths[x]=0;
	}



	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/

	public void undo() {

		System.arraycopy( pastStateHeights , 0, heights, 0, width);
		System.arraycopy(pastStateWidths, 0, widths, 0, height);
		 maxHeight = pastStateMaxHeight ;
		for(int i = 0 ; i<width ; i++){
			System.arraycopy(pastStateGrid[i] , 0,  grid[i], 0  ,  height);
		}
		commit();
	}

	private void saveState(){
		System.arraycopy(heights, 0, pastStateHeights, 0, width);
		System.arraycopy(widths, 0, pastStateWidths, 0, height);
		pastStateMaxHeight = maxHeight;
		for(int i = 0 ; i<width ; i++){
			 System.arraycopy(grid[i], 0,  pastStateGrid[i],0, height);
		}
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}

			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


