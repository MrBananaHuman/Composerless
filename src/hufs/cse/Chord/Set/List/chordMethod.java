package hufs.cse.Chord.Set.List;

import java.util.Iterator;
import java.util.Vector;

public class chordMethod {
	public String VecToStr(Vector<String> noteSet){
		
		Iterator<String> iter = noteSet.iterator();
		String str = "";
		
		while(iter.hasNext()){
			str += (String)iter.next();
		}
		
		return str;
	}
	public void ConvertorTonality (int tonality,int note) {
		// #
		 if(tonality > 0){
		 if(tonality == 1){ // ��
			 note+=7;
		 }else if(tonality == 2){//�� ��
			 note+=2;
		 }else if(tonality == 3){//�� �� ��
			 note+=9;
		 }else if(tonality == 4){//�� �� �� ��
			 note+=4;
		 }else if(tonality == 5){//�� �� �� �� ��
			 note+=11;
		 }else if(tonality == 6){//�� �� �� �� �� ��
			 note+=6;
		 }else if(tonality == 7){//�� �� �� �� �� �� ��
			 note+=1;
		 }
		 }else if(tonality < 0){ // b
		 if(tonality == -1){ // ��
			 note += 5;
		 }else if(tonality == -2){//�� ��
			 note += 10;
		 }else if(tonality == -3){//�� �� ��
			 note += 3;
		 }else if(tonality == -4){//�� �� �� ��
			 note += 8;
		 }else if(tonality == -5){//�� �� �� �� ��
			 note += 1;
		 }else if(tonality == -6){//�� �� �� �� �� ��
			 note += 6;
		 }else if(tonality == -7){//�� �� �� �� �� �� ��
			 note -= 1;
		 }
		 }
	}
}
