package mm2python.DataStructures;

/**
 * a custom Object that accepts two types:
 *  Integer
 *  String
 * Used as two-type element for arraylist
 */
public class MDSParamObject {

    private Integer intValue=null;
    private String strValue=null;
    private String label;

    public MDSParamObject(String label_, String str) {
        this.label = label_;
        this.strValue = str;
    }

    public MDSParamObject(String label_, Integer i) {
        this.label = label_;
        this.intValue = i;
    }

    public String getStr() {
        return this.strValue;
    }

    public Integer getInt() {
        return this.intValue;
    }

    public String getLabel() {
        return this.label;
    }

}
