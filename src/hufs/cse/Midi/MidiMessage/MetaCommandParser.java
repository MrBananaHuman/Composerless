/*
 * MetaCommandParser.java
 * 
 * Created on Nov 7, 2007, 10:24:33 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hufs.cse.Midi.MidiMessage;

import javax.sound.midi.MidiMessage;

/**
 * Parses a generic meta-command.  This parser does not attempt to figure out
 * what kind of meta command it is seeing.
 *
 * @see MetaCommand
 * @author Christine
 */
public class MetaCommandParser extends MidiCommandParser{

    public MetaCommandParser(){
        // nothing to do
    }
    
    @Override
    public MidiCommand parse(MidiMessage mm) {
        byte[] message = mm.getMessage();
        
        if(message[0] == RawMidiMessageParser.META_EVENT){
            return new Command(message);
        } 
        else{
            return null;
        }
    }
    
    public static class Command extends MidiCommandParser.Command
            implements MetaCommand {

        public Command(byte[] message) {
            super(message);
        }

        @Override
        public String toString() {
            return new String("Unknown Meta Event " + message[1] + ": " + toHex(message));
        }
    }
    
    
}
