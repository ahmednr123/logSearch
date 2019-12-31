import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Utils {
    public static long findLine (BoyerMoore bm, String string, String pattern, long startByte) {
        int pos = bm.search(string);

        if (pos > 0) {
            for (int i = pos; i >= 0; i--) {
                if (string.charAt(i) == '\n') {
                    return startByte + i;
                }
            }
        }
        return -1;
    }

    public static byte[] readFromFile(String filePath, long position, int size)
            throws IOException {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        file.seek(position);
        byte[] bytes = new byte[size];
        file.read(bytes);
        file.close();
        return bytes;
    }

    public static String displayLine (byte[] bytes, int lineByte, int extraLines) {
        try {
            int lines = 0;
            String response = "";
            for (int i = lineByte; i >= 0; i--) {
                char ch = (char)bytes[i];
                if (ch == '\n') {
                    if (lines == 0)
                        response = "* " + response;
                    lines++;
                }
                response = ch + response;
                if (lines > extraLines)
                    break;
            }

            lines = -1;
            //response += "* ";
            for (int i = lineByte+1; i < bytes.length; i++) {
                char ch = (char)bytes[i];
                response += ch;
                if (ch == '\n') {
                    lines++;
                    if (lines >= extraLines) {
                        break;
                    }
                }
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void displayLine (File fileMeta, long lineByte, int extraLines) {
        long startByte = lineByte - (400 * extraLines);
        int size = (400 * (extraLines + 2));

        if (startByte < 0) startByte = 0;
        if ((startByte + size) > fileMeta.length()) size =  (int)(fileMeta.length() - startByte);

        try {
            byte[] data = readFromFile(fileMeta.getAbsolutePath(), startByte, size);
            int lines = 0;
            String response = "";
            for (int i = (int)(lineByte - startByte); i >= 0; i--) {
                char ch = (char)data[i];
                if (ch == '\n') {
                    if (lines == 0)
                        response = "* " + response;
                    lines++;
                }
                response = ch + response;
                if (lines > extraLines)
                    break;
            }

            lines = -1;
            for (int i = (int)(lineByte - startByte)+1; i < data.length-1; i++) {
                char ch = (char)data[i];
                response += ch;
                if (ch == '\n') {
                    lines++;
                    if (lines >= extraLines) {
                        break;
                    }
                }
            }

            System.out.print(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("");
    }
}
