import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Schedule {

    public static class TimeSpan {
        public final int day;
        public final int startHour;
        public final int endHour;

        public TimeSpan(int day, int startTime, int endTime) {
            this.day = day;
            this.startHour = startTime;
            this.endHour = endTime;
        }
    }

    private static final List<TimeSpan> openSpans = new ArrayList<TimeSpan>();
    private static final List<Date> inputDates = new ArrayList<Date>();

    private static Date nextOpeningDate(Date nextDate) {
        for (final Date inputDate : inputDates) {
            if (!isOpenOn(inputDate)) {
                continue;
            }
            if (inputDate.getTime() > nextDate.getTime()) {
                return inputDate;
            }
        }

        return null;
    }

    private static boolean isOpenOn(final Date date) {
        for (final TimeSpan span : openSpans) {
            if (span.day != date.getDay()) {
                continue;
            }
            // Checks if date is inside an open span of time, also checks for
            // edge case where endHour
            if (date.getHours() >= span.startHour && (date.getHours() < span.endHour
                    || (date.getHours() == span.endHour && date.getMinutes() == 0))) {
                return true;
            }
        }
        return false;
    }

    public static void main(String args[]) throws Exception {
        Console console = System.console();
        String input;

        // Parse console input
        while (!(input = console.readLine()).isEmpty()) {
            String[] inputSplit = input.split("\" \"");

            inputSplit[0] = inputSplit[0].replaceAll("\"", "");
            inputSplit[1] = inputSplit[1].replaceAll("\"", "");

            String[] days = inputSplit[0].split(",");
            String[] hours = inputSplit[1].split("-");

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            int openingHour = formatter.parse(hours[0]).getHours();
            int closingHour = formatter.parse(hours[1]).getHours();

            SimpleDateFormat dayFormatter = new SimpleDateFormat("E");
            for (int i = 0; i < days.length; i++) {
                String day = days[i].trim();
                Date date = dayFormatter.parse(day);
                openSpans.add(new TimeSpan(date.getDay(), openingHour, closingHour));
            }
        }

        // Tests start here:
        SimpleDateFormat timestampFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        Date wednesday = timestampFormatter.parse("2016-05-11T12:22:11.824");
        Date thursday = timestampFormatter.parse("2016-05-12T12:22:11.824");
        Date saturday = timestampFormatter.parse("2016-05-14T09:15:00.000");
        Date sunday = timestampFormatter.parse("2016-05-15T09:15:00.000");
        Date friday_morning = timestampFormatter.parse("2016-05-13T08:00:00.000");
        Date monday_morning = timestampFormatter.parse("2016-05-16T08:00:00.000");
        Date thursday_afternoon = timestampFormatter.parse("2016-05-12T14:00:00.000");

        inputDates.add(wednesday);
        inputDates.add(thursday);
        inputDates.add(saturday);
        inputDates.add(sunday);
        inputDates.add(friday_morning);
        inputDates.add(monday_morning);
        inputDates.add(thursday_afternoon);

        inputDates.sort(new Comparator<Date>() {
            @Override
            public int compare(Date date1, Date date2) {
                return date1.compareTo(date2);
            }
        });

        System.out.println(Boolean.toString(isOpenOn(wednesday) == true));
        System.out.println(Boolean.toString(isOpenOn(thursday) == false));
        System.out.println(Boolean.toString(isOpenOn(sunday) == false));

        System.out.println(Boolean.toString(nextOpeningDate(thursday_afternoon) == friday_morning));
        System.out.println(Boolean.toString(nextOpeningDate(saturday) == monday_morning));
        System.out.println(Boolean.toString(nextOpeningDate(thursday) == thursday_afternoon));
    }
}