package Calender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * This Class Provides Calender Project Menu Bar With Two Menus
 */
public class CalenderMenuBar {
    private JMenuBar menuBar ;
    private JMenu insertMenu ;
    private JMenu editMenu ;
    private JMenuItem addEventItem ;
    private JMenuItem addNotationItem ;
    private JMenuItem editEventOrNotationItem ;
    private JMenuItem copyDateItem ;
    private JMenuItem copyOccurenceItem ;

    /**
     * Instantiates The Fields of Components of This Class And Adds Them To MenuBar And Frame
     */
    public CalenderMenuBar() {

        menuBar = new JMenuBar() ;
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

        menuBar.applyComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT ) ;
        menuBar.add( insertMenu ) ;
        menuBar.add( editMenu ) ;

        insertMenu.setDisplayedMnemonicIndex( 0 ) ;
        editMenu.setDisplayedMnemonicIndex( 0 ) ;
        addEventItem.setDisplayedMnemonicIndex( 2 ) ;
        addNotationItem.setDisplayedMnemonicIndex( 0 ) ;
        editEventOrNotationItem.setDisplayedMnemonicIndex( 2 );
        copyDateItem.setDisplayedMnemonicIndex( 0 ) ;
        copyOccurenceItem.setDisplayedMnemonicIndex( 1 ) ;

        addEventItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_E , InputEvent.CTRL_MASK ) ) ;
        addNotationItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_N , InputEvent.CTRL_MASK ) ) ;
        editEventOrNotationItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_D , InputEvent.CTRL_MASK ) ) ;
        copyDateItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_C , InputEvent.CTRL_MASK ) ) ;
        copyOccurenceItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_O , InputEvent.CTRL_MASK ) );

        Font menuFont = new Font( "Arabic Typesetting" , Font.PLAIN , 20 ) ;
        insertMenu.setFont( menuFont ) ;
        editMenu.setFont( menuFont ) ;
        addEventItem.setFont( menuFont ) ;
        addNotationItem.setFont( menuFont ) ;
        editEventOrNotationItem.setFont( menuFont ) ;
        copyDateItem.setFont( menuFont ) ;
        copyOccurenceItem.setFont( menuFont ) ;
    }

    /**
     * Returns addEventItem Field of MenuBar
     * @return
     */
    public JMenuItem getAddEventItem() {
        return addEventItem;
    }
    /**
     * Returns addNotationItem Field of MenuBar
     * @return
     */
    public JMenuItem getAddNotationItem() {
        return addNotationItem;
    }

    /**
     * Returns editEventOrNotationItem Field of MenuBar
     * @return
     */
    public JMenuItem getEditEventOrNotationItem() {
        return editEventOrNotationItem;
    }

    /**
     * Returns copyDateItem Field of MenuBar
     * @return
     */
    public JMenuItem getCopyDateItem() {
        return copyDateItem;
    }

    /**
     * Returns copyOccurenceItem Field of MenuBar
     * @return
     */
    public JMenuItem getCopyOccurenceItem() {
        return copyOccurenceItem ;
    }

    /**
     * Adds MenuBar To in North of The Frame
     * @param frame Owner of MenuBar
     */
    public void addMenuBarToFrame( JFrame frame ) {
        frame.add( this.menuBar , BorderLayout.NORTH ) ;
    }
}
