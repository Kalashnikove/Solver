package SudokuSolver;

//000007390200900001006010000900005010400000007030400002000020500600004009017500000
//5192436215
//058600000900003000024000000703060050500010004010040906000000420000800009000009370
//500000000000008207140000056800460000900020008000037005610000032208700000000000009
//020000670000090008000006019605020080000805000070030501580600000200040000039000050
//830000400002100009000080070093004000000896000000300950020040000600007300001000082
//000020470070000036500080000007500009051000640900004200000030001790000060032090000
//000982001040000098000000000000695030090000020080723000000000000620000010700834000

//not yet passed
//000100047094600000000080900040005008900060002700200030003010000000006120520009000

//hardest in the world gg 800000000003600000070090200050007000000045700000100030001000068008500010090000400

public class guiBoard {
	public int[][] board;
	private int[][] backup = new int[9][9];
	private String[][] ableBackup = new String[9][9];

	private String[][] able;

	String[] madeInferences = new String[200];
	int madeInferencesIndex = 0;

	private int[][] indeciesOfPlacedNumbers;

	public guiBoard(){
		board = new int[9][9];
		able = new String[9][9];
		for(int r = 0; r<9; r++)
			for(int c = 0; c<9; c++)
				able[r][c] = "123456789";
	}

	public int get(int r, int c){
		return board[r][c];
	}

	public void makeBackup(){
		for(int r = 0; r<9; r++)
			for(int c = 0; c<9; c++){
				backup[r][c] = board[r][c];
				ableBackup[r][c] = able[r][c];
			}
	}

	public boolean changed(){
		for(int r = 0; r<9; r++)
			for(int c = 0; c<9; c++)
				if(board[r][c]!=backup[r][c] || !able[r][c].equals(ableBackup[r][c]))return true;
		return false;
	}

	public int[][] getChangedIndecies(){
		String changes = "";
		for(int r = 0; r<9; r++)
			for(int c = 0; c<9; c++)
				if(board[r][c]!=backup[r][c] || !able[r][c].equals(ableBackup[r][c]))changes += ""+r+c;
		int[][] temp = new int[changes.length()/2][2];
		int index = 0;
		for(int i = 0; i<changes.length(); i+=2){
			temp[index][0] = Integer.parseInt(""+changes.charAt(i));
			temp[index][1] = Integer.parseInt(""+changes.charAt(i+1));
			index++;
		}
		return temp;
	}

	public boolean isEligible(int r, int c, int t){
		return able[r][c].contains(""+t);
	}

	public boolean gameDone(){
		for(int[] y: board)
			for(int x: y)
				if(x==0)return false;
		return true;
	}

	public void inputBoard(String temp){
		try{
			int index = 0;
			String tempIndecies = "";
			for(int r = 0; r<9; r++)
				for(int c = 0; c<9; c++){
					board[r][c] = Character.getNumericValue(temp.charAt(index));
					if(board[r][c]!=0){
						able[r][c] = "";
						remove(r,c,board[r][c]);
						tempIndecies += ""+r+c;
					}
					index++;
				}
			indeciesOfPlacedNumbers = new int[tempIndecies.length()/2][2];
			index = 0;
			for(int i = 0; i<tempIndecies.length(); i+=2){
				indeciesOfPlacedNumbers[index][0] = Integer.parseInt(""+tempIndecies.charAt(i));
				indeciesOfPlacedNumbers[index][1] = Integer.parseInt(""+tempIndecies.charAt(i+1));
				index++;
			}
		}
		catch(StringIndexOutOfBoundsException e){ gui.append("The string you entered isn't long enough to fill the board completely!\n"); }
	}

	public int[][] getIndecies(){
		return indeciesOfPlacedNumbers;
	}

	public void remove(int r, int c, int t){
		for(int i = 0; i<9; i++)
			able[r][i] = able[r][i].replaceAll(""+t,"");

		for(int i = 0; i<9; i++)
			able[i][c] = able[i][c].replaceAll(""+t,"");

		int cube = 0;
		if(r<=2 && c<=2)cube = 1;
		else if(r<=2 && c>=3 && c<=5)cube = 2;
		else if(r<=2 && c>=6)cube = 3;
		else if(r>=3 && r<=5 && c<=2)cube = 4;
		else if(r>=3 && r<=5 && c>=3 && c<=5)cube = 5;
		else if(r>=3 && r<=5 && c>=6)cube = 6;
		else if(r>=6 && c<=2)cube = 7;
		else if(r>=6 && c>=3 && c<=5)cube = 8;
		else if(r>=6 && c>=6)cube = 9;

		switch(cube){
		case 1:
			r = 0;
			c = 0;
			break;
		case 2:
			r = 0;
			c = 3;
			break;
		case 3:
			r = 0;
			c = 6;
			break;
		case 4:
			r = 3;
			c = 0;
			break;
		case 5:
			r = 3;
			c = 3;
			break;
		case 6:
			r = 3;
			c = 6;
			break;
		case 7:
			r = 6;
			c = 0;
			break;
		case 8:
			r = 6;
			c = 3;
			break;
		case 9:
			r = 6;
			c = 6;
			break;
		}

		for(int r2 = r; r2<r+3; r2++)
			for(int c2 = c; c2<c+3; c2++)
				able[r2][c2] = able[r2][c2].replaceAll(""+t,"");
	}

	public void set(int r, int c, int t){
		board[r][c] = t;
		remove(r,c,t);
		able[r][c] = "";
	}

	public String getAble(int r, int c){
		return able[r][c];
	}

	public boolean twoInLine(String indecies, int t){
		int combined = Integer.parseInt(indecies);
		int r1 = combined/1000, c1 = (combined/100)%10;
		int r2 = (combined/10)%10, c2 = combined%10;

		if(r1 == r2 && isEligibleInference(indecies,t)){
			removeFromRowExceptCube(r1,c1,t);
			addInference(indecies,t);
			return true;
		}
		else if(c1 == c2 && isEligibleInference(indecies,t)){
			removeFromColExceptCube(r1,c1,t);
			addInference(indecies,t);
			return true;
		}
		return false;
	}

	public boolean threeInLine(String indecies, int t){
		int combined = Integer.parseInt(indecies);
		int r1 = combined/100000, c1 = (combined/10000)%10;
		int r2 = (combined/1000)%10, c2 = (combined/100)%10;
		int r3 = (combined/10)%10, c3 = combined%10;

		if(r1 == r2 && r2 == r3 && isEligibleInference(indecies,t)){
			removeFromRowExceptCube(r1,c1,t);
			addInference(indecies,t);
			return true;
		}
		else if(c1 == c2 && c2 == c3 && isEligibleInference(indecies,t)){
			removeFromColExceptCube(r1,c1,t);
			addInference(indecies,t);
			return true;
		}
		return false;
	}

	public void removeFromRowExceptCube(int r, int c, int t){
		gui.append("MADE A ROW INFERENCE OF A "+t+" AT ROW "+(r+1)+"\n");
		int cube = 0;
		if(r<=2 && c<=2)cube = 1;
		else if(r<=2 && c>=3 && c<=5)cube = 2;
		else if(r<=2 && c>=6)cube = 3;
		else if(r>=3 && r<=5 && c<=2)cube = 4;
		else if(r>=3 && r<=5 && c>=3 && c<=5)cube = 5;
		else if(r>=3 && r<=5 && c>=6)cube = 6;
		else if(r>=6 && c<=2)cube = 7;
		else if(r>=6 && c>=3 && c<=5)cube = 8;
		else if(r>=6 && c>=6)cube = 9;

		switch(cube){
		case 1:
			c = 0;
			break;
		case 2:
			c = 3;
			break;
		case 3:
			c = 6;
			break;
		case 4:
			c = 0;
			break;
		case 5:
			c = 3;
			break;
		case 6:
			c = 6;
			break;
		case 7:
			c = 0;
			break;
		case 8:
			c = 3;
			break;
		case 9:
			c = 6;
			break;
		}

		for(int x = 0; x<9; x++){
			if(x == c){
				x += 2;
				continue;
			}
			else if(x == c+1){
				x+=1;
				continue;
			}
			else if(x == c+2){
				continue;
			}
			able[r][x] = able[r][x].replaceAll(""+t,"");
		}
	}

	public void removeFromColExceptCube(int r, int c, int t){
		gui.append("MADE A COL INFERENCE OF A "+t+" AT COL "+(c+1)+"\n");
		int cube = 0;
		if(r<=2 && c<=2)cube = 1;
		else if(r<=2 && c>=3 && c<=5)cube = 2;
		else if(r<=2 && c>=6)cube = 3;
		else if(r>=3 && r<=5 && c<=2)cube = 4;
		else if(r>=3 && r<=5 && c>=3 && c<=5)cube = 5;
		else if(r>=3 && r<=5 && c>=6)cube = 6;
		else if(r>=6 && c<=2)cube = 7;
		else if(r>=6 && c>=3 && c<=5)cube = 8;
		else if(r>=6 && c>=6)cube = 9;

		switch(cube){
		case 1:
			r = 0;
			break;
		case 2:
			r = 0;
			break;
		case 3:
			r = 0;
			break;
		case 4:
			r = 3;
			break;
		case 5:
			r = 3;
			break;
		case 6:
			r = 3;
			break;
		case 7:
			r = 6;
			break;
		case 8:
			r = 6;
			break;
		case 9:
			r = 6;
			break;
		}

		for(int x = 0; x<9; x++){
			if(x == r){
				x += 2;
				continue;
			}
			else if(x == r+1){
				x+=1;
				continue;
			}
			else if(x == r+2){
				continue;
			}
			able[x][c] = able[x][c].replaceAll(""+t,"");
		}
	}

	private String[][] log = new String[9][9];
	private int[] indecies = new int[9];

	public void log(int r, int c, int t, String indecies){
		int cube = 0;
		if(r<=2 && c<=2)cube = 0;
		else if(r<=2 && c>=3 && c<=5)cube = 1;
		else if(r<=2 && c>=6)cube = 2;
		else if(r>=3 && r<=5 && c<=2)cube = 3;
		else if(r>=3 && r<=5 && c>=3 && c<=5)cube = 4;
		else if(r>=3 && r<=5 && c>=6)cube = 5;
		else if(r>=6 && c<=2)cube = 6;
		else if(r>=6 && c>=3 && c<=5)cube = 7;
		else if(r>=6 && c>=6)cube = 8;

		log[cube][this.indecies[cube]] = t+indecies;
		this.indecies[cube]++;
	}

	public void logCheck(){
		for(int cube = 0; cube<9; cube++){
			if(indecies[cube] == 0)continue;
			for(int i = 0; i<9; i++){
				if(log[cube][i]==null)break;
				for(int s = i+1; s<9; s++){
					if(log[cube][s]==null)break;
					if(log[cube][i].substring(1,3).equals(log[cube][s].substring(1,3)) && log[cube][i].substring(3).equals(log[cube][s].substring(3)) && isEligibleInference(log[cube][s]+log[cube][s],0)){
						int combination = Integer.parseInt(log[cube][i]);
						int r1 = (combination/1000)%10, c1 = (combination/100)%10;
						int r2 = (combination/10)%10, c2 = combination%10;

						addInference(log[cube][s]+log[cube][s],0);

						char char1 = log[cube][i].charAt(0), char2 = log[cube][s].charAt(0);
						for(int t = 0; t<able[r1][c1].length(); t++){
							char temp = able[r1][c1].charAt(t);
							if(temp != char1 && temp != char2)
								able[r1][c1] = able[r1][c1].replaceAll(""+temp,"");
						}
						for(int t = 0; t<able[r2][c2].length(); t++){
							char temp = able[r2][c2].charAt(t);
							if(temp != char1 && temp != char2)
								able[r2][c2] = able[r2][c2].replaceAll(""+temp,"");
						}
						gui.append("MADE A RESTRICTION INFERENCE WITH THE NUMBERS "+char1+","+char2+" AT ("+(r1+1)+","+(c1+1)+")("+(r2+1)+","+(c2+1)+")\n");
					}
				}
			}
		}

		indecies = new int[9];
		log = new String[9][9];
	}

	public void clearLog(){
		indecies = new int[9];
		log = new String[9][9];
	}

	public String[] getRowAble(int r){
		return able[r];
	}

	public String[] getColAble(int c){
		String[] temp = new String[9];
		for(int r = 0; r<9; r++)
			temp[r] = able[r][c];
		return temp;
	}

	public void removeFromRowExcept(int r, int c1, int c2, String nums){
		for(int c= 0; c<9; c++)
			if(c!=c1 && c!=c2)
				for(int s = 0; s<nums.length(); s++)
					able[r][c] = able[r][c].replaceAll(""+nums.charAt(s),"");
	}

	public void removeFromRowExcept(int r, int c1, int c2, int c3, String nums){
		for(int c = 0; c<9; c++)
			if(c!=c1 && c!=c2 && c!=c3)
				for(int s = 0; s<nums.length(); s++)
					able[r][c] = able[r][c].replaceAll(""+nums.charAt(s),"");
	}

	public void removeFromColExcept(int c, int r1, int r2, String nums){
		for(int r = 0; r<9; r++)
			if(r!=r1 && r!=r2)
				for(int s = 0; s<nums.length(); s++)
					able[r][c] = able[r][c].replaceAll(""+nums.charAt(s),"");
	}

	public void removeFromColExcept(int c, int r1, int r2, int r3, String nums){
		for(int r = 0; r<9; r++)
			if(c!=r1 && r!=r2 && r!=r3)
				for(int s = 0; s<nums.length(); s++)
					able[r][c] = able[r][c].replaceAll(""+nums.charAt(s),"");
	}

	public boolean inTheSameCube(String rc1, String rc2){
		int rc = Integer.parseInt(rc1);
		int r1 = rc/10, c1 = rc%10;
		rc = Integer.parseInt(rc2);
		int r2 = rc/10, c2 = rc%10;

		int cube1 = 0;
		if(r1<=2 && c1<=2)cube1 = 0;
		else if(r1<=2 && c1>=3 && c1<=5)cube1 = 1;
		else if(r1<=2 && c1>=6)cube1 = 2;
		else if(r1>=3 && r1<=5 && c1<=2)cube1 = 3;
		else if(r1>=3 && r1<=5 && c1>=3 && c1<=5)cube1 = 4;
		else if(r1>=3 && r1<=5 && c1>=6)cube1 = 5;
		else if(r1>=6 && c1<=2)cube1 = 6;
		else if(r1>=6 && c1>=3 && c1<=5)cube1 = 7;
		else if(r1>=6 && c1>=6)cube1 = 8;
		int cube2 = 0;
		if(r2<=2 && c2<=2)cube2 = 0;
		else if(r2<=2 && c2>=3 && c2<=5)cube2 = 1;
		else if(r2<=2 && c2>=6)cube2 = 2;
		else if(r2>=3 && r2<=5 && c2<=2)cube2 = 3;
		else if(r2>=3 && r2<=5 && c2>=3 && c2<=5)cube2 = 4;
		else if(r2>=3 && r2<=5 && c2>=6)cube2 = 5;
		else if(r2>=6 && c2<=2)cube2 = 6;
		else if(r2>=6 && c2>=3 && c2<=5)cube2 = 7;
		else if(r2>=6 && c2>=6)cube2 = 8;

		return cube1 == cube2;
	}

	public boolean inTheSameCube(String rc1, String rc2, String rc3){
		int rc = Integer.parseInt(rc1);
		int r1 = rc/10, c1 = rc%10;
		rc = Integer.parseInt(rc2);
		int r2 = rc/10, c2 = rc%10;
		rc = Integer.parseInt(rc3);
		int r3 = rc/10, c3 = rc%10;

		int cube1 = 0;
		if(r1<=2 && c1<=2)cube1 = 0;
		else if(r1<=2 && c1>=3 && c1<=5)cube1 = 1;
		else if(r1<=2 && c1>=6)cube1 = 2;
		else if(r1>=3 && r1<=5 && c1<=2)cube1 = 3;
		else if(r1>=3 && r1<=5 && c1>=3 && c1<=5)cube1 = 4;
		else if(r1>=3 && r1<=5 && c1>=6)cube1 = 5;
		else if(r1>=6 && c1<=2)cube1 = 6;
		else if(r1>=6 && c1>=3 && c1<=5)cube1 = 7;
		else if(r1>=6 && c1>=6)cube1 = 8;
		int cube2 = 0;
		if(r2<=2 && c2<=2)cube2 = 0;
		else if(r2<=2 && c2>=3 && c2<=5)cube2 = 1;
		else if(r2<=2 && c2>=6)cube2 = 2;
		else if(r2>=3 && r2<=5 && c2<=2)cube2 = 3;
		else if(r2>=3 && r2<=5 && c2>=3 && c2<=5)cube2 = 4;
		else if(r2>=3 && r2<=5 && c2>=6)cube2 = 5;
		else if(r2>=6 && c2<=2)cube2 = 6;
		else if(r2>=6 && c2>=3 && c2<=5)cube2 = 7;
		else if(r2>=6 && c2>=6)cube2 = 8;
		int cube3 = 0;
		if(r3<=2 && c3<=2)cube3 = 0;
		else if(r3<=2 && c3>=3 && c3<=5)cube3 = 1;
		else if(r3<=2 && c3>=6)cube3 = 2;
		else if(r3>=3 && r3<=5 && c3<=2)cube3 = 3;
		else if(r3>=3 && r3<=5 && c3>=3 && c3<=5)cube3 = 4;
		else if(r3>=3 && r3<=5 && c3>=6)cube3 = 5;
		else if(r3>=6 && c3<=2)cube3 = 6;
		else if(r3>=6 && c3>=3 && c3<=5)cube3 = 7;
		else if(r3>=6 && c3>=6)cube3 = 8;

		return cube1 == cube2 && cube2 == cube3;
	}

	public void removeFromCubeExceptRow(String rc, int num){
		int i_rc = Integer.parseInt(rc);
		int r = i_rc/10, c = i_rc%10, targetRow = r;

		int cube = 0;
		if(r<=2 && c<=2)cube = 1;
		else if(r<=2 && c>=3 && c<=5)cube = 2;
		else if(r<=2 && c>=6)cube = 3;
		else if(r>=3 && r<=5 && c<=2)cube = 4;
		else if(r>=3 && r<=5 && c>=3 && c<=5)cube = 5;
		else if(r>=3 && r<=5 && c>=6)cube = 6;
		else if(r>=6 && c<=2)cube = 7;
		else if(r>=6 && c>=3 && c<=5)cube = 8;
		else if(r>=6 && c>=6)cube = 9;

		switch(cube){
		case 1:
			r = 0;
			c = 0;
			break;
		case 2:
			r = 0;
			c = 3;
			break;
		case 3:
			r = 0;
			c = 6;
			break;
		case 4:
			r = 3;
			c = 0;
			break;
		case 5:
			r = 3;
			c = 3;
			break;
		case 6:
			r = 3;
			c = 6;
			break;
		case 7:
			r = 6;
			c = 0;
			break;
		case 8:
			r = 6;
			c = 3;
			break;
		case 9:
			r = 6;
			c = 6;
			break;
		}

		for(int r1 = r; r1<r+3; r1++){
			if(r1 == targetRow)continue;
			for(int c1 = c; c1<c+3; c1++)
				able[r1][c1] = able[r1][c1].replaceAll(""+num, "");
		}
		gui.append("MADE A CUBE INFERENCE OF A "+num+" AT ROW "+(targetRow+1)+" AND CUBE "+cube+"\n");
	}

	public void removeFromCubeExceptCol(String rc, int num){
		int i_rc = Integer.parseInt(rc);
		int r = i_rc/10, c = i_rc%10, targetCol = c;

		int cube = 0;
		if(r<=2 && c<=2)cube = 1;
		else if(r<=2 && c>=3 && c<=5)cube = 2;
		else if(r<=2 && c>=6)cube = 3;
		else if(r>=3 && r<=5 && c<=2)cube = 4;
		else if(r>=3 && r<=5 && c>=3 && c<=5)cube = 5;
		else if(r>=3 && r<=5 && c>=6)cube = 6;
		else if(r>=6 && c<=2)cube = 7;
		else if(r>=6 && c>=3 && c<=5)cube = 8;
		else if(r>=6 && c>=6)cube = 9;

		switch(cube){
		case 1:
			r = 0;
			c = 0;
			break;
		case 2:
			r = 0;
			c = 3;
			break;
		case 3:
			r = 0;
			c = 6;
			break;
		case 4:
			r = 3;
			c = 0;
			break;
		case 5:
			r = 3;
			c = 3;
			break;
		case 6:
			r = 3;
			c = 6;
			break;
		case 7:
			r = 6;
			c = 0;
			break;
		case 8:
			r = 6;
			c = 3;
			break;
		case 9:
			r = 6;
			c = 6;
			break;
		}

		for(int c1 = c; c1<c+3; c1++){
			if(c1 == targetCol)continue;
			for(int r1 = r; r1<r+3; r1++)
				able[r1][c1] = able[r1][c1].replaceAll(""+num, "");
		}
		gui.append("MADE A CUBE INFERENCE OF A "+num+" AT COL "+(targetCol+1)+" AND CUBE "+cube+"\n");
	}

	public void addInference(int r, int c, int num){
		madeInferences[madeInferencesIndex] = ""+num+r+c;
		madeInferencesIndex++;
	}

	public void addInference(String indecies, int num){
		madeInferences[madeInferencesIndex] = ""+num+indecies;
		madeInferencesIndex++;
	}

	public boolean isEligibleInference(int r, int c, int num){
		String inference = ""+num+r+c;
		for(String x: madeInferences){
			if(x == null)break;
			if(x.equals(inference))return false;
		}
		return true;
	}

	public boolean isEligibleInference(String indecies, int num){
		String inference = ""+num+indecies;
		for(String x: madeInferences){
			if(x == null)break;
			if(x.equals(inference))return false;
		}
		return true;
	}

	public boolean notInSameCube(int c1, int c2){
		int cube1, cube2;
		if(c1>=6)cube1 = 3;
		else if(c1>=3)cube1 = 2;
		else cube1 = 1;

		if(c2>=6)cube2 = 3;
		else if(c2>=3)cube2 = 2;
		else cube2 = 1;

		return cube1!=cube2;
	}

	public String[][] getBoardWithoutZeros(){
		String[][] temp = new String[9][9];
		for(int r = 0; r<9; r++)
			for(int c = 0; c<9; c++){
				if(board[r][c]!=0)temp[r][c] = ""+board[r][c];
				else if(able[r][c].length()==1)temp[r][c] = " "+able[r][c]+" ";
				else temp[r][c] = able[r][c];
			}
		return temp;
	}

	private int[][][] previousBoards = new int[3][][];
	private String[][][] previousAbles = new String[3][][];
	private int move = 0;

	public void logMove(){
		if(move == 3){
			for(int r = 0; r<9; r++)
				for(int c = 0; c<9; c++){
					previousBoards[0][r][c] = previousBoards[1][r][c];
					previousAbles[0][r][c] = previousAbles[1][r][c];
					previousBoards[1][r][c] = previousBoards[2][r][c];
					previousAbles[1][r][c] = previousAbles[2][r][c];
					previousBoards[2][r][c] = board[r][c];
					previousAbles[2][r][c] = able[r][c];
				}
		}
		else{
			previousBoards[move] = new int[9][9];
			previousAbles[move] = new String[9][9];
			for(int r = 0; r<9; r++)
				for(int c = 0; c<9; c++){
					previousBoards[move][r][c] = board[r][c];
					previousAbles[move][r][c] = able[r][c];
				}
			move++;
		}
	}

	public void goBackAStep(){
		move--;
		for(int r = 0; r<9; r++)
			for(int c = 0; c<9; c++){
				board[r][c] = previousBoards[move][r][c];
				able[r][c] = previousAbles[move][r][c];
			}
	}

	public String getBoardString(){
		String temp = "";
		for(int r = 0; r<9; r++)
			for(int c = 0; c<9; c++)
				if(board[r][c]!=0)
					temp += board[r][c];
				else temp += 0;
		return temp;
	}

	public int[][] checkValidity(){
		String indecies = "";
		for(int rc = 0; rc<9; rc++){
			for(int c = 0; c<9; c++){
				boolean foundAnother = false;
				if(board[rc][c]!=0){
					for(int c1 = c+1; c1<9; c1++)
						if(board[rc][c] == board[rc][c1]){
							foundAnother = true;
							indecies += ""+rc+c1;
						}
					if(foundAnother)indecies += ""+rc+c;
				}
			}

			for(int r = 0; r<9; r++){
				boolean foundAnother = false;
				if(board[r][rc]!=0){
					for(int r1 = r+1; r1<9; r1++)
						if(board[r][rc] == board[r1][rc]){
							foundAnother = true;
							indecies += ""+r1+rc;
						}
					if(foundAnother)indecies += ""+r+rc;
				}
			}
		}

		for(int rowRotor = 0; rowRotor<3; rowRotor++)
			for(int colRotor = 0; colRotor<3; colRotor++)
				for(int r = rowRotor*3; r<rowRotor*3+3; r++)
					for(int c = colRotor*3; c<colRotor*3+3; c++){
						boolean foundAnother = false;
						if(board[r][c]!=0){
							for(int r1 = r+1; r1<rowRotor*3+3; r1++)
								for(int c1 = c+1; c1<colRotor*3+3; c1++)
									if(board[r][c] == board[r1][c1]){
										foundAnother = true;
										indecies += ""+r1+c1;
									}
							if(foundAnother)indecies += ""+r+c;
						}
					}
		if(indecies.length()==0)return null;
		
		int[][] invalidIndecies = new int[indecies.length()/2][2];
		int index = 0;
		for(int i = 0; i<indecies.length(); i+=2){
			invalidIndecies[index][0] = Integer.parseInt(""+indecies.charAt(i));
			invalidIndecies[index][1] = Integer.parseInt(""+indecies.charAt(i+1));
			index++;
		}
		return invalidIndecies;
	}
}




















