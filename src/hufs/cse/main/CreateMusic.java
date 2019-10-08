package hufs.cse.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class CreateMusic{
	public CreateMusic(){

		Vector notes = new Vector();
		Vector ChordTreavleResult = new Vector();
		Vector MelodyTreavleResult = new Vector();
	    Connection c = null;//connection DB
	    Statement getChordSetList = null;
	    Statement MainStatement = null;
	   // PreparedStatement InsertStatement = null;
	    
	    int SetId = 0;
	    int ChordSetId = 0;
	    int ChordId = 0;
	    int count = 1;
	    String temp = ""; 
	    int position = 0;
	    int tempNum = 0; //for save last num for markov
		int note = 0;
		int newSongSize = 0;
		
		String ResultChooseChord = null;
		
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:music_database.db");
	      c.setAutoCommit(false);//deny the auto commit
	      System.out.println("Opened database successfully");

	      getChordSetList = c.createStatement();
	      MainStatement = c.createStatement();
	      
	      MainStatement.executeUpdate("drop table if exists new_song");
	      MainStatement.executeUpdate("create table new_song (bar_id integer, " +
	        		"right_rhythm_set text, " +
	        		"right_melody_set text, " +
	        		"left_rhythm_set text, " +
	        		"left_melody_set text)");
	      
	      
	      ResultSet getChordSet = getChordSetList.executeQuery("SELECT * FROM set_list");
	     /* 
	      sql = "INSERT INTO new_song (bar_id,right_rhythm_set,right_melody_set,left_rhythm_set,left_melody_set)" +
	        		"VALUES (?,?,?,?,?);";
	        InsertStatement = c.prepareStatement(sql);
	       */ 
	      /**************************
	       * 
	       * start the insert part
	       *********************************************/
	      while(getChordSet.next()){
	    	  SetId = getChordSet.getInt("set_id");
	    	  position = notes.indexOf(SetId);
	    	  if(position == -1){
		        	notes.add(SetId);
		        	count++;
		        }
		        else{
		        	count++;
		        }
	      }  
	      /******
	       * get a new song rhythm size
	       *
	       */
	      ResultSet getNewRhythmSet = getChordSetList.executeQuery("SELECT * FROM rhythm_of_new_song");
	      while(getNewRhythmSet.next()){
	    	  newSongSize++;  
	      }
	      
	      /**************************
	       * 
	       * finish the insert part
	       *********************************************/
	      
	      /******************
		     * make dynamic array
		     ******************/ 
	      int size = notes.size()+1;
		    String [][] testArray = new String [size][size];
		    for(int i = 0; i < size; i++){
		    	for(int j = 0; j < size ; j++){
		    		testArray[j][i] = "0";
		    	}
		    }
		    for(int i = 1; i < size; i++){
		    	testArray[i][0] = notes.get(i-1).toString();
		    	testArray[0][i] = notes.get(i-1).toString();	
		    }
		    
		    /*************
			    * finish make dynamic array
			    * 
			    **************/
		    /********print**************/
		    
		    /*
		     * �엯�젰 �셿猷� �맂 踰≫꽣 �쟾泥� 諛곗뿴�쓣 �솗�씤�븿.
		     * 
		     */
		    
		    for(int i = 0; i < size; i++){
		    	for(int j = 0; j < size ; j++){
		    		System.out.print(testArray[j][i] + " ");
		    	}
		    	System.out.println();
		    }
		    //System.out.println(testArray.length);
		    System.out.println();
		    System.out.println();
		    System.out.println();
		    /*******************************/
		    getChordSet = getChordSetList.executeQuery("SELECT * FROM chord_list");
		    while(getChordSet.next()){
		    	ChordSetId = getChordSet.getInt("chord_set_id");
		    	ChordId = getChordSet.getInt("chord_id");
		    	if(ChordId == 1){
		    		for(int i = 1; i<size; i++){
			    		if(ChordSetId == Integer.parseInt(testArray[i][0])){
			    			tempNum = i;
			    		}
			    	}
		    	}
		    	else{
		    		for(int i = 0; i<size; i++){
				    	   if(ChordSetId == Integer.parseInt(testArray[i][0])){//exist 2,0
				    		   note = Integer.parseInt(testArray[tempNum][i]);
				    		   note += 1;
				    		   testArray[tempNum][i] = Integer.toString(note);
				    		   tempNum = i;
				    	   }
			    	   }
		    	}
		    }
		    
		    System.out.println();
		    System.out.println();
		    System.out.println();
		    
		    for(int i = 0; i < size; i++){
		    	for(int j = 0; j < size ; j++){
		    		System.out.print(testArray[j][i] + " ");
		    	}
		    	System.out.println();
		    }
		    /**********************************************************************/
		    ResultChooseChord = FirstSearchString(testArray,newSongSize);//肄붾뱶 �뀑�뿉 ���븳 �쓲由� 寃곗젙
		    System.out.println("Next Set num : " + ResultChooseChord);//紐⑤몢 紐⑥븘! c - d - e
		    
		    StringTokenizer results = new StringTokenizer(ResultChooseChord, ",");
		    String OneKey = null;
		    String ChordKeyNotes = null;
		    String MelodyNotes = null;
		    String ChordMelody = null;
		    String Melody = null;
		    
		    String RhytymSet = null;
		    String MelodySet = null;
		    String TokenRhytymSet = null;
		    String TokenMelodySet = null;
		    
		    
		    
		    int LeftTreavelNum = 0;
		    int RightTreavelNum = 0;
		    int TokenLeftNum = 0;
		    int TokenRightNum = 0;
		    int TokenLeftTreavelNum = 0;
		    int TokenRightTreavelNum = 0;
		    int cnt = 1;
		    int MelodyCnt = 1;
		    int KeyCnt = 0;
		    while (results.hasMoreTokens()) {
		    	OneKey = results.nextToken();
		    	KeyCnt++;
		    	ResultSet getChordMarkovSet = getChordSetList.executeQuery("SELECT * FROM chord_list WHERE chord_set_id="+OneKey+";");
		    	ChordTreavleResult.clear();
		    	 while(getChordMarkovSet.next()){
		    		 ChordKeyNotes = getChordMarkovSet.getString("chord_set_note");
		    		 MelodyNotes = getChordMarkovSet.getString("chord_right_note");
		    		 //System.out.println(ChordKeyNotes+" : ");
		    		 StringTokenizer ChordMelodyresults = new StringTokenizer(ChordKeyNotes, "-");
		    		 while (ChordMelodyresults.hasMoreTokens()) {
		    			 ChordMelody = ChordMelodyresults.nextToken()+"("+cnt+")"; 
		    			 position = ChordTreavleResult.indexOf(ChordMelody);
		    			 if(position == -1){
		    				 ChordTreavleResult.add(ChordMelody);
		    				 cnt++;
		    			 }
		    			 else{
		    				 cnt++;
		    			 }
		    		 }//c肄붾뱶 �뿬�윭 �삁�떆 以� �븯�굹�쓽 �삁�떆�뿉 ���빐 ChordTreavleResult踰≫꽣�뿉 �엯�젰
		    		 cnt = 1;
		    		 MelodyTreavleResult.clear();
		    		 ChordMelodyresults = new StringTokenizer(MelodyNotes, "-");
		    		 while (ChordMelodyresults.hasMoreTokens()) {
		    			 Melody = ChordMelodyresults.nextToken()+"("+cnt+")"; 
		    			 position = MelodyTreavleResult.indexOf(Melody);
		    			 if(position == -1){
		    				 MelodyTreavleResult.add(Melody);
		    				 cnt++;
		    			 }
		    			 else{
		    				 cnt++;
		    			 }
		    		 }//c肄붾뱶 �뿬�윭 �삁�떆 以� �븯�굹�쓽 �삁�떆�뿉 ���빐 ChordTreavleResult踰≫꽣�뿉 �엯�젰
		    		 cnt = 1;
		    	 }System.out.println(ChordTreavleResult);
		    	 System.out.println(MelodyTreavleResult);
		    	 
		    	 int ChordMelodyMarkovSize = ChordTreavleResult.size()+1;
		    	 int MelodyMarkovSize = MelodyTreavleResult.size()+1;
		    	 String [][] ChordMelodyArray = new String [ChordMelodyMarkovSize][ChordMelodyMarkovSize];
		    	 String [][] MelodyArray = new String [MelodyMarkovSize][MelodyMarkovSize];
		    	 
		 	     for(int i = 0; i < ChordMelodyMarkovSize; i++){
		 	    	 for(int j = 0; j < ChordMelodyMarkovSize ; j++){
		 	    	 	ChordMelodyArray[j][i] = "0";
		 	    	 }
		 	     }
		 	    for(int i = 0; i < MelodyMarkovSize; i++){
		 	    	 for(int j = 0; j < MelodyMarkovSize ; j++){
		 	    		MelodyArray[j][i] = "0";
		 	    	 }
		 	     }
		 	    ///////////////////////////////////////////
		 	     for(int i = 1; i < ChordMelodyMarkovSize; i++){
		 	    	ChordMelodyArray[i][0] = ChordTreavleResult.get(i-1).toString();
		 	    	ChordMelodyArray[0][i] = ChordTreavleResult.get(i-1).toString();
		 	    }
		 	    for(int i = 1; i < MelodyMarkovSize; i++){
		 	    	MelodyArray[i][0] = MelodyTreavleResult.get(i-1).toString();
		 	    	MelodyArray[0][i] = MelodyTreavleResult.get(i-1).toString();
		 	    }
		 	     /////////////////////////////////
			     /*
			      * update markov
			      */
			     getChordMarkovSet = getChordSetList.executeQuery("SELECT * FROM chord_list WHERE chord_set_id="+OneKey+";");
			     ChordTreavleResult.clear();
			     MelodyTreavleResult.clear();
			     int BeforeChordMelody = 0;
			     int MarkovNum = 0;
			     while(getChordMarkovSet.next()){
		    		 ChordKeyNotes = getChordMarkovSet.getString("chord_set_note");
		    		 MelodyNotes = getChordMarkovSet.getString("chord_right_note");
		    		// System.out.println(ChordKeyNotes+" : ");
		    		 StringTokenizer ChordMelodyresults = new StringTokenizer(ChordKeyNotes, "-");
		    		 while (ChordMelodyresults.hasMoreTokens()) {
		    			 ChordMelody = ChordMelodyresults.nextToken()+"("+cnt+")"; 
		    			 if(cnt == 1){
		    				 for(int i =0; i<ChordMelodyMarkovSize; i++){
		    					 if(ChordMelody.equals(ChordMelodyArray[i][0])){
		    						 BeforeChordMelody = i;
		    						 cnt++;
		    					 }
		    				 }
		    			 }
		    			 else{
		    				 for(int i =0; i<ChordMelodyMarkovSize; i++){
		    					 if(ChordMelody.equals(ChordMelodyArray[i][0])){
		    						 MarkovNum = Integer.parseInt(ChordMelodyArray[BeforeChordMelody][i]);
		    						 MarkovNum += 1;
		    						 ChordMelodyArray[BeforeChordMelody][i] = Integer.toString(MarkovNum);
		    						 BeforeChordMelody = i;
		    					 }
		    				 }cnt++;
		    			 }
		    		 }//end of while//c肄붾뱶 �뿬�윭 �삁�떆 以� �븯�굹�쓽 �삁�떆�뿉 ���빐 ChordTreavleResult踰≫꽣�뿉 �엯�젰
		    		 cnt = 1;
		    		 BeforeChordMelody = 0;
				     MarkovNum = 0;
		    		 //////////////////////////
		    		 ChordMelodyresults = new StringTokenizer(MelodyNotes, "-");
		    		 while (ChordMelodyresults.hasMoreTokens()) {
		    			 Melody = ChordMelodyresults.nextToken()+"("+cnt+")"; 
		    			 if(cnt == 1){
		    				 for(int i =0; i<MelodyMarkovSize; i++){
		    					 if(Melody.equals(MelodyArray[i][0])){
		    						 BeforeChordMelody = i;
		    						 cnt++;
		    					 }
		    				 }
		    			 }
		    			 else{
		    				 for(int i =0; i<MelodyMarkovSize; i++){
		    					 if(Melody.equals(MelodyArray[i][0])){
		    						 MarkovNum = Integer.parseInt(MelodyArray[BeforeChordMelody][i]);
		    						 MarkovNum += 1;
		    						 MelodyArray[BeforeChordMelody][i] = Integer.toString(MarkovNum);
		    						 BeforeChordMelody = i;
		    					 }
		    				 }cnt++;
		    			 }
		    		 }//end of while//c肄붾뱶 �뿬�윭 �삁�떆 以� �븯�굹�쓽 �삁�떆�뿉 ���빐 ChordTreavleResult踰≫꽣�뿉 �엯�젰
		    		 cnt = 1;
		    	 }
			     ///////////////////////////////////////////////
			     for(int i = 0; i < ChordMelodyMarkovSize; i++){
				     	for(int j = 0; j < ChordMelodyMarkovSize ; j++){
				     		System.out.print(ChordMelodyArray[j][i] + " ");
				     	}
				     	System.out.println();
			     }
			     for(int i = 0; i < MelodyMarkovSize; i++){
				     	for(int j = 0; j < MelodyMarkovSize ; j++){
				     		System.out.print(MelodyArray[j][i] + " ");
				     	}
				     	System.out.println();
			     }
			     System.out.println();
				 System.out.println();
				 System.out.println();
				   
				 ResultSet getSearchSize = getChordSetList.executeQuery("SELECT * FROM rhythm_of_new_song WHERE new_rhythm_id="+KeyCnt+";");
				 System.out.println("keyCnt : " + KeyCnt);
				 
				 
				 
				 RhytymSet = getSearchSize.getString("new_left_rhythm_set");
				 MelodySet = getSearchSize.getString("new_right_rhythm_set");
				 
				 String LeftResult = null;
				 String RightResult = null;
				 
				 while(getSearchSize.next()){
					 LeftTreavelNum = getRhythmSetSize(RhytymSet,",");
					 RightTreavelNum = getRhythmSetSize(MelodySet,",");
				 }
				 
				 ////////////////////////////////
				 int[] LeftTokenNumSet = new int[LeftTreavelNum];
				 int[] RightTokenNumSet = new int[RightTreavelNum];
				 
				 /*******************************�솕�쓬 媛쒖닔 援ы븯湲�**************************************/
				 getSearchSize = getChordSetList.executeQuery("SELECT * FROM rhythm_of_new_song WHERE new_rhythm_id="+KeyCnt+";");
				 
				 TokenRhytymSet = getSearchSize.getString("new_left_rhythm_set");
				 TokenMelodySet = getSearchSize.getString("new_right_rhythm_set");
				 
				 String TokenLeftResult = null;
				 String TokenRightResult = null;
				 
				 int k = 0;
				 int j = 0;
				 
				 while(getSearchSize.next()){
					 StringTokenizer RhythmTokenResult = new StringTokenizer(TokenRhytymSet, ",");
				      while (RhythmTokenResult.hasMoreTokens()) {
				    	  TokenLeftResult = RhythmTokenResult.nextToken();
				    	  TokenLeftNum = getRhythmSetSize(TokenLeftResult," ");
				    	  LeftTokenNumSet[k] = TokenLeftNum;
				    	  k++;
				    	} 
				      StringTokenizer MelodyTokenResult = new StringTokenizer(TokenMelodySet, ",");
				      while (MelodyTokenResult.hasMoreTokens()) {
				    	  TokenRightResult = MelodyTokenResult.nextToken();
				    	  TokenRightNum = getRhythmSetSize(TokenRightResult," ");
				    	  RightTokenNumSet[j] = TokenRightNum;
				    	  j++;
				    	}
				 }
				 //TokenLeftNum�� 諛섏＜遺��뿉�꽌 �븳 肄⑸굹臾쇱쓽 �솕�쓬 媛��닔
				 /**********************************************************************/
				 
				 
				 
				 
				 
				 /////////////////////////////////////////////////////////////////////////////////
				 LeftResult = MelodySearchString(ChordMelodyArray,LeftTreavelNum,LeftTokenNumSet);
			     System.out.println("*************************************************");
			     System.out.print("right : ");
			     RightResult = MelodySearchString(MelodyArray,RightTreavelNum,RightTokenNumSet);
			     
			     
			     System.out.println(LeftResult+"//"+RightResult+"//"+KeyCnt+"//"+MelodySet+"//"+RhytymSet);
			     String sql = "INSERT INTO new_song (bar_id,right_rhythm_set,right_melody_set,left_rhythm_set,left_melody_set) " +
		                   "VALUES ("+KeyCnt+",'"+MelodySet+"','"+RightResult+"','"+RhytymSet+"','"+LeftResult+"');"; 
			     getChordSetList.executeUpdate(sql);
			     /*
			     InsertStatement.setInt(1, KeyCnt);
			     InsertStatement.setString(2, MelodySet);
				 InsertStatement.setString(3, RhytymSet);
			     InsertStatement.setString(4, RightResult);
				 InsertStatement.setString(5, LeftResult);
				 InsertStatement.executeUpdate();
				 */
		    }
		    getChordSetList.close();
		    c.commit();
		    c.close();
		    
		    
		    
		    /********************************************************************/
	    } 
	    catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");
	}
	public static String FirstSearchString(String[][] InputArray, int SearchNum){
		  String[] PossibleSet = new String[InputArray.length-1];
		  String StartSetNum = "";
		  String NextSetNum = "";
		  String LeftSet = "";
		  for(int i = 1; i<InputArray.length; i++){
			  PossibleSet[i-1] = InputArray[i][0];
		  }
		  StartSetNum = RandomSearch(PossibleSet);
		  NextSetNum = StartSetNum;
		
		  System.out.println("Start Set num : " + StartSetNum);
		  for(int i = 0; i<SearchNum-1; i++){//肄붾뱶 �듃�젅釉붿쓣 紐� 踰� �븷爰쇰깘!
			  StartSetNum = NextSearchString(InputArray, StartSetNum);
			  NextSetNum += "," + StartSetNum; 
		  }
		//  System.out.println("Next Set num : " + NextSetNum);//紐⑤몢 紐⑥븘! c - d - e
		  return NextSetNum;
	  }
	  
	/******************************************************************************/
	  
	  public static String MelodySearchString(String[][] InputArray, int SearchNum){
		  System.out.println("#MelodySearchString Start");
		  String StartSetNum = "";
		  String NextSetNum = "";
		  String LeftSet = "";
		  int cnt = 0;
		  for(int i = 1; i<InputArray.length; i++){
			  String Key = InputArray[i][0];
			 // System.out.println("key : " + Key);
			  Pattern regex = Pattern.compile("\\(+[0-9]+\\)");
		      Matcher regexMatcher = regex.matcher(Key); 
		      while (regexMatcher.find()){
		    	  if(regexMatcher.group().equals("(1)")){
		    		  cnt++;
		    	  }
		      }
		  }
		  String[] PossibleSet = new String[cnt];
		  
		  for(int i = 1; i<InputArray.length; i++){
			  String Key = InputArray[i][0];
			  Pattern regex = Pattern.compile("\\(+[0-9]+\\)");
		      Matcher regexMatcher = regex.matcher(Key); 
		      while (regexMatcher.find()){ 
		    	  if(regexMatcher.group().equals("(1)")){//�뿬湲곗꽌 possible set�옄泥댁뿉 議곌굔�쓣 二쇱뼱�꽌 媛��뒫�븳 �냸�쓣 �꽔�쓬.
		    		  PossibleSet[i-1] = InputArray[i][0];
		    	  }
		      }
		  }
		  StartSetNum = RandomSearch(PossibleSet);
		  NextSetNum = StartSetNum;
		  
		 // System.out.println("Start Set num : " + StartSetNum);
		  for(int i = 0; i<SearchNum-1; i++){//肄붾뱶 �듃�젅釉붿쓣 紐� 踰� �븷爰쇰깘!
			  StartSetNum = NextSearchString(InputArray, StartSetNum);
			  
			  NextSetNum += "," + InputArray[Integer.parseInt(StartSetNum)][0]; 
		  }
		  System.out.println("Next Set num : " + NextSetNum+"\n");//紐⑤몢 紐⑥븘! c - d - e
		  
		  return NextSetNum;
	  }
	  /********************************************************************************/
	  
	  /*******************************************************************************/
	  
	  public static String MelodySearchString(String[][] InputArray, int SearchNum, int[] TokenSet){
		  System.out.println("#MelodySearchString2 Start");
		  String StartSetNum = "";
		  String NextSetNum = "";
		  String LeftSet = "";
		  int cnt = 0;
		  int k = 0;
		  int size = 0;
		  String[] PossibleSet;
		  int Flag = 0;
		  
		  for(int i = 1; i<InputArray.length; i++){
			  String Key = InputArray[i][0];
			 // System.out.println("key : " + Key);
			  Pattern regex = Pattern.compile("\\(+[0-9]+\\)");
		      Matcher regexMatcher = regex.matcher(Key); 
		      while (regexMatcher.find()){
		    	  if(regexMatcher.group().equals("(1)")){//�떆�옉 由щ벉�쓽 �솕�쓬 媛쒖닔�� 鍮꾧탳�븯�뿬 媛��뒫�븳 寃껋쑝濡�
		    		  size = getRhythmSetSize(Key,"+");//
		    		  if(size == TokenSet[0]){
		    			  System.out.println("TokenSetNum : " + TokenSet[0]);
		    			  cnt++;
		    		  }
		    	  }
		      }
		  }
		    
		  if(cnt == 0){
			  System.out.println("error:no match rhythm.... Search diffrent rhythm...");
			  Flag = 1;
			  for(int i = 1; i<InputArray.length; i++){
				  String Key = InputArray[i][0];
				  Pattern regex = Pattern.compile("\\(+[0-9]+\\)");
				  Matcher regexMatcher = regex.matcher(Key); 
		    	  while (regexMatcher.find()){
			    	  if(regexMatcher.group().equals("(1)")){
			    		  cnt++;
			    	  }
			      }
			  } 
	      }
		  System.out.println("cnt : " + cnt);
		  
		  PossibleSet = new String[cnt];
		  
		  int j = 0;
		  if(Flag == 0){
			  for(int i = 1; i<InputArray.length; i++){
				  String Key = InputArray[i][0];
				  Pattern regex = Pattern.compile("\\(+[0-9]+\\)");
			      Matcher regexMatcher = regex.matcher(Key); 
			      while (regexMatcher.find()){ 
			    	  if(regexMatcher.group().equals("(1)")){
			    		  size = getRhythmSetSize(Key,"+");//
			    		  if(size == TokenSet[0]){
			    			  PossibleSet[j] = InputArray[i][0];
			    			  j++;
			    		  }
			    	  }
			      }
			  }
			  j = 0;
		  }
		  
		  else{
			  for(int i = 1; i<InputArray.length; i++){
				  String Key = InputArray[i][0];
				  Pattern regex = Pattern.compile("\\(+[0-9]+\\)");
				  Matcher regexMatcher = regex.matcher(Key); 
		    	  while (regexMatcher.find()){
			    	  if(regexMatcher.group().equals("(1)")){	  
			    		  PossibleSet[j] = InputArray[i][0];
			    		  j++;
			    	  }
			      }
			  }
			  j = 0;
	      }
		  
		  StartSetNum = RandomSearch(PossibleSet);
		  NextSetNum = StartSetNum;
		  k++;
		 // System.out.println("Start Set num : " + StartSetNum);
		  for(int i = 0; i<SearchNum-1; i++){//肄붾뱶 �듃�젅釉붿쓣 紐� 踰� �븷爰쇰깘!
			  StartSetNum = NextSearchString(InputArray, StartSetNum, TokenSet[k]);
			  NextSetNum += "," + InputArray[Integer.parseInt(StartSetNum)][0]; 
			  k++;
		  }
		  System.out.println("Next Set num : " + NextSetNum+"\n");//紐⑤몢 紐⑥븘! c - d - e
		  
		  return NextSetNum;
	  }
	/******************************************************************************/
	 
	/******************************************************************************/
	  
	  public static int getRhythmSetSize(String RhythmSet, String Token){
		  System.out.println("getRhythmSetSize Start");
		  int size = 0;
		  System.out.println(RhythmSet);
		  StringTokenizer results = new StringTokenizer(RhythmSet, Token);
	      while (results.hasMoreTokens()) {
	    	  results.nextToken();
	    	  size++;
	    	}     
	      System.out.println(size);
		  return size;
	  }
	  
	  
	  /*******************************************************************************/

	  
	  /*******************************************************************************/
	  
	  public static String NextSearchString(String[][] InputArray, String BeforeSet){
		  System.out.println("#NextSearchString Start");
		//  String[] PossibleSet = new String[InputArray.length-1];
		  int IntBeforeSet = 0;
		  
		  Pattern regex = Pattern.compile("[0-9]");
	      Matcher regexMatcher = regex.matcher(BeforeSet); 
	      while (regexMatcher.find()){ 
	    	  IntBeforeSet = Integer.parseInt(regexMatcher.group());
	      }
	      
		  //IntBeforeSet = Integer.parseInt(BeforeSet);
		 // System.out.println("IntBeforeSet : " + IntBeforeSet);
		  int PossibleSetCnt = 0;
		  int k = 0;
		  String ChooseKey = null;
		  //�뿬湲곗꽌 possible cnt瑜� �씠�슜�빐 泥섎━�븷 �삁�젙�엯�땲�떎.
		  //留뚯빟 �쟾�떖�맂 960, 960 960�쓽 �솕�쓬 媛쒖닔�씤 1, 2媛쒕�� 留뚯”�떆�궎吏� 紐삵븳�떎硫�! 
		  for(int i = 1; i<InputArray.length; i++){
			  int temp = Integer.parseInt(InputArray[IntBeforeSet][i]);
			 // System.out.println("(" + IntBeforeSet + "," + i + ") : " + temp);
				  if(temp > 0){
					 // System.out.println("temp : " + temp);
					  PossibleSetCnt +=temp;
				  }
		  }
		 // System.out.println("PossibleSetCnt : " + PossibleSetCnt);
		  if(PossibleSetCnt == 0){
			  System.out.println("End of Markov");
			  System.out.println("Resarching..");
			  return "1";
		  }
		  String[] PossibleSet = new String[PossibleSetCnt];
		  
		  String PossibleSetNum = "";
		  
		  for(int i = 1; i<InputArray.length; i++){
			  PossibleSetNum = InputArray[IntBeforeSet][i];
			  int temp = Integer.parseInt(PossibleSetNum);
			  if(temp > 0){
				  for(int j = 0; j < temp; j++){
					  PossibleSet[k] = Integer.toString(i);
					  k++;
				  }
			  }
		  } 
		  
		  ChooseKey = RandomSearch(PossibleSet);
		  
		  return ChooseKey;
	  }
	  
	/******************************************************************************/
	  
	  /*******************************************************************************/
	  
	  public static String NextSearchString(String[][] InputArray, String BeforeSet, int TokenNum){
		  System.out.println("#NextSearchString2 Start");
		//  String[] PossibleSet = new String[InputArray.length-1];
		  int IntBeforeSet = 0;
		  String TokenSet = "";
		  int TokenSetSize = 0;
		  //�븳 肄⑸굹臾� �븞�뿉 �솕�쓬�쓽 媛쒖닔媛� TokenNum留뚰겮 �엳�뒿�땲�떎.
		  Pattern regex = Pattern.compile("[0-9]");
	      Matcher regexMatcher = regex.matcher(BeforeSet); 
	      while (regexMatcher.find()){ 
	    	  IntBeforeSet = Integer.parseInt(regexMatcher.group());
	      }
	      
		  //IntBeforeSet = Integer.parseInt(BeforeSet);
		 // System.out.println("IntBeforeSet : " + IntBeforeSet);
		  int PossibleSetCnt = 0;
		  int k = 0;
		  int flag = 0;
		  String ChooseKey = null;
		  //�뿬湲곗꽌 possible cnt瑜� �씠�슜�빐 泥섎━�븷 �삁�젙�엯�땲�떎.
		  //留뚯빟 �쟾�떖�맂 960, 960 960�쓽 �솕�쓬 媛쒖닔�씤 1, 2媛쒕�� 留뚯”�떆�궎吏� 紐삵븳�떎硫�! 
		  for(int i = 1; i<InputArray.length; i++){
			  int temp = Integer.parseInt(InputArray[IntBeforeSet][i]);
			 // System.out.println("(" + IntBeforeSet + "," + i + ") : " + temp);
				  if(temp > 0){
					  if(getRhythmSetSize(InputArray[0][i],"+") == TokenNum){
						  PossibleSetCnt +=temp;
					  }
					 // System.out.println("temp : " + temp);
				  }
		  }
		  if(PossibleSetCnt == 0){
			  System.out.println("error:no match rhythm.... Search diffrent rhythm...");
			  flag = 1;
			  for(int i = 1; i<InputArray.length; i++){
				  int temp = Integer.parseInt(InputArray[IntBeforeSet][i]);
				 // System.out.println("(" + IntBeforeSet + "," + i + ") : " + temp);
					  if(temp > 0){
						  PossibleSetCnt +=temp;
					  }
			  }
		  }
		  System.out.println("PossibleSetCnt : " + PossibleSetCnt);
		  if(PossibleSetCnt == 0){
			  System.out.println("End of Markov");
			  System.out.println("Resarching..");
			  return "1";
		  }
		  String[] PossibleSet = new String[PossibleSetCnt];
		  
		  String PossibleSetNum = "";
		  
		  System.out.println("InputArray.length : " + InputArray.length);
		  
		  if(flag == 0){
		  for(int i = 1; i<InputArray.length; i++){
			  System.out.println("i 가 몇일때 오류 : " + i);
			  PossibleSetNum = InputArray[IntBeforeSet][i];
			  if(getRhythmSetSize(InputArray[0][i],"+") == TokenNum){
			  System.out.println("PossibleSetNum : " + PossibleSetNum);
			  int temp = Integer.parseInt(PossibleSetNum);
			  System.out.println("temp : " + temp);
			  System.out.println("i : " + i);
			  if(temp > 0){
				  for(int j = 0; j < temp; j++){
					  System.out.println(" 여기!!");
					  PossibleSet[k] = Integer.toString(i);
					  System.out.println(" k : "+k+"//"+Integer.toString(i));
					  k++;
				  }
				  
			  }
			  }
		  } 
		  }
		  else{
			  for(int i = 1; i<InputArray.length; i++){
				  System.out.println("i 가 몇일때 오류 : " + i);
				  PossibleSetNum = InputArray[IntBeforeSet][i];
				  System.out.println("PossibleSetNum : " + PossibleSetNum);
				  int temp = Integer.parseInt(PossibleSetNum);
				  System.out.println("temp : " + temp);
				  System.out.println("i : " + i);
				  if(temp > 0){
					  for(int j = 0; j < temp; j++){
						  System.out.println(" 여기!!");
						  PossibleSet[k] = Integer.toString(i);
						  System.out.println(" k : "+k+"//"+Integer.toString(i));
						  k++;
					  }
				  }
			  }
		  }
		  System.out.println("동작?");
		  for(int i = 0; i<PossibleSetCnt; i++){
			  System.out.print("PossibleSet["+i+"] : "+PossibleSet[i]);
		  }
		  
		  System.out.println("함수 콜");
		  ChooseKey = RandomSearch(PossibleSet);
		  System.out.println("함수 콜 끗");
		  return ChooseKey;
	  }
	  
	/******************************************************************************/
	  public static String RandomSearch(String[] data){
		  System.out.println("#RandomSearch Start");
		  System.out.print("Search data :");
		  for(int i = 0; i<data.length; i++){
			  System.out.print(data[i] + "/");
		  }
		  System.out.println();
		  
		  
		  HashMap map = new HashMap();
		    
		    for(int i=0; i<7; i++){
		        String tempString = getRandArr(data);
		       // System.out.println(tempString);
		        if(map.containsKey(tempString)){
		            Integer value = (Integer)map.get(tempString);
		            map.put(tempString, new Integer(value.intValue()+1));
		        }else{
		            map.put(tempString, new Integer(1));
		        }
		    }
		   
		    Iterator it = map.keySet().iterator();
		    int ChooseTempNum = 0;
		    String ChooseKey = "";
		    while(it.hasNext()){
		    	
		        String key = (String)it.next();
		        Integer value = (Integer)map.get(key);
		        int intValue = value.intValue();
		        if(intValue > ChooseTempNum){
		        	ChooseTempNum = intValue;
		        	ChooseKey = key;
		        }
		        System.out.println(key+" : "+printGraph('#', intValue)+" "+intValue);
		       
		    }
		    System.out.println("Choose Key : " + ChooseKey);
		    
		  return ChooseKey;
	  }
	  
	  
	  public static String printGraph(char ch, int value){
	      char[] bar = new char[value];
	     
	      for(int i=0; i<bar.length; i++){
	          bar[i]= ch;
	      }
	      return new String(bar);       
	  }

	  public static String getRandArr(String[] arr){
	      return arr[getRand(arr.length-1)];
	  }

	  public static int getRand(int n){
	         return getRand(0, n);
	  }

	  public static int getRand(int from, int to){
	      return (int)(Math.random() * (Math.abs(to-from)+1))+Math.min(from, to);
	  }
	  /*******************************************************************/
}