package cz.prague.vida.vocab;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

/**
 * The listener interface for receiving frameDrag events.
 * The class that is interested in processing a frameDrag
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addFrameDragListener<code> method. When
 * the frameDrag event occurs, that object's appropriate
 * method is invoked.
 *
 * @see FrameDragEvent
 */
public class FrameDragListener extends MouseAdapter {

    private final JFrame frame;
    private Point mouseDownCompCoords = null;

    /**
     * Instantiates a new frame drag listener.
     *
     * @param frame the frame
     */
    public FrameDragListener(JFrame frame) {
        this.frame = frame;
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        mouseDownCompCoords = null;
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
        mouseDownCompCoords = e.getPoint();
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e) {
        Point currCoords = e.getLocationOnScreen();
        frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
    }
}
