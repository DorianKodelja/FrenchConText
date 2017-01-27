package org.context.implementation;

import java.util.ArrayList;

public interface ConText {

    public String preProcessSentence(String sent, String concept) throws Exception;
    ArrayList<String> applyContext(String concept, String sentence) throws Exception;

    String applyNegEx(String[] words) throws Exception;

    String applyTemporality(String[] words) throws Exception;

    String applyExperiencer(String[] words) throws Exception;
}
