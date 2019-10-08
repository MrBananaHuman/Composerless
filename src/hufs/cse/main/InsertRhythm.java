package hufs.cse.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

class InsertRhythm{
	public InsertRhythm(String Genre){

	    Connection c = null;//connection DB
	    Statement MainStatement = null;
	    Statement LeftStatement = null;//this is for left note query (DB)
	    Statement RightStatement = null;//this is for right note query (DB)
	    Statement NewStatement = null;
	    PreparedStatement InsertStatement = null;
	    
	    /****left*****/
	    int LeftStartTick = -1;//start tick
	    int LeftEndTick = -1;//end tick
	    int LeftCurrentTick = -1;//current midi tick
	    int LeftNoteIdInSong = 0;//current note ID
	    int LeftNoteId = 0;
	    int LeftSetNum = -1;//left set num
	    int LeftNoteInBarNum = -1;//note in bar num
	    int TempLeftSetNum = -1;//temp for save left set number of previous note
	    int TempLeftNoteInBarNum = -1;//temp for save note in bar number of previous note
	    int LeftNoteIdInBar = 0;
	    
	    int ChordSetNum = 1;//chord num (in the one bar)
	    int TempChordSetNum = 1;//temp for save chord number
	    
	    
	    int TempLeftStartTick = -1;//temp for save start tick of previous note
	    int TempLeftEndTick = -1;//temp for save end tick of previous note
	    int TempLeftCurrentTick = -1;//temp for save midi tick of previous note
	    /***********/
	    
	    /*****right*****/
	    int RightStartTick = -1;
	    int RightEndTick = -1;
	    int RightCurrentTick = -1;
	    int RightNoteNum = 0;
	    int RightSetNum = -1;//left set num
	    int RightNoteInBarNum = -1;//note in bar num
	    int TempRightSetNum = -1;//temp for save left set number of previous note
	    int TempRightNoteInBarNum = -1;//temp for save note in bar number of previous note
	    int RightNoteIdInSong = -1;
	    int RightNoteIdInBar = -1;
	    int RightBarNum = 0;
	    
	    int TempRightStartTick = -1;
	    int TempRightEndTick = -1;
	    int TempRightCurrentTick = -1;
	    
	    
	    /*************/
	    
	    String LeftSet = null;//left set (480, 480, 480, 480)
	    String RightSet = null;//right set
	    String TempLeftSet = null;
	    String sql = null;
	    
	    int TimeSigTop = 0;
	    int TimeSigBottom = 0;
	    
	    int LeftSetCount = 1;
	    int RhythmID = 1;
	    


	    /*************/
	    
	   
	    
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:music_database.db");
	      c.setAutoCommit(false);//deny the auto commit
	      System.out.println("Opened database successfully");
	      
	      LeftStatement = c.createStatement();//this is for left note query (DB)
	      RightStatement = c.createStatement();//this is for right note query (DB)
	      MainStatement = c.createStatement();
	      NewStatement = c.createStatement();
	      
	      MainStatement.executeUpdate("drop table if exists rhythm_list");
	      MainStatement.executeUpdate("create table rhythm_list (rhythm_id integer primary key autoincrement not null, " +
	      		"rhythm_set_count integer not null, " +
	      		"left_rhythm_set text, " +
	      		"right_rhythm_set text, " +
	      		"time_signature_top integer not null, " +
	      		"time_signature_bottom integer not null)");
	      
	      sql = "INSERT INTO rhythm_list (rhythm_set_count,left_rhythm_set,right_rhythm_set,time_signature_top,time_signature_bottom)" +
	      		"VALUES (?,?,?,?,?);";
	      InsertStatement = c.prepareStatement(sql);
	      
	      ResultSet getSongId = NewStatement.executeQuery("SELECT * FROM genre_list WHERE genre_name='"+Genre+"';");
	      
	      int GenreId = getSongId.getInt("genre_id");
	      System.out.println("genre id : "+ GenreId);
	      getSongId = NewStatement.executeQuery("SELECT * FROM song_list WHERE song_genre='"+GenreId+"';");
	while(getSongId.next()){
	    	  
	      
	      int SongId = getSongId.getInt("song_id");
	      System.out.println("song id : "+ SongId);

	      ResultSet getLeftSet = LeftStatement.executeQuery("SELECT * FROM song_left_note WHERE left_song_id="+SongId+";");
	      
	      
	      while(getLeftSet.next()){
	    	  LeftCurrentTick = getLeftSet.getInt("left_note_duration");//get a current note's midi tick (duration)
	    	  LeftStartTick = getLeftSet.getInt("left_note_start_tick");//get a current note's start midi tick
	    	  LeftEndTick = LeftCurrentTick + LeftStartTick;//end tick
	    	  //System.out.println("end tick : "+LeftEndTick + "///start tick : "+LeftStartTick + "///before end tick : "+TempLeftEndTick);
	    	  LeftSetNum = getLeftSet.getInt("left_rhythm_set_num");//get a note's rhythm set number
	    	  LeftNoteInBarNum = getLeftSet.getInt("left_note_in_bar_num");//get a note's number in the bar
	    	  LeftNoteIdInSong = getLeftSet.getInt("left_note_id_in_song");//get a note's ID
	    	  ChordSetNum = getLeftSet.getInt("left_note_chord_set_num");//get a note's chord num in the bar
	    	  //SongId = getLeftSet.getInt("left_song_id");//get a song ID
	    	  LeftNoteIdInBar = getLeftSet.getInt("left_note_id_in_bar");
	    	  
	    	  TimeSigTop = getLeftSet.getInt("left_note_time_signature_top");
	    	  TimeSigBottom = getLeftSet.getInt("left_note_time_signature_bottom");
	    	  
	    	  
	    	  if(LeftNoteIdInSong == 1){//怨≪쓽 留� 泥섏쓬�뿉 ���븳 泥섎━
	    		  if(LeftStartTick != 0){//留뚯빟 怨≪쓽 留� 泥섏쓬�씠 �돹�몴媛� �굹�솕�떎硫�
	    			  LeftSet = String.valueOf(0-LeftStartTick) + "," + String.valueOf(LeftCurrentTick);
	    		  }
	    		  else{
	    			  LeftSet = String.valueOf(LeftCurrentTick);
	    		  }
	    		  TempLeftStartTick = LeftStartTick;
				  TempLeftEndTick = LeftEndTick;
				  TempLeftCurrentTick = LeftCurrentTick;
				  TempChordSetNum = ChordSetNum;
				  TempLeftNoteInBarNum = LeftNoteInBarNum;
				  TempLeftSetNum = LeftSetNum;
	    	  }
	    	  else{//怨≪쓽 留� 泥レ쓬�씠 �븘�땶 �떎�쓬 �쓬遺��꽣�뿉 ���븳 泥섎━
	    		  if(LeftNoteIdInBar == 1){
	    			  if(TempLeftEndTick != LeftStartTick){
	    				  LeftSet += "," + String.valueOf(TempLeftEndTick - LeftStartTick);
	    			  }
	    			//  System.out.println(LeftSet + "// time signature : " + TimeSigTop + "/" + TimeSigBottom);//諛섏＜遺� 紐⑥쑝湲� 鍮�! �걮!
	    			  /*********************************Start Right Rhythm********************************************************/
	    			  ResultSet getRightSet = RightStatement.executeQuery("SELECT * FROM song_right_note WHERE right_rhythm_set_num="+TempLeftSetNum+" and right_song_id="+SongId+";");
					  while(getRightSet.next()){
						  RightCurrentTick = getRightSet.getInt("right_note_duration");//get a current note's midi tick (duration)
						  RightStartTick = getRightSet.getInt("right_note_start_tick");//get a current note's start midi tick
						  RightEndTick = RightCurrentTick + RightStartTick;//end tick
				    	  RightSetNum = getRightSet.getInt("right_rhythm_set_num");//get a note's rhythm set number
				    	  RightNoteInBarNum = getRightSet.getInt("right_note_in_bar_num");//get a note's number in the bar
				    	  RightNoteIdInSong = getRightSet.getInt("right_note_id_in_song");//get a note's ID
				    	  SongId = getRightSet.getInt("right_song_id");//get a song ID
				    	  RightNoteIdInBar = getRightSet.getInt("right_note_id_in_bar");
				    	  RightBarNum = getRightSet.getInt("right_bar_num");
				    	  int StartBarTick = ((1920/TimeSigBottom)*TimeSigTop)*(RightBarNum-1);
				    	  
				    	  if(RightNoteIdInSong == 1){//怨≪쓽 留� 泥섏쓬�뿉 ���븳 泥섎━
				    		  if(RightStartTick != 0){//留뚯빟 怨≪쓽 留� 泥섏쓬�씠 �돹�몴媛� �굹�솕�떎硫�
				    			  RightSet = String.valueOf(0-RightStartTick) + "," + String.valueOf(RightCurrentTick);
				    		  }
				    		  else{
				    			  RightSet = String.valueOf(RightCurrentTick);
				    		  }
				    		  TempRightStartTick = RightStartTick;
							  TempRightEndTick = RightEndTick;
							  TempRightCurrentTick = RightCurrentTick;
							  TempRightNoteInBarNum = RightNoteInBarNum;
							  TempRightSetNum = RightSetNum; 
				    	  }
				    	  else{
				    		  if(RightNoteIdInBar == 1){  
				    			  if(TempRightEndTick != RightStartTick){
				    				  RightSet += "," + String.valueOf(TempRightEndTick - RightStartTick);   
				    			  }
				    			  System.out.println("R - " + RightSet + "// time signature : " + TimeSigTop + "/" + TimeSigBottom);
				    			  InsertStatement.setString(3, RightSet);
				    			  InsertStatement.executeUpdate();
				    			  RhythmID++;
				    			 // temp = RightSet;
								  RightSet = String.valueOf(RightCurrentTick);
				    		  }
				    		  else{
				    			  if(RightNoteInBarNum == TempRightNoteInBarNum){
				    				  RightSet += " " + String.valueOf(RightCurrentTick);
				    				  }
				    			  else{
				    				//  System.out.println(RightEndTick + " vs(1)" +RightStartTick);
				    				  RightSet += "," + String.valueOf(RightCurrentTick);
				    				  if(TempRightEndTick != RightStartTick){
				    					  RightSet += "," + String.valueOf(TempRightEndTick - RightStartTick);
				    					  }
				    				  }
				    			  }
				    		  TempRightStartTick = RightStartTick;
							  TempRightEndTick = RightEndTick;
							  TempRightCurrentTick = RightCurrentTick;
							  TempRightNoteInBarNum = RightNoteInBarNum;
							  TempRightSetNum = RightSetNum;
				    	  }
					  }//end of right while loop
					  if(LeftSet.equals(TempLeftSet)){  
						  LeftSetCount++;
					  }
					  else{
						  
						  LeftSetCount = 1;
						  TempLeftSet = LeftSet;
					  }
					  InsertStatement.setInt(1, LeftSetCount);
					  InsertStatement.setString(2, LeftSet);
					  InsertStatement.setInt(4, TimeSigTop);
					  InsertStatement.setInt(5, TimeSigBottom);
					  System.out.println("L - " + LeftSet + "// time signature : " + TimeSigTop + "/" + TimeSigBottom + "("+LeftSetCount+")");
				/*********************************End of right Rhythm********************************************************/
					  LeftSet = String.valueOf(LeftCurrentTick);  
	    		  }
	    			  else{
	    				  if(TempChordSetNum != ChordSetNum){
	    					  LeftSet += ",/";
	    				  }
	    			  
	    				  if(LeftNoteInBarNum == TempLeftNoteInBarNum){
	    					  LeftSet += " " + String.valueOf(LeftCurrentTick);
	    				  }

	    				  else{
	    					  LeftSet += "," + String.valueOf(LeftCurrentTick);	 
	    					  	if(TempLeftEndTick != LeftStartTick){
	    					  		LeftSet += "," + String.valueOf(TempLeftEndTick - LeftStartTick);
	    					  		
	    					  	}
	    				  }
	    			  }
	    		  TempLeftStartTick = LeftStartTick;
				  TempLeftEndTick = LeftEndTick;
				  TempLeftCurrentTick = LeftCurrentTick;
				  TempChordSetNum = ChordSetNum;  
				  TempLeftNoteInBarNum = LeftNoteInBarNum;
				  TempLeftSetNum = LeftSetNum;
	    		  }
	    	  

	      }//end of left while loop
	      
	      if(TempLeftEndTick != LeftStartTick){//for the last index
	    	  LeftSet += "," + String.valueOf(LeftStartTick - TempLeftEndTick);
	      }
	      /**********************留덉�留됱쓣 �쐞�븳 �삤瑜몄넀************************/
	      ResultSet getRightSet = RightStatement.executeQuery("SELECT * FROM song_right_note WHERE right_rhythm_set_num="+TempLeftSetNum+" and right_song_id="+SongId+";");
	      while(getRightSet.next()){
			  RightCurrentTick = getRightSet.getInt("right_note_duration");//get a current note's midi tick (duration)
			  RightStartTick = getRightSet.getInt("right_note_start_tick");//get a current note's start midi tick
			  RightEndTick = RightCurrentTick + RightStartTick;//end tick
	    	  RightSetNum = getRightSet.getInt("right_rhythm_set_num");//get a note's rhythm set number
	    	  RightNoteInBarNum = getRightSet.getInt("right_note_in_bar_num");//get a note's number in the bar
	    	  RightNoteIdInSong = getRightSet.getInt("right_note_id_in_song");//get a note's ID
	    	  SongId = getRightSet.getInt("right_song_id");//get a song ID
	    	  RightNoteIdInBar = getRightSet.getInt("right_note_id_in_bar");
	    	  RightBarNum = getRightSet.getInt("right_bar_num");
	    	  int StartBarTick = ((1920/TimeSigBottom)*TimeSigTop)*(RightBarNum-1);
	    	  
	    	  if(RightNoteIdInSong == 1){//怨≪쓽 留� 泥섏쓬�뿉 ���븳 泥섎━
	    		  if(RightStartTick != 0){//留뚯빟 怨≪쓽 留� 泥섏쓬�씠 �돹�몴媛� �굹�솕�떎硫�
	    			  RightSet = String.valueOf(0-RightStartTick) + "," + String.valueOf(RightCurrentTick);
	    		  }
	    		  else{
	    			  RightSet = String.valueOf(RightCurrentTick);
	    		  }
	    		  TempRightStartTick = RightStartTick;
				  TempRightEndTick = RightEndTick;
				  TempRightCurrentTick = RightCurrentTick;
				  TempRightNoteInBarNum = RightNoteInBarNum;
				  TempRightSetNum = RightSetNum; 
	    	  }
	    	  else{
	    		  if(RightNoteIdInBar == 1){  
	    			  if(TempRightEndTick != RightStartTick){
	    				  RightSet += "," + String.valueOf(TempRightEndTick - RightStartTick);   
	    			  }
	    			  System.out.println("R - " + RightSet + "// time signature : " + TimeSigTop + "/" + TimeSigBottom);
	    			  
					  RightSet = String.valueOf(RightCurrentTick);
	    		  }
	    		  else{
	    			  if(RightNoteInBarNum == TempRightNoteInBarNum){
	    				  RightSet += " " + String.valueOf(RightCurrentTick);
	    				  }
	    			  else{
	    				//  System.out.println(RightEndTick + " vs(1)" +RightStartTick);
	    				  RightSet += "," + String.valueOf(RightCurrentTick);
	    				  if(TempRightEndTick != RightStartTick){
	    					  RightSet += "," + String.valueOf(TempRightEndTick - RightStartTick);
	    					  }
	    				  }
	    			  }
	    		  TempRightStartTick = RightStartTick;
				  TempRightEndTick = RightEndTick;
				  TempRightCurrentTick = RightCurrentTick;
				  TempRightNoteInBarNum = RightNoteInBarNum;
				  TempRightSetNum = RightSetNum;
	    	  }
		  }//end of last right loop
	      if(TempRightEndTick != RightStartTick){
			  RightSet += "," + String.valueOf(RightStartTick - TempRightEndTick);
		  }
	 
	      /**********************留덉�留됱쓣 �쐞�븳 �삤瑜몄넀 �걮 ************************/
	      if(LeftSet.equals(TempLeftSet)){
			  LeftSetCount++;
			  RhythmID++;
			  sql = "UPDATE rhythm_list SET "+LeftSetCount+" WHERE rhythm_id="+RhythmID+";";
		  }
		  else{
			  LeftSetCount = 1;
			  InsertStatement.setInt(1, LeftSetCount);
			  InsertStatement.setString(2, LeftSet);
			  InsertStatement.setString(3, RightSet);
			  InsertStatement.setInt(4, TimeSigTop);
			  InsertStatement.setInt(5, TimeSigBottom);
			  InsertStatement.executeUpdate();
		  }
	      
	      
	      System.out.println("L - " + LeftSet+ "// time signature : " + TimeSigTop + "/" + TimeSigBottom);
	      System.out.println("R - " + RightSet + "// time signature : " + TimeSigTop + "/" + TimeSigBottom);//諛섏＜遺� 紐⑥쑝湲� 鍮�! �걮!
	   // }
	      
	      //getLeftSet.close();
	      //getRightSet.close();
	}
	      
	      LeftStatement.close();
	      RightStatement.close();
	      InsertStatement.close();
	      c.commit();
	      c.close();
	      
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");
	  }
}