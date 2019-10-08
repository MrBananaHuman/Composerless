package hufs.cse.main;

import hufs.cse.Midi.write.SimpleMidiWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

class MidiOut{
	public MidiOut(String fileName){

	    Connection c = null;//connection DB
	    Statement MainStatement = null;
	    SimpleMidiWriter writer = null;
	    

	    //  channel  |   track   |   patch number:
	    //     0           1               0  (acoustic grand piano)
	    //     1           1               56 (trumpet)
	    //     2           1               22 (harmonica)
	    //int[][] patches = {{0,1,0},{1,1,56},{2,1,22}};
	    // pick half a second per quarter note
	    int microsecondsPerQuarterNote = (int) (0.5 * 1000000.0);
	    // pick a resolution of 100 ticks per beat.  This means that the
	    // shortest note we can specify is 1/100th of a beat.
	    int resolution = 120;//鍮좊Ⅴ湲� 
	    // pick a time signature, 4/4 (common time) with 1 midi clock per quarter note and 8 32nd notes per quarter note.
	    int[] timeSignature = {4,4,1,8};
	    // pick some name for the resulting file.
	    // String fileName = "Temp.mid";
	        


	    /*************/
	    
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:music_database.db");
	      c.setAutoCommit(false);//deny the auto commit
	      System.out.println("Opened database successfully");
	     
	      
	      MainStatement = c.createStatement();
	      ResultSet getNewSong = MainStatement.executeQuery("SELECT * FROM new_song_midi;");
	      int size = 0;
	      while(getNewSong.next()){
	    	  size++;
	      }
	      double[] onset = new double[size];//�쓬�씠 �떆�옉�븯�뒗 珥�
	      // make each note 2 beats long
	      double[] duration = new double[size];//�쓬�씠 �쑀吏��릺�뒗 湲몄씠
	      // pitches C4 (middle C), D4, E4
	      int[] pitch = new int[size];//�닚李⑥쟻�쑝濡� �슱由щ뒗 �쓬
	      // put each note on a different channel
	      int[] channel = new int[size];
	      // use a variety of velocity (loudness) values
	      int[] velocity = new int[size];
	      // put all the notes on track 1
	      int[] track = new int[size];
	      
	      
	      
	      getNewSong = MainStatement.executeQuery("SELECT * FROM new_song_midi;");
	      int k = 0;
	      while(getNewSong.next()){
	    	  double temp = getNewSong.getDouble("note_midi_duration");
	    	  if(temp > 0){
	    		  onset[k] = getNewSong.getDouble("note_midi_onset")/480;
		    	  duration[k] = getNewSong.getDouble("note_midi_duration")/480;
		    	  pitch[k] = getNewSong.getInt("note_midi_pitch");
		    	  track[k] = getNewSong.getInt("note_midi_track");
		    	  velocity[k] = 90;
		    	  k++;  
	    	  }  
	      }
	  	
	    
	    

	      writer.write(fileName, onset, duration, channel, pitch, velocity, track, microsecondsPerQuarterNote, resolution, timeSignature);
	       
	      
	      c.commit();
	      c.close();
	      
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");
	}
}