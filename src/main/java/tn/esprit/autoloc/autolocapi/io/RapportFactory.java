package tn.esprit.autoloc.autolocapi.io;

public class RapportFactory {

    public static IRapport createRapport(RapportType type) {
        if (type == null) {
            return new RapportTextuel();
        }
        return switch (type) {
            case CONSOLE_TOTAL -> new ConsoleRapportDecorator(new RapportTextuel());
            case TEXTUEL -> new RapportTextuel();
        };
    }

    public static IRapport createRapport(String type) {
        if (type != null && (type.equalsIgnoreCase("CONSOLE") || type.equalsIgnoreCase("CONSOLE_TOTAL"))) {
            return new ConsoleRapportDecorator(new RapportTextuel());
        }
        return new RapportTextuel();
    }
}
