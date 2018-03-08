package org.villagex.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    public static String parseDateTime(String dateString){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        try {
            date = formatter.parse(dateString);

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d");

            return dateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

}