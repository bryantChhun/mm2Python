package mm2python.mmEventHandler;

import mmcorej.CMMCore;

public class LocalCore {

    private static CMMCore s;

    public LocalCore(CMMCore mm_) {
        if(s==null) {
            s = mm_;
        }
    }

    public LocalCore() {

    }

    public CMMCore getCore() {
        return s;
    }
}