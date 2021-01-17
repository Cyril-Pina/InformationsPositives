package com.pinalopes.informationspositives.story.model;

import android.view.MotionEvent;
import android.view.View;

import static com.pinalopes.informationspositives.Constants.DURATION_VIEW_ANIMATION;
import static com.pinalopes.informationspositives.Constants.INITIAL_VIEW_POSITION;
import static com.pinalopes.informationspositives.Constants.INITIAL_Y_VALUE;

public class StoryHeaderService {

    private final View viewToMove;
    private final float swipeMaxPercent;
    private final HeaderMovementListener listener;
    private float initialY;

    public StoryHeaderService(View viewToMove, float swipeMaxPercent, HeaderMovementListener listener) {
        this.viewToMove = viewToMove;
        this.swipeMaxPercent = swipeMaxPercent;
        this.listener = listener;
        this.initialY = INITIAL_Y_VALUE;
    }

    public boolean setNewMovement(MotionEvent event) {
        int action = event.getActionMasked();

        if (this.initialY > 0) {
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    this.moveView(event.getY());
            break;
            case MotionEvent.ACTION_UP:
                moveViewToInitialPos();
                break;
            default:
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean setInitialY(MotionEvent event) {
        int action = event.getActionMasked();

        if (action == MotionEvent.ACTION_DOWN) {
            this.initialY = event.getY();
            return true;
        }
        return false;
    }

    private void moveView(float currentY) {
        if (currentY > this.initialY) {
            float diffY = currentY - this.initialY;
            this.viewToMove.setTranslationY(diffY);
            if (diffY >= (this.viewToMove.getHeight() * this.swipeMaxPercent)) {
                listener.onMovementFinished();
                this.initialY = INITIAL_Y_VALUE;
            }
        }
    }

    private void moveViewToInitialPos() {
        this.viewToMove.animate().translationY(INITIAL_VIEW_POSITION).setDuration(DURATION_VIEW_ANIMATION);
        this.initialY = INITIAL_Y_VALUE;
    }
}
