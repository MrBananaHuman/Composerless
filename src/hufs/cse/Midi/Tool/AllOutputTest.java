/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hufs.cse.Midi.Tool;


import hufs.cse.Midi.MidiMessage.*;
import hufs.cse.Midi.read.NotesInMidi;
import hufs.cse.Midi.read.PianoRoll;
import hufs.cse.Midi.read.PianoRollViewParser;

import java.io.File;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

/*
 * This program is for input at DataBase.
 * 
 * 
 * 
 * 
 */

public class AllOutputTest {
    public static void main(String[] args) {
    	int OneBarDuration = 0;
    	int BarNumber = 1;
    	int NoteNumberInBar = 1;
    	int NoteIdInBar = 1;
    	int NoteIdInSong = 1;
    	int TempOne = -1;//for bar number
    	int TempTwo = -1;//for note number in bar
    	int TempThree = -1; //for note ID in bar
    	int TempFour = -1; //for note ID in bar
        try {
        	File file = new File("czerny no.2.mid");
            Sequence mySeq = MidiSystem.getSequence(file);
            PianoRoll roll = PianoRollViewParser.parse(mySeq);
            NotesInMidi[] notes = roll.getNotes();
            
            RawMidiMessageParser parser = new RawMidiMessageParser();

            Track[] tracks = mySeq.getTracks();
//출력 순서 : right/left, start midi tick, duration, note, note, bar#, note# in bar

            for (int i = 0; i < tracks.length; i++) {
                for (int j = 0; j < tracks[i].size(); j++) {
                    MidiEvent me = tracks[i].get(j);
                    MidiCommand mc = parser.parse(me.getMessage());
                    byte[] message = mc.getMessage();
                    //print time signature
                    if (message[0] == RawMidiMessageParser.META_EVENT 
                            && message[1] == RawMidiMessageParser.TIME_SIGNATURE){
                    		System.out.println("one bar duraion : " + mc + "(" + me.getTick() + ")");
                    		OneBarDuration = Integer.parseInt(mc.toString());
                    }
                    if (message[0] == RawMidiMessageParser.META_EVENT 
                            && message[1] == RawMidiMessageParser.KEY_SIGNATURE) 
                    		System.out.println(mc + "," + me.getTick());
                    
                }
            }
            //right hand notes
            //System.out.println("\n(right/left, start midi tick, duration, note, note, bar#, note# in bar)");
            System.out.println("\n****right hand******");
            for(int i=0;i<notes.length;i++){
            	if(notes[i].toString().startsWith("1")){
            		BarNumber = (int)notes[i].getStartTick()/OneBarDuration + 1;
            		if(TempOne != BarNumber){
            			NoteIdInBar = 1;
            		}
            		if(notes[i].getStartTick() % OneBarDuration == 0 || TempOne != BarNumber){
            			NoteNumberInBar = 1;
            			TempOne = BarNumber;
            			TempTwo = (int) notes[i].getStartTick();
            		}		
            		else if(TempTwo != (int) notes[i].getStartTick()){
            			NoteNumberInBar+=1;
            			TempTwo = (int) notes[i].getStartTick();
            		}
            		System.out.println(notes[i].toString()+","+BarNumber+","+NoteNumberInBar+ "," + NoteIdInSong +","+NoteIdInBar);
            		
            		NoteIdInBar+=1;
            		NoteIdInSong+=1;
            		
            	}
            }
            NoteIdInSong = 1;
            //left hand notes
            System.out.println("\n****left hand******");
            for(int i=0;i<notes.length;i++){
            	if(notes[i].toString().startsWith("2")){
            		BarNumber = (int)notes[i].getStartTick()/OneBarDuration + 1;
            		if(TempOne != BarNumber){
            			NoteIdInBar = 1;
            		}
            		if(notes[i].getStartTick() % OneBarDuration == 0 || TempOne != BarNumber){
            			NoteNumberInBar = 1;
            			TempOne = BarNumber;
            			TempTwo = (int) notes[i].getStartTick();
            		}
            		else if(TempTwo != (int) notes[i].getStartTick()){
            			NoteNumberInBar+=1;
            			TempTwo = (int) notes[i].getStartTick();
            		}
            		System.out.println(notes[i].toString()+","+BarNumber+","+NoteNumberInBar+ "," + NoteIdInSong +","+NoteIdInBar);
            		NoteIdInBar+=1;
            		NoteIdInSong+=1;
            	}
            }


        } catch (Exception e) {
            System.out.println("Problem!");
            e.printStackTrace();
            System.out.println(e.toString());
        }
        
    }

}
