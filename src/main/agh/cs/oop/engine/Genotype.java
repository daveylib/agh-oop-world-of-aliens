package agh.cs.oop.engine;

import java.util.Arrays;

public class Genotype {
    private long genotype = 0;

    public Genotype(byte[] genotype) {
        this.setGenotype(genotype);
    }

    public Genotype() {
        byte[] genotype = this.getRandomGenotype();

        this.setGenotype(genotype);
    }

    public Genotype(long genotype) {
        this.genotype = genotype;
    }

    // Set genotype from given array of bytes
    private void setGenotype(byte[] genotype) {
        if (genotype.length != 32) {
            throw new IllegalArgumentException("The size of genotype " + Arrays.toString(genotype) + " must equals 32");
        }

        byte[] gens = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };

        for (int i = 0; i < 32; i++) {
            if (genotype[i] < 0 || genotype[i] > 7) {
                throw new IllegalArgumentException("Gen at position " + i + " in genotype " + Arrays.toString(genotype) + " does not exists");
            }

            gens[genotype[i]] += 1;
        }

        int sum = 0;
        for (int i = 0; i < 8; i++) {
            if (gens[i] <= 0 || gens[i] >= 26) {
                throw new IllegalArgumentException("Genotype " + Arrays.toString(genotype) + " is not correct. " +
                        "Quantity of gen " + i + " can not be set to " + gens[i]);
            }

            sum += gens[i];
        }

        if (sum != 32) {
            throw new IllegalArgumentException("Quantity of all gens in genotype " + Arrays.toString(genotype) + " must equals 32");
        }

        for (int i = 0; i < 8; i++) {
            this.setGen(i, gens[i]);
        }
    }

    // Correct given genotype if necessary and return array of gens
    private static byte[] correctGenotype(byte[] genotype) {
        byte[] gens = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };

        for (int i = 0; i < 32; i++) {
            gens[genotype[i]] += 1;
        }

        boolean isCorrect;

        do {
            isCorrect = true;

            for (int i = 0; i < 8; i++) {
                if (gens[i] == 0) {
                    isCorrect = false;

                    byte randomOther;

                    do {
                        randomOther = genotype[(byte) (Math.random() * 31)];
                    } while (randomOther == i);

                    gens[i] += 1;
                    gens[randomOther] -= 1;
                }
            }
        } while (!isCorrect);

        return makeGenotypeFromGens(gens);
    }

    // Create full genotype as array of bytes from given array of gens
    private static byte[] makeGenotypeFromGens(byte[] gens) {
        byte[] genotypeArray = new byte[32];

        byte currentIdx = 0;
        for (int gen = 0; gen < 8; gen++) {
            short howManyGens = gens[gen];

            for (int i = 0; i < howManyGens; i++) {
                genotypeArray[currentIdx++] = (byte) gen;
            }
        }

        return genotypeArray;
    }

    // Return genotype as array of bytes
    public byte[] getGenotype() {
        byte[] genotypeArray = new byte[32];

        byte currentIdx = 0;
        for (int gen = 0; gen < 8; gen++) {
            short howManyGens = this.getGen(gen);

            for (int i = 0; i < howManyGens; i++) {
                genotypeArray[currentIdx++] = (byte) gen;
            }
        }

        return genotypeArray;
    }

    // Return genotype as Long
    public long getLongGenotype() {
        return this.genotype;
    }

    // Get quantity of given gen in the genotype
    private short getGen(int whichOne) {
        if (whichOne < 0 || whichOne > 7) {
            throw new IllegalArgumentException("Gen " + whichOne + " does not exists in a genotype");
        }

        long genHelper = 31L << (whichOne * 5);

        return (short) ((this.genotype & genHelper) >> (whichOne * 5));
    }

    // Set quantity of given gen in the genotype
    private void setGen(int whichOne, long howMany) {
        if (whichOne < 0 || whichOne > 7) {
            throw new IllegalArgumentException("Gen " + whichOne + " does not exists in a genotype");
        }

        if (howMany <= 0 || howMany >= 26) {
            throw new IllegalArgumentException("Quantity of gen " + whichOne + " can not be set to " + howMany);
        }

        long newGenotype = howMany << (whichOne * 5);
        this.genotype |= newGenotype;
    }

    // Cross genotype of alien with other one
    public static byte[] crossGenotypes(long first, long second) {
        byte[] firstGenotype = new Genotype(first).getGenotype();
        byte[] secondGenotype = new Genotype(second).getGenotype();

        byte firstIdx = (byte) ((Math.random() * (29 - 1)) + 1);
        byte secondIdx = (byte) ((Math.random() * (30 - (firstIdx + 1))) + firstIdx + 1);

        byte[] newGenotype = new byte[32];

        System.arraycopy(firstGenotype, 0, newGenotype, 0, firstIdx + 1);
        System.arraycopy(secondGenotype, firstIdx + 1, newGenotype, firstIdx + 1, secondIdx - firstIdx);
        System.arraycopy(firstGenotype, secondIdx + 1, newGenotype, secondIdx + 1, 31 - secondIdx);

        return correctGenotype(newGenotype);
    }

    // Get random correct genotype
    private byte[] getRandomGenotype() {
        short usedQuantity = 0;

        byte[] gens = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };

        for (int gen = 0; gen < 8; gen++) {
            short maxQuantity = (short) (32 - usedQuantity - (7 - gen));
            gens[gen] = (byte) (Math.random() * (maxQuantity - 1) + 1);
            usedQuantity += gens[gen];
        }

        return makeGenotypeFromGens(gens);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.getGenotype());
    }

    // Pretty print for given genotype as long number
    public static String prettyPrintOf(long genotype) {
        Genotype genotypeObj = new Genotype(genotype);

        return genotypeObj.toString();
    }
}
