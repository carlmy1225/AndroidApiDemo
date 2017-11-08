package com.jwj.demo.androidapidemo.custom_view.recycler;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/26
 * Copyright: Ctrip
 */

public interface ISnappyLayoutManager {

    /**
     * @param velocityX
     * @param velocityY
     * @return the resultant position from a fling of the given velocity.
     */
    int getPositionForVelocity(int velocityX, int velocityY);

    /**
     * @return the position this list must scroll to to fix a state where the
     * views are not snapped to grid.
     */
    int getFixScrollPos();

}
