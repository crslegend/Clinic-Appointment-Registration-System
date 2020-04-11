/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.xmlAdapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Date;

/**
 *
 * @author p.tm
 */
public class DateAdapter extends XmlAdapter<String, Date> {
    
    @Override
    public String marshal(Date date) {
        return  date.toString();
    }
    
    @Override
    public Date unmarshal(String date) {
        return Date.valueOf(date);
    }
    
}
