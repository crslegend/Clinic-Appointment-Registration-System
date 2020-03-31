/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.sql.Date;
import java.sql.Time;
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

        while (!curr.equals(Time.valueOf("17:30:00"))) {
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
        // Time currentTime = new Time(System.currentTimeMillis());
        Time defaultTime = Time.valueOf("12:42:35");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day > 0 && day < 4) {
            return getMonWed(defaultTime);
        } else if (day == 4) {
            return getThur(defaultTime);
        } else if (day == 5) {
            return getFri(defaultTime);
        } else {
            return new ArrayList<>();
        }

    }

    private List<Time> getMonWed(Time currentTime) {
        List<Time> monToWed = new ArrayList<>(operatingHours);
        System.out.println(monToWed);
        monToWed.removeIf(time -> time.before(currentTime));

        while (monToWed.size() > 6) {
            monToWed.remove(monToWed.size() - 1);
        }
        return monToWed;

    }

    private List<Time> getThur(Time currentTime) {
        List<Time> thurs = new ArrayList<>(operatingHours);
        thurs.remove(Time.valueOf("17:00:00"));
        thurs.remove(Time.valueOf("17:30:00"));

        thurs.removeIf(time -> time.before(currentTime));
        while (thurs.size() > 6) {
            thurs.remove(thurs.size() - 1);
        }
        return thurs;
    }

    private List<Time> getFri(Time currentTime) {
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

        if (day > 1 && day < 4) {
            return opHr;
        } else if (day == 4) {
            opHr.remove(Time.valueOf("17:00:00"));
            opHr.remove(Time.valueOf("17:30:00"));
            return opHr;
        } else if (day == 5) {
            opHr.remove(Time.valueOf("17:30:00"));
            return opHr;
        }

        return new ArrayList<>();
    }

}
