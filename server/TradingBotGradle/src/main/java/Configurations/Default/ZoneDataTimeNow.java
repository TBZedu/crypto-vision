package Configurations.Default;

import Interfaces.IZoneDateTimeEnd;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Is a class that can be called as the default value of IZoneDateTimeEnd
 */
public class ZoneDataTimeNow implements IZoneDateTimeEnd {

    @Override
    public ZonedDateTime getZoneDateTime() {
        return ZonedDateTime.now(ZoneId.of("America/New_York"));
    }
}
