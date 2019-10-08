package hufs.cse.main;

import java.io.File;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

class MidiPlayer {
	private Sequencer sequencer = null; // ���� �����̰�
	private Sequence sequence = null; // �÷��̾��� ���ϸ� ����.

	MidiPlayer() {
		try {
			sequencer = MidiSystem.getSequencer(); // �⺻ �������� ��� �´�.
			sequencer.open(); // �������� ����.
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	void loaded(String path) {
		try {
			sequence = MidiSystem.getSequence(new File(path));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	void start() {
		try {
			sequencer.setSequence(sequence); // �������� �������� �����Ѵ�.
			sequencer.start(); // ���� �����Ѵ�.
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	void pause() {
		try {
			sequencer.stop(); // �̹� ���� �����ϰ� ���� �� ���� ��� ���� ���� �����.
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}