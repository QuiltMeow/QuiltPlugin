package ew.quilt.Item.Lore;

public class FormatType {

    public static final char[] FORMAT_TYPE = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F',
        'L', 'N', 'O', 'K', 'M', 'R'
    };

    public static boolean isFormatType(char type) {
        for (char check : FORMAT_TYPE) {
            if (type == check) {
                return true;
            }
        }
        return false;
    }
}
