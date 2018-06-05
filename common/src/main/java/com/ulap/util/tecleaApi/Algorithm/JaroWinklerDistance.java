package com.ulap.util.tecleaApi.Algorithm;

import java.io.UnsupportedEncodingException;

/**
 *
 */
public class JaroWinklerDistance implements MatchAlgorithm
{
    public static final MatchAlgorithm INSTANCE = new JaroWinklerDistance();
    
    private static final String NAME = "Jaro-Winkler Distance";

    @Override
    public String getName() 
	{
        return NAME;
    }

    @Override
    public double compare(String suspect, String candidate) throws UnsupportedEncodingException 
	{
        org.apache.lucene.search.spell.JaroWinklerDistance distance = new org.apache.lucene.search.spell.JaroWinklerDistance();        
        return (double)Math.round((distance.getDistance(suspect.toLowerCase(), candidate.toLowerCase())) * 10000) / 100;        
    }    
}
