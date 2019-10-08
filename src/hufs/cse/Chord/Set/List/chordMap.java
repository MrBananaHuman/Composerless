package hufs.cse.Chord.Set.List;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Vector;

public class chordMap {
	private Vector<Integer> set_id = new Vector<Integer>();
	private Vector<Integer> song_id = new Vector<Integer>();
	private Vector<Integer> bar_num = new Vector<Integer>();
	private Vector<Integer> chord_num = new Vector<Integer>();
	private Vector<String> left_note = new Vector<String>();
	private Vector<LinkedHashSet<Integer>> value= new Vector<LinkedHashSet<Integer>>();
	private int size;
	
	public void DuplicationMap(){
		size = 0;
	}
	
	public void push(int song_id, int bar_num, int chord_num, String left_note, LinkedHashSet<Integer> value){
		this.song_id.add(song_id);
		this.bar_num.add(bar_num);
		this.chord_num.add(chord_num);
		this.left_note.add(left_note);
		this.value.add(value);
		++size;
	}
	
	public void setId(Vector<Integer>valueid, LinkedHashSet<LinkedHashSet<Integer>> value){
		
		int index = 0;
		int valueIndex = 1;
		
		Iterator<LinkedHashSet<Integer>> temp = value.iterator();
		Vector<LinkedHashSet<Integer>> valueSet = new Vector<LinkedHashSet<Integer>>();
		Vector<Integer> valueId = new Vector<Integer>();

		//		// add in vector 
		while(temp.hasNext()){
			valueSet.add(temp.next());
			valueId.add(index+1);
//			System.out.println("function set: " + valueSet.elementAt(index) + "index: " + (index+1));
			index++;
		}
		
		temp = null;

		// set_id
		
		Iterator<LinkedHashSet<Integer>> myValue = (this.value).iterator();
		temp = value.iterator();
			while(myValue.hasNext()){
				LinkedHashSet<Integer> myTemp = myValue.next();
				while(temp.hasNext()){
					LinkedHashSet<Integer> setTemp = temp.next();
						if(myTemp.equals(setTemp)){
							int indx = valueSet.indexOf(setTemp);
							set_id.add(valueId.elementAt(indx));
						}
				}
				temp = null;
				temp = value.iterator();
		}
		
	}
	public int indexOf(LinkedHashSet<Integer> value){
		int index = this.value.indexOf(value);
		return this.bar_num.elementAt(index);
	}
	public Vector getSetId(){
		return set_id;
	}
	public Vector getSongId(){
		return song_id;
	}
	public Vector getBarNum(){
		return bar_num;
	}
	public Vector getChordNum(){
		return chord_num;
	}
	public Vector getLeftNote(){
		return left_note;
	}
	public Vector getValue(){
		return value;
	}
	public Vector get (int bar_num){
			Iterator KeyIter = this.bar_num.iterator();
			Iterator ValueIter = this.value.iterator();
			
			Vector<LinkedHashSet<Integer>> keyValues = new Vector<LinkedHashSet<Integer>>();
			
			int index = 0;
			
			while(KeyIter.hasNext()){
				int temp = (Integer)KeyIter.next();
					if(temp == bar_num){
						LinkedHashSet<Integer> t = value.get(index);
						keyValues.add(t);
						}
					index++;
				}
			return keyValues;
	}
	public void print(){
		Iterator ValueIter = this.value.iterator();
		Iterator KeyIter = this.bar_num.iterator();
		
		while(KeyIter.hasNext()){
			System.out.println("{"+KeyIter.next()+","+ValueIter.next()+"}");
		}
	}
	public int size(){
		return size;
	}
}
