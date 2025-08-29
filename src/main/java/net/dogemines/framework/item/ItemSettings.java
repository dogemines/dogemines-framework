package net.dogemines.framework.item;

public class ItemSettings {
    public static final ItemSettings DEFAULT = new ItemSettings(true);

    private final boolean hasModel;

    public ItemSettings(boolean hasModel) {
        this.hasModel = hasModel;
    }

    public boolean hasModel() {
        return hasModel;
    }

    public static class Builder {
        private boolean hasModel = false;

        public void setHasModel(boolean hasModel) {
            this.hasModel = hasModel;
        }
        public ItemSettings build() {
            return new ItemSettings(hasModel);
        }
    }
}
