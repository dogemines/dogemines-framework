package net.dogemines.framework.data.resource;

import java.io.File;

public interface HostingMethod {
    String getResourceURL();
    void hostPack(File resourcePackZip);
    void disable();
}
