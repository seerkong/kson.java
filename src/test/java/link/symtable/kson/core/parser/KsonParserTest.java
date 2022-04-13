package link.symtable.kson.core.parser;

import org.junit.jupiter.api.Test;

import link.symtable.kson.core.Kson;
import link.symtable.kson.core.node.KsonNode;


public class KsonParserTest {
    @Test
    public void testList1() throws RuntimeException {
        String source = "\n( 1 \n 2 3 )  ";
        KsonNode node = Kson.parse(source);
        System.out.println(node);
    }

    @Test
    public void testList2() throws RuntimeException {
        String source = "\n( __F_u_N_c_1__ [1 2 3] \n (4 5 6) {\"m\" : 7 \"n\" : 8} )  ";
        KsonNode node = Kson.parse(source);
        System.out.println(node);
    }

    @Test
    public void testArray1() throws RuntimeException {
        String source = "\n[ 1 \n , 2 , 3 ]  ";
        KsonNode node = Kson.parse(source);
        System.out.println(node);
    }

    @Test
    public void testArray2() throws RuntimeException {
        String source = "\n[ 1 \n , 2 , 3, ]  ";
        KsonNode node = Kson.parse(source);
        System.out.println(node);
    }

    @Test
    public void testArray3() throws RuntimeException {
        String source = "\n[ 1 \n  2  3 ]  ";
        KsonNode node = Kson.parse(source);
        System.out.println(node);
    }


    @Test
    public void testMap1() throws RuntimeException {
        String source = "\n{ \"abc\": 1 \n , efg : \"m中 \\uAAAA \\u1337\" , \"t\" : 3 }  ";
        KsonNode node = Kson.parse(source);
        System.out.println(node);
    }

    @Test
    public void testMap2() throws RuntimeException {
        String source = "\n{ \"abc\": 1 \n , \"efg\": \"m\" , \"t\" : 3, }  ";
        KsonNode node = Kson.parse(source);
        System.out.println(node);
    }

    @Test
    public void testMap3() throws RuntimeException {
        String source = "\n{ \"abc\": 1 \n  \"efg\": \"m\"  \"t\" : 3 }  ";
        KsonNode node = Kson.parse(source);
        System.out.println(node);
    }

    @Test
    public void wordSymbolString() throws RuntimeException {
        String source = "[word $symbol \"string\"] ";
        KsonNode node = Kson.parse(source);
        System.out.println(node);
    }
}
