package Calender;

import javax.swing.JFrame ;
import javax.swing.UIManager ;

public class Main extends JFrame{

    public static void main( String[] args ) {

        try {
            UIManager.setLookAndFeel( "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel" ) ;
        }
        catch ( Exception ex ) {
            ex.printStackTrace() ;
        }

        CalenderGUI myCal = new CalenderGUI() ;
        myCal.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
        myCal.setVisible( true ) ;
    }

}
