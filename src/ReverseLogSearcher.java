import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicLong;

public class ReverseLogSearcher {
    static int BREAK_SIZE = 1000000;
    static int THREADS = 1;

    AtomicLong Pos;
    PatternMap patternMap = new PatternMap(true);
    Long startTime;

    File file;
    BoyerMoore bm;
    String pattern;

    ReverseLogSearcher (File file) {
        this.file = file;
        System.out.println("File: " + file.length());
        Pos = new AtomicLong(file.length());
    }

    public PatternMap search (String pattern) {
        bm = new BoyerMoore(pattern.toCharArray(), 256);
        this.pattern = pattern;

        startTime = System.currentTimeMillis();
        patternMap = new PatternMap(true);

        for (int i = 0 ; i < THREADS; i++) {
            (new Thread(this::ThreadBrain)).start();
        }

        return patternMap;
    }

    void ThreadBrain () {
        try (RandomAccessFile rFile = new RandomAccessFile(file.getAbsolutePath(), "r")) {
            long pos = Pos.getAndAdd(-BREAK_SIZE);
            if (pos < 0 && pos + BREAK_SIZE > 0)
                pos = 0;

            if (pos >= 0) {
                int size = BREAK_SIZE;
                if (pos + BREAK_SIZE != file.length()) {
                    size += pattern.length();
                } else if (pos + BREAK_SIZE > file.length()) {
                    size = (int)file.length();
                }

                rFile.seek(pos);
                byte[] data = new byte[size];
                rFile.read(data);

                int found = bm.search(data);
                if (found != -1) {
                    patternMap.foundAt(pos + found);
                }
                (new Thread(this::ThreadBrain)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getLoadStatus () {
        double totalSize = file.length() - 1;
        double curr = totalSize - Pos.get();
        if (curr < 0) curr = totalSize;

        double loadState = (curr/totalSize) * 100;
        if (loadState > 100) return 100;
        return loadState;
    }
}
