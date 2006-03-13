//----------------------------------------------------------------------------
// $Id$
// $Source$
//----------------------------------------------------------------------------

package net.sf.gogui.go;

import java.io.PrintStream;

//----------------------------------------------------------------------------

/** Static utility functions related to class Board. */
public final class BoardUtils
{
    /** Print board position in text format.
        @param board The board to print.
        @param out The stream to print to.
        @param withGameInfo Print game information (prisoners, recent moves)
    */
    public static void print(Board board, PrintStream out,
                             boolean withGameInfo)
    {
        StringBuffer s = new StringBuffer(1024);
        int size = board.getSize();
        printXCoords(size, s);
        for (int y = size - 1; y >= 0; --y)
        {
            printYCoord(y, s);
            s.append(' ');
            for (int x = 0; x < size; ++x)
            {
                GoPoint point = GoPoint.get(x, y);
                GoColor color = board.getColor(point);
                if (color == GoColor.BLACK)
                    s.append("@ ");
                else if (color == GoColor.WHITE)
                    s.append("O ");
                else
                {
                    if (board.isHandicap(point))
                        s.append("+ ");
                    else
                        s.append(". ");
                }
            }
            printYCoord(y, s);
            if (withGameInfo)
                printGameInfo(board, s, y);
            s.append("\n");
        }
        printXCoords(size, s);
        if (! withGameInfo)
        {
            printToMove(board, s);
            s.append('\n');
        }
        out.print(s);
    }

    /** Make constructor unavailable; class is for namespace only. */
    private BoardUtils()
    {
    }

    private static void printGameInfo(Board board, StringBuffer s, int yIndex)
    {
        int size = board.getSize();
        if (yIndex == size - 1)
        {
            s.append("  ");
            printToMove(board, s);
        }
        else if (yIndex == size - 2)
        {
            s.append("  Prisoners: B ");
            s.append(board.getCapturedB());
            s.append("  W ");
            s.append(board.getCapturedW());
        }
        else
        {
            int n = board.getMoveNumber() - yIndex - 1;
            if (n >= 0)
            {
                Move move = board.getMove(n);
                s.append("  ");
                s.append(n + 1);
                s.append(' ');
                s.append(move.getColor() == GoColor.BLACK ? "B " : "W ");
                s.append(GoPoint.toString(move.getPoint()));
            }
        }
    }

    private static void printToMove(Board board, StringBuffer buffer)
    {
        buffer.append(board.getToMove() == GoColor.BLACK ? "Black" : "White");
        buffer.append(" to move");
    }

    private static void printXCoords(int size, StringBuffer s)
    {
        s.append("   ");
        int x;
        char c;
        for (x = 0, c = 'A'; x < size; ++x, ++c)
        {
            if (c == 'I')
                ++c;
            s.append(c);
            s.append(' ');
        }
        s.append('\n');
    }

    private static void printYCoord(int y, StringBuffer s)
    {
        String string = Integer.toString(y + 1);
        s.append(string);
        if (string.length() == 1)
            s.append(' ');
    }
}

//----------------------------------------------------------------------------
