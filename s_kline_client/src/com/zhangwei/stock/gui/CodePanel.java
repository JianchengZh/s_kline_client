package com.zhangwei.stock.gui;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: User
 * Date: Feb 6, 2008
 * Time: 9:45:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class CodePanel extends JPanel implements KeyListener, Scrollable
{

    /**
     * The font size for the text in the CodePanel
     */
    private int fontSize = 12;

    /**
     * The spacing between lines; defined to be 1.5*fontSize
     */
    private int lineSpacing = 18;

    /**
     * The x-inset where text will be aligned - be sure to leave enough space for the
     * line number panel
     */
    private int xinset = 40;

    /**
     * The y-inset where the first line will start
     */
    private int yinset = 20;

    /**
     * The inset where line numbers will be displayed
     */
    private int lineNumberInset = 5;

    /**
     * The preferred size that the CodePanel needs to paint all of the code
     */
    private Dimension preferredScrollableViewportSize;

    /**
     * Denotes if anything changed in the code that necessitates a change in the
     * viewport size, such as a new line added or a line of code has grown longer
     * than the current longest
     */
    private boolean preferredScrollableViewportSizeChanged = false;

    /**
     * Model: contains the code to be displayed
     */
    private CodeModel model = new CodeModel();

    public CodePanel()
    {
        // Listing to all key strokes
        this.addKeyListener( this );
    }

    public int getFontSize()
    {
        return fontSize;
    }

    public void setFontSize( int fontSize )
    {
        this.fontSize = fontSize;
        this.lineSpacing = ( int )( fontSize * 1.5 );
    }

    /**
     * Computes the dimension required to paint this document
     * @return
     */
    private Dimension computeDimension()
    {
        // Handle the initial case
        if( this.preferredScrollableViewportSize == null )
        {
            this.preferredScrollableViewportSize = new Dimension();
            this.preferredScrollableViewportSizeChanged = true;
        }

        if( this.preferredScrollableViewportSizeChanged )
        {
            // Compute the height
            int height = yinset + ( lineSpacing * model.getLineCount() );

            // Find the longest line
            int maxLineLength = 0;
            for( int i=0; i<model.getLineCount(); i++ )
            {
                int lineLength = model.getLine( i ).length();
                if( lineLength > maxLineLength )
                {
                    maxLineLength = lineLength;
                }
            }
            int width = xinset + ( 7 * maxLineLength );

            // Build and return a new Dimension object
            this.preferredScrollableViewportSize.setSize( width, height );

            // Update our boolean flag
            this.preferredScrollableViewportSizeChanged = false;
        }
        return this.preferredScrollableViewportSize;
    }

    private BufferedImage offscreen;

    public void paint( Graphics graphics )
    {
        this.requestFocusInWindow();

        // Get the size of the panel
        Dimension panelSize = this.getSize();

        // Create a back buffer: lazy create
        if( this.offscreen == null ||
            this.offscreen.getWidth() != panelSize.width ||
            this.offscreen.getHeight() != panelSize.height )
        {
            this.offscreen = new BufferedImage( panelSize.width, panelSize.height, BufferedImage.TYPE_USHORT_565_RGB );
        }

        // Get a reference to the offscreen's Graphics object
        Graphics g = offscreen.getGraphics();

        // Fill in the background
        g.setColor( Color.white );
        g.fillRect( 0, 0, panelSize.width, panelSize.height );

        // Fill in the left border
        g.setColor( new Color( 255, 255, 215 ) );
        g.fillRect( 0, 0, xinset - 5, panelSize.height );

        // Set the font
        g.setFont( new Font( "Courier New", Font.PLAIN, 12 ) );
        g.setColor( Color.black );

        // Start drawing the text
        int x=xinset;
        int y=yinset;
        int lineCount = model.getLineCount();
        for( int i=0; i<lineCount; i++ )
        {
            String line = model.getLine( i );

            // Draw the text
            g.setColor( Color.black );
            g.drawString( line, x, y );

            // Draw the line number
            g.setColor( Color.gray );
            g.drawString( Integer.toString( i ), lineNumberInset, y );

            // Increment to the next line
            y += lineSpacing;
        }

        // Draw the buffer to the screen
        graphics.drawImage( offscreen, 0, 0, offscreen.getWidth(), offscreen.getHeight(), null );
    }

    public static void main( String[] args )
    {
        JFrame frame = new JFrame( "Code Panel" );
        frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

        CodePanel codePanel = new CodePanel();
        
        JScrollPane scrollPane = new JScrollPane( codePanel );
        scrollPane.getViewport().setBackground( Color.white );
        frame.add( scrollPane );

        //frame.add( codePanel );

        frame.setSize( 1024, 768 );
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation( d.width / 2 - 512, d.height / 2 - 384  );
        frame.setVisible( true );
    }

    public void keyTyped(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
        System.out.println( "Key Typed: " + e.getKeyCode() + "(" + e.getKeyChar() + ")" );
    }

    public void keyPressed(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
        System.out.println( "Key Pressed: " + e.getKeyCode() + "(" + e.getKeyChar() + ")" );
    }

    public void keyReleased(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
        System.out.println( "Key Released: " + e.getKeyCode() + "(" + e.getKeyChar() + ")" );
    }

    public Dimension getPreferredScrollableViewportSize() {
        Dimension d = this.computeDimension();
        //System.out.println( "getPreferredScrollableViewportSize: " + d );
        return d;
    }

    /**
     * Return the total size of the CodePanel
     * @return
     */
    public Dimension getPreferredSize()
    {
        Dimension d = this.computeDimension();
        //System.out.println( "getPreferredSize: " + d );
        return d;
    }

    /**
     * Return the number of pixels to move when a unit increment is requested (for example when the user presses
     * an arrow button on the scrollbar 
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        //System.out.println( "getScrollableUnitIncrement: " + visibleRect + ", " + orientation + ", " + direction );
        if( orientation == SwingConstants.HORIZONTAL )
        {
            // Return the width of a single character
            return 7;
        }
        else
        {
            // Return the height of a single line
            return this.lineSpacing;
        }
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        //System.out.println( "getScrollableBlockIncrement: " + visibleRect + ", " + orientation + ", " + direction );
        if( orientation == SwingConstants.HORIZONTAL )
        {
            // Return the full width of the rectangle
            return visibleRect.width;
        }
        else
        {
            // Return the height of the visibleRect mod-ed by the line spacing so that
            // we'll move by full line blocks
            int height = visibleRect.height - ( visibleRect.height % this.lineSpacing );
            //System.out.println( "Return: " + height );
            return height;
        }
    }

    /**
     * Return false because code won't wordwrap so we don't want to force the width of the scrollable to
     * match the width of the CodePanel
     * @return
     */
    public boolean getScrollableTracksViewportWidth() {
        //System.out.println( "getScrollableTracksViewportWidth" );
        return false;
    }

    /**
     * Return false because we want vertical scrolling enabled to be able to be able to page through
     * the code.
     * @return
     */
    public boolean getScrollableTracksViewportHeight() {
        //System.out.println( "getScrollableTracksViewportHeight" );
        return false;
    }
}

