/*
 * ParsedMidiCommandWithChannel.java
 * 
 * Created on Nov 7, 2007, 10:19:32 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hufs.cse.Midi.MidiMessage;

/**
 * Interface for midi commands that have a channel.
 *
 * @see MidiCommandWithChannelParser
 * @author Christine
 */
public interface MidiCommandWithChannel extends MidiCommand{
    public int getChannel();
}
