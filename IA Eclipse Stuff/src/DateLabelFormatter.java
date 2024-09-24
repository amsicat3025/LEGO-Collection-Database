import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFormattedTextField.AbstractFormatter;

//Class initialies the DateLabel Formatter information used in DatePicker (the Calendar date-picking GUI)
public class DateLabelFormatter extends AbstractFormatter 
{

    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    //Parses the String parameter to an object
    @Override
    public Object stringToValue(String text) throws ParseException 
    {
        return dateFormatter.parseObject(text);
    }

    //Does... Something
    @Override
    public String valueToString(Object value) throws ParseException 
    {
        if (value != null)
        {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }

        return "";
    }

}
