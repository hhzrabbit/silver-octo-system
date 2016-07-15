/*======================================
  SKEELTON for
  class MazeSolver
  ======================================*/

import java.io.*;
import java.util.*;

class Node {
    private int row;
    private int col;
    private Node parent;
    
    public Node(int r, int c){
	row = r;
	col = c;
	parent = null;
    }

    public Node(int r, int c, Node p){
	this(r, c);
	parent = p;
    }

    public void setParent(Node n){
	parent = n;
    }

    public int getRow(){
	return row;
    } 

    public int getCol(){
	return col;
    }

    public Node getParent(){
	return parent;
    }

    public String toString(){
	return row + " " + col ;//" " + parent;
    }

}


class MazeSolver {

    private char[][] maze;
    private int h, w; //height, width of maze
    private int startR, startC = -2;
    private boolean solved;

    //initialize constants
    final private char START =        'S';
    final private char PATH =         ' ';
    final private char WALL =         '#';
    final private char EXIT =         'E';
    final private char VISITED_PATH = '.';
    final private char ANSWER =       '+';


    public MazeSolver( String inputFile ) {

	//init 2D array to represent maze 
	// ...same dimensions as default terminal window
	maze = new char[80][80];
	h = 0;
	w = 0;

		try {
	    Scanner sc = new Scanner( new File(inputFile) );
	    System.out.println( "reading in file..." );
	    int row = 0;
	    while( sc.hasNext() ) {
		String line = sc.nextLine();
		if ( w < line.length() ) 
		    w = line.length();
		for( int i=0; i<line.length(); i++ ) {
		    char c = line.charAt(i);
		    if (c == START) {
			startR = row;
			startC = i;
		    }
		    maze[row][i] = line.charAt( i );
		}
		h++;
		row++;
	    } 
	    	} 
	catch( FileNotFoundException e ) { System.out.println( "Error reading file" ); 
	    System.exit(0);}
	solved = false;
    }//end constructor


    public String toString() {
	//send ANSI code "ESC[0;0H" to place cursor in upper left
	String retStr = "";// "[0;0H"; 
	//String retStr = "";
	int i, j;
	for( i=0; i<h; i++ ) {
	    for( j=0; j<w; j++ )
		retStr = retStr + maze[i][j];
	    retStr = retStr + "\n";
	}
	return retStr;
    }

    //helper method to keep try/catch clutter out of main flow
    private void delay( int n ) {
	try {
	    Thread.sleep(n);
	}
	catch( InterruptedException e ) {
	    System.exit(0);
	}
    }

    public boolean inBounds(int r, int c){
	return (r >= 0 && c >= 0 && r < h && c < w);
    }

    public boolean visitable(int r, int c){
	return inBounds(r, c) && maze[r][c] != WALL && maze[r][c] != VISITED_PATH;
    }

    public boolean isEnd(int r, int c){
	return maze[r][c] == EXIT;
    }

    public void mark(int r, int c){
	maze[r][c] = VISITED_PATH;
    }

    public void solve(Node n){
	if (solved) return;
	int r = n.getRow();
	int c = n.getCol(); 
	Deque<Node> nodes = new ArrayDeque<Node>();
	int[][] adjacents = { {r + 1, c},
			      {r - 1, c},
			      {r, c + 1},
			      {r, c - 1} };
	for (int[] adj : adjacents){
	    int row = adj[0];
	    int col = adj[1];
	    if (visitable(row, col)) {
		if (isEnd(row, col)){	
		    solved = true;
		    finish(new Node(row, col, n));
		    return;
		}
		else {		   
		    mark(row, col);
		    nodes.add(new Node(row, col, n));
		}
	    }
	}

	while (! nodes.isEmpty()){
	    solve(nodes.remove());
	}
	return;
    }

    public void solve(){
	solve(new Node(startR, startC));
	if (!solved) System.out.println("NO SOLUTION HOMIE");
	
    }

    public void finish(Node n){
	while (n != null){
	    maze[n.getRow()][n.getCol()] = ANSWER;
	    n = n.getParent();
	}
	System.out.println(this);
	System.out.println("Solved!");
    }


}

public class Maze {

    public static void main( String[] args ) {
	if (args.length < 1) {
	    System.out.println( "Error reading input file" );
	    System.out.println( "Usage: java Maze < filename >" );
	}
	else {
	    String mazeInputFile = args[0];

	    MazeSolver ms = new MazeSolver( mazeInputFile );
	    //clear screen
	    System.out.println( "[2J" ); 

	    //display maze 
	    System.out.println( ms );

	    //drop hero into the maze (be sure coords are on the path)
	    //comment next line out when ready to randomize startpos
	    ms.solve(); 

	}
    }

}//end class Maze
