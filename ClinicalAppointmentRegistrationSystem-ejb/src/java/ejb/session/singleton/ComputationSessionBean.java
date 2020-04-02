/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import java.sql.Date;
import java.sql.Time;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Singleton;

/**
 *
 * @author p.tm
 */
@Singleton
@Local(ComputationSessionBeanLocal.class)
@Remote(ComputationSessionBeanRemote.class)
public class ComputationSessionBean implements ComputationSessionBeanRemote, ComputationSessionBeanLocal {

    List<Time> operatingHours = new ArrayList<>();

    @PostConstruct
    public void postConstruct() {
        System.out.println("********** Epstein didn't kill himself");

        Time curr = Time.valueOf("08:30:00");
        long milliseconds = curr.getTime();

        while (!curr.equals(Time.valueOf("18:00:00"))) {
            operatingHours.add(curr);
            milliseconds += 30 * 60 * 1000;
            curr = new Time(milliseconds);
        }
        operatingHours.remove(Time.valueOf("12:30:00"));
        operatingHours.remove(Time.valueOf("13:00:00"));

    }

    @Override
    public List<Time> getNextSixTimeSlots() {

        // set default time to 12:42
        //Time defaultTime = new Time(System.currentTimeMillis());
        ZonedDateTime defaultTime = ZonedDateTime.now(ZoneId.of( "Asia/Singapore" ));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        //Time defaultTime = Time.valueOf("12:42:35");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println(day);

        if (day > 1 && day < 5) {
            return getMonWed(defaultTime.format(formatter));
        } else if (day == 5) {
            return getThur(defaultTime.format(formatter));
        } else if (day == 6) {
            return getFri(defaultTime.format(formatter));
        } else {
            return new ArrayList<>();
        }

    }

    private List<Time> getMonWed(String currentTime) {
        List<Time> monToWed = new ArrayList<>(operatingHours);
        System.out.println(monToWed);
        monToWed.removeIf(time -> {
            //System.out.println(time.toString());
            System.out.println(time.toString().compareTo(currentTime) < 0);
            //return time.before(currentTime);
            return time.toString().compareTo(currentTime) < 0;
        });

        System.out.println(monToWed.size());
        while (monToWed.size() > 6) {
            monToWed.remove(monToWed.size() - 1);
        }
        return monToWed;

    }

    private List<Time> getThur(String currentTime) {
        List<Time> thurs = new ArrayList<>(operatingHours);
        thurs.remove(Time.valueOf("17:00:00"));
        thurs.remove(Time.valueOf("17:30:00"));

        thurs.removeIf(time -> {
            //time.before(currentTime)
            System.out.println(time.toString().compareTo(currentTime) < 0);
            return time.toString().compareTo(currentTime) < 0;
        });
        while (thurs.size() > 6) {
            thurs.remove(thurs.size() - 1);
        }
        return thurs;
    }

    private List<Time> getFri(String currentTime) {
        List<Time> fri = new ArrayList<>(operatingHours);
        fri.remove(Time.valueOf("17:00:00"));

        while (fri.size() > 6) {
            fri.remove(fri.size() - 1);
        }
        return fri;
    }

    @Override
    public List<Time> getAllTimeSlots(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        List<Time> opHr = new ArrayList<>(operatingHours);

        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day > 1 && day < 5) {
            return opHr;
        } else if (day == 5) {
            opHr.remove(Time.valueOf("17:00:00"));
            opHr.remove(Time.valueOf("17:30:00"));
            return opHr;
        } else if (day == 6) {
            opHr.remove(Time.valueOf("17:30:00"));
            return opHr;
        }

        return new ArrayList<>();
    }

}
