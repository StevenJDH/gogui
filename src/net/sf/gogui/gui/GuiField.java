//----------------------------------------------------------------------------
// $Id$
// $Source$
//----------------------------------------------------------------------------

package net.sf.gogui.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import javax.swing.UIManager;
import net.sf.gogui.go.GoColor;
import net.sf.gogui.utils.RadialGradientPaint;
 
//----------------------------------------------------------------------------

/** Component representing a field on the board.
    The implementation assumes that the size of the component is a square,
    which is automatically guaranteed if the board uses SquareLayout.
*/
public class GuiField
{
    public GuiField()
    {
    }

    public void clearInfluence()
    {
        m_influenceSet = false;
        m_influence = 0;
    }

    public void draw(Graphics graphics, int size, int x, int y,
                     boolean fastPaint)
    {
        m_fastPaint = fastPaint;
        m_graphics = graphics.create(x, y, size, size);
        if (! m_fastPaint && m_graphics instanceof Graphics2D)
            m_graphics2D = (Graphics2D)m_graphics;
        else
            m_graphics2D = null;
        m_size = size;
        if (m_fieldColor != null)
            drawFieldColor();
        if (m_territory != GoColor.EMPTY && m_graphics2D == null)
            drawTerritoryGraphics();
        if (m_color != GoColor.EMPTY)
            drawStone();
        if (m_territory != GoColor.EMPTY && m_graphics2D != null)
            drawTerritoryGraphics2D();
        if (m_influenceSet)
            drawInfluence();
        drawMarks();
        if (m_crossHair)
            drawCrossHair();
        if (m_lastMoveMarker)
            drawLastMoveMarker();
        if (m_select)
            drawSelect();
        else
            drawLabel();
        if (m_cursor)
            drawCursor();
        m_graphics = null;
    }

    public GoColor getColor()
    {
        return m_color;
    }

    public boolean getCursor()
    {
        return m_cursor;
    }

    public boolean getCrossHair()
    {
        return m_crossHair;
    }

    public Color getFieldBackground()
    {
        return m_fieldColor;
    }

    public boolean getMark()
    {
        return m_mark;
    }

    public boolean getMarkCircle()
    {
        return m_markCircle;
    }

    public boolean getMarkSquare()
    {
        return m_markSquare;
    }

    public boolean getMarkTriangle()
    {
        return m_markTriangle;
    }

    public boolean getSelect()
    {
        return m_select;
    }

    public static int getStoneMargin(int size)
    {
        return size / 17;
    }

    public String getLabel()
    {
        return m_label;
    }

    public GoColor getTerritory()
    {
        return m_territory;
    }

    public boolean isInfluenceSet()
    {
        return m_influenceSet;
    }

    public void paintComponent(Graphics graphics)
    {
    }

    public void setFieldBackground(Color color)
    {
        m_fieldColor = color;
    }

    public void setColor(GoColor color)
    {
        m_color = color;
    }

    public void setCrossHair(boolean crossHair)
    {
        m_crossHair = crossHair;
    }

    public void setCursor(boolean cursor)
    {
        m_cursor = cursor;
    }

    public void setInfluence(double value)
    {
        if (value > 1.)
            value = 1.;
        else if (value < -1.)
            value = -1.;
        m_influence = value;
        m_influenceSet = true;
    }

    public void setLastMoveMarker(boolean lastMoveMarker)
    {
        m_lastMoveMarker = lastMoveMarker;
    }

    public void setMark(boolean mark)
    {
        m_mark = mark;
    }

    public void setMarkCircle(boolean mark)
    {
        m_markCircle = mark;
    }

    public void setMarkSquare(boolean mark)
    {
        m_markSquare = mark;
    }

    public void setMarkTriangle(boolean mark)
    {
        m_markTriangle = mark;
    }

    public void setSelect(boolean select)
    {
        m_select = select;
    }

    public void setLabel(String s)
    {
        m_label = s;
    }

    public void setTerritory(GoColor color)
    {
        assert(color != null);
        m_territory = color;
    }

    private boolean m_crossHair;

    private boolean m_fastPaint;

    private boolean m_cursor;

    private boolean m_lastMoveMarker;

    private boolean m_mark;

    private boolean m_markCircle;

    private boolean m_markSquare;

    private boolean m_markTriangle;

    private boolean m_influenceSet;

    private boolean m_select;

    private static int m_cachedFontFieldSize;

    private int m_paintSizeBlack;

    private int m_paintSizeWhite;

    private int m_size;

    private double m_influence;

    /** Serial version to suppress compiler warning.
        Contains a marker comment for serialver.sourceforge.net
    */
    private static final long serialVersionUID = 0L; // SUID

    private static final AlphaComposite m_composite5
        = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);

    private static final AlphaComposite m_composite7
        = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f); 

    private static final Stroke m_thickStroke
        = new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);

    private String m_label = "";

    private Color m_fieldColor;

    private GoColor m_territory = GoColor.EMPTY;

    private static final Color m_influenceBlackColor = Color.gray;

    private static final Color m_influenceWhiteColor = Color.white;

    private static final Color m_lastMoveMarkerColor
        = Color.decode("#c34543");

    private static final Color m_colorBlackStone = Color.decode("#030303");

    private static final Color m_colorBlackStoneBright
        = Color.decode("#666666");

    private static final Color m_colorWhiteStone = Color.decode("#d7d0c9");

    private static final Color m_colorWhiteStoneBright
        = Color.decode("#f6eee6");

    private static Font m_cachedFont;

    private GoColor m_color = GoColor.EMPTY;

    private Graphics m_graphics;

    private Graphics2D m_graphics2D;

    private RadialGradientPaint m_paintBlack;

    private RadialGradientPaint m_paintWhite;

    private void drawCircle(Color color)
    {
        m_graphics.setColor(color);
        int d = m_size * 36 / 100;
        int w = m_size - 2 * d;
        m_graphics.fillOval(d, d, w, w);
    }

    private void drawCrossHair()
    {
        setComposite(m_composite7);
        int d = m_size / 5;
        int center = m_size / 2;
        m_graphics.setColor(Color.red);
        m_graphics.drawLine(d, center, m_size - d, center);
        m_graphics.drawLine(center, d, center, m_size - d);
        m_graphics.setPaintMode();
    }

    private void drawCursor()
    {
        setComposite(m_composite5);
        int d = m_size / 6;
        int w = m_size;
        int d2 = 2 * d;
        m_graphics.setColor(Color.red);
        m_graphics.drawLine(d, d, d2, d);
        m_graphics.drawLine(d, d, d, d2);
        m_graphics.drawLine(d, w - d2 - 1, d, w - d - 1);
        m_graphics.drawLine(d, w - d - 1, d2, w - d - 1);
        m_graphics.drawLine(w - d2 - 1, d, w - d - 1, d);
        m_graphics.drawLine(w - d - 1, d, w - d - 1, d2);
        m_graphics.drawLine(w - d - 1, w - d - 1, w - d - 1, w - d2 - 1);
        m_graphics.drawLine(w - d - 1, w - d - 1, w - d2 - 1, w - d - 1);
        m_graphics.setPaintMode();
    }

    private void drawFieldColor()
    {
        setComposite(m_composite5);
        m_graphics.setColor(m_fieldColor);
        m_graphics.fillRect(0, 0, m_size, m_size);
        m_graphics.setPaintMode();
    }

    private void drawInfluence()
    {
        double d = Math.abs(m_influence);
        if (d < 0.01)
            return;
        setComposite(m_composite7);
        if (m_influence > 0)
            m_graphics.setColor(m_influenceBlackColor);
        else
            m_graphics.setColor(m_influenceWhiteColor);
        int dd = (int)(m_size * (0.38 + (1 - d) * 0.62));
        int width = m_size - dd;
        m_graphics.fillRect(dd / 2, dd / 2, width, width);
    }

    private void drawLabel()
    {
        setFont(m_graphics, m_size);
        FontMetrics metrics = m_graphics.getFontMetrics();
        int stringWidth = metrics.stringWidth(m_label);
        int stringHeight = metrics.getAscent();
        int x = Math.max((m_size - stringWidth) / 2, 0);
        int y = stringHeight + (m_size - stringHeight) / 2;
        if (m_color == GoColor.WHITE)
            m_graphics.setColor(Color.black);
        else
            m_graphics.setColor(Color.white);
        Rectangle clip = null;
        if (stringWidth > 0.95 * m_size)
        {
            clip = m_graphics.getClipBounds();
            m_graphics.setClip(clip.x, clip.y,
                               (int)(0.95 * clip.width), clip.height);
        }
        m_graphics.drawString(m_label, x, y);
        if (clip != null)
            m_graphics.setClip(clip.x, clip.y, clip.width, clip.height);
    }

    private void drawLastMoveMarker()
    {
        setComposite(m_composite7);
        drawCircle(m_lastMoveMarkerColor);
        m_graphics.setPaintMode();
    }

    private void drawMarks()
    {
        setComposite(m_composite7);
        int d = m_size / 4;
        int width = m_size - 2 * d;
        m_graphics.setColor(Color.blue);
        Stroke oldStroke = null;
        if (m_graphics2D != null && m_size > 10)
        {
            oldStroke = m_graphics2D.getStroke();
            m_graphics2D.setStroke(m_thickStroke);
        }
        if (m_mark)
        {
            m_graphics.drawLine(d, d, d + width, d + width);
            m_graphics.drawLine(d, d + width, d + width, d);
        }
        if (m_markCircle)
            m_graphics.drawOval(d, d, width - 1, width - 1);
        if (m_markSquare)
            m_graphics.drawRect(d, d, width - 1, width - 1);
        if (m_markTriangle)
        {
            int height = (int)(0.866 * width);
            int top = (int)(0.866 * (width - height) / 2);
            int bottom = top + height - 1;
            m_graphics.drawLine(d, d + bottom, d + width / 2, d + top);
            m_graphics.drawLine(d + width / 2, d + top,
                                d + width, d + bottom);
            m_graphics.drawLine(d + width, d + bottom, d, d + bottom);
        }
        if (oldStroke != null)
            m_graphics2D.setStroke(oldStroke);
        m_graphics.setPaintMode();
    }

    private void drawSelect()
    {
        setComposite(m_composite7);
        drawCircle(Color.blue);
        m_graphics.setPaintMode();
    }

    private void drawStone()
    {
        if (m_color == GoColor.BLACK)
            drawStone(m_colorBlackStone, m_colorBlackStoneBright);
        else if (m_color == GoColor.WHITE)
            drawStone(m_colorWhiteStone, m_colorWhiteStoneBright);
    }

    private void drawStone(Color colorNormal, Color colorBright)
    {
        int margin = getStoneMargin(m_size);
        if (m_graphics2D != null && m_size >= 7)
        {
            RadialGradientPaint paint =
                getPaint(m_color, m_size, colorNormal, colorBright);
            m_graphics2D.setPaint(paint);
        }
        else
        {
            m_graphics.setColor(colorNormal);
        }
        m_graphics.fillOval(margin, margin,
                            m_size - 2 * margin, m_size - 2 * margin);
    }

    private void drawTerritoryGraphics()
    {
        if (m_territory == GoColor.BLACK)
            m_graphics.setColor(Color.darkGray);
        else
        {
            assert(m_territory == GoColor.WHITE);
            m_graphics.setColor(Color.lightGray);
        }
        m_graphics.fillRect(0, 0, m_size, m_size);
    }

    private void drawTerritoryGraphics2D()
    {
        setComposite(m_composite5);
        if (m_territory == GoColor.BLACK)
            m_graphics2D.setColor(Color.darkGray);
        else
        {
            assert(m_territory == GoColor.WHITE);
            m_graphics2D.setColor(Color.white);
        }
        m_graphics2D.fillRect(0, 0, m_size, m_size);
        m_graphics2D.setPaintMode();
    }

    private RadialGradientPaint getPaint(GoColor color, int size,
                                         Color colorNormal,
                                         Color colorBright)
    {
        RadialGradientPaint paint;
        int paintSize;
        if (color == GoColor.BLACK)
        {
            paint = m_paintBlack;
            paintSize = m_paintSizeBlack;
        }
        else
        {
            assert(color == GoColor.WHITE);
            paint = m_paintWhite;
            paintSize = m_paintSizeWhite;
        }
        if (size == paintSize && paint != null)
            return paint;
        int radius = Math.max(size / 3, 1);
        int center = size / 3;
        Point2D.Double centerPoint =
            new Point2D.Double(center, center);
        Point2D.Double radiusPoint =
            new Point2D.Double(radius, radius);
        paint = new RadialGradientPaint(centerPoint, colorBright,
                                        radiusPoint, colorNormal);
        if (color == GoColor.BLACK)
        {
            m_paintBlack = paint;
            m_paintSizeBlack = size;
        }
        else
        {
            m_paintWhite = paint;
            m_paintSizeWhite = size;
        }
        return paint;
    }

    private void setComposite(AlphaComposite composite)
    {
        if (m_graphics2D != null)
            m_graphics2D.setComposite(composite);
    }

    private static void setFont(Graphics graphics, int fieldSize)
    {
        if (m_cachedFont != null && m_cachedFontFieldSize == fieldSize)
        {
            graphics.setFont(m_cachedFont);
            return;
        }
        Font font = UIManager.getFont("Label.font");
        if (font != null)
        {
            FontMetrics metrics = graphics.getFontMetrics(font);
            double scale = (double)fieldSize / metrics.getAscent() / 2.3;
            if (scale < 0.95)
            {
                int size = font.getSize();
                Font derivedFont
                    = font.deriveFont(Font.BOLD, (float)(size * scale));
                if (derivedFont != null)
                    font = derivedFont;
            }
            else
                font = font.deriveFont(Font.BOLD);
        }
        m_cachedFont = font;
        m_cachedFontFieldSize = fieldSize;
        graphics.setFont(font);
    }
}

//----------------------------------------------------------------------------
