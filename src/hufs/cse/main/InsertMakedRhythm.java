package hufs.cse.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;



class InsertMakedRhythm{
	public InsertMakedRhythm(int MakeRhythmNum) {
		 Connection c = null;//connection DB
		    Statement findRhythm = null;//this is for left note query (DB)z
		    PreparedStatement InsertStatement = null;
		    Statement MainStatement = null;
		    String LeftSet = "";
		    String RightSet = "";
		    int RhythmId = 0;
		    int RhythmSetCount = 0;
		    
		    int TimeSigTop = 0;
		    int TimeSigBottom = 0;
		    
		    int RandomNum = 0;
		    
		    int count = 0;
		    int flag = 0;
		    int temp = 0;
		    
		    String LeftRhythmSet = "";
		    String RightRhythmSet = "";
		    String sql = null;
		    
		    int tempCount = 0;
		    
		    int RepeatTime = 0;
		    
		    int SongId = 1;
		    int BarNumber = 1;
		    
		    Scanner scanner = new Scanner(System.in);

		    
		    System.out.println("the number of rhythm : "+MakeRhythmNum);
		    //RepeatTime = scanner.nextInt();
		    RepeatTime = MakeRhythmNum;
		    
		    sql = "INSERT INTO rhythm_of_new_song (new_song_id,new_bar_number,new_left_rhythm_set,new_right_rhythm_set," +
		      		"new_time_signature_top,new_time_signature_bottom)" +
		        		"VALUES (?,?,?,?,?,?);";
		    
		    try {
		    	//for(int k = 0; k < RepeatTime-48; k++){
		    		//System.out.println("k : " +k);
		    		 Random random = new Random();
			      Class.forName("org.sqlite.JDBC");
			      c = DriverManager.getConnection("jdbc:sqlite:music_database.db");
			      c.setAutoCommit(false);//deny the auto commit
			      System.out.println("Opened database successfully");
			      findRhythm = c.createStatement();//this is for left note query (DB)
			      
			      MainStatement = c.createStatement();
			      
			      MainStatement.executeUpdate("drop table if exists rhythm_of_new_song");
			      MainStatement.executeUpdate("create table rhythm_of_new_song (new_rhythm_id integer primary key autoincrement not null, " +
			      		"new_song_id integer integer not null, " +
			        		"new_bar_number integer not null, " +
			        		"new_left_rhythm_set text, " +
			        		"new_right_rhythm_set text, " +
			        		"new_time_signature_top integer not null, " +
			        		"new_time_signature_bottom integer not null)");
			      
			      InsertStatement = c.prepareStatement(sql);
			      
			      for(int k = 0; k < RepeatTime; k++){
			    	 // System.out.println(RepeatTime);
			      count = 0;
			      flag = 0;
			      tempCount = 0;
			      RandomNum = 0;
			      temp = 0;
			      RhythmId = 0;
			      RhythmSetCount = 0;
			      
			      
			   
			      ResultSet getRhythmSet = findRhythm.executeQuery("SELECT * FROM rhythm_list;");
			      while(getRhythmSet.next()){
			    	  count++;
			      }
			      /////////////////////////////////////////////////////////////////////
			      RandomNum = random.nextInt(count);// between 0 and count
			      if(RandomNum == 0){
			    	  RandomNum++;
			      }
			      //ResultSet getRandomRhythmSet = findRhythm.executeQuery("SELECT * FROM rhythm_list WHERE rhythm_id="+RandomNum+";");
			      ResultSet getRandomRhythmSet = findRhythm.executeQuery("SELECT * FROM rhythm_list;");
			      
			      while(getRandomRhythmSet.next()){
			    	  RhythmId = getRandomRhythmSet.getInt("rhythm_id");
			    	  TimeSigTop = getRandomRhythmSet.getInt("time_signature_top");
			    	  TimeSigBottom = getRandomRhythmSet.getInt("time_signature_bottom");
			    	  if(RhythmId == RandomNum){
			    		  LeftSet = getRandomRhythmSet.getString("left_rhythm_set");
			    		  RhythmSetCount = getRandomRhythmSet.getInt("rhythm_set_count");
			    		  flag = 1;
			    	  }
			    	  else if(flag == 1){
			    		  temp = getRandomRhythmSet.getInt("rhythm_set_count");
			    		  if(temp != RhythmSetCount+1){
			    			  flag = 0;
			    			  break;
			    		  }
			    		  else{
			    			  RhythmSetCount++;
			    		  }  
			    	  }
			      }
			      
			     // System.out.println("random choose : " + RandomNum);
			      System.out.println("* left_set : " + LeftSet);
			      InsertStatement.setString(3, LeftSet);
			      //k += RhythmSetCount;
			    //  System.out.println("Repeate Count : " + RhythmSetCount);
			      
			      ResultSet CreateRhythmSet = findRhythm.executeQuery("SELECT * FROM rhythm_list WHERE left_rhythm_set='"+LeftSet+"';");
			      count = 0;
			      while(CreateRhythmSet.next()){
			    	  count++;
			      }
			      //System.out.println("�쁽�옱 left�뿉 �빐�떦�븯�뒗 right�쓽 媛��닔 : " + count);
			      
			      for(int i = 0; i<RhythmSetCount; i++){
			    	  //System.out.println("i : " + i);
				      RandomNum = random.nextInt(count);// between 0 and count
				      if(RandomNum == 0){
				    	  RandomNum++;
				      }
				    //  System.out.println("RandomNum : " + RandomNum);
				      CreateRhythmSet = findRhythm.executeQuery("SELECT * FROM rhythm_list WHERE left_rhythm_set='"+LeftSet+"';");
				      while(CreateRhythmSet.next()){
				    	  tempCount++;
				    	  if(RandomNum == tempCount){
			    			  RightRhythmSet = CreateRhythmSet.getString("right_rhythm_set");
			    			  System.out.println("right_set : " + RightRhythmSet);
			    			  //RightSet += "/" + CreateRhythmSet.getString("right_rhythm_set");
			    			  InsertStatement.setInt(1, SongId);
							  InsertStatement.setInt(2, BarNumber);
							 // InsertStatement.setString(3, LeftSet);
							  InsertStatement.setString(4, RightRhythmSet);
							  InsertStatement.setInt(5, TimeSigTop);
							  InsertStatement.setInt(6, TimeSigBottom);
							  InsertStatement.executeUpdate();
							  BarNumber++;
			    			  tempCount = 0;
			    			  break;
				    	  }
				      }
			      }
			
			      
			      }
			      //getRhythmSet.close();
			      //getRandomRhythmSet.close();
			     // CreateRhythmSet.close();
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