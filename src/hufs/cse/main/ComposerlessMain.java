package hufs.cse.main;

import hufs.cse.Chord.Set.List.chordMap;
import hufs.cse.Chord.Set.List.chordMethod;
import hufs.cse.Chord.Set.List.rightMap;
import hufs.cse.Midi.write.SimpleMidiWriter;


import java.awt.Point;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ComposerlessMain {
		public ComposerlessMain(int TimeSigTop, int TimeSigBottom, int RhythmNum, String Genre, String FileName){
			InsertRhythm insertrhythm = new InsertRhythm(Genre);
			InsertMakedRhythm insertmakedrhythm = new InsertMakedRhythm(RhythmNum);
			InsertChord insertchord = new InsertChord(Genre);
			CreateMusic createmusic = new CreateMusic();
			MakeNewSongDB makenewsongdb = new MakeNewSongDB();
			MidiOut midiout = new MidiOut(FileName);
			
		}
}


