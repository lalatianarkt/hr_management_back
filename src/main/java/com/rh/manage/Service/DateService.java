package com.rh.manage.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Service;

@Service
public class DateService {
    public String extractNomJourByDate(LocalDate date) {
        if (date == null) {
            return "Date invalide";
        }
        
        return date.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.FRENCH);
    }
}
