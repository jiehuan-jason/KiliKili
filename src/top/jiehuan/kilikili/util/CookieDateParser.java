package top.jiehuan.kilikili.util;

import java.util.Vector;

public class CookieDateParser {
    private static final String[] MONTHS = {
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    public static long parseCookieDate(String dateStr) {
        try {
            // 例子: "Wed, 09 Jun 2021 10:18:14 GMT"
            String[] parts = split(dateStr, ' ');
            if (parts.length < 6) return -1;

            int day = Integer.parseInt(parts[1]);
            int month = getMonthIndex(parts[2]);
            int year = Integer.parseInt(parts[3]);

            String[] timeParts = split(parts[4], ':');
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            int second = Integer.parseInt(timeParts[2]);

            return toUnixTime(year, month, day, hour, minute, second);
        } catch (Exception e) {
            return -1;
        }
    }

    private static int getMonthIndex(String monthStr) {
        for (int i = 0; i < MONTHS.length; i++) {
            if (MONTHS[i].equalsIgnoreCase(monthStr)) return i;
        }
        return -1;
    }

    private static long toUnixTime(int year, int month, int day,
                                   int hour, int minute, int second) {
        // 这是一个简化版本，仅支持 1970 年之后，不考虑夏令时等
        int[] daysInMonth = {
            31, 28, 31, 30, 31, 30,
            31, 31, 30, 31, 30, 31
        };

        long days = 0;

        for (int y = 1970; y < year; y++) {
            days += isLeapYear(y) ? 366 : 365;
        }

        for (int m = 0; m < month; m++) {
            days += daysInMonth[m];
            if (m == 1 && isLeapYear(year)) days += 1;
        }

        days += day - 1;

        return days * 86400L + hour * 3600 + minute * 60 + second;
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private static String[] split(String str, char ch) {
        Vector v = new Vector();
        int start = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {
                v.addElement(str.substring(start, i));
                start = i + 1;
            }
        }
        v.addElement(str.substring(start));

        String[] result = new String[v.size()];
        v.copyInto(result);
        return result;
    }
}
