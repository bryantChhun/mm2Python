package mm2python.DataStructures.Builders;

import java.security.InvalidParameterException;

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

    public MDSParamObject(String str) {
        this.strValue = str;
    }

    public MDSParamObject(Integer i) {
        this.intValue = i;
    }

    public String getStr() {
        if(this.strValue!=null) {
            return this.strValue;
        } else {
            throw new InvalidParameterException("MDSParamObject is not a Str object");
        }
    }

    public Integer getInt() {
        if(this.intValue!=null) {
            return this.intValue;
        } else {
            throw new InvalidParameterException("MDSParamObject is not an Int object");
        }
    }

    public String getLabel() {
        if(this.label!=null) {
            return this.label;
        } else {
            throw new InvalidParameterException("MDSParamObject has no assigned label");
        }
    }

}
