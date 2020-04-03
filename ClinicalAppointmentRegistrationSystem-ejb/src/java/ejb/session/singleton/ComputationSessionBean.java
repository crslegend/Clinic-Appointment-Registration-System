/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import util.exception.ClinicNotOpenException;

/**
 *
 * @author p.tm
 */
@Singleton
@Startup
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
    public List<Time> getNextSixTimeSlots() throws ClinicNotOpenException {

        // set default time to 12:42
//        Time defaultTime = Time.valueOf("12:42:35");
//        ZonedDateTime defaultTime = ZonedDateTime.now(ZoneId.of("Asia/Singapore"));
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        Time defaultTime = Time.valueOf(LocalTime.now());
//        System.out.println("Time should be before " + defaultTime.before(Time.valueOf("23:59:00")));
//        System.out.println("Time should be after " + defaultTime.before(Time.valueOf("10:59:00")));

//        System.out.println(day);
//        if (day > 1 && day < 5) {
//            return getMonWed(defaultTime.format(formatter));
//        } else if (day == 5) {
//            return getThur(defaultTime.format(formatter));
//        } else if (day == 6) {
//            return getFri(defaultTime.format(formatter));
//        } else {
//            return new ArrayList<>();
//        }
        if (day > 1 && day < 5) {
            return getMonWed(defaultTime);
        } else if (day == 5) {
            return getThur(defaultTime);
        } else if (day == 6) {
            return getFri(defaultTime);
        } else {
            throw new ClinicNotOpenException("Clinic is not open!");
        }

    }

    private List<Time> getMonWed(Time currentTime) {
        List<Time> monToWed = new ArrayList<>(operatingHours);
        System.out.println(monToWed);
        monToWed.removeIf(time -> {
            System.out.println(time.before(currentTime));
            return time.before(currentTime);
//            System.out.println(time.toString().compareTo(currentTime) < 0);
//            return time.toString().compareTo(currentTime) < 0;
        });

        System.out.println(monToWed.size());
        while (monToWed.size() > 6) {
            monToWed.remove(monToWed.size() - 1);
        }
        return monToWed;

    }

    private List<Time> getThur(Time currentTime) {
        List<Time> thurs = new ArrayList<>(operatingHours);
        thurs.remove(Time.valueOf("17:00:00"));
        thurs.remove(Time.valueOf("17:30:00"));

        thurs.removeIf(time -> {
            System.out.println(time.before(currentTime));
            return time.before(currentTime);
//            System.out.println(tsime.toString().compareTo(currentTime) < 0);
//            return time.toString().compareTo(currentTime) < 0;
        });
        while (thurs.size() > 6) {
            thurs.remove(thurs.size() - 1);
        }
        return thurs;
    }

    private List<Time> getFri(Time currentTime) {
        List<Time> fri = new ArrayList<>(operatingHours);
        fri.remove(Time.valueOf("17:00:00"));

        fri.removeIf(time -> {
            System.out.println(time.before(currentTime));
            return time.before(currentTime);
//            System.out.println(time.toString().compareTo(currentTime) < 0);
//            return time.toString().compareTo(currentTime) < 0;
        });
        while (fri.size() > 6) {
            fri.remove(fri.size() - 1);
        }
        return fri;
    }

    @Override
    public List<Time> getAllTimeSlots(Date date) throws ClinicNotOpenException {

        int day = getDayOfWeek(date);

        List<Time> opHr = new ArrayList<>(operatingHours);

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

        throw new ClinicNotOpenException("Clinic is not open on " + date.toString() + "!");
    }

    @Override
    public int getDayOfWeek(Date date) throws ClinicNotOpenException {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day == 1 || day == 7) {
            throw new ClinicNotOpenException("Clinic is not open on " + date.toString() + "!");
        }
        return day;

    }

}
