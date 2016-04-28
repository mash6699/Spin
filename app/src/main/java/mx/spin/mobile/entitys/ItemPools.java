package mx.spin.mobile.entitys;

/**
 * Created by gorro on 12/01/16.
 */
public class ItemPools {

    String name, lastAnalize;

    public ItemPools(String name, String lastAnalize) {
        this.name = name;
        this.lastAnalize = lastAnalize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastAnalize() {
        return lastAnalize;
    }

    public void setLastAnalize(String lastAnalize) {
        this.lastAnalize = lastAnalize;
    }
}
