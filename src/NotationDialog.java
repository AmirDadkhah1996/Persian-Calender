package Calender;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * When Need To Add a Notation To Calender This Class Provides a Dialog For This Work
 */
public class NotationDialog {

    private  int[] date ;
    private JDialog dialog ;
    private JTextArea textArea = new JTextArea( 5 , 10 ) ;
    private JButton saveButton ;
    private JButton cancelButton ;
    private JPanel southPanel ;
    private JPanel buttonsPanel ;
    private JScrollPane scroll ;

    /**
     * Instantiates Fields of This Class And Build The Dialog For Adding Notation
     * @param frame
     * @param inputDate
     * @param text
     */
    public NotationDialog( JFrame frame , int[] inputDate , String text ) {

        date = new int[3] ;

        System.arraycopy( inputDate , 0 , date , 0 , 3 ) ;

        dialog = new JDialog( frame , "یادداشت" , true ) ;

        scroll = new JScrollPane( textArea , JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                , JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ) ;

        saveButton = new JButton( "ذخیره" ) ;
        cancelButton = new JButton( "انصراف" ) ;
        buttonsPanel = new JPanel() ;
        southPanel = new JPanel( new BorderLayout() ) ;

        saveButton.setFont( new Font( "B Koodak" , Font.PLAIN , 14 ) ) ;
        saveButton.addActionListener( new saveButtonHandler() ) ;

        cancelButton.setFont( new Font( "B Koodak" , Font.PLAIN , 14 ) ) ;
        cancelButton.addActionListener( new cancelButtonHandler() ) ;

        textArea.setFont( new Font( "Tahoma" , Font.PLAIN , 14 ) );
        textArea.setLineWrap( true ) ;
        textArea.setWrapStyleWord( true ) ;
        textArea.applyComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT ) ;
        textArea.setText( text ) ;

        dialog.setLayout( new BorderLayout() ) ;

        buttonsPanel.add( saveButton ) ;
        buttonsPanel.add( cancelButton ) ;

        southPanel.add( buttonsPanel , BorderLayout.SOUTH ) ;

        dialog.add( southPanel , BorderLayout.SOUTH ) ;
        dialog.add( scroll ) ;
        dialog.setSize( 200 , 200 ) ;

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
            String dateOfNote = String.format( "%d-%02d-%02d" , date[0] , date[1] , date[2] ) ;
            File notationFile = new File( "./src/Notations/" + dateOfNote + ".txt" ) ;
            BufferedWriter writer = null ;
            try {
                writer = new BufferedWriter( new FileWriter( notationFile ) ) ;
                String text1 = textArea.getText() ;
                String text2 = text1.replaceAll( "\n" , System.lineSeparator() ) ;

                if ( ! text1.isEmpty() ) {
                    writer.write( text2 ) ;
                    writer.flush() ;
                }
                else {
                    notationFile.delete() ;
                }

            }
            catch ( IOException ex ) {
                ex.printStackTrace( System.err ) ;
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
