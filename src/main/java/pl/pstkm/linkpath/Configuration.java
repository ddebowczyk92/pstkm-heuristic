package pl.pstkm.linkpath;

/**
 * Created by DominikD on 2016-01-09.
 */
public class Configuration {

    public static final String NO_CONF = "KX";

    private final String id;

    public Configuration(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
