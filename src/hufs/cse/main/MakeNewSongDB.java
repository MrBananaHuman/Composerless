package hufs.cse.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class MakeNewSongDB{
	public MakeNewSongDB(){

	    Connection c = null;//connection DB
	    Statement MainStatement = null;
	    Statement RightRhythmStatement = null;//this is for left note query (DB)
	    Statement RightMelodyStatement = null;//this is for right note query (DB)
	    Statement LeftRhythmStatement = null;//this is for left note query (DB)
	    Statement LeftMelodyStatement = null;//this is for right note query (DB)
	    PreparedStatement InsertStatement = null;
	    
	    Vector RightDurationVector = new Vector();
	    Vector RightMelodyVector = new Vector();
	    Vector RightOnsetVector = new Vector();
	    
	    Vector LeftDurationVector = new Vector();
	    Vector LeftMelodyVector = new Vector();
	    Vector LeftOnsetVector = new Vector();
	    
	    String sql = null;
	    
	    String RightRhythmSet = null;
	    String RightMelodySet = null;
	    
	    String LeftRhythmSet = null;
	    String LeftMelodySet = null;
	    
	    int BarId = 0;
	    int NumberInBar = 0;
	    int TokenNum = 0;
	    int k = 0;
	    
	    String RightMelodyToken = null;
	    int RightMelody = 0;
	    
	    String LeftMelodyToken = null;
	    
	    int LeftMelody = 0;
	    
	    int MidiTick = 0;
	    


	    /*************/
	    
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:music_database.db");
	      c.setAutoCommit(false);//deny the auto commit
	      System.out.println("Opened database successfully");
	      
	      RightRhythmStatement = c.createStatement();//this is for left note query (DB)
	      RightMelodyStatement = c.createStatement();//this is for right note query (DB)
	      
	      LeftRhythmStatement = c.createStatement();//this is for left note query (DB)
	      LeftMelodyStatement = c.createStatement();//this is for right note query (DB)
	      
	      MainStatement = c.createStatement();
	      
	      MainStatement.executeUpdate("drop table if exists new_song_midi");
	      MainStatement.executeUpdate("create table new_song_midi (note_id integer primary key autoincrement not null, " +
	      		"note_midi_onset integer not null, " +
	      		"note_midi_pitch integer not null, " +
	      		"note_midi_duration integer not null, " +
	      		"note_midi_track integer not null)");
	      
	      sql = "INSERT INTO new_song_midi (note_midi_onset,note_midi_pitch,note_midi_duration,note_midi_track)" +
	      		"VALUES (?,?,?,?);";
	      InsertStatement = c.prepareStatement(sql);

	      ResultSet getRightRhythm = RightRhythmStatement.executeQuery("SELECT * FROM new_song;");
	      int lastDuration = 0;
	      while(getRightRhythm.next()){
	    	  RightRhythmSet = getRightRhythm.getString("right_rhythm_set");
	    	  BarId = getRightRhythm.getInt("bar_id");
	    	  //System.out.println("bar id : " + BarId);
	    	  StringTokenizer getRhythmSetToken = new StringTokenizer(RightRhythmSet, ",");
	    	  NumberInBar = 0;
	    	  TokenNum = 0;
	    	  k = 0;
	    	  while(getRhythmSetToken.hasMoreTokens()){
	    		  String RightDurationSet = getRhythmSetToken.nextToken();
	    		  StringTokenizer RightDurationToken =  new StringTokenizer(RightDurationSet, " ");
	    		  int RightDuration = Integer.parseInt(RightDurationToken.nextToken());
	    		  TokenNum++;
	    		  ResultSet getRightMelody = RightMelodyStatement.executeQuery("SELECT * FROM new_song WHERE bar_id="+BarId+";");
	    		  RightMelodySet = getRightMelody.getString("right_melody_set");
	    		  
	    		 // System.out.println(RightMelodySet);
	    		  StringTokenizer getMelodySetToken = new StringTokenizer(RightMelodySet, ",");
	    		 // System.out.println(TokenNum);
	    		  for(int i = 0; i < TokenNum; i++){
	    			  RightMelodyToken = getMelodySetToken.nextToken();
	    		  }
	    		  //System.out.println(RightMelodyToken+"/"+BarId+"/"+RightDuration);
	    		  int TokenSize = getRhythmSetSize(RightMelodyToken,"+");
	    		  if(RightDuration > 0){
	    			  MidiTick += RightDuration;
	    		  }
	    		  else{
	    			  MidiTick += -RightDuration;
	    		  }
	    			  
	    		  for(int i = 0; i<TokenSize; i++){
	    			  RightDurationVector.add(RightDuration);
	    			  RightOnsetVector.add(MidiTick);	  
	    		  }
	    		  
	    		  StringTokenizer getMelodyToken = new StringTokenizer(RightMelodyToken, "+");
	    		  Pattern regex = Pattern.compile("[0-9]+[\\, \\[0-9]");
	    	      Matcher regexMatcher = regex.matcher(RightMelodyToken);
	    	      while(regexMatcher.find()){
	    	    	  RightMelody = Integer.parseInt(regexMatcher.group());
	    	    	 // System.out.println(RightMelody);
	    	    	  RightMelodyVector.add(RightMelody);
	    	      }
	    	  }
	      }//end of right while loop
	    //  RightOnsetVector.removeElementAt(RightOnsetVector.size()-1);
	      System.out.println(RightMelodyVector.size()+"/"+RightOnsetVector.size()+"/"+RightDurationVector.size());
	      for(int i = 0; i<RightDurationVector.size(); i++){
	    	  System.out.println(RightMelodyVector.elementAt(i)+"/"+(int)RightOnsetVector.elementAt(i)+"/"+RightDurationVector.elementAt(i));
	    	    int OnSet = 0;
	    	    if((int)RightDurationVector.elementAt(i)>0){
	    	    	OnSet = (int)RightOnsetVector.elementAt(i)-(int)RightDurationVector.elementAt(i);
	    	    }
	    	    else{
	    	    	OnSet = (int)RightOnsetVector.elementAt(i)+(int)RightDurationVector.elementAt(i);
	    	    }
	    	    
	    	  	InsertStatement.setInt(1, OnSet);
			     InsertStatement.setInt(2, (int)RightMelodyVector.elementAt(i));
				 InsertStatement.setInt(3, (int)RightDurationVector.elementAt(i));
			     InsertStatement.setInt(4, 1);
				 InsertStatement.executeUpdate();
	    	  // System.out.println(RightDurationVector.elementAt(i));
	      }
	      
	      /**********************************************************************************************/
	      MidiTick = 0;
	      ResultSet getLeftRhythm = LeftRhythmStatement.executeQuery("SELECT * FROM new_song;");
	      while(getLeftRhythm.next()){
	    	  LeftRhythmSet = getLeftRhythm.getString("left_rhythm_set");
	    	  BarId = getLeftRhythm.getInt("bar_id");
	    	  //System.out.println("bar id : " + BarId);
	    	  StringTokenizer getRhythmSetToken = new StringTokenizer(LeftRhythmSet, ",");
	    	  NumberInBar = 0;
	    	  TokenNum = 0;
	    	  k = 0;
	    	  while(getRhythmSetToken.hasMoreTokens()){
	    		  String LeftDurationSet = getRhythmSetToken.nextToken();
	    		  StringTokenizer LeftDurationToken =  new StringTokenizer(LeftDurationSet, " ");
	    		  int LeftDuration = Integer.parseInt(LeftDurationToken.nextToken());
	    		  TokenNum++;
	    		  ResultSet getLeftMelody = LeftMelodyStatement.executeQuery("SELECT * FROM new_song WHERE bar_id="+BarId+";");
	    		  LeftMelodySet = getLeftMelody.getString("left_melody_set");
	    		  
	    		 // System.out.println(LeftMelodySet);
	    		  StringTokenizer getMelodySetToken = new StringTokenizer(LeftMelodySet, ",");
	    		 // System.out.println(TokenNum);
	    		  for(int i = 0; i < TokenNum; i++){
	    			  LeftMelodyToken = getMelodySetToken.nextToken();
	    		  }
	    		  //System.out.println(LeftMelodyToken+"/"+BarId+"/"+LeftDuration);
	    		  int TokenSize = getRhythmSetSize(LeftMelodyToken,"+");
	    		  if(LeftDuration > 0){
	    			  MidiTick += LeftDuration;
	    		  }
	    		  else{
	    			  MidiTick += -LeftDuration;
	    		  }
	    			  
	    		  for(int i = 0; i<TokenSize; i++){
	    			  LeftDurationVector.add(LeftDuration);
	    			  LeftOnsetVector.add(MidiTick);	  
	    		  }
	    		  
	    		  StringTokenizer getMelodyToken = new StringTokenizer(LeftMelodyToken, "+");
	    		  Pattern regex = Pattern.compile("[0-9]+[\\, \\[0-9]");
	    	      Matcher regexMatcher = regex.matcher(LeftMelodyToken);
	    	      while(regexMatcher.find()){
	    	    	  LeftMelody = Integer.parseInt(regexMatcher.group());
	    	    	//  System.out.println(LeftMelody);
	    	    	  LeftMelodyVector.add(LeftMelody);
	    	      }
	    	  }
	      }//end of Left while loop
	    //  LeftOnsetVector.removeElementAt(LeftOnsetVector.size()-1);
	      System.out.println("*********************************************");
	      System.out.println(LeftMelodyVector.size()+"/"+LeftOnsetVector.size()+"/"+LeftDurationVector.size());
	      for(int i = 0; i<LeftDurationVector.size(); i++){
	    	  System.out.println(LeftMelodyVector.elementAt(i)+"/"+(int)LeftOnsetVector.elementAt(i)+"/"+LeftDurationVector.elementAt(i));
	    	 // System.out.println(LeftDurationVector.elementAt(i));
	    	  
	    	  int OnSet = 0;
	  	    if((int)LeftDurationVector.elementAt(i)>0){
	  	    	OnSet = (int)LeftOnsetVector.elementAt(i)-(int)LeftDurationVector.elementAt(i);
	  	    }
	  	    else{
	  	    	OnSet = (int)LeftOnsetVector.elementAt(i)+(int)LeftDurationVector.elementAt(i);
	  	    }
	  	    
	  	  	InsertStatement.setInt(1, OnSet);
			     InsertStatement.setInt(2, (int)LeftMelodyVector.elementAt(i));
				 InsertStatement.setInt(3, (int)LeftDurationVector.elementAt(i));
			     InsertStatement.setInt(4, 2);
				 InsertStatement.executeUpdate();
	      }
	     
	    
	  
	      InsertStatement.close();
	      c.commit();
	      c.close();
	      
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");
	}
	  public static int getRhythmSetSize(String RhythmSet, String Token){
		  int size = 0;
		//  System.out.println(RhythmSet);
		  StringTokenizer results = new StringTokenizer(RhythmSet, Token);
	      while (results.hasMoreTokens()) {
	    	  results.nextToken();
	    	  size++;
	    	}     
	     // System.out.println(size);
		  return size;
	  }
}