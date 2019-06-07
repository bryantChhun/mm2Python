package mm2python.mmEventHandler;

import org.micromanager.Studio;

public class LocalStudio {

    private static Studio s;

    public LocalStudio(Studio mm_) {
        if(s==null) {
            s = mm_;
        }
    }

    public LocalStudio() {

    }

    public Studio getStudio() {
        return s;
    }
}
