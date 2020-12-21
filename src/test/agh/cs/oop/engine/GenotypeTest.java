package agh.cs.oop.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GenotypeTest {
    @Test
    void incorrentGenotypesTest() {
        byte[] genotypeArrayA = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7};
        Assertions.assertThrows(RuntimeException.class, () -> new Genotype(genotypeArrayA));

        byte[] genotypeArrayB = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7, 7};
        Assertions.assertThrows(RuntimeException.class, () -> new Genotype(genotypeArrayB));

        byte[] genotypeArrayC = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 8};
        Assertions.assertThrows(RuntimeException.class, () -> new Genotype(genotypeArrayC));

        byte[] genotypeArrayD = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 6};
        Assertions.assertThrows(RuntimeException.class, () -> new Genotype(genotypeArrayD));
    }

    @Test
    void getGenotypeMethod() {
        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotypeA = new Genotype(genotypeArray);

        assertArrayEquals(genotypeA.getGenotype(), genotypeArray);

        long genotypeLong = 72080329862L;
        Genotype genotypeB = new Genotype(genotypeLong);

        assertArrayEquals(genotypeB.getGenotype(), genotypeArray);
    }

    @Test
    void getLongGenotypeMethod() {
        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotypeA = new Genotype(genotypeArray);

        assertEquals(genotypeA.getLongGenotype(), 72080329862L);

        long genotypeLong = 72080329862L;
        Genotype genotypeB = new Genotype(genotypeLong);

        assertEquals(genotypeB.getLongGenotype(), 72080329862L);
    }

    @Test
    void toStringMethod() {
        String genotypeStr = "[0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7]";

        byte[] genotypeArray = new byte[] {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7};
        Genotype genotypeA = new Genotype(genotypeArray);

        assertEquals(genotypeA.toString(), genotypeStr);

        long genotypeLong = 72080329862L;
        Genotype genotypeB = new Genotype(genotypeLong);

        assertEquals(genotypeB.toString(), genotypeStr);
    }

    @Test
    void prettyPrintOfMethod() {
        String genotypeStr = "[0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 7, 7]";
        long genotypeLong = 72080329862L;

        assertEquals(Genotype.prettyPrintOf(genotypeLong), genotypeStr);
    }
}
