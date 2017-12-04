package com.slt.rest_trackingtimeline;

/**
 * Created by Usman Ahmad on 04.12.2017.
 */

/**
 * Tracking initialization:
 * 1. Initialize myTimeline Object from the User Class
 * 2. Initialize a currentTimelineSegment and Pushes into the List<TimelineSegment> from myTimeline Object
 * 3. If new Location is detected: Create a LocationEntry Object and push it into the "List<LocationEntry>" from currentTimelineSegment
 * 4. Post Request Procedure:
 *    - convert via Gson myTimeline into Json Body.
 *    - send the json Body via Gson Request and push it into the data base.
 *
 * During the Tracking process:
 * 1. If The second Location Point is detected: Create a LocationEntry Object and push it into the
 * "List<LocationEntry>" from currentTimelineSegment.
 * 2. Post Request Procedure:
 *    - convert via Gson the new detected LocationEntry Object into json Body
 *    - send the json Body via Gson Request and push it into the data base.
 * 3. If Tracking is not finished: Repeat step 1 to step 2
 */
public class PostRequest {
}
