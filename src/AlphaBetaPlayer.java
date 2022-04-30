
public class AlphaBetaPlayer implements Player{
	int id; 
	int opponent_id;
    int cols; 
    int alpha;
    int beta;
    
    /**
     * Return the name of this player.
     * 
     * @return A name for this player
     */
    @Override
    public String name() {
        return "Alpha";
    }
    
    /**
     * Initialize the player. The game calls this method once,
     * before any calls to calcMove().
     * 
     * @param id integer identifier for the player (can get opponent's id via 3-id);
     * @param msecPerMove time allowed for each move
     * @param rows the number of rows in the board
     * @param cols the number of columns in the board
     */
    @Override
    public void init(int id, int msecPerMove, int rows, int cols) {
    	this.id = id; //id is your player's id, opponent's id is 3-id
    	this.cols = cols;
    	opponent_id = 3-id;
    }
    /**
     * Called by driver program to calculate the next move.
     *  
     * @param board current connect 4 board
     * @param oppMoveCol column of opponent's most recent move; -1 if this is the first move 
     * 		  of the game; note that the board may not be empty on the first move of the game!
     * @param arb handles communication between game and player
     * @throws TimeUpException If the game determines the player has run out of time 
     * @throws Error: The board is full! if the game determines that there is no room to move
     */
    @Override
    public void calcMove(
        Connect4Board board, int oppMoveCol, Arbitrator arb) 
        throws TimeUpException {
        // Make sure there is room to make a move.
        if (board.isFull()) {
            throw new Error ("Complaint: The board is full!");
        }
        
        int move = 0; 
        int maxDepth = 1;
        
        // while there's time left and maxDepth <= number of moves remaining
        while(!arb.isTimeUp() && maxDepth <= board.numEmptyCells()) {
        	// do minimax search
        	// start the first level of minimax, set move as you're finding the bestScore
        
            arb.setMove(move);
            maxDepth++;
        }        

    }
     //function alphabeta(node, depth, α, β, maximizingPlayer): 
    public int alphabeta(Connect4Board board, int depth,int alpha , int beta,boolean isMaximizing, Arbitrator arb) {	
    	int value=0;
    	//if depth = 0 or node is a terminal node then
		//return the heuristic value of node
    	if (depth == 0 || board.numEmptyCells() == 0 || arb.isTimeUp()) {
    		return score(board);
    	}
    	
			//if maximizingPlayer then
			//value = −∞
			//for each child of node do
			//value = max(value, alphabeta(child, depth − 1, α, β, FALSE)) 
			//α = max(α, value)
			//if α ≥ β then
			//break 
			//return value
    	if(isMaximizing==true)
    	{
    		value=-1000;
    		for(int cols=0;cols<board.numCols();cols++)
    		{	
    			if(board.isValidMove(cols)) {
    				board.move(cols,id);		
        			value=Math.max(value, alphabeta(board, depth - 1, alpha,beta,false,arb));
        			board.unmove(cols,id);
        			alpha=Math.max(alpha,value);
        			if(alpha>=beta)
        			{
        				break;
        			}
 
    			}
    		}
    		return value;

    	}
    	
    	
	/* 
			else
			value = +∞
			for each child of node do
			value = min(value, alphabeta(child, depth − 1, α, β, TRUE)) 
			β = min(β, value)
			if α ≥ β then
			break 
			return value
     */
    	else
    	{
    		value=1000;
    		for(int cols=0;cols<board.numCols();cols++)
    		{	
    			if(board.isValidMove(cols)) {
    				board.move(cols,id);		
        			value=Math.min(value, alphabeta(board, depth - 1, alpha,beta,true,arb));
        			board.unmove(cols,id);
        			beta=Math.min(beta,value);
        			if(alpha>=beta)
        			{
        				break;
        			}
    			}
    		}
    		return value;
    	}
    }
    public int score(Connect4Board board) {
    	return calcScore(board, id) - calcScore(board, opponent_id);
    }
    
    
	// Return the number of connect-4s that player #id has.
	public int calcScore(Connect4Board board, int id)
	{
		final int rows = board.numRows();
		final int cols = board.numCols();
		int score = 0;
		// Look for horizontal connect-4s.
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c <= cols - 4; c++) {
				if (board.get(r, c + 0) != id) continue;
				if (board.get(r, c + 1) != id) continue;
				if (board.get(r, c + 2) != id) continue;
				if (board.get(r, c + 3) != id) continue;
				score++;
			}
		}
		// Look for vertical connect-4s.
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c) != id) continue;
				if (board.get(r + 1, c) != id) continue;
				if (board.get(r + 2, c) != id) continue;
				if (board.get(r + 3, c) != id) continue;
				score++;
			}
		}
		// Look for diagonal connect-4s.
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c + 0) != id) continue;
				if (board.get(r + 1, c + 1) != id) continue;
				if (board.get(r + 2, c + 2) != id) continue;
				if (board.get(r + 3, c + 3) != id) continue;
				score++;
			}
		}
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = rows - 1; r >= 4 - 1; r--) {
				if (board.get(r - 0, c + 0) != id) continue;
				if (board.get(r - 1, c + 1) != id) continue;
				if (board.get(r - 2, c + 2) != id) continue;
				if (board.get(r - 3, c + 3) != id) continue;
				score++;
			}
		}
		return score;
	}
}
