package Configurations.Default;

import Interfaces.IZoneDateTimeStart;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZoneDateTimeStart implements IZoneDateTimeStart {

    private int minusMinutes;

    public ZoneDateTimeStart(int minusMinutes){
        this.minusMinutes = minusMinutes + 1;
    }

    @Override
    public ZonedDateTime getZoneDateTime() {
        return ZonedDateTime.now(ZoneId.of("America/New_York")).minusMinutes(minusMinutes);
    }
}
