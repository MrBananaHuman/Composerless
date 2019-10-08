package hufs.cse.Chord.Set.List;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Vector;

public class rightMap {
	private Vector<Integer> song_id = new Vector<Integer>();
	private Vector<Integer> bar_num = new Vector<Integer>();
	private Vector<String> right_note = new Vector<String>();
	private int size;
	
	public void DuplicationMap(){
		size = 0;
	}
	
	public void push(int song_id, int bar_num, String right_note){
		this.song_id.add(song_id);
		this.bar_num.add(bar_num);
		this.right_note.add(right_note);
		++size;
	}
	public Vector getSongId(){
		return song_id;
	}
	public Vector getBarNum(){
		return bar_num;
	}
	public Vector getRightNote(){
		return right_note;
	}
	public int size(){
		return size;
	}
}
