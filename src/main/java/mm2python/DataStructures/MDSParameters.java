package mm2python.DataStructures;

import java.util.ArrayList;

/**
 * object used to retrieve MDS based on parameters
 *  t, p, z, c
 *  uses the MDSParamBuilder to generate.
 */
public class MDSParameters {

    private ArrayList<MDSParamObject> params;

    MDSParameters(ArrayList<MDSParamObject> params_) {
        params = params_;
    }

    public ArrayList<MDSParamObject> getParams() {
        return params;
    }


}
