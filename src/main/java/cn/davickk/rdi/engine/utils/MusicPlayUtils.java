package cn.davickk.rdi.engine.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MusicPlayUtils {
    public void play(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        final File file = new File(filePath);
        play(file);
    }
    public void play(File file) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
            final AudioInputStream in = AudioSystem.getAudioInputStream(file);
            play(in);
    }
    public void play(AudioInputStream in) throws LineUnavailableException, IOException {
            final AudioFormat outFormat = getOutFormat(in.getFormat());
            final DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);

            final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            if (line != null) {
                line.open(outFormat);
                line.start();
                stream(AudioSystem.getAudioInputStream(outFormat, in), line);
                line.drain();
                line.stop();
            }

    }
    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    private void stream(AudioInputStream in, SourceDataLine line)
            throws IOException {
        final byte[] buffer = new byte[65536];
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            line.write(buffer, 0, n);
        }
    }
}
