import java.util.Arrays;
import java.util.TreeSet;

public class PatternMap {
    TreeSet<Long> positions = new TreeSet<>();
    Long pointer = null;
    boolean powerToPrint;

    PatternMap (boolean powerToPrint) {
        this.powerToPrint = powerToPrint;
    }

    boolean foundAt (Long position) {
        if (!positions.contains(position)) {
            positions.add(position);
            if (pointer == null) {
                pointer = position;
            }
            return false;
        }
        return true;
    }

    Long next () {
        Object[] array = positions.toArray();

        for (int i = 0; i < array.length; i++) {
            Long curr = (Long)array[i];
            if (curr == pointer) {
                if (i+1 == array.length) {
                    if (powerToPrint) {
                        System.out.println("1/" + array.length);
                    }
                    pointer = (Long)array[0];
                } else {
                    if (powerToPrint) {
                        System.out.println((i+2) + "/" + array.length);
                    }
                    pointer = (Long)array[i+1];
                }
                return pointer;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    Long prev () {
        Object[] array = positions.toArray();

        for (int i = 0; i < array.length; i++) {
            Long curr = (Long)array[i];
            if (curr == pointer) {
                if (i-1 < 0) {
                    if (powerToPrint) {
                        System.out.println(array.length + "/" + array.length);
                    }
                    pointer = (Long)array[array.length-1];
                } else {
                    if (powerToPrint) {
                        System.out.println(i + "/" + array.length);
                    }
                    pointer = (Long)array[i-1];
                }
                return pointer;
            }
        }
        throw new IndexOutOfBoundsException();
    }
}