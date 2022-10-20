import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.*;

 
public class DocTests {
    @Test
    public void testBasePair() throws IOException, URISyntaxException {
        Handler h = new Handler("./technical/");
        URI url = new URI("http://localhost:4000/search?s=base%20pair");
        assertThat(h.handleRequest(url), containsString("There were 76 files found:"));
    }
}