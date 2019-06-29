package com.vitorpamplona.utils;

import static org.junit.Assert.assertEquals;

import com.vitorpamplona.utils.CascadingFors.For;

import org.junit.Test;

public class CascadingForsTest {

    @Test
    public void testCascadingFor() {
        For f1 = new For(0, 2, 1);

        String expectedOutcome = 
                "0.0 33%\n"+
                "1.0 67%\n"+
                "2.0 100%\n";
                

        StringBuilder buildTrace = new StringBuilder();   

        f1.each(() -> {
            buildTrace.append(f1.value + " " + f1.fmtProgress() + "\n");
        });

        assertEquals(expectedOutcome, buildTrace.toString());
    }

    @Test
    public void testBasic() {
        For f1 = new For(0, 2, 1);
        For f2 = new For(0, 4, 1);
        
        CascadingFors fs = new CascadingFors(f1, f2);

        String expectedOutcome = 
                "0.0 0.0 7%\n"+
                "0.0 1.0 13%\n"+
                "0.0 2.0 20%\n"+
                "0.0 3.0 27%\n"+
                "0.0 4.0 33%\n"+
                "1.0 0.0 40%\n"+
                "1.0 1.0 47%\n"+
                "1.0 2.0 53%\n"+
                "1.0 3.0 60%\n"+
                "1.0 4.0 67%\n"+
                "2.0 0.0 73%\n"+
                "2.0 1.0 80%\n"+
                "2.0 2.0 87%\n"+
                "2.0 3.0 93%\n"+
                "2.0 4.0 100%\n";

        StringBuilder buildTrace = new StringBuilder();   

        fs.run(() -> {
            buildTrace.append(f1.value + " " + f2.value + " " + fs.fmtProgress() + "\n");
        });

        assertEquals(expectedOutcome, buildTrace.toString());
    }

}