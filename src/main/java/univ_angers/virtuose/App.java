package univ_angers.virtuose;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

import univ_angers.virtuose.cut.Cut;
import univ_angers.virtuose.search.Search;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //Cut.start(args);
    	try {
			Search.start(args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
