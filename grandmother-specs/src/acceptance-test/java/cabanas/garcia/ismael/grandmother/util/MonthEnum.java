package cabanas.garcia.ismael.grandmother.util;

import java.util.Optional;

public enum MonthEnum {
    ENERO("ENERO",1),
    FEBRERO("FEBRERO",2),
    MARZO("MARZO",3),
    ABRIL("ABRIL",4),
    MAYO("MAYO",5),
    JUNIO("JUNIO", 6),
    JULIO("JULIO", 7),
    AGOSTO("AGOSTO", 8),
    SEPTIEMPBRE("SEPTIEMBRE", 9),
    OCTUBRE("OCTUBRE", 10),
    NOVIEMBRE("NOVIEMBRE", 11),
    DICIEMBRE("DICIEMBRE", 12);

    private String name;
    private int position;

    private static final MonthEnum[] ENUMS = MonthEnum.values();

    private MonthEnum(String name, int position){
        this.name = name;
        this.position = position;
    }

    public static Optional<MonthEnum> monthOf(String monthName){
        for (int i = 0; i < ENUMS.length-1; i++) {
            if(monthName.toUpperCase().equals(ENUMS[i].name))
                return Optional.of(ENUMS[i]);
        }
        return Optional.empty();
    }

    public int getPosition() {
        return position;
    }
}
