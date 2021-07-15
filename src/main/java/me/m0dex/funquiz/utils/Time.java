package me.m0dex.funquiz.utils;

import me.m0dex.funquiz.FunQuiz;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time {

    private final long milli;

    public Time(long _milli) {
        milli = _milli;
    }

    public long toTicks() {
        return milli / 50;
    }
    public long toMilli() { return milli; }

    public boolean isZero() {
        return milli == 0;
    }

    public static Time fromTimeString(String timeString) {

        long _milli = 0;

        try {

            if (timeString.equals("0"))
                return new Time(0);

            Pattern timePattern = Pattern.compile("(\\d+)([hms])", Pattern.CASE_INSENSITIVE);
            Matcher matcher = timePattern.matcher(timeString);

            while (matcher.find()) {
                int amount = Common.tryParseInt(matcher.group(1));

                switch (matcher.group(2)) {
                    case "h":
                        _milli += amount * 60 * 60 * 1000;
                        break;
                    case "m":
                        _milli += amount * 60 * 1000;
                        break;
                    case "s":
                    default:
                        _milli += amount * 1000;
                        break;
                }
            }
        } catch (Exception e) {
            FunQuiz.getInstance().getLogger().severe("Couldn't parse time string -> {}" + timeString + ":");
            e.printStackTrace();
        }

        return new Time(_milli);
    }
}
