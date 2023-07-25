import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @created 24.07.2023 15:41
 */
public class AddressObject {
    private String OBJECTID;
    private String TYPENAME;
    private String NAME;
    private LocalDate STARTDATE;
    private LocalDate ENDDATE;
    private String ISACTUAL;
    private String ISACTIVE;



    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AddressObject(String OBJECTID, String TYPENAME, String NAME, String STARTDATE, String ENDDATE, String ISACTUAL, String ISACTIVE) {
        this.OBJECTID = OBJECTID;
        this.TYPENAME = TYPENAME;
        this.NAME = NAME;
        this.STARTDATE = LocalDate.parse(STARTDATE,formatter);
        this.ENDDATE = LocalDate.parse(ENDDATE,formatter);
        this.ISACTUAL = ISACTUAL;
        this.ISACTIVE = ISACTIVE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressObject addrObj = (AddressObject) o;
        return Objects.equals(OBJECTID, addrObj.OBJECTID) && Objects.equals(TYPENAME, addrObj.TYPENAME) && Objects.equals(NAME, addrObj.NAME) && Objects.equals(STARTDATE, addrObj.STARTDATE) && Objects.equals(ENDDATE, addrObj.ENDDATE) && Objects.equals(ISACTUAL, addrObj.ISACTUAL) && Objects.equals(ISACTIVE, addrObj.ISACTIVE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(OBJECTID, TYPENAME, NAME, STARTDATE, ENDDATE, ISACTUAL, ISACTIVE);
    }

    @Override
    public String toString() {
        return OBJECTID +
                ": " + TYPENAME +
                " " + NAME;
    }

    public boolean isDateBetween(LocalDate date) {
        if (date.isAfter(this.STARTDATE) && date.isBefore(this.ENDDATE)){
            return true;
        }
        return false;
    }


}
