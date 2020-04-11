/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.xmlAdapters;

import java.sql.Time;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author p.tm
 */
public class TimeAdapter extends XmlAdapter<String, Time> {
    
    @Override
    public String marshal(Time time) {
        return time.toString();
    }
 
    @Override
    public Time unmarshal(String time) {
        return Time.valueOf(time);
    }
}
