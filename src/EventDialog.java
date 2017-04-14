package Calender;

import javax.swing.* ;
import java.awt.* ;
import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.io.BufferedWriter ;
import java.io.File ;
import java.io.FileWriter ;
import java.io.IOException ;
import java.text.ParseException ;
import java.text.SimpleDateFormat ;
import java.util.Date ;
import java.util.TimerTask;
import java.util.Timer ;

/**
 * When Need To Add a Event To Calender This Class Provides a Dialog For This Work
 */
public class EventDialog {

    private int[] date ;
    private JDialog dialog ;
    private JTextArea textArea ;
    private JButton saveButton ;
    private JButton cancelButton ;
    private JPanel southPanel ;
    private JPanel buttonsPanel ;

    private JSpinner hourSpinner ;
    private JSpinner minuteSpinner ;
    private JSpinner secondSpinner ;
    private JLabel hourLabel ;
    private JLabel minuteLabel ;
    private JLabel secondLabel ;
    private JPanel spinnersAndLabelsPanel ;
    private JScrollPane scroll ;

    /**
     * Instantiates Fields of This Class And Build The Dialog For Adding Event
     * @param frame
     * @param inputDate
     * @param text
     * @param time
     */
    public EventDialog( JFrame frame , int[] inputDate , String text , int[] time ) {
        date = new int[3] ;

        System.arraycopy( inputDate , 0 , date , 0 , 3 ) ;

        dialog = new JDialog( frame , "رویداد" , true ) ;

        textArea = new JTextArea( 5 , 10 ) ;
        scroll = new JScrollPane( textArea , JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                , JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ) ;

        saveButton = new JButton( "ذخیره" ) ;
        cancelButton = new JButton( "انصراف" ) ;
        buttonsPanel = new JPanel() ;
        southPanel = new JPanel( new BorderLayout() ) ;

        spinnersAndLabelsPanel = new JPanel() ;
        hourSpinner = new JSpinner( new SpinnerNumberModel( 1 , 1 , 24 , 1 ) ) ;
        minuteSpinner = new JSpinner( new SpinnerNumberModel( 0 , 0 , 59 , 1 ) ) ;
        secondSpinner = new JSpinner( new SpinnerNumberModel( 0 , 0 , 59 , 1 ) ) ;

        hourLabel = new JLabel( "ساعت" , JLabel.CENTER ) ;
        minuteLabel = new JLabel( "دقیقه" , JLabel.CENTER ) ;
        secondLabel = new JLabel( "ثانیه" , JLabel.CENTER) ;

        Font labelsAndSpinnersFont = new Font( "B Roya" , Font.PLAIN , 14 ) ;

        ( ( JSpinner.DefaultEditor )hourSpinner.getEditor() ).getTextField().setEditable( false ) ;
        ( ( JSpinner.DefaultEditor )minuteSpinner.getEditor() ).getTextField().setEditable( false ) ;
        ( ( JSpinner.DefaultEditor )secondSpinner.getEditor() ).getTextField().setEditable( false ) ;

        hourSpinner.getEditor().setFont( labelsAndSpinnersFont ) ;
        minuteSpinner.getEditor().setFont( labelsAndSpinnersFont ) ;
        secondSpinner.getEditor().setFont( labelsAndSpinnersFont ) ;

        hourSpinner.setValue( time[0] ) ;
        minuteSpinner.setValue( time[1] ) ;
        secondSpinner.setValue( time[2] ) ;

        hourLabel.setFont( labelsAndSpinnersFont ) ;
        minuteLabel.setFont( labelsAndSpinnersFont ) ;
        secondLabel.setFont( labelsAndSpinnersFont ) ;

        spinnersAndLabelsPanel.add( secondSpinner ) ;
        spinnersAndLabelsPanel.add( secondLabel ) ;
        spinnersAndLabelsPanel.add( minuteSpinner ) ;
        spinnersAndLabelsPanel.add( minuteLabel ) ;
        spinnersAndLabelsPanel.add( hourSpinner ) ;
        spinnersAndLabelsPanel.add( hourLabel ) ;


        saveButton.setFont( new Font( "B Koodak" , Font.PLAIN , 14 ) ) ;
        saveButton.addActionListener( new saveButtonHandler() ) ;
        cancelButton.setFont( new Font( "B Koodak" , Font.PLAIN , 14 ) ) ;
        cancelButton.addActionListener( new cancelButtonHandler() );
        textArea.setFont( new Font( "Tahoma" , Font.PLAIN , 14 ) );
        textArea.setLineWrap( true ) ;
        textArea.setWrapStyleWord( true ) ;
        textArea.applyComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT ) ;
        textArea.setText( text ) ;

        dialog.setLayout( new BorderLayout() ) ;

        buttonsPanel.add( saveButton ) ;
        buttonsPanel.add( cancelButton ) ;

        southPanel.add( buttonsPanel , BorderLayout.SOUTH ) ;
        southPanel.add( spinnersAndLabelsPanel , BorderLayout.NORTH ) ;

        dialog.add( southPanel , BorderLayout.SOUTH ) ;
        dialog.add( scroll ) ;

        dialog.pack() ;
        dialog.setLocation( 200 , 200 ) ;
        dialog.setResizable( false ) ;
        dialog.setVisible( true ) ;

    }

    /**
     * When Save Button of Dialog Clicked This Class Execute Some Operations On a File and Other Variables
     */
    private class saveButtonHandler implements ActionListener {

        @Override
        public void actionPerformed( ActionEvent event ) {
            CalenderConvertor calCon = new CalenderConvertor() ;

            Timer timer = new Timer() ;

            calCon.PersianToGregorian( date[0] , date[1] , date[2] ) ;

            String dateAndTime = String.format( "%s  %02d:%02d:%02d" , calCon.toString() , hourSpinner.getValue()
                    , minuteSpinner.getValue() , secondSpinner.getValue() ) ;

            SimpleDateFormat dateFormat = new SimpleDateFormat( "y-M-d  H:m:s" ) ;
            Date time = null ;
            try {
                time = dateFormat.parse( dateAndTime ) ;
            }
            catch ( ParseException ex ) {
                ex.printStackTrace( System.err ) ;
            }


            String dateOfNote = String.format( "%d-%02d-%02d" , date[0] , date[1] , date[2] ) ;
            File eventFile = new File( "./src/Events/" + dateOfNote + ".txt" ) ;
            BufferedWriter writer = null ;

            try {
                writer = new BufferedWriter( new FileWriter( eventFile ) ) ;
                String text1 = dateAndTime + "\n" + textArea.getText() ;
                writer.write( text1.replaceAll( "\n" , System.lineSeparator() ) ) ;
                writer.flush() ;
            }
            catch ( IOException ex ) {
                ex.printStackTrace( System.err ) ;
            }

            String dialogText = textArea.getText().replaceAll( "\n" , "<br/>" ) ;
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog( null , "<html><body><p><span style = 'font-family : B Koodak ; font-size: 14px ' >"
                            + dialogText + "</span></p></body></html>" , "رویداد" , JOptionPane.INFORMATION_MESSAGE ) ;
                    eventFile.delete() ;
                    this.cancel() ;
                }
            } ;

            if ( time != null ) {
                try {
                    CalenderGUI.getTimer().schedule( task , time ) ;
                }
                catch ( IllegalArgumentException ex ) {
                    System.err.println( "IllegalArgumentException ☺☺☻☻" ) ;
                }

            }

            dialog.dispose() ;
        }

    }

    /**
     * When cancel Button of Dialog Clicked This Class Close The Dialog
     */
    private class cancelButtonHandler implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent event ) {
            dialog.dispose() ;
        }
    }


}
