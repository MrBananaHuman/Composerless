package hufs.cse.main;

import hufs.cse.Chord.Set.List.chordMap;
import hufs.cse.Chord.Set.List.chordMethod;
import hufs.cse.Chord.Set.List.rightMap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Vector;


class InsertChord{
	public InsertChord(String Genre){

		Connection conn = null;
		Statement LeftStm = null; // song_left_note
		Statement RightStm = null; // song_right_note
		Statement SListStm = null; // song_list
		Statement SetListStm = null; // set_list
		Statement MainStm = null;

		// 0 : song_id
		// 1 : left_note
		// 2 : left_bar_num
		// 3 : left_note_in_bar_num
		// 4 : left_tonality;
		// 5 : left_chord_set_num;
		Vector[] LeftDBvector = new Vector[6];
		Vector SongDBvector = new Vector();
		
		chordMethod chordObject = new chordMethod();
		chordMap chord_map = new chordMap();
		
		try {
			Class.forName("org.sqlite.JDBC");

			conn = DriverManager.getConnection("jdbc:sqlite:music_database.db");
			
			conn.setAutoCommit(false);

			System.out.println("Opened database successfully");

			// DB Statement 占쏙옙
			LeftStm = conn.createStatement(); // song_left_note
			RightStm = conn.createStatement(); // song_right_note
			SListStm = conn.createStatement(); // song_list
			SetListStm = conn.createStatement(); // set_list
			MainStm = conn.createStatement();;
			
			MainStm.executeUpdate("drop table if exists chord_list");
			MainStm.executeUpdate("create table chord_list (chord_set_id integer, " +
		        		"chord_set_note text, " +
		        		"chord_right_note text, " +
		        		"chord_id integer)");
			
			MainStm.executeUpdate("drop table if exists set_list");
			MainStm.executeUpdate("create table set_list (set_id integer, " +
		        		"set_note text)");
			
			PreparedStatement CListPrep = conn.prepareStatement(
	                "insert into chord_list values (?, ?, ?, ?)"); // chord_list
			
			PreparedStatement SListPrep = conn.prepareStatement(
					"insert into set_list values (?, ?)"); // set_list

			// song_list DB 占쌀뤄옙占쏙옙占쏙옙
			/********************************************/
			ResultSet getSongId = MainStm.executeQuery("SELECT * FROM genre_list WHERE genre_name='"+Genre+"';");
		      int GenreId = getSongId.getInt("genre_id");
			
			/****************************************/
			
			
			
			
			ResultSet SListData = SListStm
					.executeQuery("SELECT * FROM song_list WHERE song_genre="+GenreId+";");

			// song_left_note DB 占쌀뤄옙占쏙옙占쏙옙
			ResultSet LeftData = LeftStm
					.executeQuery("SELECT * FROM song_left_note");

			// song_right_note DB 占쌀뤄옙占쏙옙占쏙옙
			ResultSet RightData = RightStm
					.executeQuery("SELECT * FROM song_right_note");
			
			// set_list DB 占쌀뤄옙占쏙옙占쏙옙
			ResultSet SetListData = SetListStm.executeQuery("SELECT * FROM set_list");
//			
//			// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> save vector
			while(SListData.next())
				SongDBvector.add(SListData.getInt("song_id"));
			
			// 0 : left_song_id
			// 1 : left_note
			// 2 : left_bar_num
			// 3 : left_note_in_bar_num
			// 4 : left_tonality;
			// 5 : left_note_chord_set_num;
			
			int LeftDBvector_size = 0;
			
			for(int i = 0; i < 6; i++)
				LeftDBvector[i] = new Vector<Integer>();
			
			while(LeftData.next()){
				LeftDBvector[0].add(LeftData.getInt("left_song_id"));
				LeftDBvector[1].add(LeftData.getInt("left_note"));
				LeftDBvector[2].add(LeftData.getInt("left_bar_num"));
				LeftDBvector[3].add(LeftData.getInt("left_note_in_bar_num"));
				LeftDBvector[4].add(LeftData.getInt("left_note_tonality"));
				LeftDBvector[5].add(LeftData.getInt("left_note_chord_set_num"));
				
				LeftDBvector_size++;
			}

			// Iterator Setting
			Iterator SongIter = SongDBvector.iterator();
			Iterator[] LeftIter = new Iterator[6];
			
			for(int i = 0; i < 6 ; i++)
				LeftIter[i] = LeftDBvector[i].iterator();
			
			// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Create Set
			
			int song_sequence = 0;
			int chord_sequence = 1;
			LinkedHashSet<LinkedHashSet<Integer>> Set_HashSet= new LinkedHashSet<LinkedHashSet<Integer>>();
			
		// 占쏙옙占쏙옙 占쏙옙占쏙옙 占쌘몌옙占쏙옙
		while(SongIter.hasNext()){
				boolean equalSongId = false;
				int song_id = (Integer)SongIter.next();
				int search_Sequence = 0;
				chordMethod chordConvert = new chordMethod();
				Vector<String> noteSet = new Vector<String>();
				
			//  1. set id 占쏙옙 占쌓놂옙 Set_HashSet 占쏙옙 占쏙옙載� 占쏙옙占� 占싹곤옙
			//  2. set_note 占쏙옙 占쌓놂옙 Set
				
			// 탐占쏙옙 占싹댐옙 占쏙옙 占싸곤옙?
			if(LeftDBvector[0].elementAt(search_Sequence).equals(song_id)){
				
				Vector[] BarVector = new Vector[5];
				
				while(search_Sequence < LeftDBvector_size - 1){
					int chord_num = 1;
					int bar_search_sequence = search_Sequence + 1;
					
					if(search_Sequence == LeftDBvector_size - 1)	
						bar_search_sequence = search_Sequence - 1;
					
					boolean Bar_Search = true;
					String string = null;
					
					LinkedHashSet<Integer> left_Note_HashSet= new LinkedHashSet<Integer>();
					
					while(Bar_Search && (search_Sequence < LeftDBvector_size)){
						// 占쏙옙占쏙옙 占쏙옙占쏙옙
						if((Integer)LeftDBvector[2].elementAt(search_Sequence) == (Integer)LeftDBvector[2].elementAt(bar_search_sequence)){
							
							boolean change_chord_search = true;
							
							while(change_chord_search){
								System.out.print("left_bar_num: " + LeftDBvector[2].elementAt(search_Sequence));
								System.out.print("left_bar_num_next: " + LeftDBvector[2].elementAt(bar_search_sequence));
								
								int chord_search_sequence  = search_Sequence+ 1;
								
								if(search_Sequence  == LeftDBvector_size -1 )
									chord_search_sequence = search_Sequence - 1;
									
								// 占쏙옙 占쏙옙占쏙옙 占싫울옙占쏙옙 占쌘드가 占쏙옙 占쌕뀐옙 占쏙옙
								if(LeftDBvector[5].elementAt(search_Sequence).equals(LeftDBvector[5].elementAt(chord_search_sequence))){
									chord_num = 1; // 占십깍옙화
									
									// 占쏙옙占쏙옙 占쌕꾸깍옙
									chordConvert.ConvertorTonality((Integer)LeftDBvector[4].elementAt(search_Sequence),(Integer)LeftDBvector[1].elementAt(search_Sequence));
									
									// 占쏙옙占쏙옙 占쏙옙 bar_in_num
									int current_bar_in_num = (Integer)LeftDBvector[3].elementAt(search_Sequence);
									
									// 占쏙옙占쏙옙 占쏙옙 bar_in_num
									int next_bar_in_num = (Integer)LeftDBvector[3].elementAt(chord_search_sequence);
									
									// 占쏙옙占쏙옙 占쏙옙占쏙옙 note占쏙옙 화占쏙옙占싱띰옙占�
									if(current_bar_in_num == next_bar_in_num){
										// 占쏙옙占쏙옙占쏙옙 占쏙옙 占싱띰옙占�
										if(search_Sequence  == LeftDBvector_size -1 )
											string = LeftDBvector[1].elementAt(search_Sequence).toString();
										else
											string = (Integer)LeftDBvector[1].elementAt(search_Sequence) + "+";
											
										noteSet.add(string);
									}else{
										// 占쏙옙占쏙옙占쏙옙 占쏙옙 占싱띰옙占�
										if(search_Sequence  == LeftDBvector_size -1 )
											string = LeftDBvector[1].elementAt(search_Sequence).toString();
										// 占쏙옙占쏙옙 占쏙옙占� 占쌕몌옙占쌕몌옙
										else if(next_bar_in_num == 1)
											string = LeftDBvector[1].elementAt(search_Sequence).toString();
										else
											string = (Integer)LeftDBvector[1].elementAt(search_Sequence) + "-";
										noteSet.add(string);
									}
									
									change_chord_search = false;
									left_Note_HashSet.add((Integer)LeftDBvector[1].elementAt(search_Sequence));
									
									// 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙 占쌩곤옙
									if(search_Sequence  == LeftDBvector_size -1 ){
										// left_Note_HashSet 占쏙옙 占쌩곤옙
										Set_HashSet.add(left_Note_HashSet);
										String NoteSetStr = chordObject.VecToStr(noteSet);
										chord_map.push(song_id, (Integer)LeftDBvector[2].elementAt(search_Sequence), chord_num, NoteSetStr, left_Note_HashSet);
									}
									
									System.out.println("left_Note_HashSet: " + left_Note_HashSet + " <no change> " 
									+ "left_note: " + LeftDBvector[1].elementAt(search_Sequence) );
								} 
								// 占쏙옙 占쏙옙占쏜에쇽옙 占쌘드가 占쌕뀐옙 占쏙옙
								else{
									chordConvert.ConvertorTonality((Integer)LeftDBvector[4].elementAt(search_Sequence),(Integer)LeftDBvector[1].elementAt(search_Sequence));
									string = LeftDBvector[1].elementAt(search_Sequence).toString();	
									noteSet.add(string);

									left_Note_HashSet.add((Integer)LeftDBvector[1].elementAt(search_Sequence));
									
									// left_Note_HashSet 占쏙옙 占쌩곤옙
									chord_num++;
									String NoteSetStr = chordObject.VecToStr(noteSet);
									chord_map.push(song_id, (Integer)LeftDBvector[2].elementAt(search_Sequence), chord_num, NoteSetStr, left_Note_HashSet);
									Set_HashSet.add(left_Note_HashSet);
									
									System.out.println("Set_HashSet" + left_Note_HashSet + " <change> "+ "left_note: " + LeftDBvector[1].elementAt(search_Sequence));
									
									// left_Note 占십깍옙화
									noteSet = null;
									noteSet = new Vector<String>();
									left_Note_HashSet = null;
									left_Note_HashSet = new LinkedHashSet<Integer>();
									change_chord_search = false;
								}
							}
						} else{ // 占쌕몌옙 占쏙옙占쏙옙
							// left_Note_HashSet 占쏙옙 占쌩곤옙
							string = LeftDBvector[1].elementAt(search_Sequence).toString();
							noteSet.add(string);
							left_Note_HashSet.add((Integer)LeftDBvector[1].elementAt(search_Sequence));

							Set_HashSet.add(left_Note_HashSet);
							String NoteSetStr = chordObject.VecToStr(noteSet);
							chord_map.push(song_id, (Integer)LeftDBvector[2].elementAt(search_Sequence-1), chord_num,NoteSetStr, left_Note_HashSet);
							System.out.println("Set_HashSet" + left_Note_HashSet + "change");
							
							// left_Note 占십깍옙화
							left_Note_HashSet = null;
							left_Note_HashSet = new LinkedHashSet<Integer>();
							noteSet = null;
							noteSet = new Vector<String>();
							Bar_Search = false;
						}
						search_Sequence++;
						bar_search_sequence = search_Sequence + 1;
						
						if(search_Sequence == LeftDBvector_size - 1)	
							bar_search_sequence = search_Sequence - 1;


					}
				}
					BarVector = null;
			} else{
					equalSongId = true;
			}
				noteSet = null;
				song_sequence++;
			}
			
//		// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> set_id Inert
			Iterator<LinkedHashSet<Integer>> iter = Set_HashSet.iterator();
			Vector<Integer> valueId = new Vector<Integer>();
			
			int id = 1;
			
			while(iter.hasNext()){
				LinkedHashSet<Integer> temp= iter.next();
				
				String t = temp.toString();
				
				SListPrep.setInt(1, id);
				SListPrep.setString(2, t);
				SListPrep.executeUpdate();
				valueId.add(id);
				
				id++;
			}
			
		Vector<Integer> SongId = new Vector<Integer>();
		Vector<Integer> SetId = new Vector<Integer>();
		Vector<String> LeftNotes = new Vector<String>();
		Vector<Integer> BarNum = new Vector<Integer>();
		Vector<Integer> Value = new Vector<Integer>();
		Vector<LinkedHashSet<Integer>> ChordNum = new Vector<LinkedHashSet<Integer>>();
		
		// id setting
		chord_map.setId(valueId,Set_HashSet);
		
		// query 占쌔븝옙
		// load map 
		LeftNotes = chord_map.getLeftNote();
		Value = chord_map.getValue();
		BarNum = chord_map.getBarNum();
		SetId = chord_map.getSetId();
		
		int map_size = chord_map.size();
		int sequence = 0;

		while(sequence < map_size){          
			Iterator<LinkedHashSet<Integer>> temp_Iter = Set_HashSet.iterator();
					LinkedHashSet<Integer> temp = temp_Iter.next();
					String st = LeftNotes.elementAt(sequence);
						
					System.out.println("LeftBarNum: " +BarNum.elementAt(sequence)  + "LeftNotes: " + st + "Set_id: " + SetId.elementAt(sequence)+ "set: " + Value.elementAt(sequence));
					sequence++;
		}
		
		// >>>>>>>>>>>>>>>>>>>>>>>>>>> Rigth DB 占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙
		int RightDBvector_size = 0;
	
		Vector[] RightDBvector = new Vector[5];
		
		for(int i = 0; i < 5; i++)
			RightDBvector[i] = new Vector<Integer>();
		
//		[0] right_song_id
//		[1] right_note
//		[2] right_bar_num
//		[3] right_note_in_bar_num
//		[4] right_note_tonality
		
		while(RightData.next()){
			RightDBvector[0].add(RightData.getInt("right_song_id"));
			RightDBvector[1].add(RightData.getInt("right_note"));
			RightDBvector[2].add(RightData.getInt("right_bar_num"));
			RightDBvector[3].add(RightData.getInt("right_note_in_bar_num"));
			RightDBvector[4].add(RightData.getInt("right_note_tonality"));
			
			RightDBvector_size++;
		}
		
		SongIter = SongDBvector.iterator();
		rightMap rightmap = new rightMap();
		chordMethod chordConvert = new chordMethod();
		
		// 占쏙옙占쏙옙 占쏙옙占쏙옙 占쌘몌옙占쏙옙
		
		while(SongIter.hasNext()){
				int song_id = (Integer)SongIter.next();
				int search_Sequence = 0;
						
				//  1. set id 占쏙옙 占쌓놂옙 Set_HashSet 占쏙옙 占쏙옙載� 占쏙옙占� 占싹곤옙
				//  2. set_note 占쏙옙 占쌓놂옙 Set
						
			// 탐占쏙옙 占싹댐옙 占쏙옙 占싸곤옙?
			if(RightDBvector[0].elementAt(search_Sequence).equals(song_id)){
						boolean Bar_Search = true;
						String string = null;
						Vector<String> noteSet = new Vector<String>();
							
						while(Bar_Search && (search_Sequence < RightDBvector_size)){
							
							int bar_search_sequence = search_Sequence + 1;
							
							// 占쏙옙占쏙옙占쏙옙 占쏙옙 占싱띰옙占� 
							if(search_Sequence == RightDBvector_size - 1)	
								bar_search_sequence = search_Sequence - 1;
							
							  // 占쏙옙占쏙옙 占쏙옙占쏙옙
							if(RightDBvector[2].elementAt(search_Sequence).equals((Integer)RightDBvector[2].elementAt(bar_search_sequence))){
									 // 占쏙옙占쏙옙 占쌕꾸깍옙
									chordConvert.ConvertorTonality((Integer)RightDBvector[4].elementAt(search_Sequence),(Integer)RightDBvector[1].elementAt(search_Sequence));

									 // 占쏙옙占쏙옙 占쏙옙 bar_in_num
									int current_bar_in_num = (Integer)RightDBvector[3].elementAt(search_Sequence);
											
									// 占쏙옙占쏙옙 占쏙옙 bar_in_num
									int next_bar_in_num = (Integer)RightDBvector[3].elementAt(bar_search_sequence);
											
									// 占쏙옙占쏙옙 占쏙옙占쏙옙 note占쏙옙 화占쏙옙占싱띰옙占�
									if(current_bar_in_num == next_bar_in_num){
										chordConvert.ConvertorTonality((Integer)RightDBvector[4].elementAt(search_Sequence), (Integer)RightDBvector[1].elementAt(search_Sequence));
										string = (Integer)RightDBvector[1].elementAt(search_Sequence) + "+";
									}else{
										chordConvert.ConvertorTonality((Integer)RightDBvector[4].elementAt(search_Sequence), (Integer)RightDBvector[1].elementAt(search_Sequence));
										if(search_Sequence  == RightDBvector_size -1 ){
											string = RightDBvector[1].elementAt(search_Sequence).toString();	
										}else{
											string = RightDBvector[1].elementAt(search_Sequence) + "-";	
										}
										
									}

									noteSet.add(string);
									String noteSetStr = chordObject.VecToStr(noteSet);
									
									// 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙 占쌩곤옙
									if(search_Sequence  == RightDBvector_size -1 ){
										rightmap.push((Integer)RightDBvector[0].elementAt(search_Sequence), (Integer)RightDBvector[2].elementAt(search_Sequence), noteSetStr);
									}
											System.out.println("Right_Note_HashSet: " + noteSet + " <no change> " 
											+ "Right_note: " + RightDBvector[1].elementAt(search_Sequence) 
											+ "current_bar_in_num: " + current_bar_in_num + " next_bar_in_num: " + next_bar_in_num);
							} else{ // 占쌕몌옙 占쏙옙占쏙옙
									chordConvert.ConvertorTonality((Integer)RightDBvector[4].elementAt(search_Sequence), (Integer)RightDBvector[1].elementAt(search_Sequence));
									string = RightDBvector[1].elementAt(search_Sequence).toString();	
//									string = RightDBvector[1].elementAt(search_Sequence) + "-";	
									
									noteSet.add(string);
									String noteSetStr = chordObject.VecToStr(noteSet);
									
									rightmap.push((Integer)RightDBvector[0].elementAt(search_Sequence), (Integer)RightDBvector[2].elementAt(search_Sequence), noteSetStr);
									
									System.out.println("Set_HashSet" + noteSet + "change");
									
									// left_Note 占십깍옙화
									noteSet = null;
									noteSet = new Vector<String>();
							}
								search_Sequence++;
						}
			} else{
						song_sequence++;
			}
		}
		
		
		// >>>>>>>>>>>>>>>>>>>>>>>>>> 
				
		
		Vector<Integer> RightBarNum = rightmap.getBarNum();
		Vector<String> RightNote = rightmap.getRightNote();
		Vector<Integer> RightSongId = rightmap.getSongId();
		
// 		chord_map 占쌔븝옙
//		LeftNotes = chord_map.getLeftNote();
//		Value = chord_map.getValue();
//		BarNum = chord_map.getBarNum();
//		SetId = chord_map.getSetId();
//		
		int r_s = 0;
		int chord_id = 1;
		while(r_s < rightmap.size()){  
			int l_s = 0;
			while(l_s < chord_map.size()){
				if(BarNum.elementAt(l_s).equals(RightBarNum.elementAt(r_s))){
					// db 占쏙옙占쏙옙
					CListPrep.setInt(1, SetId.elementAt(l_s));
					CListPrep.setString(2, LeftNotes.elementAt(l_s));
					CListPrep.setString(3, RightNote.elementAt(r_s));
					CListPrep.setInt(4, chord_id);
					CListPrep.executeUpdate();
					chord_id++;
				}
				l_s++;
			}
			r_s++;
		}
	
			LeftData.close();
			RightData.close();
			SListData.close();
			SetListData.close();
			SListPrep.close();
			CListPrep.close();
			conn.commit();
			conn.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		System.out.println("Records created successfully");
	}
}