package link.symtable.kson.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import link.symtable.kson.core.node.KsNode;

public class TestBase {
    public InputStream locate(String file) {
        InputStream in = getClass().getResourceAsStream(file);
        if (in == null) {
            throw new IllegalStateException("Unable to locate \"" + file + '"');
        }
        return in;
    }


    public InputStreamReader reader(String file) {
        return new InputStreamReader(locate(file), StandardCharsets.UTF_8);
    }

    public KsNode parseFile(String file) {
        InputStreamReader instream = reader(file);
        BufferedReader reader = new BufferedReader(instream);
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            instream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String content = sb.toString();
        KsNode node = Kson.parse(content);
        return node;
    }

}
