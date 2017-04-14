package Calender;

import javax.swing.* ;
import java.awt.* ;
import java.awt.event.* ;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException ;
import java.text.SimpleDateFormat ;
import java.time.LocalDate ;
import java.time.format.DateTimeFormatter ;
import java.util.*;
import java.util.Calendar;
import java.util.Timer;

import com.ibm.icu.text.DateFormat ;
import com.ibm.icu.util.*;

/**
 * This Class Is a Panel That Contains Many Components And Their Events Handling
 * Almost Main Class of This Project
 */
public class CalenderTableAndTools extends JPanel {

    private JPanel panelOfToolbarAndMonthLabel ;
    private JPanel panelOfGrid ;
    private JToolBar toolBar ;
    private JButton nextMonthButton ;
    private JButton previousMonthButton ;
    private JButton addEventButton ;
    private JButton addNotationButton ;
    private JButton editEventOrNotationButton ;
    private JLabel currentMonthAndYearLabel ;
    private JLabel[] daysOfWeek ;
    private JLabel[] daysOfMonth ;
    private JPopupMenu popupMenuOfDay ;
    private JMenu insertMenu ;
    private JMenu editMenu ;
    private JMenuItem addEventItem ;
    private JMenuItem addNotationItem ;
    private JMenuItem editEventOrNotationItem ;
    private JMenuItem copyDateItem ;
    private JMenuItem copyOccurenceItem ;
    private int[] displayingDate ;
    private int[] todayDate ;
    private int[] rightClickedDate ;

    /**
     * Instantiates The Fields of Components of This Class Adds Them To Panel That Places In Center of Frame
     */
    public CalenderTableAndTools() {

        panelOfGrid = new JPanel( new GridLayout( 7 , 7  , 5 , 5 ) ) ;
        panelOfToolbarAndMonthLabel = new JPanel( new BorderLayout() ) ;

        toolBar = new JToolBar( SwingConstants.HORIZONTAL ) ;

        Font buttonsToolTipFont = new Font( "B Koodak" , Font.PLAIN , 13 ) ;

        nextMonthButton = new JButton( "►►►" ){
            @Override
            public JToolTip createToolTip() {
                JToolTip tip = super.createToolTip() ;
                tip.setFont( buttonsToolTipFont ) ;
                return tip ;
            }
        } ;
        previousMonthButton = new JButton( "◄◄◄" ) {
            @Override
            public JToolTip createToolTip() {
                JToolTip tip = super.createToolTip() ;
                tip.setFont( buttonsToolTipFont ) ;
                return tip ;
            }
        } ;
        addEventButton = new JButton( "⏰" ) {
            @Override
            public JToolTip createToolTip() {
                JToolTip tip = super.createToolTip() ;
                tip.setFont( buttonsToolTipFont ) ;
                return tip ;
            }
        } ;
        addNotationButton = new JButton( "⌨" ) {
            @Override
            public JToolTip createToolTip() {
                JToolTip tip = super.createToolTip() ;
                tip.setFont( buttonsToolTipFont ) ;
                return tip ;
            }
        } ;
        editEventOrNotationButton = new JButton( "✎ ✓" ){
            @Override
            public JToolTip createToolTip() {
                JToolTip tip = super.createToolTip() ;
                tip.setFont( buttonsToolTipFont ) ;
                return tip ;
            }
        } ;

        daysOfWeek = new JLabel[7] ;
        daysOfMonth = new JLabel[42] ;
        currentMonthAndYearLabel = new JLabel() ;

        displayingDate = new int[3] ;
        todayDate = new int[3] ;
        rightClickedDate = new int[3] ;

        setLayout( new BorderLayout() ) ;

        Font toolBarButtonsFont = new Font( "Segoe UI Symbol" , Font.PLAIN , 20 ) ;
        addNotationButton.setFont( toolBarButtonsFont ) ;
        addNotationButton.setForeground( Color.RED ) ;
        addNotationButton.setToolTipText( "افزودن یادداشت" ) ;

        addEventButton.setFont( toolBarButtonsFont );
        addEventButton.setForeground( Color.RED ) ;
        addEventButton.setToolTipText( "افزودن رویداد" ) ;

        editEventOrNotationButton.setFont( toolBarButtonsFont ) ;
        editEventOrNotationButton.setForeground( Color.RED ) ;
        editEventOrNotationButton.setToolTipText( "ویرایش یادداشت و رویداد" ) ;

        nextMonthButton.setToolTipText( "ماه بعد" ) ;
        nextMonthButton.setForeground( Color.RED ) ;
        nextMonthButton.addActionListener( new nextMonthButtonHandler() ) ;

        previousMonthButton.setToolTipText( "ماه قبل" ) ;
        previousMonthButton.setForeground( Color.RED ) ;
        previousMonthButton.addActionListener( new previousMonthButtonHandler() ) ;

        toolBar.setLayout( new GridLayout( 1 , 4 , 2 , 2 ) ) ;
        toolBar.applyComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT ) ;
        toolBar.setFloatable( false ) ;
        toolBar.add( nextMonthButton ) ;
        toolBar.add( addEventButton ) ;
        toolBar.add( addNotationButton ) ;
        toolBar.add( editEventOrNotationButton ) ;
        toolBar.add( previousMonthButton ) ;

        currentMonthAndYearLabel.setFont( new Font( "B Roya" , Font.PLAIN , 24 ) ) ;
        currentMonthAndYearLabel.setHorizontalAlignment( JLabel.CENTER ) ;
        currentMonthAndYearLabel.setOpaque( true ) ;
        currentMonthAndYearLabel.setBackground( Color.BLUE ) ;
        currentMonthAndYearLabel.setForeground( Color.WHITE ) ;
        panelOfToolbarAndMonthLabel.add( toolBar , BorderLayout.NORTH ) ;
        panelOfToolbarAndMonthLabel.add( currentMonthAndYearLabel , BorderLayout.SOUTH ) ;


        panelOfGrid.applyComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT ) ;
        Font daysOfWeekFont = new Font( "B Roya" , Font.PLAIN , 16 ) ;
        daysOfWeek[0] = new JLabel( "شنبه" ) ;
        daysOfWeek[0].setFont( daysOfWeekFont ) ;
        panelOfGrid.add( daysOfWeek[0] ) ;
        for ( int i = 1  ; i <= 5 ; i++ ) {
            daysOfWeek[i] = new JLabel( i + "شنبه" ) ;
            daysOfWeek[i].setFont( daysOfWeekFont ) ;
            panelOfGrid.add( daysOfWeek[i] ) ;
        }
        daysOfWeek[6] = new JLabel( "جمعه" ) ;
        daysOfWeek[6].setFont( daysOfWeekFont ) ;
        panelOfGrid.add( daysOfWeek[6] ) ;

        for ( int i = 0 ; i <= 6 ; i++ ) {
            daysOfWeek[i].setOpaque( true ) ;
            daysOfWeek[i].setBackground( new Color( 255 , 128 , 0 ) ) ;
            daysOfWeek[i].setHorizontalAlignment( JLabel.CENTER ) ;
        }

        NumbersLeftClickHandler NLCH = new NumbersLeftClickHandler() ;


        for ( int i = 0 ; i <= 41 ; i++ ) {

            daysOfMonth[i] = new JLabel( ) {
                @Override
                public JToolTip createToolTip() {
                    JToolTip tip = super.createToolTip() ;
                    tip.applyComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT ) ;
                    tip.setFont( new Font( "B Koodak" , Font.PLAIN , 12 ) ) ;
                    return tip ;
                }

            } ;

            daysOfMonth[i].setOpaque( true ) ;
            daysOfMonth[i].setFont( new Font("B Koodak" , Font.PLAIN , 14 ) ) ;
            daysOfMonth[i].setBackground( new Color( 0 , 172 , 230 ) ) ;
            daysOfMonth[i].setHorizontalAlignment( JLabel.CENTER ) ;
            daysOfMonth[i].setFocusable( true ) ;
            daysOfMonth[i].addMouseListener( NLCH ) ;
            panelOfGrid.add( daysOfMonth[i] ) ;
        }


        CalenderConvertor calConvertor = new CalenderConvertor() ;

        int year = Integer.parseInt( LocalDate.now().toString().substring( 0 , 4 ) ) ;
        int month = Integer.parseInt( LocalDate.now().toString().substring( 5 , 7 ) ) ;
        int day = Integer.parseInt( LocalDate.now().toString().substring( 8 ) ) ;

        calConvertor.GregorianToPersian( year , month , day  ) ;
        String solarHijiri = calConvertor.toString() ;


        int hijiriYearNumber = Integer.parseInt( solarHijiri.substring( 0 , 4 ) ) ;
        int hijiriMonthNumber = Integer.parseInt( solarHijiri.substring( 5 , 7 ) ) ;
        int dayOfMonth = Integer.parseInt( solarHijiri.substring( 8 , 10 ) ) ;

        displayingDate[0] = hijiriYearNumber ;
        displayingDate[1] = hijiriMonthNumber ;
        displayingDate[2] = dayOfMonth ;

        System.arraycopy( displayingDate , 0 , todayDate , 0 , 3 ) ;

        int j , i ;
        int m = getIndexOfFirstDayInMonth( solarHijiri.substring( 0 , 4 ) , solarHijiri.substring( 5 , 7 ) ) ;
        int n = m + getMonthNumberOfDays( hijiriMonthNumber , hijiriYearNumber ) ;

        setPopupMenuOfDays() ;
        setCurrentMonthAndYearLabel() ;

        try {
            for ( i = m , j = 1 ; i < n ; i++ , j++ ) {
                daysOfMonth[i].setText( "" + j ) ;
                daysOfMonth[i].setToolTipText( "<html><body><p align = 'right'>" + setDateOfToolTip( daysOfMonth[i] )
                        + "<br/>" + "<span style = ' color : green ' >" + setOccurenceOfToolTip( daysOfMonth[i] ) + "</span>"
                        + "<span style = ' color : red ' >" + setNotationOfToolTip( daysOfMonth[i] ) + "</span>"
                        + "<span style = ' color : blue ' >" + setEventOfToolTip( daysOfMonth[i] ) + "</span>"
                        + "</p></body></html>" ) ;

                daysOfMonth[i].setComponentPopupMenu( popupMenuOfDay ) ;
                updateToolTipOfDays( daysOfMonth[i] ) ;
            }
        }
        catch ( ArrayIndexOutOfBoundsException ex ) {
            ex.printStackTrace() ;
        }


        specifyToday() ;
        updateDay() ;


        add( panelOfToolbarAndMonthLabel , BorderLayout.NORTH ) ;
        add( panelOfGrid ) ;
    }

    /**
     * Returns displayingDate Field That Contains Date Of Selected Day
     * @return
     */
    public int[] getDisplayingDate() {
        return displayingDate ;
    }

    /**
     * Returns rightClickedDate Field That Contains Date Of Right Clicked Day
     * @return
     */
    public int[] getRightClickedDate() {
        return rightClickedDate ;
    }

    /**
     * Returns todayDate Field That Contains Date of Today
     * @return
     */
    public int[] getTodayDate() {
        return todayDate ;
    }

    /**
     * Returns addNotationButton Field
     * @return
     */
    public JButton getAddNotationButton() {
        return addNotationButton ;
    }

    /**
     * Returns addEventButton Field
     * @return
     */
    public JButton getAddEventButton() {
        return addEventButton ;
    }

    /**
     * Returns editEventOrNotationButton Field
     * @return
     */
    public JButton getEditEventOrNotationButton() {
        return editEventOrNotationButton ;
    }

    /**
     * Returns addEventItem Field Of PopupMenu
     * @return
     */
    public JMenuItem getAddEventItem() {
        return addEventItem ;
    }

    /**
     * Returns addNotationItem Field Of PopupMenu
     * @return
     */
    public JMenuItem getAddNotationItem() {
        return addNotationItem ;
    }

    /**
     * Returns copyDateItem Field Of PopupMenu
     * @return
     */
    public JMenuItem getCopyDateItem() {
        return copyDateItem ;
    }

    /**
     * Returns editEventOrNationItem Field Of PopupMenu
     * @return
     */
    public JMenuItem getEditEventOrNotationItem() {
        return editEventOrNotationItem ;
    }

    /**
     * Returns copyOccurenceItem Field Of PopupMenu
     * @return
     */
    public JMenuItem getCopyOccurenceItem() {
        return copyOccurenceItem ;
    }

    /**
     * Specifies That First Day of Solar Hijiri Month Is Which Day of Week
     * @param solarHijiriYear
     * @param solarHijiriMonth
     * @return
     */
    public int getIndexOfFirstDayInMonth( String solarHijiriYear , String solarHijiriMonth ) {
        int y = Integer.parseInt( solarHijiriYear ) ;
        int m = Integer.parseInt( solarHijiriMonth ) ;
        CalenderConvertor calCon = new CalenderConvertor() ;
        calCon.PersianToGregorian( y , m , 1 ) ;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "y-M-d" ) ;
        LocalDate date = LocalDate.parse( calCon.toString() , formatter ) ;
        String day = date.getDayOfWeek().toString() ;

        int firstDayInMonthIndex ;

        if ( day.equals( "SATURDAY" ) ) {
            firstDayInMonthIndex = 0 ;
        }
        else if ( day.equals( "SUNDAY" ) ) {
            firstDayInMonthIndex = 1 ;
        }
        else if ( day.equals( "MONDAY" ) ) {
            firstDayInMonthIndex = 2 ;
        }
        else if ( day.equals( "TUESDAY" ) ) {
            firstDayInMonthIndex = 3 ;
        }
        else if ( day.equals( "WEDNESDAY" ) ) {
            firstDayInMonthIndex = 4 ;
        }
        else if ( day.equals( "THURSDAY" ) ) {
            firstDayInMonthIndex = 5 ;
        }
        else if ( day.equals( "FRIDAY" ) ) {
            firstDayInMonthIndex = 6 ;
        }
        else {
            firstDayInMonthIndex = -1 ;
        }

        return firstDayInMonthIndex ;

    }

    /**
     * Returns The Number of Days of a Solar Hijiri Month
     * @param currentSolarMonthNumber
     * @param currentSolarYearNumber
     * @return
     */
    public int getMonthNumberOfDays( int currentSolarMonthNumber , int currentSolarYearNumber ) {
        if ( currentSolarMonthNumber <= 6 ) {
            return 31 ;
        }
        else if ( currentSolarMonthNumber >= 6 && currentSolarMonthNumber <= 11 ) {
            return 30 ;
        }
        else if ( currentSolarMonthNumber == 12 && isLeapYear( currentSolarYearNumber ) ) {
            return 30 ;
        }
        else if ( currentSolarMonthNumber == 12 && ! isLeapYear( currentSolarYearNumber ) ) {
            return 29 ;
        }
        else {
            return -1 ;
        }

    }

    /**
     * Specifies That a Solar Hijiri Year Is Leap Or Not
     * @param solarHijiriYear
     * @return
     */
    public boolean isLeapYear( int solarHijiriYear ) {
        return solarHijiriYear % 4 == 3 ;
    }

    /**
     * When Month Changes In Calender The currentMonthAndYearLabel Text Most Be Change
     * This Method Do That Work
     */
    public void setCurrentMonthAndYearLabel() {
        switch ( displayingDate[1] ) {
            case 1 :
                currentMonthAndYearLabel.setText( "فروردین" + " " + displayingDate[0] ) ;
                break ;
            case 2 :
                currentMonthAndYearLabel.setText( "اردیبهشت" + " " + displayingDate[0] ) ;
                break ;
            case 3 :
                currentMonthAndYearLabel.setText( "خرداد" + " " + displayingDate[0] ) ;
                break ;
            case 4 :
                currentMonthAndYearLabel.setText( "تیر" + " " + displayingDate[0] ) ;
                break ;
            case 5 :
                currentMonthAndYearLabel.setText( "مرداد" + " " + displayingDate[0] ) ;
                break ;
            case 6 :
                currentMonthAndYearLabel.setText( "شهریور" + " " + displayingDate[0] ) ;
                break ;
            case 7 :
                currentMonthAndYearLabel.setText( "مهر" + " " + displayingDate[0] ) ;
                break ;
            case 8 :
                currentMonthAndYearLabel.setText( "آبان" + " " + displayingDate[0] ) ;
                break ;
            case 9 :
                currentMonthAndYearLabel.setText( "آذر" + " " + displayingDate[0] ) ;
                break ;

            case 10 :
                currentMonthAndYearLabel.setText( "دی" + " " + displayingDate[0] ) ;
                break ;
            case 11 :
                currentMonthAndYearLabel.setText( "بهمن" + " " + displayingDate[0] ) ;
                break ;
            case 12 :
                currentMonthAndYearLabel.setText( "اسفند" + " " + displayingDate[0] ) ;
                break ;
            default :
                break ;
        }
    }

    /**
     * This Method Checks If a Day In Calender Have a New Event Or Notation And Have Change Nation And Event
     * Changes The ToolTip Of Days
     * @param label
     */
    public void updateToolTipOfDays( JLabel label ) {

        TimerTask task = new TimerTask(){
            @Override
            public void run() {
                if ( ! label.getText().isEmpty() ) {
                label.setToolTipText( "<html><body><p align = 'right' >" + setDateOfToolTip( label )
                        + "<br/>" + "<span style = ' color : green ' >" + setOccurenceOfToolTip( label ) + "</span>"
                        + "<span style = ' color : red ' >" + setNotationOfToolTip( label ) + "</span>"
                        + "<span style = ' color : blue ' >" + setEventOfToolTip( label ) + "</span>"
                        + "</p></body></html>" ) ;
                }
            }
        } ;

        CalenderGUI.getTimer().schedule( task , 2000 , 5000 ) ;

    }

    /**
     * Returns The Text of Event of A Day In String Object For ToolTip of That Day
     * @param label
     * @return
     */
    public String setEventOfToolTip( JLabel label ) {

        int day = 50 ;
        if ( ! label.getText().isEmpty() ) {
            day = Integer.parseInt( label.getText() ) ;
        }

        String line = "" ;
        String text1 = "" ;
        String text2 = "" ;
        String firstLine = "" ;

        String address = String.format( "./src/Events/%d-%02d-%02d.txt" , displayingDate[0] , displayingDate[1] , day ) ;

        File file = new File( address ) ;
        BufferedReader reader ;

        if ( file.exists() ) {
            try {
                reader = new BufferedReader( new FileReader( file ) ) ;
                firstLine = reader.readLine() ;

                while ( ( line = reader.readLine() ) != null ) {
                    text1 += line + "<br/>" ;
                }

            }
            catch ( IOException ex ) {
                System.err.println( ex.getMessage() ) ;
            }
        }

        text2 = firstLine.replaceAll( " " , "&nbsp;&nbsp;" ) + "<br/>" + text1 ;
        return text2 ;
    }

    /**
     * Returns The Text of Notation of A Day In String Object For ToolTip of That Day
     * @param label
     * @return
     */
    public String setNotationOfToolTip( JLabel label ) {

        int day = 50 ;
        if ( ! label.getText().isEmpty() ) {
            day = Integer.parseInt( label.getText() ) ;
        }

        String line = "" ;
        String text1 = "" ;

        String address = String.format( "./src/Notations/%d-%02d-%02d.txt" , displayingDate[0] , displayingDate[1] , day ) ;
        File file = new File( address ) ;
        BufferedReader reader ;


        if ( file.exists() ) {
            try {
                reader = new BufferedReader( new FileReader( file ) ) ;
                while ( ( line = reader.readLine() ) != null ) {
                    text1 += line + "<br/>" ;
                }
            }
            catch ( IOException ex ) {
                ex.printStackTrace( System.err ) ;
            }
        }

        return text1 ;
    }

    /**
     * Returns The Text of Occurence of A Day In String Object For ToolTip of That Day
     * @param label
     * @return
     */
    public String setOccurenceOfToolTip( JLabel label ) {

        StringBuilder string = new StringBuilder( currentMonthAndYearLabel.getText() ) ;
        string.delete( string.indexOf( " " ) , string.length() ) ;
        String monthName = string.toString() ;
        String day = label.getText() + " " ;
        String text1 = "" ;
        String text2 = "" ;

        File occurences = new File( "./src/Occurences/Occurences1395.txt" ) ;
        BufferedReader reader = null ;
        try {
            reader = new BufferedReader( new FileReader( occurences ) ) ;
            String line = "" ;


            while ( ( line = reader.readLine() ) != null && displayingDate[0] == 1395 ) {

                if ( line.startsWith( "مناسبت های ماه : " + monthName ) ) {

                    while ( ( line = reader.readLine() ) != null ) {
                        if ( line.startsWith( day ) ) {
                            text1 = line.substring( day.length() + monthName.length() + 1 ) ;
                            text2 += text1 + "<br/>" ;
                        }
                        else if ( line.startsWith( "%" ) ) {
                            break ;
                        }
                    }
                }
            }

        }
        catch ( IOException ex ) {
            ex.printStackTrace( System.err ) ;
        }

        return text2 ;
    }

    /**
     * Returns The Text of Tabular Islamic Date And Gregorian Date of A Day In String Object For ToolTip of That Day
     * @param label
     * @return
     */
    public String setDateOfToolTip( JLabel label ) {

        if ( ! label.getText().isEmpty() ) {
            CalenderConvertor calCon = new CalenderConvertor() ;
            int solarYear = displayingDate[0] ;
            int solarMonth = displayingDate[1] ;
            int solarDay = Integer.parseInt( label.getText() ) ;

            calCon.PersianToGregorian( solarYear , solarMonth , solarDay ) ;

            int gregorianMonth = Integer.parseInt( calCon.toString().substring( 5 , 7 ) ) ;
            int greYear = Integer.parseInt( calCon.toString().substring( 0 , 4 ) ) ;
            int greDay = Integer.parseInt( calCon.toString().substring( 8 ) ) ;

            String greMonth = "" ;
            switch ( gregorianMonth ) {
                case 1 :
                    greMonth = "ژانویه" ;
                    break ;
                case 2 :
                    greMonth = "فوریه" ;
                    break ;
                case 3 :
                    greMonth = "مارس" ;
                    break ;
                case 4 :
                    greMonth = "آوریل" ;
                    break ;
                case 5 :
                    greMonth = "مه" ;
                    break ;
                case 6 :
                    greMonth = "ژوئن" ;
                    break ;
                case 7 :
                    greMonth = "ژوئیه" ;
                    break ;
                case 8 :
                    greMonth = "اوت" ;
                    break ;
                case 9 :
                    greMonth = "سپتامبر" ;
                    break ;
                case 10 :
                    greMonth = "اکتبر" ;
                    break ;
                case 11 :
                    greMonth = "نوامبر" ;
                    break ;
                case 12 :
                    greMonth = "دسامبر" ;
                    break ;
                default :
                    break ;
            }

            String tabularIslamicDate = gregorianToTabularIslamic( greYear , gregorianMonth , greDay ) ;
            String islamYear = tabularIslamicDate.substring( 0 , 4 ) ;
            int islamDay = Integer.parseInt( tabularIslamicDate.substring( 8 ) ) ;
            int islamMonth = Integer.parseInt( tabularIslamicDate.substring( 5 , 7 ) ) ;

            String islamicMonth = "" ;

            switch ( islamMonth ) {
                case 1 :
                    islamicMonth = "محرم" ;
                    break ;
                case 2 :
                    islamicMonth = "صفر" ;
                    break ;
                case 3 :
                    islamicMonth = "ربیع الاول" ;
                    break ;
                case 4 :
                    islamicMonth = "ربیع الثانی" ;
                    break ;
                case 5 :
                    islamicMonth = "جمادی الاول" ;
                    break ;
                case 6 :
                    islamicMonth = "جمادی الثانی" ;
                    break ;
                case 7 :
                    islamicMonth = "رجب" ;
                    break ;
                case 8 :
                    islamicMonth = "شعبان" ;
                    break ;
                case 9 :
                    islamicMonth = "رمضان" ;
                    break ;
                case 10 :
                    islamicMonth = "شوال" ;
                    break ;
                case 11 :
                    islamicMonth = "ذی القعده" ;
                    break ;
                case 12 :
                    islamicMonth = "ذی الحجه" ;
                    break ;
                default :
            }

            String toolTipDate = islamDay + " " + islamicMonth + " " + islamYear
                    + " &nbsp;&nbsp;&nbsp;&nbsp; " + greDay + " " + greMonth + " " + greYear ;

            return toolTipDate ;
        }
        else {
            return "" ;
        }
    }

    /**
     * This Method Converts The Gregorian Date To Tabular Islamic Date
     * With Gven Year and Month and Day In Gregorian Calender
     * @param year
     * @param month
     * @param day
     * @return
     */
    public String gregorianToTabularIslamic( int year , int month , int day ) {
        String str =  year + "-" + month + "-" + day ;
        ULocale locale = new ULocale( "@calendar=islamic-umalqura" ) ; /*"fa_IR@calendar=persian"*/ /*"@calendar=islamic-umalqura"*/
        com.ibm.icu.util.Calendar calendar = com.ibm.icu.util.Calendar.getInstance( locale ) ;
        SimpleDateFormat formatter = new SimpleDateFormat( "y-M-d" ) ;
        Date date = null ;
        try {
            date = formatter.parse( str );
        }
        catch ( ParseException e ) {
            e.printStackTrace();
        }
        calendar.setTime( date ) ;
        DateFormat dateFormat = DateFormat.getDateInstance( DateFormat.DATE_FIELD , locale ) ;

        return dateFormat.format( calendar ).substring( 3 ) ;
    }

    /**
     * Instantiates The Fields Of Popup Menu Of Days And Set The Popup Menu of a Day
     */
    private void setPopupMenuOfDays() {
        popupMenuOfDay = new JPopupMenu() ;
        insertMenu = new JMenu( "درج" ) ;
        editMenu = new JMenu( "ویرایش" ) ;
        addEventItem = new JMenuItem( "افزودن رویداد" ) ;
        addNotationItem = new JMenuItem( "افزودن یادداشت" ) ;
        editEventOrNotationItem = new JMenuItem( "ویرایش یادداشت و رویداد" ) ;
        copyDateItem = new JMenuItem( "کپی کردن تاریخ" ) ;
        copyOccurenceItem = new JMenuItem( "کپی کردن مناسبت ها" ) ;

        insertMenu.applyComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT ) ;
        addEventItem.applyComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT ) ;
        insertMenu.add( addEventItem ) ;
        addNotationItem.applyComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT ) ;
        insertMenu.add( addNotationItem ) ;

        editMenu.applyComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT ) ;
        editEventOrNotationItem.applyComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT ) ;
        editMenu.add( editEventOrNotationItem ) ;
        copyDateItem.applyComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT ) ;
        editMenu.add( copyDateItem ) ;
        copyOccurenceItem.applyComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT ) ;
        editMenu.add( copyOccurenceItem ) ;

        popupMenuOfDay.applyComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT ) ;
        popupMenuOfDay.add( insertMenu ) ;
        popupMenuOfDay.add( editMenu ) ;

        Font menuFont = new Font( "B Koodak" , Font.PLAIN , 16 ) ;
        insertMenu.setFont( menuFont ) ;
        editMenu.setFont( menuFont ) ;
        addEventItem.setFont( menuFont ) ;
        addNotationItem.setFont( menuFont ) ;
        editEventOrNotationItem.setFont( menuFont ) ;
        copyDateItem.setFont( menuFont ) ;
        copyOccurenceItem.setFont( menuFont ) ;

    }

    /**
     * Specify Today in Calender With Different ForeGround Color
     */
    private void specifyToday() {

        if ( todayDate[1] == displayingDate[1] && todayDate[0] == displayingDate[0] ) {
            int i = getIndexOfFirstDayInMonth( todayDate[0] + "" , todayDate[1] + "" ) ;
            daysOfMonth[ i + todayDate[2] - 1 ].setForeground( Color.RED ) ;


        }
    }

    /**
     * This Method Checks If Today Changes Apply It In Calender Table
     */
    public void updateDay() {
        Calendar cal = Calendar.getInstance() ;

        CalenderConvertor calCon = new CalenderConvertor() ;
        calCon.PersianToGregorian( todayDate[0] , todayDate[1] , todayDate[2] ) ;
        String dateString = calCon.toString() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat( "y-M-d" ) ;

        Date date = null ;
        try {
            date = dateFormat.parse( dateString ) ;
        }
        catch ( ParseException ex ) {
            ex.printStackTrace() ;
        }

        cal.setTime( date ) ;
        cal.add( Calendar.DATE , 1 ) ;


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if ( todayDate[2] < getMonthNumberOfDays( todayDate[1] , todayDate[0] ) ) {
                    todayDate[2] += 1 ;
                }
                else if ( todayDate[2] == getMonthNumberOfDays( todayDate[1] , todayDate[0] ) && todayDate[1] < 12 ) {
                    todayDate[2] = 1 ;
                    todayDate[1] += 1 ;
                }
                else if ( todayDate[2] == getMonthNumberOfDays( todayDate[1] , todayDate[0] ) && todayDate[1] == 12 ) {
                    todayDate[2] = 1 ;
                    todayDate[1] = 1 ;
                    todayDate[1] += 1 ;
                }
            }
        } ;

        CalenderGUI.getTimer().schedule( task , cal.getTime() ) ;

    }

    /**
     * When A Day Have Been Selected
     * This Method Set Text of Status Bar Of Calender That Is Text Area Component
     * @param l
     */
    public void setStatusBarText( JLabel l ) {
        String text = "" ;
        if ( ! l.getText().equals( "" ) ) {
            text = setDateOfToolTip( l ).replaceAll( "&nbsp;" , " " )
                    + "\n" + setOccurenceOfToolTip( l ).replaceAll( "<br/>" , "\n" ) ;

            CalenderGUI.getStatusBar().setText( text ) ;
        }
        else {
            CalenderGUI.getStatusBar().setText( null ) ;
        }

    }

    /**
     * This Method Does Some Operations When Month of Calender Changes
     */
    private void whenMonthChanging() {

        for( JLabel l : daysOfMonth ) {
            l.setText( "" ) ;
            l.setBackground( new Color( 0 , 172 , 230 ) ) ;
            l.setForeground( null ) ;
        }

        CalenderGUI.getStatusBar().setText( null ) ;

        displayingDate[2] = -1 ;

        int m = getIndexOfFirstDayInMonth( "" + displayingDate[0] , "" + displayingDate[1] ) ;
        int n = m + getMonthNumberOfDays( displayingDate[1] , displayingDate[0] ) ;
        int j , i ;

        setCurrentMonthAndYearLabel() ;

        try {
            for ( i = m , j = 1 ; i < n ; i++ , j++ ) {
                daysOfMonth[i].setText( "" + j ) ;
                daysOfMonth[i].setToolTipText( "<html><body><p align = 'right' >" + setDateOfToolTip( daysOfMonth[i])
                        + "<br/>" + "<span style = ' color : green ' >" + setOccurenceOfToolTip( daysOfMonth[i] ) + "</span>"
                        + "<span style = ' color : red ' >" + setNotationOfToolTip( daysOfMonth[i] ) + "</span>"
                        + "<span style = ' color : blue ' >" + setEventOfToolTip( daysOfMonth[i] ) + "</span>"
                        + "</p></body></html>" ) ;

                updateToolTipOfDays( daysOfMonth[i] ) ;
            }
        }
        catch ( ArrayIndexOutOfBoundsException ex ) {
            ex.printStackTrace() ;
        }

        specifyToday() ;

        for ( JLabel l : daysOfMonth ) {
            if ( l.getText().isEmpty() ) {
                l.setToolTipText( null ) ;
                l.setComponentPopupMenu( null ) ;
            }
            else {
                l.setComponentPopupMenu( popupMenuOfDay ) ;
            }
        }
    }

    /**
     * This Class When In Calender nextMonthButton Clicked Changes Some Components Text
     */
    private class nextMonthButtonHandler implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent event ) {

            if ( displayingDate[1] <= 11 ) {
                displayingDate[1] += 1 ;
            }
            else if ( displayingDate[1] == 12 ) {
                displayingDate[0] += 1 ;
                displayingDate[1] = 1 ;
            }

            whenMonthChanging() ;

            setCurrentMonthAndYearLabel() ;
        }
    }

    /**
     * This Class When In Calender previousMonthButton Clicked Changes Some Components Text
     */
    private class previousMonthButtonHandler implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent event ) {
            if ( displayingDate[1] >= 2 ) {
                displayingDate[1] -= 1 ;
            }
            else if ( displayingDate[1] == 1 ) {
                displayingDate[0] -= 1 ;
                displayingDate[1] = 12 ;
            }

            whenMonthChanging() ;

            setCurrentMonthAndYearLabel() ;
        }
    }

    /**
     * This Class When A Day in Calender Clicked Changes Background of Its Label
     */
    private class NumbersLeftClickHandler extends MouseAdapter {

        @Override

        public void mousePressed( MouseEvent event ) {
            if ( SwingUtilities.isLeftMouseButton( event ) ) {

                for ( JLabel l : daysOfMonth ) {
                    l.setBackground( new Color( 0 , 172 , 230 ) ) ;
                }

                String componentText = ( (JLabel) event.getComponent() ).getText() ;

                if ( ! componentText.isEmpty() ) {
                    event.getComponent().setBackground( Color.BLUE ) ;
                    displayingDate[2] = Integer.parseInt( componentText ) ;
                    setStatusBarText( (JLabel) event.getComponent() ) ;


                }
                else {
                    displayingDate[2] = -1 ;
                    setStatusBarText( (JLabel) event.getComponent() ) ;
                }
            }

            if ( SwingUtilities.isRightMouseButton( event ) ) {
                String componentText = ( (JLabel) event.getComponent() ).getText() ;

                if ( ! componentText.isEmpty() ) {
                    rightClickedDate[0] = displayingDate[0] ;
                    rightClickedDate[1] = displayingDate[1] ;
                    rightClickedDate[2] = Integer.parseInt( componentText ) ;
                }
                else {
                    rightClickedDate[0] = displayingDate[0] ;
                    rightClickedDate[1] = displayingDate[1] ;
                    rightClickedDate[2] = -1 ;
                }

            }
        }
    }
}
