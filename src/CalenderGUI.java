package Calender;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer ;
import java.util.TimerTask;

/**
 * Provides Calender Project Main Frame That Contains All Components And Some Event Listeners .
 */
public class CalenderGUI extends JFrame {

    private static JTextArea statusBar ;
    private CalenderMenuBar menuBar ;
    private static CalenderTableAndTools tableAndTools ;
    private static Timer timer = new Timer() ;
    private JScrollPane scroll ;
    private JPopupMenu trayIconMenu ;
    private JMenu insertMenu ;
    private JMenu editMenu ;
    private JMenuItem showWindowItem ;
    private JMenuItem exitItem ;

    /**
     * Instantiate More Of Components And Classes Like tableAndTools And menuBar And A Timer For
     * Using in This Class And Other Classes
     */
    public CalenderGUI() {
        super( "Persian Calender-تقویم پارسی" ) ;

        menuBar = new CalenderMenuBar() ;
        tableAndTools = new CalenderTableAndTools() ;

        statusBar = new JTextArea( 4 , 3 ) ;
        statusBar.setFocusable( false ) ;
        statusBar.setFont( new Font( "B Koodak" , Font.PLAIN , 14 ) ) ;
        statusBar.applyComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT ) ;

        scroll = new JScrollPane( statusBar , JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                , JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ) ;

        setLocation( 200 , 200 ) ;
        setSize( 320 , 460 ) ;

        menuBar.addMenuBarToFrame( this ) ;

        AddNotationButtonHandler ANBH = new AddNotationButtonHandler() ;
        AddEventButtonHandler AEBH = new AddEventButtonHandler() ;
        EditEventAndNotationButtonHandler EEANBH = new EditEventAndNotationButtonHandler() ;
        EditEventAndNotationButtonHandlerInPopupMenu EEANBHIPM = new EditEventAndNotationButtonHandlerInPopupMenu() ;
        CopyDateItemHandler CDIH = new CopyDateItemHandler() ;
        CopyOccurenceItemHandler COIH = new CopyOccurenceItemHandler() ;
        CopyDateItemHandlerInPopupMenu CDIHIPM = new CopyDateItemHandlerInPopupMenu() ;
        CopyOccurenceItemHandlerInPopupMenu COIHIPM = new CopyOccurenceItemHandlerInPopupMenu() ;

        tableAndTools.getAddNotationButton().addActionListener( ANBH ) ;
        tableAndTools.getAddEventButton().addActionListener( AEBH ) ;
        tableAndTools.getEditEventOrNotationButton().addActionListener( EEANBH ) ;

        tableAndTools.getAddEventItem().addActionListener( AEBH ) ;
        tableAndTools.getAddNotationItem().addActionListener( ANBH ) ;
        tableAndTools.getEditEventOrNotationItem().addActionListener( EEANBHIPM ) ;
        tableAndTools.getCopyDateItem().addActionListener( CDIHIPM ) ;
        tableAndTools.getCopyOccurenceItem().addActionListener( COIHIPM ) ;

        menuBar.getAddEventItem().addActionListener( AEBH ) ;
        menuBar.getAddNotationItem().addActionListener( ANBH ) ;
        menuBar.getEditEventOrNotationItem().addActionListener( EEANBH ) ;
        menuBar.getCopyDateItem().addActionListener( CDIH ) ;
        menuBar.getCopyOccurenceItem().addActionListener( COIH ) ;

        add( tableAndTools ) ;
        //add( scroll , BorderLayout.SOUTH ) ;
        add( statusBar , BorderLayout.SOUTH ) ;
        systemNotification() ;
        loadEventsAtStartup() ;

    }

    /**
     * Returns The Static Field timer
     * @return timer
     */
    public static Timer getTimer() {
        return timer ;
    }

    /**
     * Returns The Outer Class (CalenderGUI) For Inner Classes
     * @return  This Constructor Of CalenderGUI Class
     */
    public JFrame getOuter() {
        return CalenderGUI.this ;
    }

    /**
     * Returns The Static Field statusBar
     * @return  statusBar
     */
    public static JTextArea getStatusBar() {
        return statusBar ;
    }

    /**
     * Creates The System Notification Area Icon For This Frame That Contains Some Things
     */
    public void systemNotification() {
        trayIconMenu = new JPopupMenu() ;
        insertMenu = new JMenu() ;
        editMenu = new JMenu() ;


        int[] currentDate = tableAndTools.getDisplayingDate() ;

        SystemTray tray = SystemTray.getSystemTray() ;
        String imageAddress = "/Numbers/" + currentDate[2] + ".jpg" ;

        ImageIcon imageIcon = new ImageIcon( getClass().getResource( imageAddress ) ) ;
        Image img = imageIcon.getImage() ;

        TrayIcon icon = new TrayIcon( img ,
                 tableAndTools.setDateOfToolTip( new JLabel( "" + tableAndTools.getTodayDate()[2] ) ).replaceAll( "&nbsp;" , " " ) + "\n"
                + tableAndTools.setOccurenceOfToolTip( new JLabel( "" + tableAndTools.getTodayDate()[2] ) ).replaceAll( "<br/>" , "\n" ) + "\n"
                + tableAndTools.setNotationOfToolTip( new JLabel( "" + tableAndTools.getTodayDate()[2] ) ).replaceAll( "<br/>" , "\n" ) + "\n"
                + tableAndTools.setEventOfToolTip( new JLabel( "" + tableAndTools.getTodayDate()[2] ) ).replaceAll( "<br/>" , "\n" ) ) ;

        icon.setImageAutoSize( true ) ;

        img.getScaledInstance( 30 , 60 , Image.SCALE_DEFAULT ) ;

        try {
            tray.add( icon ) ;
            icon.displayMessage( "Persian Calender- تقویم پارسی" , "☺☺!! به برنامه تقویم پارسی خوش آمدید" , TrayIcon.MessageType.INFO ) ;
        }
        catch ( AWTException ex ) {
            ex.printStackTrace() ;
        }

    }

    /**
     * Loads The Events Files In Startup Of Program And Runs Them With TimerTask Class
     */
    public void loadEventsAtStartup() {

        File eventsDirectory = new File( "./src/Events" ) ;
        File[] listOfFiles = eventsDirectory.listFiles() ;

        for ( File file : listOfFiles ) {

            if ( file.isFile() ) {

                BufferedReader reader = null ;
                String firstLine = "" ;
                String line = "" ;
                String text = "" ;
                Date dateAndTime = null ;

                try {
                    reader = new BufferedReader( new FileReader( file ) ) ;
                    firstLine = reader.readLine() ;

                    while ( ( line = reader.readLine() ) != null ) {
                        text += line + "<br/>" ;
                    }

                    SimpleDateFormat dateFormat = new SimpleDateFormat( "y-M-d  H:m:s" ) ;
                    dateAndTime = dateFormat.parse( firstLine ) ;

                    TimerTask task = new TimerTask() {
                        private String string = "" ;

                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog( getOuter() ,
                                    "<html><body><p><span style = 'font-family : B Koodak ; font-size: 14px ' > "
                                    + string
                                    + "</span></p></body></html>" , "رویداد" , JOptionPane.INFORMATION_MESSAGE ) ;
                            file.deleteOnExit() ;
                            this.cancel() ;
                        }

                        private TimerTask inIt( String str ) {
                            string = str ;
                            return this ;
                        }

                    }.inIt( text ) ;

                    timer.schedule( task , dateAndTime ) ;

                }
                catch ( IOException ex ) {
                    ex.printStackTrace( System.err ) ;
                }
                catch ( ParseException ex) {
                    ex.printStackTrace( System.err ) ;
                }


            }
        }

    }

    /**
     * After Pressing The Edit Button In Frame This Method Searches For The Notation File And Opens It In A JDialog
     * @param file
     */
    public void provideNotationDialog( File file ) {
        String line = "" ;
        String text = "" ;

        BufferedReader reader ;
        try {
            reader = new BufferedReader( new FileReader( file ) ) ;
            while( ( line = reader.readLine() ) != null ) {
                text += line ;
            }

        }
        catch ( IOException ex ) {
            ex.printStackTrace( System.err ) ;
        }

        NotationDialog notationDialog = new NotationDialog( getOuter() , tableAndTools.getDisplayingDate() , text ) ;

    }

    /**
     * After Pressing The Edit Button In Frame This Method Searches For The Event File And Opens It In A JDialog
     * @param file
     */
    public void provideEventDialog( File file ) {

        String firstLine = "" ;
        String line = "" ;
        String text = "" ;

        BufferedReader reader ;
        try {
            reader = new BufferedReader( new FileReader( file ) ) ;

            firstLine = reader.readLine() ;

            while ( ( line = reader.readLine() ) != null ) {
                text += line ;
            }
        }
        catch ( IOException ex ) {
            ex.printStackTrace( System.err ) ;
        }

        int hour = Integer.parseInt( firstLine.substring( 12 , 14 ) )  ;
        int minute = Integer.parseInt( firstLine.substring( 15 , 17 ) ) ;
        int second = Integer.parseInt( firstLine.substring( 18 ) )  ;
        int[] time = { hour , minute , second } ;
        EventDialog eventDialog = new EventDialog( getOuter() , tableAndTools.getDisplayingDate() , text , time ) ;



    }

    /**
     * This Class Implements ActionListener Class For Handling The Add Notation Button Event In Frame
     */
    private class AddNotationButtonHandler implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent event ) {

            if ( tableAndTools.getDisplayingDate()[2] >= 1 ) {
                NotationDialog notationDialog = new NotationDialog( getOuter() , tableAndTools.getDisplayingDate() , "" ) ;
            }
            else {
                JOptionPane.showMessageDialog( getOuter() , "<html><body><p><span style='font-family: B Koodak; font-size:14px'>" +
                        "لطفا یک روز را انتخاب کنید !!</span></p></body></html>" , "⚠ توجه ⚠" , JOptionPane.INFORMATION_MESSAGE );
            }
        }
    }

    /**
     * This Class Implements ActionListener Class For Handling The Add Event Button Event In Frame
     */
    private class AddEventButtonHandler implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent event ) {
            int[] time = { 1 , 0 , 0 } ;
            if ( tableAndTools.getDisplayingDate()[2] >= 1 ) {
                EventDialog eventDialog = new EventDialog( getOuter() , tableAndTools.getDisplayingDate() , "" , time ) ;
            }
            else {
                JOptionPane.showMessageDialog( getOuter() , "<html><body><p><span style='font-family: B Koodak; font-size:14px'>" +
                        "لطفا یک روز را انتخاب کنید !!</span></p></body></html>" , "⚠ توجه ⚠" , JOptionPane.INFORMATION_MESSAGE ) ;
            }
        }
    }

    /**
     * This Class Implements ActionListener Class For Handling The Edit Notation And Event Button Event In Frame
     * Uses provideEventDialog And provideNotationDialog Methods
     */
    private class EditEventAndNotationButtonHandler implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent event ) {
            if ( tableAndTools.getDisplayingDate()[2] >= 1 ) {

                String notationFileAddress = String.format( "./src/Notations/%d-%02d-%02d.txt"
                        , tableAndTools.getDisplayingDate()[0] , tableAndTools.getDisplayingDate()[1]
                        , tableAndTools.getDisplayingDate()[2] ) ;

                String eventFileAddress = String.format("./src/Events/%d-%02d-%02d.txt"
                        , tableAndTools.getDisplayingDate()[0] , tableAndTools.getDisplayingDate()[1]
                        , tableAndTools.getDisplayingDate()[2] ) ;

                File notationFile = new File( notationFileAddress ) ;
                File eventFile = new File( eventFileAddress ) ;

                if ( ! notationFile.exists() && ! eventFile.exists() ) {
                    JOptionPane.showMessageDialog( getOuter() , "<html><body><p><span style='font-family: B Koodak; font-size:14px'>" +
                            "برای این روز هیچ یادداشت یا رویدادی تعریف نشده است !!</span></p></body></html>"
                            , "⚠ توجه ⚠" , JOptionPane.INFORMATION_MESSAGE ) ;
                }

                else if ( notationFile.exists() && ! eventFile.exists() ) {
                    provideNotationDialog( notationFile ) ;

                }

                else if ( ! notationFile.exists() && eventFile.exists() ) {
                    provideEventDialog( eventFile ) ;
                }

                else if ( notationFile.exists() && eventFile.exists() ) {
                    provideEventDialog( eventFile) ;
                    provideNotationDialog( notationFile ) ;
                }

            }

            else {
                JOptionPane.showMessageDialog( getOuter() , "<html><body><p><span style='font-family: B Koodak; font-size:14px'>" +
                        "لطفا یک روز را انتخاب کنید !!</span></p></body></html>" , "⚠ توجه ⚠" , JOptionPane.INFORMATION_MESSAGE ) ;
            }

        }

    }

    /**
     * This Class Copies Date of Selected Day in Frame In Three Calenders To Clip Board
     * When Its Item In MenuBar Clicked
     */
    private class CopyDateItemHandler implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent event ) {
            CalenderConvertor calCon = new CalenderConvertor() ;
            calCon.PersianToGregorian( tableAndTools.getDisplayingDate()[0] , tableAndTools.getDisplayingDate()[1]
                    , tableAndTools.getDisplayingDate()[2] ) ;

            String gregorian = calCon.toString() ;
            String islamic = tableAndTools.gregorianToTabularIslamic( Integer.parseInt( gregorian.substring( 0 , 4 ) )
                    , Integer.parseInt( gregorian.substring( 5 , 7 ) )
                    , Integer.parseInt( gregorian.substring( 8 ) ) ) ;

            String date = String.format( "%d/%02d/%02d\n%s\n%s" , tableAndTools.getDisplayingDate()[0]
                    , tableAndTools.getDisplayingDate()[1] , tableAndTools.getDisplayingDate()[2]
                    , islamic.replaceAll( "-" , "/" ) , gregorian.replaceAll( "-" , "/" ) ) ;

            StringSelection strSelect = new StringSelection( date ) ;
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard() ;
            clipboard.setContents( strSelect , strSelect ) ;
        }

    }

    /**
     * This Class Copies Occurences of Selected Day in Frame
     * When Its Item In MenuBar Clicked
     */
    private class CopyOccurenceItemHandler implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent event ) {
            String occurences = tableAndTools.setOccurenceOfToolTip( new JLabel( "" + tableAndTools.getDisplayingDate()[2] ) )
                    .replaceAll( "<br/>" , "\n" ) ;

            StringSelection strSelect = new StringSelection( occurences ) ;
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard() ;
            clipboard.setContents( strSelect , strSelect ) ;
        }
    }

    /**
     * This Class Implements ActionListener Interface For Handling The Edit Notation And Event Item In Popup Menu In Frame
     * Uses provideEventDialog And provideNotationDialog Methods
     */
    private class EditEventAndNotationButtonHandlerInPopupMenu implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent event ) {
            if ( tableAndTools.getRightClickedDate()[2] >= 1 ) {

                String notationFileAddress = String.format( "./src/Notations/%d-%02d-%02d.txt"
                        , tableAndTools.getDisplayingDate()[0] , tableAndTools.getDisplayingDate()[1]
                        , tableAndTools.getRightClickedDate()[2] ) ;

                String eventFileAddress = String.format("./src/Events/%d-%02d-%02d.txt"
                        , tableAndTools.getDisplayingDate()[0] , tableAndTools.getDisplayingDate()[1]
                        , tableAndTools.getRightClickedDate()[2] ) ;

                File notationFile = new File( notationFileAddress ) ;
                File eventFile = new File( eventFileAddress ) ;

                if ( ! notationFile.exists() && ! eventFile.exists() ) {
                    JOptionPane.showMessageDialog( getOuter() , "<html><body><p><span style='font-family: B Koodak; font-size:14px'>" +
                                    "برای این روز هیچ یادداشت یا رویدادی تعریف نشده است !!</span></p></body></html>"
                            , "⚠ توجه ⚠" , JOptionPane.INFORMATION_MESSAGE ) ;
                }

                else if ( notationFile.exists() && ! eventFile.exists() ) {
                    provideNotationDialog( notationFile ) ;

                }

                else if ( ! notationFile.exists() && eventFile.exists() ) {
                    provideEventDialog( eventFile ) ;
                }

                else if ( notationFile.exists() && eventFile.exists() ) {
                    provideEventDialog( eventFile) ;
                    provideNotationDialog( notationFile ) ;
                }

            }

            else {
                JOptionPane.showMessageDialog( getOuter() , "<html><body><p><span style='font-family: B Koodak; font-size:14px'>" +
                        "لطفا یک روز را انتخاب کنید !!</span></p></body></html>" , "⚠ توجه ⚠" , JOptionPane.INFORMATION_MESSAGE ) ;
            }

        }

    }

    /**
     * This Class Copies Date of Selected Day in Frame In Three Calenders To Clip Board
     * When Its Item In Popup Menu Clicked
     */
    private class CopyDateItemHandlerInPopupMenu implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent event ) {
            CalenderConvertor calCon = new CalenderConvertor() ;
            calCon.PersianToGregorian( tableAndTools.getDisplayingDate()[0] , tableAndTools.getDisplayingDate()[1]
                    , tableAndTools.getRightClickedDate()[2] ) ;

            String gregorian = calCon.toString() ;
            String islamic = tableAndTools.gregorianToTabularIslamic( Integer.parseInt( gregorian.substring( 0 , 4 ) )
                    , Integer.parseInt( gregorian.substring( 5 , 7 ) )
                    , Integer.parseInt( gregorian.substring( 8 ) ) ) ;

            String date = String.format( "%d/%02d/%02d\n%s\n%s" , tableAndTools.getDisplayingDate()[0]
                    , tableAndTools.getDisplayingDate()[1] , tableAndTools.getDisplayingDate()[2]
                    , islamic.replaceAll( "-" , "/" ) , gregorian.replaceAll( "-" , "/" ) ) ;

            StringSelection strSelect = new StringSelection( date ) ;
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard() ;
            clipboard.setContents( strSelect , strSelect ) ;
        }

    }

    /**
    * This Class Copies Occurences of Selected Day in Frame
    * When Its Item In Popup Menu Clicked
    */
    private class CopyOccurenceItemHandlerInPopupMenu implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent event ) {
            String occurences = tableAndTools.setOccurenceOfToolTip( new JLabel( "" + tableAndTools.getRightClickedDate()[2] ) )
                    .replaceAll( "<br/>" , "\n" ) ;

            StringSelection strSelect = new StringSelection( occurences ) ;
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard() ;
            clipboard.setContents( strSelect , strSelect ) ;
        }
    }


}
