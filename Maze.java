/*======================================
  SKEELTON for
  class MazeSolver
  ======================================*/

import java.io.*;
import java.util.*;

class Node {
    private int x;
    private int y;
    private Node parent;
    
    public Node(int x1, int y1){
	x = x1;
	y = y1;
	parent = null;
    }

    public Node(int x1, int y1, Node p){
	this(x1, y1);
	parent = p;
    }

    public void setParent(Node n){
	parent = n;
    }

    public int getX(){
	return x;
    } 

    public int getY(){
	return y;
    }

    public Node getParent(){
	return parent;
    }

    public String toString(){
	return x + " " + y ;//" " + parent;
    }

}


class MazeSolver {

    private char[][] maze;
    private int h, w; //height, width of maze
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
	maze = new char[80][25];
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
		for( int i=0; i<line.length(); i++ )
		    maze[i][row] = line.charAt( i );
		h++;
		row++;
	    } 
	} 
	catch( Exception e ) { System.out.println( "Error reading file" ); }
	solved = false;
    }//end constructor


    public String toString() {
	//send ANSI code "ESC[0;0H" to place cursor in upper left
	String retStr = "[0;0H"; 
	//String retStr = "";
	int i, j;
	for( i=0; i<h; i++ ) {
	    for( j=0; j<w; j++ )
		retStr = retStr + maze[j][i];
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

    public boolean inBounds(int x, int y){
	return (x >= 0 && y >= 0 && x < w && y < h);
    }

    public boolean visitable(int x, int y){
	return inBounds(x, y) && maze[x][y] != WALL && maze[x][y] != VISITED_PATH;
    }

    public boolean isEnd(int x, int y){
	return maze[x][y] == EXIT;
    }

    public void mark(int x, int y){
	maze[x][y] = VISITED_PATH;
    }

    public void solve(Node n){
	if (solved) return;
	//	System.out.println(n);
	//maze[x][y] = VISITED_PATH;
	int x = n.getX();
	int y = n.getY(); 
	Deque<Node> nodes = new ArrayDeque<Node>();
	int[][] adjacents = { {x + 1, y},
			      {x - 1, y},
			      {x, y + 1},
			      {x, y - 1} };
	for (int[] adj : adjacents){
	    int xcor = adj[0];
	    int ycor = adj[1];
	    if (visitable(xcor, ycor)) {
		if (isEnd(xcor, ycor)){	
		    solved = true;
		    finish(new Node(xcor, ycor, n));
		    return;
		}
		else {		   
		    mark(xcor, ycor);
		    nodes.add(new Node(xcor, ycor, n));
		}
	    }
	}

	while (! nodes.isEmpty()){
	    solve(nodes.remove());
	}
	return;
    }

    public void solve(int x, int y){
	solve(new Node(x, y));
	if (!solved) System.out.println("NO SOLUTION HOMIE");
    }

    public void finish(Node n){
	while (n != null){
	    maze[n.getX()][n.getY()] = ANSWER;
	    n = n.getParent();
	}
	System.out.println(this);
	System.out.println("Solved!");
    }


}

public class Maze {

    public static void main( String[] args ) {

	try {
	    String mazeInputFile = args[0];

	    MazeSolver ms = new MazeSolver( mazeInputFile );
	    //clear screen
	    System.out.println( "[2J" ); 

	    //display maze 
	    System.out.println( ms );

	    //drop hero into the maze (be sure coords are on the path)
	    //comment next line out when ready to randomize startpos
	    ms.solve( 1, 1 ); 

	}
	catch( Exception e ) { 
	    System.out.println( "Error reading input file." );
	    System.out.println( "Usage: java Maze <filename>" ); 
	}
    }

}//end class Maze
