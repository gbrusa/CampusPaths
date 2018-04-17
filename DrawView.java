package gbrusa.cse331_17au_campus_paths;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.Toast;

import java.util.List;

import hw8.CampusPoint;

/**
 * <b>DrawView</b> represents the View of the CampusPaths application. It is concerned with the
 * displaying of the campus map, as well as the start and destination buildings and the path
 * connecting them.
 * It holds temporary references to variables that are either pulled from the Model or passed in
 * by the controller and must be remembered when invalidated.
 */

public class DrawView extends AppCompatImageView {

    /** indicates when the path should be drawn */
    private boolean drawPath = false;

    /** indicates when the start circle should be drawn */
    private boolean drawStart = false;

    /** stores a reference to the start building name */
    private String start = null;

    /** indicates when the destination circle should be drawn */
    private boolean drawDest = false;

    /** stores a reference to the destination building name */
    private String dest = null;

    /** stores a reference to the current path so that its values can be used when drawing */
    private List<CampusPoint> path = null;


    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();

        // paints the destination and path in red
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5.f);

        if (drawDest) {
            CampusPoint cp = CampusPathsModel.getPoint(dest);
            canvas.drawCircle((float)cp.getX(), (float)cp.getY(), 10.f, paint);
        }

        if (drawPath) {
            for (int i = 0; i < path.size() - 1; i++) {
                canvas.drawLine((float)path.get(i).getX(), (float)path.get(i).getY(),
                        (float)path.get(i+1).getX(), (float)path.get(i+1).getY(), paint);
            }
        }

        // paints the start in cyan to distinguish it from the path and destination
        // it is painted last so that it is in front of the path (removing instances where the path
        // is visible over the cyan circle)
        paint.setColor(Color.CYAN);

        if (drawStart) {
            CampusPoint cp = CampusPathsModel.getPoint(start);
            canvas.drawCircle((float)cp.getX(), (float)cp.getY(), 10.f, paint);
        }
    }

    /**
     * Pulls the path search information from the Model using building data supplied by the user
     */
    public void drawPath() {
        if (start != null && dest != null) {
            path = CampusPathsModel.search(start, dest);
            drawPath = true;
            invalidate();
        } else {
            Toast.makeText(getContext(), "Please select start and destination",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Clears all the path and building information from the View
     */
    public void clear() {
        drawPath = false;
        drawStart = false;
        drawDest = false;
        start = null;
        dest = null;
        path = null;
        invalidate();
    }

    /**
     * Prompts the view to draw a circle representing the start building
     * @param start
     *          The building at the start of the path
     */
    public void drawStart(String start) {
        drawPath = false;
        this.start = start;
        drawStart = true;
        invalidate();
    }

    /**
     * Prompts the view to draw a circle representing the destination
     * @param dest
     *          The destination building of the path
     */
    public void drawDest(String dest) {
        drawPath = false;
        this.dest = dest;
        drawDest = true;
        invalidate();
    }
}
