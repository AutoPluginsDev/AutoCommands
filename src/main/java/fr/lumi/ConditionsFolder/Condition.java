package fr.lumi.ConditionsFolder;

import java.util.ArrayList;
import java.util.List;

public abstract class Condition {

    protected List<String> m_parameters;

    public Condition() {
        m_parameters = new ArrayList<String>();
    }

    public abstract boolean verify();


    public abstract String getName();

    public String getParam(int id) {
        return m_parameters.get(id);
    }

    public void setParams(List<String> list) {
        m_parameters = list;
    }

    protected abstract void formatingParams();

    abstract boolean paramVerifier();

    public abstract String getCondition();

}
